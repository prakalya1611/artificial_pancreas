package com.example.glucosync

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

// minSdk is 23, but the two-arg getParcelableExtra(key, Class) / getParcelableArrayExtra(key, Class)
// APIs only exist on API 33+. These helpers fall back to the deprecated single-arg versions on
// older devices so the app doesn't crash on phones below Android 13.
@Suppress("DEPRECATION")
private fun <T : android.os.Parcelable> Intent.getParcelableExtraCompat(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= 33) {
        getParcelableExtra(key, clazz)
    } else {
        getParcelableExtra(key)
    }
}

@Suppress("DEPRECATION")
private fun <T : android.os.Parcelable> Intent.getParcelableArrayExtraCompat(key: String, clazz: Class<T>): Array<out android.os.Parcelable>? {
    return if (Build.VERSION.SDK_INT >= 33) {
        getParcelableArrayExtra(key, clazz)
    } else {
        getParcelableArrayExtra(key)
    }
}

class MainActivity : AppCompatActivity() {

    companion object {
        // Private MIME type used ONLY for pump tags that GlucoSync itself has written.
        // This is what lets us tell a pump tag apart from a sensor tag by content,
        // not by whatever the app happens to be doing at the moment.
        private const val MIME_PUMP = "application/vnd.glucosync.pump"
        private const val MIME_SENSOR = "text/plain"
    }

    // The only piece of "mode" state left. It is set exclusively by the user
    // tapping "Set Up Pump Tag" in the drawer, and cleared right after a
    // provisioning write succeeds (or the user leaves the screen).
    // It is NEVER inferred from whether we happen to have insulin data queued.
    private enum class NfcMode { IDLE, PROVISION_PUMP }
    private var nfcMode: NfcMode = NfcMode.IDLE

    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null

    // Insulin dose waiting to be written the NEXT time a genuine pump tag
    // (i.e. one that reports MIME_PUMP) is tapped. This only ever affects
    // WHAT gets written to a pump tag, never WHETHER a tap is treated as a
    // pump write vs. a sensor read.
    private var pendingDose: String? = null

    private val entries = ArrayList<Entry>()
    private var timeIndex = 0f

    private lateinit var txtGlucose: TextView
    private lateinit var lineChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtGlucose = findViewById(R.id.txtGlucose)
        lineChart = findViewById(R.id.lineChart)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val menuIcon = findViewById<ImageButton>(R.id.menuIcon)
        val menuInsulin = findViewById<LinearLayout>(R.id.menuInsulin)
        val menuReports = findViewById<LinearLayout>(R.id.menuReports)
        val menuProvisionPump = findViewById<LinearLayout>(R.id.menuProvisionPump)

        menuIcon.setOnClickListener { drawerLayout.open() }

        menuInsulin.setOnClickListener {
            startActivity(Intent(this, InsulinActivity::class.java))
            drawerLayout.close()
        }

        menuReports.setOnClickListener {
            startActivity(Intent(this, ReportsActivity::class.java))
            drawerLayout.close()
        }

        // Explicit, user-initiated provisioning. This is the ONLY way nfcMode
        // ever becomes PROVISION_PUMP.
        menuProvisionPump.setOnClickListener {
            nfcMode = NfcMode.PROVISION_PUMP
            Toast.makeText(
                this,
                "Tap a blank NFC tag now to set it up as your pump tag",
                Toast.LENGTH_LONG
            ).show()
            drawerLayout.close()
            // Refresh dispatch filters immediately so the blank-tag filter is live.
            enableNfcDispatch()
        }

        // Dose queued from InsulinActivity. Note: this ONLY populates what
        // will be written on the next pump-tag tap. It does not change how
        // any tag gets classified.
        pendingDose = intent.getStringExtra("INSULIN_DATA")

        // Graph setup
        lineChart.description.isEnabled = false
        lineChart.axisRight.isEnabled = false

