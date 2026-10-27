package com.example.svktmedtech

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val GraphNavy = Color(0xFF13213A)
private val GraphTeal = Color(0xFF009F91)
private val GraphBackground = Color(0xFFF1F5FA)
private val GraphMuted = Color(0xFF8496AF)
private val GraphRed = Color(0xFFFF7785)
private val GraphBlue = Color(0xFF6DA4FF)

@Composable
fun GraphScreen() {
    var selectedRange by remember { mutableStateOf("24H") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GraphBackground)
    ) {
        // Top app bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .padding(horizontal = 15.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("‹", fontSize = 29.sp, color = GraphNavy)

            Spacer(Modifier.width(12.dp))

            Text(
                "Glucose Graph",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = GraphNavy
            )

            Spacer(Modifier.weight(1f))

            Text("☾", color = GraphMuted, fontSize = 20.sp)
            Spacer(Modifier.width(15.dp))
            Text("☰", color = GraphMuted, fontSize = 20.sp)
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

        // Scrollable content; navigation stays at the bottom
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Text(
                "Glucose Graph",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = GraphNavy
            )

            Spacer(Modifier.height(4.dp))

            Text(
                "Historical glucose trends and analysis",
                fontSize = 12.sp,
                color = Color(0xFF657F9E)
            )

            Spacer(Modifier.height(15.dp))

            // Summary statistics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                GraphStat(
                    title = "Time in\nRange",
                    value = "100%",
                    detail = "Target:\n>70%",
                    valueColor = GraphTeal,
                    modifier = Modifier.weight(1f)
                )

                GraphStat(
                    title = "Average",
                    value = "133",
                    detail = "mg/dL",
                    valueColor = GraphTeal,
                    modifier = Modifier.weight(1f)
                )

                GraphStat(
                    title = "High\nEvents",
                    value = "0",
                    detail = ">180\nmg/dL",
                    valueColor = Color(0xFFFF3535),
                    modifier = Modifier.weight(1f)
                )

                GraphStat(
                    title = "Low\nEvents",
                    value = "0",
                    detail = "<70\nmg/dL",
                    valueColor = Color(0xFF3478F6),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(15.dp))

            // Glucose trend card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Glucose\nTrend",
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = GraphNavy
                    )

                    Spacer(Modifier.weight(1f))

                    listOf("1H", "6H", "24H", "7D", "30D").forEach { range ->
                        Box(
                            modifier = Modifier
                                .padding(start = 2.dp)
                                .background(
                                    if (selectedRange == range)
                                        Color(0xFFF0F3F8)
                                    else Color.Transparent,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedRange = range }
                                .padding(horizontal = 6.dp, vertical = 8.dp)
                        ) {
                            Text(
                                range,
                                fontSize = 10.sp,
                                color = if (selectedRange == range)
                                    GraphNavy
                                else GraphMuted,
                                fontWeight = if (selectedRange == range)
                                    FontWeight.Bold
                                else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(Modifier.height(18.dp))

                // Axis labels + real line-chart drawing
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(218.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .width(27.dp)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.Start
                    ) {
                        listOf("220", "185", "140", "95", "50").forEach {
                            Text(
                                it,
                                fontSize = 9.sp,
                                color = GraphMuted
                            )
                        }
                    }

                    Canvas(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        val chartWidth = size.width
                        val chartHeight = size.height
                        val px = 1.dp.toPx()

                        fun yFor(value: Float): Float {
                            return ((220f - value) / 170f) * chartHeight
                        }

                        val dashed = PathEffect.dashPathEffect(
                            floatArrayOf(3.dp.toPx(), 3.dp.toPx())
                        )

                        // Target glucose zone: approximately 70–180 mg/dL
                        drawRect(
                            color = Color(0xFFEFFAF6),
                            topLeft = Offset(0f, yFor(180f)),
                            size = Size(
                                chartWidth,
                                yFor(70f) - yFor(180f)
                            )
                        )

                        // Horizontal grid lines
                        listOf(220f, 185f, 140f, 95f, 50f).forEach { value ->
                            drawLine(
                                color = Color(0xFFE5ECF2),
                                start = Offset(0f, yFor(value)),
                                end = Offset(chartWidth, yFor(value)),
                                strokeWidth = px,
                                pathEffect = dashed
                            )
                        }

                        // Vertical dotted grid lines
                        for (i in 0..8) {
                            val x = chartWidth * i / 8f
                            drawLine(
                                color = Color(0xFFEAF0F5),
                                start = Offset(x, 0f),
                                end = Offset(x, chartHeight),
                                strokeWidth = px,
                                pathEffect = dashed
                            )
                        }

                        // High and low target boundaries
                        drawLine(
                            color = GraphRed,
                            start = Offset(0f, yFor(180f)),
                            end = Offset(chartWidth, yFor(180f)),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = dashed
                        )

                        drawLine(
                            color = GraphBlue,
                            start = Offset(0f, yFor(70f)),
                            end = Offset(chartWidth, yFor(70f)),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = dashed
                        )

                        // Sample values used for the prototype graph
                        val values = listOf(
                            116f, 121f, 118f, 130f, 125f,
                            135f, 128f, 121f, 133f, 129f,
                            144f, 137f, 142f, 136f, 140f,
                            130f, 137f, 132f
                        )

                        val points = values.mapIndexed { index, value ->
                            Offset(
                                x = chartWidth * index / (values.size - 1f),
                                y = yFor(value)
                            )
                        }

                        // Smooth teal glucose trend line
                        val path = Path().apply {
                            moveTo(points.first().x, points.first().y)

                            for (i in 1 until points.size - 1) {
                                val current = points[i]
                                val next = points[i + 1]

                                quadraticTo(
                                    current.x,
                                    current.y,
                                    (current.x + next.x) / 2f,
                                    (current.y + next.y) / 2f
                                )
                            }

                            lineTo(points.last().x, points.last().y)
                        }

                        drawPath(
                            path = path,
                            color = GraphTeal,
                            style = Stroke(width = 2.dp.toPx())
                        )

                        // Last reading marker
                        drawCircle(
                            color = GraphTeal,
                            radius = 3.dp.toPx(),
                            center = points.last()
                        )
                    }
                }

                Spacer(Modifier.height(5.dp))

                // Time labels below the x-axis
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 27.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("13:57", "16:57", "18:57", "20:27", "21:57").forEach {
                        Text(it, fontSize = 8.sp, color = GraphMuted)
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GraphLegend("High\n(>180)", GraphRed)
                    GraphLegend("Target (70–\n180)", Color(0xFF61D4B4))
                    GraphLegend("Low\n(<70)", GraphBlue)
                }
            }

            Spacer(Modifier.height(12.dp))
        }

        // Fixed bottom navigation — remains at the bottom of the phone
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .navigationBarsPadding()
                .padding(top = 7.dp, bottom = 7.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GraphBottomItem("⌂", "Home", false)
            GraphBottomItem("↗", "Graph", true)
            GraphBottomItem("♧", "Bolus", false)
            GraphBottomItem("▤", "Records", false)
        }
    }
}

