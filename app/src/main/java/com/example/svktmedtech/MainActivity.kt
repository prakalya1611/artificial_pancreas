package com.example.svktmedtech
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Build
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import android.nfc.NdefRecord




import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.svktmedtech.ui.theme.SvktMedtechTheme
import android.content.IntentFilter
class MainActivity : ComponentActivity() {
    // NFC connection state
    private var nfcAdapter: NfcAdapter? = null
    private var nfcPendingIntent: PendingIntent? = null

    // Observable state for the Compose UI
    private var nfcStatus by mutableStateOf("Tap Device to scan an NFC tag.")
    private var lastNfcPayload by mutableStateOf("No NFC tag read yet.")
    private var waitingForNfcTag by mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialise NFC
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        if (nfcAdapter == null) {
            nfcStatus = "NFC is not supported on this device."
        } else if (nfcAdapter?.isEnabled == false) {
            nfcStatus = "Please enable NFC in your phone settings."
        }

// Prepare the NFC foreground-dispatch intent
        val nfcIntent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        val pendingIntentFlags =
            PendingIntent.FLAG_UPDATE_CURRENT or
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        PendingIntent.FLAG_MUTABLE
                    } else {
                        0
                    }

        nfcPendingIntent = PendingIntent.getActivity(
            this,
            0,
            nfcIntent,
            pendingIntentFlags
        )
        setContent {
            SvktMedtechTheme {

                var showHome by rememberSaveable { mutableStateOf(false) }

                var currentScreen by rememberSaveable { mutableStateOf("login") }

                when (currentScreen) {
                    "login" -> {
                        LoginScreen(
                            onSignInClick = {
                                currentScreen = "home"
                            }
                        )
                    }

                    "home" -> {
                        HomeScreen(
                            onGraphClick = {
                                currentScreen = "graph"
                            },
                            onBolusClick = {
                                currentScreen = "bolus"
                            },
                            onRecordsClick = {
                                currentScreen = "records"
                            }
                        )
                    }

                    "graph" -> {
                        GraphScreen()
                    }

                    "bolus" -> {
                        BolusScreen(
                            onHomeClick = {
                                currentScreen = "home"
                            },
                            onGraphClick = {
                                currentScreen = "graph"
                            },
                            onRecordsClick = {
                                currentScreen = "records"
                            }
                        )
                    }

                    "records" -> {
                        RecordsScreen(
                            onHomeClick = {
                                currentScreen = "home"
                            },
                            onGraphClick = {
                                currentScreen = "graph"
                            },
                            onBolusClick = {
                                currentScreen = "bolus"
                            }
                        )
                    }
                }            }
        }
    }
}

