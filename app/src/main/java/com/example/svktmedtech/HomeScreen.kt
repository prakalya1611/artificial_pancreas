package com.example.svktmedtech

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val HomeNavy = Color(0xFF111A30)
private val HomeTeal = Color(0xFF009F91)
private val HomeBlue = Color(0xFF3478FA)
private val HomeBg = Color(0xFFF1F5FA)
private val HomeMuted = Color(0xFF8495AC)

@Composable
fun HomeScreen(
    onGraphClick: () -> Unit,
    onBolusClick: () -> Unit,
    onRecordsClick: () -> Unit = {}
) {
    var showDeviceNotice by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HomeBg)
    ) {

        // TOP HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(HomeNavy)
                .statusBarsPadding()
                .padding(horizontal = 15.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(HomeTeal, RoundedCornerShape(9.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("⌁", color = Color.White, fontSize = 20.sp)
            }

            Spacer(Modifier.width(8.dp))

            Text(
                "GlucoSense",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.weight(1f))

            Text("☾", color = Color(0xFFB5C4D8), fontSize = 19.sp)

            Spacer(Modifier.width(16.dp))

            Text("☰", color = Color(0xFFB5C4D8), fontSize = 20.sp)

            Spacer(Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(Color(0xFF2878EB), RoundedCornerShape(50)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "SM",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // SCROLLABLE DASHBOARD CONTENT
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {

            // CURRENT GLUCOSE CARD
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                Color(0xFF172B49),
                                Color(0xFF164A67),
                                Color(0xFF079A8C)
                            )
                        ),
                        shape = RoundedCornerShape(22.dp)
                    )
                    .padding(18.dp)
            ) {
                Text(
                    "CURRENT GLUCOSE",
                    color = Color(0xFFB7C9E0),
                    fontSize = 10.sp,
                    letterSpacing = 1.sp
                )

                Spacer(Modifier.height(3.dp))

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        "126",
                        fontSize = 46.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(Modifier.width(7.dp))

                    Text(
                        "mg/dL",
                        fontSize = 13.sp,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 7.dp)
                    )
                }

                Spacer(Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "✓ Normal",
                        color = Color(0xFF23D6A6),
                        fontSize = 11.sp,
                        modifier = Modifier
                            .background(
                                Color(0x3323D6A6),
                                RoundedCornerShape(9.dp)
                            )
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                    )

                    Spacer(Modifier.width(10.dp))

                    Text(
                        "↗ Rising",
                        color = Color(0xFFFFB44C),
                        fontSize = 11.sp
                    )
                }

                Spacer(Modifier.height(12.dp))

                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0x335FFFFFF))
                )

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "◷ Not synced yet",
                        fontSize = 10.sp,
                        color = Color(0xFFCCD7E5)
                    )

                    Text(
                        "♨ Insulin: 62%",
                        fontSize = 10.sp,
                        color = Color(0xFFCCD7E5)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // TAP DEVICE BUTTON
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color(0xFF0CA99A),
                                Color(0xFF079BB5)
                            )
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .clickable { showDeviceNotice = true },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⌁", fontSize = 21.sp, color = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Tap Device",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            // CGM GRAPH CARD
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .padding(15.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text(
                            "CGM Graph",
                            color = HomeNavy,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            "Last 60 minutes · every 5 min",
                            color = HomeMuted,
                            fontSize = 10.sp
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    Text(
                        "Full view  ›",
                        fontSize = 10.sp,
                        color = HomeTeal,
                        modifier = Modifier.clickable { onGraphClick() }
                    )
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(145.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .width(26.dp)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("220", "180", "140", "100").forEach { label ->
                            Text(label, fontSize = 8.sp, color = HomeMuted)
                        }
                    }

                    Canvas(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        val w = size.width
                        val h = size.height
                        val unit = 1.dp.toPx()

                        fun y(value: Float): Float =
                            ((220f - value) / 170f) * h

                        val dash = PathEffect.dashPathEffect(
                            floatArrayOf(3.dp.toPx(), 3.dp.toPx())
                        )

                        // Target range shading
                        drawRect(
                            color = Color(0xFFEEFAF6),
                            topLeft = Offset(0f, y(180f)),
                            size = Size(w, y(70f) - y(180f))
                        )

                        // Grid lines
                        listOf(220f, 180f, 140f, 100f, 70f, 50f).forEach {
                            drawLine(
                                color = Color(0xFFE7EDF4),
                                start = Offset(0f, y(it)),
                                end = Offset(w, y(it)),
                                strokeWidth = unit,
                                pathEffect = dash
                            )
                        }

                        // Clinical threshold guides (visual reference only)
                        drawLine(
                            color = Color(0xFFFFB45F),
                            start = Offset(0f, y(180f)),
                            end = Offset(w, y(180f)),
                            strokeWidth = unit,
                            pathEffect = dash
                        )

                        drawLine(
                            color = Color(0xFFFF9090),
                            start = Offset(0f, y(70f)),
                            end = Offset(w, y(70f)),
                            strokeWidth = unit,
                            pathEffect = dash
                        )

                        // Sample readings, drawn as a continuous line
                        val readings = listOf(
                            126f, 124f, 123f, 127f, 126f,
                            130f, 127f, 125f, 129f, 132f,
                            131f, 134f, 132f, 130f, 136f,
                            135f, 138f, 136f
                        )

                        val points = readings.mapIndexed { index, value ->
                            Offset(
                                w * index / (readings.size - 1f),
                                y(value)
                            )
                        }

                        val line = Path().apply {
                            moveTo(points.first().x, points.first().y)

                            for (i in 1 until points.size - 1) {
                                val p = points[i]
                                val next = points[i + 1]

                                quadraticTo(
                                    p.x,
                                    p.y,
                                    (p.x + next.x) / 2f,
                                    (p.y + next.y) / 2f
                                )
                            }

                            lineTo(points.last().x, points.last().y)
                        }

                        drawPath(
                            path = line,
                            color = HomeBlue,
                            style = Stroke(width = 2.dp.toPx())
                        )

                        points.forEach { point ->
                            drawCircle(
                                color = HomeBlue,
                                radius = 1.8.dp.toPx(),
                                center = point
                            )
                        }

                        drawCircle(
                            color = Color.White,
                            radius = 5.dp.toPx(),
                            center = points.last()
                        )

                        drawCircle(
                            color = HomeBlue,
                            radius = 3.dp.toPx(),
                            center = points.last()
                        )
                    }
                }

                Spacer(Modifier.height(6.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 26.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("13:22", "13:37", "13:52", "14:07").forEach {
                        Text(it, color = HomeMuted, fontSize = 8.sp)
                    }
                }

                Spacer(Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    HomeLegend("Low <70", Color(0xFFFF515A))
                    HomeLegend("Normal 70–180", Color(0xFF16B994))
                    HomeLegend("High >180", Color(0xFFFFA000))
                }
            }

            Spacer(Modifier.height(12.dp))

            // STATISTICS CARD — SIX TILES
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .padding(15.dp)
            ) {
                Text(
                    "Statistics",
                    color = HomeNavy,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HomeStatTile(
                        "Current", "128", "mg/dL",
                        HomeTeal, Modifier.weight(1f)
                    )
                    HomeStatTile(
                        "Average", "123", "mg/dL",
                        HomeTeal, Modifier.weight(1f)
                    )
                    HomeStatTile(
                        "Highest", "128", "mg/dL",
                        Color(0xFFFFA000), Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HomeStatTile(
                        "Lowest", "117", "mg/dL",
                        HomeTeal, Modifier.weight(1f)
                    )
                    HomeStatTile(
                        "Readings", "12", "total",
                        HomeTeal, Modifier.weight(1f)
                    )
                    HomeStatTile(
                        "In Range", "100%", "TIR",
                        HomeTeal, Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // TWO-BY-TWO QUICK ACTIONS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HomeActionCard(
                    icon = "♧",
                    title = "Live",
                    subtitle = "Monitoring",
                    modifier = Modifier.weight(1f),
                    onClick = { showDeviceNotice = true }
                )

                HomeActionCard(
                    icon = "▧",
                    title = "Glucose",
                    subtitle = "Graph",
                    modifier = Modifier.weight(1f),
                    onClick = onGraphClick
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HomeActionCard(
                    icon = "♧",
                    title = "Bolus",
                    subtitle = "Input",
                    modifier = Modifier.weight(1f),
                    onClick = onBolusClick
                )

                HomeActionCard(
                    icon = "▤",
                    title = "Records",
                    subtitle = "",
                    modifier = Modifier.weight(1f),
                    onClick = onRecordsClick
                )
            }

            Spacer(Modifier.height(12.dp))
        }

        // FIXED BOTTOM NAVIGATION
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .navigationBarsPadding()
                .padding(top = 7.dp, bottom = 7.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HomeBottomItem("⌂", "Home", true, {})
            HomeBottomItem("↗", "Graph", false, onGraphClick)
            HomeBottomItem("♧", "Bolus", false, onBolusClick)
            HomeBottomItem("▤", "Records", false, onRecordsClick)
        }
    }

    if (showDeviceNotice) {
        AlertDialog(
            onDismissRequest = { showDeviceNotice = false },
            title = { Text("Device connection") },
            text = {
                Text(
                    "Device connection is not implemented yet. " +
                            "The glucose values displayed here are sample prototype data."
                )
            },
            confirmButton = {
                TextButton(onClick = { showDeviceNotice = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
private fun HomeLegend(
    label: String,
    color: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .background(color, RoundedCornerShape(50))
        )

        Spacer(Modifier.width(4.dp))

        Text(label, color = HomeMuted, fontSize = 8.sp)
    }
}

@Composable
private fun HomeStatTile(
    title: String,
    value: String,
    detail: String,
    valueColor: Color,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .background(HomeBg, RoundedCornerShape(14.dp))
            .padding(vertical = 11.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, fontSize = 9.sp, color = HomeMuted)

        Spacer(Modifier.height(5.dp))

        Text(
            value,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )

        Spacer(Modifier.height(2.dp))

        Text(detail, fontSize = 9.sp, color = HomeMuted)
    }
}

@Composable
private fun HomeActionCard(
    icon: String,
    title: String,
    subtitle: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .height(66.dp)
            .background(Color.White, RoundedCornerShape(15.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(
                    Color(0xFFE7F5F5),
                    RoundedCornerShape(11.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 19.sp, color = HomeTeal)
        }

        Spacer(Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                fontSize = 11.sp,
                color = HomeNavy,
                fontWeight = FontWeight.Medium
            )

            if (subtitle.isNotEmpty()) {
                Text(
                    subtitle,
                    fontSize = 11.sp,
                    color = HomeNavy,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Text("›", fontSize = 17.sp, color = HomeMuted)
    }
}

@Composable
private fun HomeBottomItem(
    icon: String,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(62.dp)
            .clickable(onClick = onClick)
            .padding(vertical = 1.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .background(
                    if (selected) HomeTeal else Color.Transparent,
                    RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                icon,
                fontSize = 19.sp,
                color = if (selected) Color.White else HomeMuted
            )
        }

        Spacer(Modifier.height(3.dp))

        Text(
            label,
            fontSize = 10.sp,
            color = if (selected) HomeTeal else HomeMuted
        )
    }
}