        // NFC setup
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC not supported", Toast.LENGTH_LONG).show()
        }

        pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_MUTABLE
        )
    }

    override fun onResume() {
        super.onResume()
        enableNfcDispatch()
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    /**
     * Builds and enables the foreground dispatch filters for the CURRENT nfcMode.
     * - Sensor tags (MIME_SENSOR) and pump tags (MIME_PUMP) are always recognized.
     * - Blank/unformatted tags (ACTION_TAG_DISCOVERED) are ONLY dispatched to us
     *   while nfcMode == PROVISION_PUMP, so a stray tap on some unrelated blank
     *   tag never gets treated as "format this as my pump tag".
     */
    private fun enableNfcDispatch() {
        val adapter = nfcAdapter ?: return

        val filters = ArrayList<IntentFilter>()

        val sensorFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        sensorFilter.addCategory(Intent.CATEGORY_DEFAULT)
        try {
            sensorFilter.addDataType(MIME_SENSOR)
        } catch (e: IntentFilter.MalformedMimeTypeException) { /* won't happen, constant is valid */ }
        filters.add(sensorFilter)

        val pumpFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        pumpFilter.addCategory(Intent.CATEGORY_DEFAULT)
        try {
            pumpFilter.addDataType(MIME_PUMP)
        } catch (e: IntentFilter.MalformedMimeTypeException) { /* won't happen, constant is valid */ }
        filters.add(pumpFilter)

        if (nfcMode == NfcMode.PROVISION_PUMP) {
            filters.add(IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED))
        }

        adapter.enableForegroundDispatch(
            this,
            pendingIntent,
            filters.toTypedArray(),
            null
        )
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        // A fresh dose may have arrived from InsulinActivity via FLAG_ACTIVITY_SINGLE_TOP.
        pendingDose = intent.getStringExtra("INSULIN_DATA") ?: pendingDose

        when {
            // --- Case 1: a real pump tag was tapped (identified by its own MIME type) ---
            intent.action == NfcAdapter.ACTION_NDEF_DISCOVERED && intent.type == MIME_PUMP -> {
                handlePumpTagTapped(intent)
            }

            // --- Case 2: a real sensor tag was tapped ---
            intent.action == NfcAdapter.ACTION_NDEF_DISCOVERED && intent.type == MIME_SENSOR -> {
                handleSensorTagTapped(intent)
            }

            // --- Case 3: a blank/unformatted tag, and we're explicitly provisioning ---
            intent.action == NfcAdapter.ACTION_TAG_DISCOVERED && nfcMode == NfcMode.PROVISION_PUMP -> {
                provisionPumpTag(intent)
            }

            // --- Anything else: don't guess. Tell the user and do nothing. ---
            else -> {
                Toast.makeText(this, "Tag not recognized as a GlucoSync sensor or pump tag", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleSensorTagTapped(intent: Intent) {
        val rawMessages = intent.getParcelableArrayExtraCompat(
            NfcAdapter.EXTRA_NDEF_MESSAGES,
            NdefMessage::class.java
        )
        if (rawMessages == null || rawMessages.isEmpty()) {
            Toast.makeText(this, "Sensor tag detected but no data", Toast.LENGTH_SHORT).show()
            return
        }

        val messages = rawMessages.map { it as NdefMessage }
        val record = messages[0].records[0]
        val payload = record.payload

        val langLength = payload[0].toInt() and 0x3F
        val text = String(
            payload,
            langLength + 1,
            payload.size - langLength - 1
        )

        Toast.makeText(this, "Sensor detected!", Toast.LENGTH_SHORT).show()
        txtGlucose.text = "Message: $text"

        try {
            val value = text.replace(" V", "").toFloat()

            entries.add(Entry(timeIndex, value))
            timeIndex++

            val dataSet = LineDataSet(entries, "Voltage Trend")
            dataSet.setDrawCircles(true)
            dataSet.setDrawValues(false)

            lineChart.data = LineData(dataSet)
            lineChart.invalidate()
        } catch (e: Exception) {
            Toast.makeText(this, "Could not parse sensor value: $text", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handlePumpTagTapped(intent: Intent) {
        val dose = pendingDose

        if (dose != null) {
            // We have a dose queued up — write it to this confirmed pump tag.
            writeToNfcTag(intent, dose)
            Toast.makeText(this, "Sent: $dose", Toast.LENGTH_SHORT).show()
            pendingDose = null
        } else {
            // No dose queued. Read back whatever is currently on the pump tag
            // as a confirmation/audit read rather than silently doing nothing.
            val rawMessages = intent.getParcelableArrayExtraCompat(
                NfcAdapter.EXTRA_NDEF_MESSAGES,
                NdefMessage::class.java
            )
            val lastDose = (rawMessages?.firstOrNull() as? NdefMessage)?.records?.firstOrNull()
                ?.let { String(it.payload) }

            if (lastDose != null) {
                Toast.makeText(this, "Pump tag last dose: $lastDose", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Pump tag detected, nothing queued to send", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /** One-time formatting of a brand-new/blank tag into a recognized pump tag. */
    private fun provisionPumpTag(intent: Intent) {
        val tag = intent.getParcelableExtraCompat(NfcAdapter.EXTRA_TAG, Tag::class.java)
        if (tag == null) {
            Toast.makeText(this, "No tag detected", Toast.LENGTH_SHORT).show()
            return
        }

        val ndef = android.nfc.tech.Ndef.get(tag)
        if (ndef == null) {
            Toast.makeText(this, "This tag doesn't support NDEF and can't be used as a pump tag", Toast.LENGTH_LONG).show()
            return
        }

        try {
            ndef.connect()
            val message = NdefMessage(arrayOf(NdefRecord.createMime(MIME_PUMP, "INSULIN:0".toByteArray())))
            ndef.writeNdefMessage(message)
            ndef.close()

            nfcMode = NfcMode.IDLE
            enableNfcDispatch() // drop the blank-tag filter now that we're done
            Toast.makeText(this, "Pump tag set up successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to set up pump tag: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun writeToNfcTag(intent: Intent, text: String) {
        val tag = intent.getParcelableExtraCompat(NfcAdapter.EXTRA_TAG, Tag::class.java)
        if (tag == null) {
            Toast.makeText(this, "No tag detected", Toast.LENGTH_SHORT).show()
            return
        }

        val ndef = android.nfc.tech.Ndef.get(tag)
        if (ndef != null) {
            ndef.connect()
            val message = NdefMessage(arrayOf(NdefRecord.createMime(MIME_PUMP, text.toByteArray())))
            ndef.writeNdefMessage(message)
            ndef.close()
        } else {
            Toast.makeText(this, "Tag not writable", Toast.LENGTH_SHORT).show()
        }
    }
}
