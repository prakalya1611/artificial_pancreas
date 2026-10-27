package com.example.svktmedtech

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val RecordsNavy = Color(0xFF13213A)
private val RecordsTeal = Color(0xFF07998E)
private val RecordsPurple = Color(0xFF655FF5)
private val RecordsBg = Color(0xFFF1F5FA)
private val RecordsMuted = Color(0xFF8395AE)

@Composable
fun RecordsScreen(
    onHomeClick: () -> Unit = {},
    onGraphClick: () -> Unit = {},
    onBolusClick: () -> Unit = {}
) {
    var selectedPeriod by remember { mutableStateOf("Weekly") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RecordsBg)
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
            Text(
                "‹",
                color = RecordsNavy,
                fontSize = 29.sp,
                modifier = Modifier.clickable { onHomeClick() }
            )

            Spacer(Modifier.width(12.dp))

            Text(
                "Records",
                color = RecordsNavy,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.weight(1f))

            Text("☾", color = RecordsMuted, fontSize = 19.sp)
            Spacer(Modifier.width(15.dp))
            Text("☰", color = RecordsMuted, fontSize = 20.sp)
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

        // Scrollable Records content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Text(
                "Insulin Records",
                color = RecordsNavy,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(4.dp))

            Text(
                "Detailed insulin delivery history",
                color = Color(0xFF657F9E),
                fontSize = 12.sp
            )

            Spacer(Modifier.height(15.dp))

            // Today / Yesterday / Weekly / Monthly
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("Today", "Yesterday", "Weekly", "Monthly").forEach { period ->
                    Box(
                        modifier = Modifier
                            .background(
                                if (selectedPeriod == period) Color.White
                                else Color.Transparent,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { selectedPeriod = period }
                            .padding(horizontal = 10.dp, vertical = 9.dp)
                    ) {
                        Text(
                            period,
                            color = if (selectedPeriod == period)
                                RecordsNavy else Color(0xFF526987),
                            fontSize = 10.sp,
                            fontWeight = if (selectedPeriod == period)
                                FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Summary cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                RecordsSummaryCard(
                    title = if (selectedPeriod == "Weekly")
                        "Total (7\ndays)" else "Total",
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        if (selectedPeriod == "Today") "33.1U"
                        else if (selectedPeriod == "Yesterday") "31.5U"
                        else if (selectedPeriod == "Monthly") "990U"
                        else "231U",
                        color = RecordsTeal,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                RecordsSummaryCard(
                    title = "Daily\nAverage",
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        if (selectedPeriod == "Today") "33.1U"
                        else if (selectedPeriod == "Yesterday") "31.5U"
                        else if (selectedPeriod == "Monthly") "33.0U"
                        else "33.1U",
                        color = RecordsPurple,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                RecordsSummaryCard(
                    title = "Bolus /\nBasal",
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "42%",
                        color = Color(0xFFFF9900),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "/",
                        color = Color(0xFFFF9900),
                        fontSize = 17.sp
                    )
                    Text(
                        "58%",
                        color = Color(0xFFFF9900),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            // Daily breakdown chart card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Text(
                    if (selectedPeriod == "Weekly") "Daily Breakdown"
                    else "$selectedPeriod Breakdown",
                    color = RecordsNavy,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(172.dp)
                ) {
                    // Vertical axis labels
                    Column(
                        modifier = Modifier
                            .width(23.dp)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.End
                    ) {
                        listOf("36", "27", "18", "9", "0").forEach { label ->
                            Text(
                                label,
                                color = RecordsMuted,
                                fontSize = 9.sp
                            )
                        }
                    }

                    Spacer(Modifier.width(5.dp))

                    // Stacked bolus/basal bar chart
                    Canvas(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        val chartWidth = size.width
                        val chartHeight = size.height
                        val maxValue = 36f

                        fun y(value: Float): Float =
                            chartHeight - (value / maxValue) * chartHeight

                        // Horizontal grid lines
                        listOf(0f, 9f, 18f, 27f, 36f).forEach { value ->
                            drawLine(
                                color = Color(0xFFE8EDF4),
                                start = Offset(0f, y(value)),
                                end = Offset(chartWidth, y(value)),
                                strokeWidth = 1.dp.toPx()
                            )
                        }

                        // Sample weekly entries: bolus and basal units
                        val bolus = listOf(
                            12f, 13f, 11f, 15f, 10f, 12f, 11f
                        )
                        val basal = listOf(
                            20f, 21f, 19f, 21f, 19f, 21f, 21f
                        )

                        val groupWidth = chartWidth / 7f
                        val barWidth = groupWidth * 0.68f

                        for (i in 0..6) {
                            val left =
                                i * groupWidth + (groupWidth - barWidth) / 2f

                            val bolusTop = y(bolus[i])
                            val totalTop = y(bolus[i] + basal[i])
                            val bottom = chartHeight

                            // Lower purple part: bolus
                            drawRect(
                                color = RecordsPurple,
                                topLeft = Offset(left, bolusTop),
                                size = Size(
                                    barWidth,
                                    bottom - bolusTop
                                )
                            )

                            // Upper teal part: basal
                            drawRect(
                                color = RecordsTeal,
                                topLeft = Offset(left, totalTop),
                                size = Size(
                                    barWidth,
                                    bolusTop - totalTop
                                )
                            )
                        }
                    }
                }

                Spacer(Modifier.height(7.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 28.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                        .forEach {
                            Text(
                                it,
                                color = RecordsMuted,
                                fontSize = 9.sp
                            )
                        }
                }
            }

            Spacer(Modifier.height(12.dp))
        }

        // Fixed bottom navigation
        HorizontalDivider(color = Color(0xFFE9EDF3))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .navigationBarsPadding()
                .padding(top = 7.dp, bottom = 7.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RecordsBottomItem("⌂", "Home", false, onHomeClick)
            RecordsBottomItem("↗", "Graph", false, onGraphClick)
            RecordsBottomItem("♧", "Bolus", false, onBolusClick)
            RecordsBottomItem("▤", "Records", true, {})
        }
    }
}

@Composable
private fun RecordsSummaryCard(
    title: String,
    modifier: Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .height(122.dp)
            .background(Color.White, RoundedCornerShape(14.dp))
            .padding(horizontal = 5.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            title,
            color = RecordsMuted,
            fontSize = 9.sp,
            lineHeight = 12.sp
        )

        Spacer(Modifier.height(7.dp))

        content()
    }
}

@Composable
private fun RecordsBottomItem(
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
                    if (selected) RecordsTeal else Color.Transparent,
                    RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                icon,
                color = if (selected) Color.White else RecordsMuted,
                fontSize = 19.sp
            )
        }

        Spacer(Modifier.height(3.dp))

        Text(
            label,
            fontSize = 10.sp,
            color = if (selected) RecordsTeal else RecordsMuted
        )
    }
}