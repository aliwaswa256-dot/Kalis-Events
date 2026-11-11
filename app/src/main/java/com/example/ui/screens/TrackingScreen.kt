package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookingEntity
import com.example.data.repository.KalisRepository
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.KalisBlack
import com.example.ui.theme.KalisBorder
import com.example.ui.theme.KalisCardSurface
import com.example.ui.theme.KalisCardSurfaceElevated
import com.example.ui.theme.KalisDarkCharcoal
import com.example.ui.theme.KalisSuccess
import com.example.ui.theme.KalisWarning
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.util.IntentUtils
import com.example.ui.viewmodel.Screen

@Composable
fun TrackingScreen(
    bookings: List<BookingEntity>,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KalisBlack)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Text(
                text = "My Bookings & Quotations",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
            Text(
                text = "Live tracking of event gear reservations and technical dispatch",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }

        if (bookings.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Assignment,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No event bookings yet",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Text(
                        text = "Build a customized quotation request for sound, stage, tents or lighting.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onNavigate(Screen.Catalogue) },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan, contentColor = KalisBlack),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Browse Equipment Fleet", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(bookings, key = { it.id }) { booking ->
                    BookingTrackingCard(
                        booking = booking,
                        onWhatsAppClick = {
                            val msg = "Hello KALIS EVENTS UG, regarding Booking #${booking.id} for ${booking.eventType} on ${booking.startDate} at ${booking.eventLocation}. Total: ${KalisRepository.formatUgx(booking.totalAmountUgx)}."
                            IntentUtils.openWhatsApp(context, msg)
                        },
                        onCallClick = {
                            IntentUtils.openDialer(context)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BookingTrackingCard(
    booking: BookingEntity,
    onWhatsAppClick: () -> Unit,
    onCallClick: () -> Unit
) {
    val (statusLabel, statusColor) = when (booking.status) {
        "PENDING_REVIEW" -> Pair("Under Review", KalisWarning)
        "QUOTED" -> Pair("Quotation Ready", ElectricCyan)
        "CONFIRMED" -> Pair("Confirmed & Reserved", KalisSuccess)
        "SETUP_DISPATCHED" -> Pair("Rigging Dispatched", ElectricBlue)
        "COMPLETED" -> Pair("Completed", NeonPurple)
        else -> Pair(booking.status, TextSecondary)
    }

    val stepIndex = when (booking.status) {
        "PENDING_REVIEW" -> 1
        "QUOTED" -> 2
        "CONFIRMED" -> 3
        "SETUP_DISPATCHED" -> 4
        "COMPLETED" -> 5
        else -> 1
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("booking_card_${booking.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = KalisCardSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(KalisBorder, statusColor.copy(alpha = 0.4f))))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with ID & Status Chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "#${booking.id}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = ElectricCyan
                    )
                }

                Box(
                    modifier = Modifier
                        .background(statusColor.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                        .border(1.dp, statusColor.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = statusLabel.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = booking.eventType,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = booking.eventLocation, fontSize = 12.sp, color = TextSecondary)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Event, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${booking.startDate} • ${booking.durationDays} ${if (booking.durationDays == 1) "Day" else "Days"}",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Step Progress Timeline
            TrackingTimeline(currentStep = stepIndex)

            Spacer(modifier = Modifier.height(14.dp))

            // Equipment list summary
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(KalisDarkCharcoal, RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(text = "EQUIPMENT INCLUDED", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = booking.equipmentNames,
                        fontSize = 12.sp,
                        color = TextPrimary,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Total Estimated Cost:", fontSize = 12.sp, color = TextSecondary)
                        Text(
                            text = KalisRepository.formatUgx(booking.totalAmountUgx),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onCallClick,
                    shape = RoundedCornerShape(10.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(KalisBorder, KalisBorder))),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Call, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Call Hotline", color = TextSecondary, fontSize = 12.sp)
                }

                Button(
                    onClick = onWhatsAppClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan, contentColor = KalisBlack),
                    modifier = Modifier.weight(1.2f)
                ) {
                    Icon(imageVector = Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("WhatsApp Update", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun TrackingTimeline(currentStep: Int) {
    val steps = listOf("Received", "Quoted", "Confirmed", "Dispatched", "Complete")

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        steps.forEachIndexed { index, stepName ->
            val stepNumber = index + 1
            val isDone = stepNumber <= currentStep
            val isCurrent = stepNumber == currentStep

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(
                            when {
                                isDone -> ElectricCyan
                                else -> KalisBorder
                            },
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isDone) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = KalisBlack,
                            modifier = Modifier.size(14.dp)
                        )
                    } else {
                        Text(
                            text = "$stepNumber",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stepName,
                    fontSize = 9.sp,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                    color = if (isDone) TextPrimary else TextMuted
                )
            }
        }
    }
}
