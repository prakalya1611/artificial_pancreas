package com.example.svktmedtech

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

private val BolusNavy = Color(0xFF111A30)
private val BolusTeal = Color(0xFF009F91)
private val BolusPurple = Color(0xFF8025F5)
private val BolusBackground = Color(0xFFF1F5F9)
private val BolusMuted = Color(0xFF8192AA)
private val BolusPill = Color(0xFFF0F4F8)

@Composable
fun BolusScreen(
    onHomeClick: () -> Unit = {},
    onGraphClick: () -> Unit = {},
    onRecordsClick: () -> Unit = {}
) {
    var units by remember { mutableStateOf(4.0) }
    var selectedMeal by remember { mutableStateOf("Lunch") }
    var showConfirmation by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BolusBackground)
    ) {

        // Top app bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "‹",
                color = BolusNavy,
                fontSize = 30.sp,
                modifier = Modifier.clickable { onHomeClick() }
            )

            Spacer(Modifier.width(12.dp))

            Text(
                text = "Bolus Input",
                color = BolusNavy,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.weight(1f))

            Text("☾", color = BolusMuted, fontSize = 20.sp)

            Spacer(Modifier.width(17.dp))

            Text("☰", color = BolusMuted, fontSize = 20.sp)

            Spacer(Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(
                        Color(0xFF2878EB),
                        RoundedCornerShape(50)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "SM",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Scrollable screen content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 13.dp)
        ) {

            Text(
                "Bolus Input",
                color = BolusNavy,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(4.dp))

            Text(
                "Calculate and deliver mealtime insulin dose",
                color = Color(0xFF637D9E),
                fontSize = 12.sp
            )

            Spacer(Modifier.height(17.dp))

            // Bolus units card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = 18.dp,
                        vertical = 19.dp
                    )
                ) {
                    Text(
                        "Bolus Units",
                        color = Color(0xFF506783),
                        fontSize = 12.sp
                    )

                    Spacer(Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(
                                    BolusPill,
                                    RoundedCornerShape(13.dp)
                                )
                                .clickable {
                                    units = (units - 0.5).coerceAtLeast(0.5)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("−", fontSize = 23.sp, color = BolusNavy)
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                String.format(Locale.US, "%.1f", units),
                                color = BolusNavy,
                                fontSize = 38.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                "units",
                                color = BolusMuted,
                                fontSize = 11.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(
                                    Color(0xFFC9FAF0),
                                    RoundedCornerShape(13.dp)
                                )
                                .clickable {
                                    units = (units + 0.5).coerceAtMost(8.0)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("+", fontSize = 22.sp, color = BolusTeal)
                        }
                    }

                    Spacer(Modifier.height(13.dp))

                    // Unit presets: 1u to 6u
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        (1..6).forEach { value ->
                            BolusUnitChip(
                                value = value,
                                selected = units == value.toDouble(),
                                modifier = Modifier.weight(1f),
                                onClick = { units = value.toDouble() }
                            )
                        }
                    }

                    Spacer(Modifier.height(6.dp))

                    // Unit presets: 7u and 8u
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        (7..8).forEach { value ->
                            BolusUnitChip(
                                value = value,
                                selected = units == value.toDouble(),
                                modifier = Modifier.width(34.dp),
                                onClick = { units = value.toDouble() }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(17.dp))

            // Meal type card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = 17.dp,
                        vertical = 17.dp
                    )
                ) {
                    Text(
                        "Meal Type",
                        color = Color(0xFF506783),
                        fontSize = 12.sp
                    )

                    Spacer(Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        listOf("Breakfast", "Lunch", "Dinner").forEach { meal ->
                            MealTypeChip(
                                meal = meal,
                                selected = selectedMeal == meal,
                                onClick = { selectedMeal = meal }
                            )
                        }
                    }

                    Spacer(Modifier.height(7.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        listOf("Snack", "Correction").forEach { meal ->
                            MealTypeChip(
                                meal = meal,
                                selected = selectedMeal == meal,
                                onClick = { selectedMeal = meal }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Deliver button — demo interaction only
            Button(
                onClick = { showConfirmation = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(49.dp),
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BolusNavy
                )
            ) {
                Text(
                    "Deliver ${String.format(Locale.US, "%.1f", units)} units",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(14.dp))

            // Recent boluses card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column {
                    Text(
                        "Recent Boluses",
                        modifier = Modifier.padding(
                            horizontal = 17.dp,
                            vertical = 15.dp
                        ),
                        color = BolusNavy,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    HorizontalDivider(color = Color(0xFFF0F2F6))

                    RecentBolusRow(
                        meal = "Breakfast",
                        time = "08:30 AM",
                        glucose = "142 mg/dL",
                        units = "4.5u"
                    )

                    RecentBolusRow(
                        meal = "Lunch",
                        time = "01:15 PM",
                        glucose = "158 mg/dL",
                        units = "5u"
                    )

                    RecentBolusRow(
                        meal = "Dinner",
                        time = "07:45 PM",
                        glucose = "136 mg/dL",
                        units = "3.5u"
                    )

                    RecentBolusRow(
                        meal = "Correction",
                        time = "10:30 PM",
                        glucose = "187 mg/dL",
                        units = "1u",
                        isLast = true
                    )
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
                .padding(top = 8.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BolusNavItem("⌗", "Home", false, onHomeClick)
            BolusNavItem("↗", "Graph", false, onGraphClick)
            BolusNavItem("♧", "Bolus", true, {})
            BolusNavItem("▤", "Records", false, onRecordsClick)
        }
    }

    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = { showConfirmation = false },
            title = { Text("Prototype only") },
            text = {
                Text(
                    "This SVKT screen is a UI prototype. It does not calculate, " +
                            "recommend, or deliver insulin. No dose has been delivered."
                )
            },
            confirmButton = {
                TextButton(onClick = { showConfirmation = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
private fun BolusUnitChip(
    value: Int,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(28.dp)
            .background(
                if (selected) BolusTeal else BolusPill,
                RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "${value}u",
            color = if (selected) Color.White else Color(0xFF425775),
            fontSize = 10.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun MealTypeChip(
    meal: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                if (selected) BolusPurple else BolusPill,
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 13.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            meal,
            fontSize = 11.sp,
            color = if (selected) Color.White else Color(0xFF425775),
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun RecentBolusRow(
    meal: String,
    time: String,
    glucose: String,
    units: String,
    isLast: Boolean = false
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 17.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    meal,
                    color = BolusNavy,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(3.dp))

                Text(
                    "$time · $glucose",
                    color = BolusMuted,
                    fontSize = 10.sp
                )
            }

            Text(
                units,
                color = BolusNavy,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (!isLast) {
            HorizontalDivider(color = Color(0xFFF0F2F6))
        }
    }
}

@Composable
private fun BolusNavItem(
    icon: String,
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .width(64.dp)
            .padding(vertical = 1.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .background(
                    if (selected) BolusTeal else Color.Transparent,
                    RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                icon,
                color = if (selected) Color.White else BolusMuted,
                fontSize = 19.sp
            )
        }

        Spacer(Modifier.height(3.dp))

        Text(
            title,
            color = if (selected) BolusTeal else BolusMuted,
            fontSize = 10.sp
        )
    }
}

