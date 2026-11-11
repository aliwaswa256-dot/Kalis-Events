package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.KalisBlack
import com.example.ui.theme.KalisBorder
import com.example.ui.theme.KalisCardSurface
import com.example.ui.theme.KalisCardSurfaceElevated
import com.example.ui.theme.KalisDarkCharcoal
import com.example.ui.theme.KalisSuccess
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.util.IntentUtils
import com.example.ui.viewmodel.Screen

@Composable
fun ContactScreen(
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(KalisBlack),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Brand Header & Logo
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = KalisCardSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(ElectricCyan.copy(0.4f), NeonPurple.copy(0.4f))))
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(KalisDarkCharcoal)
                            .border(1.5.dp, Brush.linearGradient(listOf(ElectricCyan, NeonPurple)), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.kalis_logo),
                            contentDescription = "KALIS EVENTS Official Brand Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(60.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "KALIS EVENTS UG",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                                color = TextPrimary
                            )
                        }
                        Text(
                            text = "Professional Event Production & Equipment Hire",
                            fontSize = 11.sp,
                            color = ElectricCyan,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Kampala • Entebbe • Jinja • Nationwide Uganda",
                            fontSize = 10.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }

        // Section: Direct Channels
        item {
            Text(
                text = "Contact & Customer Support",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
        }

        // WhatsApp Card
        item {
            ContactActionCard(
                icon = Icons.Default.Chat,
                iconColor = KalisSuccess,
                title = "Official WhatsApp Operations",
                subtitle = "+256 701 445566 (Instant quotes, gear photos & site visits)",
                actionLabel = "Open WhatsApp Chat",
                onClick = { IntentUtils.openWhatsApp(context) }
            )
        }

        // Phone Calls Card
        item {
            ContactActionCard(
                icon = Icons.Default.Call,
                iconColor = ElectricCyan,
                title = "24/7 Hotline & Dispatch Desk",
                subtitle = "Primary: +256 701 445566\nSecondary: +256 772 889900",
                actionLabel = "Call Operations Now",
                onClick = { IntentUtils.openDialer(context) }
            )
        }

        // Location & Google Maps Card
        item {
            ContactActionCard(
                icon = Icons.Default.Map,
                iconColor = NeonPurple,
                title = "Kampala Warehouse & Showroom",
                subtitle = "Plot 14, Lugogo Bypass / Jinja Road, Kampala, Uganda",
                actionLabel = "Open in Google Maps",
                onClick = { IntentUtils.openGoogleMaps(context) }
            )
        }

        // Email Support Card
        item {
            ContactActionCard(
                icon = Icons.Default.Email,
                iconColor = ElectricBlue,
                title = "Corporate Inquiries & Tenders",
                subtitle = "info@kalisevents.ug",
                actionLabel = "Send Official Email",
                onClick = { IntentUtils.openEmail(context) }
            )
        }

        // Hours & Technical Readiness
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = KalisCardSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(KalisBorder, KalisBorder)))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AccessTime, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Operational Hours",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "• Warehouse & Consultations: Monday – Saturday, 8:00 AM – 6:30 PM\n• Live Event Rigging & Technical Support: 24 Hours / 7 Days a week nationwide across Uganda.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Quick Navigation to Other Sections
        item {
            Text(
                text = "More Hub Features",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = KalisCardSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(KalisBorder, KalisBorder)))
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    HubNavigationRow(
                        icon = Icons.Default.PhotoLibrary,
                        iconTint = ElectricCyan,
                        title = "Event Production Showcase",
                        subtitle = "Concerts, Weddings & Festival Rigs in Uganda",
                        onClick = { onNavigate(Screen.Gallery) }
                    )

                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(KalisBorder.copy(alpha = 0.5f)))

                    HubNavigationRow(
                        icon = Icons.Default.Star,
                        iconTint = com.example.ui.theme.KalisGold,
                        title = "Client Reviews & Testimonials",
                        subtitle = "4.9★ from 150+ produced events",
                        onClick = { onNavigate(Screen.Reviews) }
                    )

                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(KalisBorder.copy(alpha = 0.5f)))

                    HubNavigationRow(
                        icon = Icons.Default.AdminPanelSettings,
                        iconTint = NeonPurple,
                        title = "Operations & Admin Hub",
                        subtitle = "Manage equipment inventory, bookings & broadcasts",
                        onClick = { onNavigate(Screen.Admin) }
                    )
                }
            }
        }
    }
}

@Composable
fun ContactActionCard(
    icon: ImageVector,
    iconColor: androidx.compose.ui.graphics.Color,
    title: String,
    subtitle: String,
    actionLabel: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = KalisCardSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(KalisBorder, KalisBorder)))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(iconColor.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = subtitle, fontSize = 12.sp, color = TextSecondary, lineHeight = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = iconColor.copy(alpha = 0.15f),
                    contentColor = iconColor
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(iconColor, iconColor)))
            ) {
                Text(text = actionLabel, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun HubNavigationRow(
    icon: ImageVector,
    iconTint: androidx.compose.ui.graphics.Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(iconTint.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                Text(text = subtitle, fontSize = 11.sp, color = TextSecondary)
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(18.dp)
        )
    }
}