@Composable
fun LoginScreen(onSignInClick: () -> Unit) {

    var email by remember { mutableStateOf("sarah@demo.com") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    val darkBlue = Color(0xFF1F304A)
    val teal = Color(0xFF168B87)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FC))
            .padding(24.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 45.dp)
        ) {

            // Logo and app name
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(teal),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⌁",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "GlucoSense Pro",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = darkBlue
                )
            }

            Spacer(modifier = Modifier.height(38.dp))

            Text(
                text = "Sign in",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = darkBlue
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Access your glucose management\ndashboard",
                fontSize = 14.sp,
                color = Color.Gray,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Email address",
                fontSize = 13.sp,
                color = darkBlue
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = teal,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Password",
                fontSize = 13.sp,
                color = darkBlue
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter your password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = teal,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = teal
                    )
                )

                Text(
                    text = "Remember me",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Forgot password?",
                    fontSize = 12.sp,
                    color = teal,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onSignInClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = darkBlue
                )
            ) {
                Text(
                    text = "Sign in",
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Demo accounts box
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(18.dp)
            ) {
                Text(
                    text = "Demo accounts",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Sarah Mitchell", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Text("sarah@demo.com", fontSize = 11.sp, color = Color.Gray)
                    }

                    Text(
                        "Patient",
                        fontSize = 12.sp,
                        color = teal
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Dr. Harrington", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Text("dr.h@demo.com", fontSize = 11.sp, color = Color.Gray)
                    }

                    Text(
                        "Physician",
                        fontSize = 12.sp,
                        color = teal
                    )
                }
            }
        }
    }override fun onResume() {
        super.onResume()

        if (waitingForNfcTag) {
            enableNfcDispatch()
        }
    }

    override fun onPause() {
        nfcAdapter?.disableForegroundDispatch(this)
        super.onPause()
    }

    private fun enableNfcDispatch() {
        val adapter = nfcAdapter ?: return
        val pending = nfcPendingIntent ?: return

        if (!waitingForNfcTag) return

        if (!adapter.isEnabled) {
            nfcStatus = "Please enable NFC in your phone settings."
            waitingForNfcTag = false
            return
        }

        val filter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED).apply {
            addCategory(Intent.CATEGORY_DEFAULT)

            try {
                addDataType("text/plain")
            } catch (e: IntentFilter.MalformedMimeTypeException) {
                nfcStatus = "Could not configure NFC scanning."
                waitingForNfcTag = false
                return
            }
        }

        adapter.enableForegroundDispatch(
            this,
            pending,
            arrayOf(filter),
            null
        )
    }override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        if (intent.action != NfcAdapter.ACTION_NDEF_DISCOVERED &&
            intent.action != NfcAdapter.ACTION_TAG_DISCOVERED &&
            intent.action != NfcAdapter.ACTION_TECH_DISCOVERED
        ) {
            return
        }

        val messages: Array<NdefMessage>? =
            if (Build.VERSION.SDK_INT >= 33) {
                intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES,
                    NdefMessage::class.java
                )
            } else {
                @Suppress("DEPRECATION")
                (intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES
                )?.filterIsInstance<NdefMessage>()?.toTypedArray())
            }

        val record = messages
            ?.firstOrNull()
            ?.records
            ?.firstOrNull()

        if (record == null) {
            nfcStatus = "Tag detected, but no readable NDEF text was found."
            lastNfcPayload = "No readable text"
            waitingForNfcTag = false

            Toast.makeText(
                this,
                nfcStatus,
                Toast.LENGTH_LONG
            ).show()

            nfcAdapter?.disableForegroundDispatch(this)
            return
        }

        val payload = record.payload

        val text = if (
            record.tnf == NdefRecord.TNF_WELL_KNOWN &&
            record.type.contentEquals(NdefRecord.RTD_TEXT) &&
            payload.isNotEmpty()
        ) {
            val statusByte = payload[0].toInt() and 0xFF
            val languageLength = statusByte and 0x3F
            val start = languageLength + 1

            if (start <= payload.size) {
                val charset =
                    if ((statusByte and 0x80) == 0) {
                        Charsets.UTF_8
                    } else {
                        Charsets.UTF_16
                    }

                String(
                    payload,
                    start,
                    payload.size - start,
                    charset
                )
            } else {
                String(payload, Charsets.UTF_8)
            }
        } else {
            String(payload, Charsets.UTF_8)
        }

        lastNfcPayload = text
        nfcStatus = "NFC tag read successfully"
        waitingForNfcTag = false

        Toast.makeText(
            this,
            "NFC data received",
            Toast.LENGTH_SHORT
        ).show()

        nfcAdapter?.disableForegroundDispatch(this)
    }private fun startNfcScan() {
        val adapter = nfcAdapter

        if (adapter == null) {
            nfcStatus = "NFC is not supported on this device."
            Toast.makeText(this, nfcStatus, Toast.LENGTH_LONG).show()
            return
        }

        if (!adapter.isEnabled) {
            nfcStatus = "Please enable NFC in your phone settings."
            Toast.makeText(this, nfcStatus, Toast.LENGTH_LONG).show()
            return
        }

        waitingForNfcTag = true
        nfcStatus = "Ready to scan. Hold your phone near an NFC tag."
        lastNfcPayload = "Waiting for a tag..."

        enableNfcDispatch()

        Toast.makeText(
            this,
            "Ready to scan. Hold your phone near an NFC tag.",
            Toast.LENGTH_LONG
        ).show()
    }
}