@Composable
private fun GraphStat(
    title: String,
    value: String,
    detail: String,
    valueColor: Color,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .height(112.dp)
            .background(Color.White, RoundedCornerShape(15.dp))
            .padding(9.dp)
    ) {
        Text(title, fontSize = 9.sp, lineHeight = 12.sp, color = GraphMuted)

        Spacer(Modifier.height(5.dp))

        Text(
            value,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )

        Spacer(Modifier.height(2.dp))

        Text(
            detail,
            fontSize = 9.sp,
            lineHeight = 12.sp,
            color = GraphMuted
        )
    }
}

@Composable
private fun GraphLegend(
    label: String,
    color: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, RoundedCornerShape(50))
        )

        Spacer(Modifier.width(5.dp))

        Text(
            label,
            fontSize = 9.sp,
            lineHeight = 11.sp,
            color = GraphMuted
        )
    }
}

@Composable
private fun GraphBottomItem(
    icon: String,
    label: String,
    selected: Boolean
) {
    Column(
        modifier = Modifier.width(64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .background(
                    if (selected) GraphTeal else Color.Transparent,
                    RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                icon,
                fontSize = 19.sp,
                color = if (selected) Color.White else GraphMuted
            )
        }

        Spacer(Modifier.height(3.dp))

        Text(
            label,
            fontSize = 10.sp,
            color = if (selected) GraphTeal else GraphMuted
        )
    }
}