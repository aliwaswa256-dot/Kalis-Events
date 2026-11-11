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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.EquipmentEntity
import com.example.data.model.ReviewEntity
import com.example.data.repository.KalisRepository
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.KalisBlack
import com.example.ui.theme.KalisBorder
import com.example.ui.theme.KalisCardSurface
import com.example.ui.theme.KalisCardSurfaceElevated
import com.example.ui.theme.KalisDarkCharcoal
import com.example.ui.theme.KalisGold
import com.example.ui.theme.KalisSuccess
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.util.IntentUtils
import com.example.ui.viewmodel.Screen

data class ServiceCategoryItem(
    val name: String,
    val description: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun HomeScreen(
    featuredEquipment: List<EquipmentEntity>,
    recentReviews: List<ReviewEntity>,
    onSelectEquipment: (EquipmentEntity) -> Unit,
    onToggleCartItem: (EquipmentEntity) -> Unit,
    isInCart: (EquipmentEntity) -> Boolean,
    onNavigate: (Screen) -> Unit,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val serviceCategories = listOf(
        ServiceCategoryItem("Sound Systems", "Concert Line Arrays & PA", Icons.Default.VolumeUp, ElectricCyan),
        ServiceCategoryItem("Stage Setup", "Ground Support & Roof Truss", Icons.Default.FlashOn, ElectricBlue),
        ServiceCategoryItem("Event Tents", "Luxury Clear Alpine Marquees", Icons.Default.MeetingRoom, NeonPurple),
        ServiceCategoryItem("Stage Lighting", "Moving Heads & Ambient FX", Icons.Default.Lightbulb, ElectricCyan),
        ServiceCategoryItem("DJ Equipment", "Pioneer CDJ-3000 & DJM-A9", Icons.Default.Headphones, ElectricBlue),
        ServiceCategoryItem("LED Video Walls", "Daylight P3.91 Outdoor Displays", Icons.Default.Tv, NeonPurple)
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(KalisBlack),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // 1. Hero Concert Production Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(270.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_concert),
                    contentDescription = "KALIS Concert Production in Kampala",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dark gradient overlay for high text contrast
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    KalisBlack.copy(alpha = 0.6f),
                                    KalisBlack
                                )
                            )
                        )
                )

                // Overlay Content
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(
                                Brush.linearGradient(listOf(ElectricCyan, NeonPurple)),
                                RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MilitaryTech,
                            contentDescription = null,
                            tint = KalisBlack,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "#1 EVENT PRODUCTION IN UGANDA",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = KalisBlack,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Turn Any Venue Into A Spectacle",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            lineHeight = 28.sp
                        ),
                        color = TextPrimary
                    )

                    Text(
                        text = "Concert Sound • Truss Stages • Clear Marquees • Moving Lights",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { onNavigate(Screen.Booking) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ElectricCyan,
                                contentColor = KalisBlack
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("hero_instant_quote_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ReceiptLong,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Get Instant Quote",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        OutlinedButton(
                            onClick = { IntentUtils.openWhatsApp(context) },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = TextPrimary
                            ),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = Brush.linearGradient(listOf(ElectricCyan, NeonPurple))
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("hero_whatsapp_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = null,
                                tint = ElectricCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "WhatsApp Us",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // 2. Direct Quick Actions Strip (Call, WhatsApp, Maps, Tracking)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = KalisDarkCharcoal),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(KalisBorder, KalisCardSurfaceElevated)))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    QuickActionItem(
                        icon = Icons.Default.Phone,
                        label = "Call Us",
                        color = ElectricCyan,
                        onClick = { IntentUtils.openDialer(context) }
                    )
                    QuickActionItem(
                        icon = Icons.Default.Chat,
                        label = "WhatsApp",
                        color = KalisSuccess,
                        onClick = { IntentUtils.openWhatsApp(context) }
                    )
                    QuickActionItem(
                        icon = Icons.Default.LocationOn,
                        label = "Kampala HQ",
                        color = NeonPurple,
                        onClick = { IntentUtils.openGoogleMaps(context) }
                    )
                    QuickActionItem(
                        icon = Icons.Default.DateRange,
                        label = "Book Date",
                        color = ElectricBlue,
                        onClick = { onNavigate(Screen.Booking) }
                    )
                }
            }
        }

        // 3. Service Categories
        item {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Specialized Services",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Text(
                            text = "Complete production solutions across Uganda",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }

                    Text(
                        text = "View All",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan,
                        modifier = Modifier
                            .clickable { onNavigate(Screen.Catalogue) }
                            .padding(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(serviceCategories) { cat ->
                        ServiceCategoryCard(
                            item = cat,
                            onClick = {
                                onCategoryClick(cat.name)
                                onNavigate(Screen.Catalogue)
                            }
                        )
                    }
                }
            }
        }

        // 4. Featured Equipment Fleet
        item {
            Column(modifier = Modifier.padding(top = 24.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Featured Equipment Fleet",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Text(
                            text = "Available for hire with certified audio & rig engineers",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }

                    Text(
                        text = "Catalogue",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan,
                        modifier = Modifier
                            .clickable { onNavigate(Screen.Catalogue) }
                            .padding(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(featuredEquipment) { equip ->
                        FeaturedEquipmentCard(
                            equipment = equip,
                            isInCart = isInCart(equip),
                            onCardClick = { onSelectEquipment(equip) },
                            onToggleCart = { onToggleCartItem(equip) }
                        )
                    }
                }
            }
        }

        // 5. Why Choose KALIS EVENTS UG
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = KalisCardSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(ElectricCyan.copy(0.3f), NeonPurple.copy(0.3f))))
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = "Why Uganda Chooses KALIS EVENTS",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    TrustFactorRow(
                        title = "100% On-Time Setup Guarantee",
                        subtitle = "We rig sound, stage and lighting at least 4 hours before guest doors open."
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TrustFactorRow(
                        title = "Certified Ugandan Sound & Lighting Engineers",
                        subtitle = "Dedicated on-site FOH mix engineers, lighting board techs, and stage hands."
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TrustFactorRow(
                        title = "Uninterrupted Silent Generator Power",
                        subtitle = "Heavy 60kVA soundproofed power generators prevent blackouts."
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TrustFactorRow(
                        title = "Transparent UGX Invoicing",
                        subtitle = "Official quotations and tracking receipts with zero hidden logistical fees."
                    )
                }
            }
        }

        // 6. Recent Client Reviews Teaser
        item {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Verified Client Reviews",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified",
                                tint = KalisSuccess,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = "4.9★ rating across 150+ produced events in Uganda",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }

                    Text(
                        text = "All Reviews",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan,
                        modifier = Modifier
                            .clickable { onNavigate(Screen.Reviews) }
                            .padding(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(recentReviews) { review ->
                        ReviewTeaserCard(review = review)
                    }
                }
            }
        }

        // 7. Event Showcase Teaser Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
                    .clickable { onNavigate(Screen.Gallery) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = KalisDarkCharcoal),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(ElectricCyan, NeonPurple)))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Explore Production Gallery",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Text(
                            text = "Concerts, Speke Resort Weddings, Corporate Summits & Festivals",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                Brush.linearGradient(listOf(ElectricCyan, ElectricBlue)),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Open Gallery",
                            tint = KalisBlack,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionItem(
    icon: ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(color.copy(alpha = 0.15f), CircleShape)
                .border(1.dp, color.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary
        )
    }
}

