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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.EquipmentEntity
import com.example.data.model.UserEntity
import com.example.data.repository.KalisRepository
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.KalisBlack
import com.example.ui.theme.KalisBorder
import com.example.ui.theme.KalisCardSurface
import com.example.ui.theme.KalisCardSurfaceElevated
import com.example.ui.theme.KalisDarkCharcoal
import com.example.ui.theme.KalisError
import com.example.ui.theme.KalisSuccess
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.util.IntentUtils
import com.example.ui.viewmodel.BookingFormState
import com.example.ui.viewmodel.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    cartItems: List<EquipmentEntity>,
    formState: BookingFormState,
    currentUser: UserEntity?,
    onUpdateForm: (
        eventType: String?,
        eventLocation: String?,
        customLocation: String?,
        startDate: String?,
        endDate: String?,
        durationDays: Int?,
        estimatedGuests: Int?,
        notes: String?
    ) -> Unit,
    onRemoveCartItem: (EquipmentEntity) -> Unit,
    onSubmitBooking: () -> Unit,
    onResetSuccess: () -> Unit,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val eventTypes = listOf(
        "Concert / Music Festival",
        "Wedding / Reception",
        "Corporate Gala / Product Launch",
        "Church Crusade / Conference",
        "Private VIP Party / Birthday",
        "Sports / Outdoor Tournament"
    )

    val venues = listOf(
        "Lugogo Cricket Oval, Kampala",
        "Speke Resort Munyonyo, Kampala",
        "Serena Hotel Kigo / Victoria Ballrooms",
        "Kololo Independence Grounds, Kampala",
        "Jinja Nile Resort / River Nile Front",
        "Entebbe Botanical Beach Gardens",
        "Wakiso / Mukono District Venue",
        "Other / Custom Venue"
    )

    var eventTypeExpanded by remember { mutableStateOf(false) }
    var venueExpanded by remember { mutableStateOf(false) }

    val dailySubtotal = cartItems.sumOf { it.pricePerDayUgx }
    val totalEstimated = dailySubtotal * formState.durationDays

    // Success Dialog on booking submission
    if (formState.successBookingId != null) {
        val trackingId = formState.successBookingId
        AlertDialog(
            onDismissRequest = onResetSuccess,
            containerColor = KalisDarkCharcoal,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = ElectricCyan,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Quotation Submitted!",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "Your event production request has been registered with KALIS EVENTS UG.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(KalisBlack, RoundedCornerShape(8.dp))
                            .border(1.dp, ElectricCyan, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(text = "OFFICIAL TRACKING ID", fontSize = 10.sp, color = TextMuted)
                            Text(
                                text = "#$trackingId",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = ElectricCyan
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Estimated Total: ${KalisRepository.formatUgx(totalEstimated)}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Our operations manager will review availability and dispatch your formal invoice.",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onResetSuccess()
                        onNavigate(Screen.Tracking)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan, contentColor = KalisBlack),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("View My Bookings", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        val msg = "Hello KALIS EVENTS, I just submitted Quotation Request #$trackingId for ${formState.eventType} at ${formState.eventLocation}. Total est: ${KalisRepository.formatUgx(totalEstimated)}."
                        IntentUtils.openWhatsApp(context, msg)
                        onResetSuccess()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = KalisCardSurfaceElevated, contentColor = TextPrimary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("WhatsApp Us", fontSize = 12.sp)
                }
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(KalisBlack),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        item {
            Column {
                Text(
                    text = "Book Equipment & Request Quote",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Text(
                    text = "Transparent UGX pricing for professional sound, stage, tents & lighting",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }

        // Selected Equipment List
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = KalisCardSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(KalisBorder, KalisBorder)))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Selected Equipment (${cartItems.size})",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )

                        Text(
                            text = "+ Add More Gear",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan,
                            modifier = Modifier
                                .clickable { onNavigate(Screen.Catalogue) }
                                .padding(4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (cartItems.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(KalisDarkCharcoal, RoundedCornerShape(12.dp))
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Inventory2,
                                    contentDescription = null,
                                    tint = TextMuted,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "No equipment selected yet",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Choose line arrays, stage setups, tents, or lighting to build your quotation.",
                                    fontSize = 12.sp,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = { onNavigate(Screen.Catalogue) },
                                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan, contentColor = KalisBlack),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Explore Catalogue", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            cartItems.forEach { item ->
                                SelectedCartItemRow(
                                    item = item,
                                    onRemove = { onRemoveCartItem(item) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Event Details Form
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = KalisCardSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(KalisBorder, KalisBorder)))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Event Specifications & Logistics",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Event Type Dropdown
                    ExposedDropdownMenuBox(
                        expanded = eventTypeExpanded,
                        onExpandedChange = { eventTypeExpanded = !eventTypeExpanded }
                    ) {
                        OutlinedTextField(
                            value = formState.eventType,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Event Type", color = TextSecondary) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = eventTypeExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = KalisDarkCharcoal,
                                unfocusedContainerColor = KalisDarkCharcoal,
                                focusedBorderColor = ElectricCyan,
                                unfocusedBorderColor = KalisBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = eventTypeExpanded,
                            onDismissRequest = { eventTypeExpanded = false },
                            modifier = Modifier.background(KalisDarkCharcoal)
                        ) {
                            eventTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type, color = TextPrimary) },
                                    onClick = {
                                        onUpdateForm(type, null, null, null, null, null, null, null)
                                        eventTypeExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Venue Location Dropdown
                    ExposedDropdownMenuBox(
                        expanded = venueExpanded,
                        onExpandedChange = { venueExpanded = !venueExpanded }
                    ) {
                        OutlinedTextField(
                            value = formState.eventLocation,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Event Venue / District", color = TextSecondary) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = venueExpanded) },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = ElectricCyan)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = KalisDarkCharcoal,
                                unfocusedContainerColor = KalisDarkCharcoal,
                                focusedBorderColor = ElectricCyan,
                                unfocusedBorderColor = KalisBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = venueExpanded,
                            onDismissRequest = { venueExpanded = false },
                            modifier = Modifier.background(KalisDarkCharcoal)
                        ) {
                            venues.forEach { venue ->
                                DropdownMenuItem(
                                    text = { Text(venue, color = TextPrimary) },
                                    onClick = {
                                        onUpdateForm(null, venue, null, null, null, null, null, null)
                                        venueExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Custom Location input if selected
                    if (formState.eventLocation == "Other / Custom Venue") {
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = formState.customLocation,
                            onValueChange = { onUpdateForm(null, null, it, null, null, null, null, null) },
                            label = { Text("Enter Venue Name & District in Uganda", color = TextSecondary) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = KalisDarkCharcoal,
                                unfocusedContainerColor = KalisDarkCharcoal,
                                focusedBorderColor = ElectricCyan,
                                unfocusedBorderColor = KalisBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Dates and Duration Stepper
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Start Date
                        OutlinedTextField(
                            value = formState.startDate,
                            onValueChange = { onUpdateForm(null, null, null, it, null, null, null, null) },
                            label = { Text("Start Date", color = TextSecondary, fontSize = 11.sp) },
                            leadingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(16.dp)) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = KalisDarkCharcoal,
                                unfocusedContainerColor = KalisDarkCharcoal,
                                focusedBorderColor = ElectricCyan,
                                unfocusedBorderColor = KalisBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )

                        // Duration Stepper (Days)
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = KalisDarkCharcoal),
                            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(KalisBorder, KalisBorder)))
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Duration (Days)", fontSize = 11.sp, color = TextSecondary)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    IconButton(
                                        onClick = {
                                            if (formState.durationDays > 1) {
                                                onUpdateForm(null, null, null, null, null, formState.durationDays - 1, null, null)
                                            }
                                        },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = ElectricCyan)
                                    }
                                    Text(
                                        text = "${formState.durationDays} ${if (formState.durationDays == 1) "Day" else "Days"}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = TextPrimary
                                    )
                                    IconButton(
                                        onClick = {
                                            onUpdateForm(null, null, null, null, null, formState.durationDays + 1, null, null)
                                        },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Increase", tint = ElectricCyan)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Estimated Guests
                    OutlinedTextField(
                        value = "${formState.estimatedGuests}",
                        onValueChange = { str ->
                            val num = str.filter { it.isDigit() }.toIntOrNull() ?: 100
                            onUpdateForm(null, null, null, null, null, null, num, null)
                        },
                        label = { Text("Estimated Attendance / Guest Count", color = TextSecondary) },
                        leadingIcon = { Icon(Icons.Default.People, contentDescription = null, tint = ElectricCyan) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = KalisDarkCharcoal,
                            unfocusedContainerColor = KalisDarkCharcoal,
                            focusedBorderColor = ElectricCyan,
                            unfocusedBorderColor = KalisBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Notes / Special Instructions
                    OutlinedTextField(
                        value = formState.notes,
                        onValueChange = { onUpdateForm(null, null, null, null, null, null, null, it) },
                        label = { Text("Special Technical Instructions (Stage orientation, generators, band line-up)", color = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = KalisDarkCharcoal,
                            unfocusedContainerColor = KalisDarkCharcoal,
                            focusedBorderColor = ElectricCyan,
                            unfocusedBorderColor = KalisBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }
        }

        // Cost Breakdown & Summary
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = KalisDarkCharcoal),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(ElectricCyan.copy(0.4f), NeonPurple.copy(0.4f))))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Official Quotation Estimation",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    CostLineItem("Equipment Daily Subtotal", KalisRepository.formatUgx(dailySubtotal))
                    CostLineItem("Event Duration Multiplier", "x ${formState.durationDays} ${if (formState.durationDays == 1) "Day" else "Days"}")
                    CostLineItem("Certified Sound & Lighting Crew", "Included in quote")
                    CostLineItem("Pre-Event Rigging & Soundcheck", "Included in quote")

                    Spacer(modifier = Modifier.height(10.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(KalisBorder))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Estimated Total (UGX)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = KalisRepository.formatUgx(totalEstimated),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = ElectricCyan
                        )
                    }

                    if (formState.errorMessage != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Error, contentDescription = null, tint = KalisError, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = formState.errorMessage,
                                color = KalisError,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onSubmitBooking,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_quotation_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ElectricCyan,
                            contentColor = KalisBlack
                        ),
                        enabled = !formState.isSubmitting && cartItems.isNotEmpty()
                    ) {
                        if (formState.isSubmitting) {
                            CircularProgressIndicator(
                                color = KalisBlack,
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.ReceiptLong,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Submit Booking & Quotation Request",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SelectedCartItemRow(
    item: EquipmentEntity,
    onRemove: () -> Unit
) {
    val drawableRes = when (item.imageResName) {
        "img_sound_rig" -> R.drawable.img_sound_rig
        "img_hero_concert" -> R.drawable.img_hero_concert
        "img_tent_decor" -> R.drawable.img_tent_decor
        "img_stage_lighting" -> R.drawable.img_stage_lighting
        else -> R.drawable.kalis_logo
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(KalisDarkCharcoal, RoundedCornerShape(10.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Image(
                painter = painterResource(id = drawableRes),
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = TextPrimary
                )
                Text(
                    text = KalisRepository.formatUgx(item.pricePerDayUgx) + " / day",
                    fontSize = 11.sp,
                    color = ElectricCyan,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        IconButton(
            onClick = onRemove,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Remove",
                tint = KalisError.copy(alpha = 0.8f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun CostLineItem(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, fontSize = 12.sp, color = TextSecondary)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
    }
}
