package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.KalisBlack
import com.example.ui.theme.KalisBorder
import com.example.ui.theme.KalisCardSurface
import com.example.ui.theme.KalisDarkCharcoal
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

data class GalleryItem(
    val id: String,
    val title: String,
    val category: String,
    val location: String,
    val date: String,
    val equipmentUsed: String,
    val imageRes: Int
)

@Composable
fun GalleryScreen(
    modifier: Modifier = Modifier
) {
    val items = listOf(
        GalleryItem(
            id = "gal_01",
            title = "Uganda Live Nights Concert Festival",
            category = "Concerts",
            location = "Lugogo Cricket Oval, Kampala",
            date = "September 2026",
            equipmentUsed = "L-Acoustics K2 Line Array, Moving Head Beams, Heavy-Duty Ground Support Truss Stage",
            imageRes = R.drawable.img_hero_concert
        ),
        GalleryItem(
            id = "gal_02",
            title = "Royal Lakeside Wedding Reception",
            category = "Weddings",
            location = "Speke Resort Munyonyo, Kampala",
            date = "October 2026",
            equipmentUsed = "500-Seater Clear-Span Alpine Marquee, Warm Fairy-Light Ceiling, Wireless Sound Rig",
            imageRes = R.drawable.img_tent_decor
        ),
        GalleryItem(
            id = "gal_03",
            title = "Electronic Stage & DJ Showcase",
            category = "Club & DJ",
            location = "Industrial Area, Kampala",
            date = "August 2026",
            equipmentUsed = "Pioneer CDJ-3000 & DJM-A9 Setup, Subwoofer Stacks, Yamaha CL5 Digital Audio Rig",
            imageRes = R.drawable.img_sound_rig
        ),
        GalleryItem(
            id = "gal_04",
            title = "National Music Awards & Live Broadcast",
            category = "Lighting & FX",
            location = "Serena Conference Centre, Kampala",
            date = "July 2026",
            equipmentUsed = "16x Sharpy Moving Beams, DMX Hazer FX, P3.91 Daylight LED Video Wall",
            imageRes = R.drawable.img_stage_lighting
        )
    )

    var selectedCategory by remember { mutableStateOf("All") }
    var selectedPhoto by remember { mutableStateOf<GalleryItem?>(null) }

    val categories = listOf("All", "Concerts", "Weddings", "Club & DJ", "Lighting & FX")

    val filteredItems = items.filter {
        selectedCategory == "All" || it.category == selectedCategory
    }

    // Full screen photo dialog
    if (selectedPhoto != null) {
        val item = selectedPhoto!!
        Dialog(onDismissRequest = { selectedPhoto = null }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = KalisDarkCharcoal),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Box(modifier = Modifier.fillMaxWidth().height(260.dp)) {
                        Image(
                            painter = painterResource(id = item.imageRes),
                            contentDescription = item.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        IconButton(
                            onClick = { selectedPhoto = null },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .background(KalisBlack.copy(0.7f), CircleShape)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextPrimary)
                        }
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = item.location, fontSize = 12.sp, color = TextSecondary)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Equipment Provided:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan
                        )
                        Text(
                            text = item.equipmentUsed,
                            fontSize = 12.sp,
                            color = TextPrimary,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KalisBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Text(
                text = "Production Showcase",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
            Text(
                text = "Vibrant events produced and powered by KALIS across Uganda",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
                        label = { Text(category, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = KalisDarkCharcoal,
                            labelColor = TextSecondary,
                            selectedContainerColor = ElectricCyan,
                            selectedLabelColor = KalisBlack
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = KalisBorder,
                            selectedBorderColor = ElectricCyan
                        )
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredItems, key = { it.id }) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedPhoto = item },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = KalisCardSurface),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(KalisBorder, KalisBorder)))
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(190.dp)
                        ) {
                            Image(
                                painter = painterResource(id = item.imageRes),
                                contentDescription = item.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            Text(
                                text = item.category,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(10.dp)
                                    .background(KalisBlack.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = item.location, fontSize = 11.sp, color = TextSecondary)
                                }
                                Text(text = item.date, fontSize = 10.sp, color = TextMuted)
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Rig: ${item.equipmentUsed}",
                                fontSize = 11.sp,
                                color = ElectricCyan,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}