@Composable
fun ServiceCategoryCard(
    item: ServiceCategoryItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = KalisCardSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(item.color.copy(0.4f), KalisBorder)))
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(item.color.copy(alpha = 0.15f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.name,
                    tint = item.color,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = item.name,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = item.description,
                fontSize = 11.sp,
                color = TextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
fun FeaturedEquipmentCard(
    equipment: EquipmentEntity,
    isInCart: Boolean,
    onCardClick: () -> Unit,
    onToggleCart: () -> Unit
) {
    val drawableRes = when (equipment.imageResName) {
        "img_sound_rig" -> R.drawable.img_sound_rig
        "img_hero_concert" -> R.drawable.img_hero_concert
        "img_tent_decor" -> R.drawable.img_tent_decor
        "img_stage_lighting" -> R.drawable.img_stage_lighting
        else -> R.drawable.kalis_logo
    }

    Card(
        modifier = Modifier
            .width(220.dp)
            .clickable { onCardClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = KalisCardSurface),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = if (isInCart) Brush.linearGradient(listOf(ElectricCyan, NeonPurple)) else Brush.linearGradient(listOf(KalisBorder, KalisBorder))
        )
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp)
            ) {
                Image(
                    painter = painterResource(id = drawableRes),
                    contentDescription = equipment.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Category Chip
                Text(
                    text = equipment.category,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .background(KalisBlack.copy(alpha = 0.75f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                )

                // Availability badge
                Text(
                    text = if (equipment.isAvailable) "IN STOCK" else "RESERVED",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (equipment.isAvailable) KalisBlack else TextPrimary,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .background(
                            if (equipment.isAvailable) ElectricCyan else Color(0xFFEF4444),
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = equipment.name,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = KalisRepository.formatUgx(equipment.pricePerDayUgx),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = ElectricCyan
                    )
                )
                Text(
                    text = "per day • setup crew included",
                    fontSize = 10.sp,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onToggleCart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isInCart) NeonPurple else KalisCardSurfaceElevated,
                        contentColor = TextPrimary
                    )
                ) {
                    Text(
                        text = if (isInCart) "✓ Added to Quote" else "+ Add to Quote",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun TrustFactorRow(title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = ElectricCyan,
            modifier = Modifier
                .padding(top = 2.dp)
                .size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 15.sp
            )
        }
    }
}

@Composable
fun ReviewTeaserCard(review: ReviewEntity) {
    Card(
        modifier = Modifier.width(260.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = KalisCardSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(KalisBorder, KalisBorder)))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(review.rating) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = KalisGold,
                        modifier = Modifier.size(14.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = review.eventDate,
                    fontSize = 10.sp,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "\"${review.comment}\"",
                fontSize = 12.sp,
                color = TextPrimary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = review.authorName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = ElectricCyan
            )
            Text(
                text = review.authorRole,
                fontSize = 10.sp,
                color = TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
