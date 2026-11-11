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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.data.model.EquipmentEntity
import com.example.data.repository.KalisRepository
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.KalisBlack
import com.example.ui.theme.KalisBorder
import com.example.ui.theme.KalisCardSurface
import com.example.ui.theme.KalisCardSurfaceElevated
import com.example.ui.theme.KalisDarkCharcoal
import com.example.ui.theme.KalisError
import com.example.ui.theme.KalisGold
import com.example.ui.theme.KalisSuccess
import com.example.ui.theme.KalisWarning
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.util.IntentUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    bookings: List<BookingEntity>,
    equipmentList: List<EquipmentEntity>,
    onUpdateBookingStatus: (bookingId: String, status: String) -> Unit,
    onToggleEquipmentAvailability: (id: String, isAvailable: Boolean) -> Unit,
    onUpdateEquipmentPrice: (id: String, priceUgx: Long) -> Unit,
    onSendBroadcastNotification: (title: String, message: String) -> Unit,
    onToggleAdminMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Bookings", "Equipment Fleet", "Broadcasts")

    // Price edit dialog
    var editingPriceEquipment by remember { mutableStateOf<EquipmentEntity?>(null) }
    var newPriceText by remember { mutableStateOf("") }

    if (editingPriceEquipment != null) {
        AlertDialog(
            onDismissRequest = { editingPriceEquipment = null },
            containerColor = KalisDarkCharcoal,
            title = {
                Text(
                    text = "Update Daily Rental Price",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
            },
            text = {
                Column {
                    Text(
                        text = editingPriceEquipment!!.name,
                        fontSize = 13.sp,
                        color = ElectricCyan,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPriceText,
                        onValueChange = { newPriceText = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Price in UGX", color = TextSecondary) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = KalisBlack,
                            unfocusedContainerColor = KalisBlack,
                            focusedBorderColor = ElectricCyan,
                            unfocusedBorderColor = KalisBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val price = newPriceText.toLongOrNull()
                        if (price != null && price > 0) {
                            onUpdateEquipmentPrice(editingPriceEquipment!!.id, price)
                            editingPriceEquipment = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan, contentColor = KalisBlack)
                ) {
                    Text("Save Price", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Button(
                    onClick = { editingPriceEquipment = null },
                    colors = ButtonDefaults.buttonColors(containerColor = KalisCardSurfaceElevated, contentColor = TextPrimary)
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KalisBlack)
    ) {
        // Admin Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = KalisCardSurface),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(NeonPurple, ElectricBlue)))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(NeonPurple.copy(alpha = 0.2f), CircleShape)
                                .border(1.dp, NeonPurple, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = NeonPurple, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("KALIS OPERATIONS HUB", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = TextPrimary)
                            Text("Administrator & Dispatch Management", fontSize = 11.sp, color = TextSecondary)
                        }
                    }

                    Button(
                        onClick = onToggleAdminMode,
                        colors = ButtonDefaults.buttonColors(containerColor = KalisCardSurfaceElevated, contentColor = ElectricCyan),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("admin_switch_client_button")
                    ) {
                        Icon(Icons.Default.SwitchAccount, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Exit Admin", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // KPI Stat Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AdminKpiItem(
                        title = "Bookings",
                        value = "${bookings.size}",
                        color = ElectricCyan,
                        modifier = Modifier.weight(1f)
                    )
                    AdminKpiItem(
                        title = "Fleet Items",
                        value = "${equipmentList.size}",
                        color = ElectricBlue,
                        modifier = Modifier.weight(1f)
                    )
                    AdminKpiItem(
                        title = "Pending",
                        value = "${bookings.count { it.status == "PENDING_REVIEW" }}",
                        color = KalisWarning,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = KalisBlack,
            contentColor = TextPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = ElectricCyan
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == index) ElectricCyan else TextSecondary
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Tab Content
        when (selectedTab) {
            0 -> AdminBookingsTab(
                bookings = bookings,
                onUpdateStatus = onUpdateBookingStatus,
                onCall = { IntentUtils.openDialer(context, it) },
                onWhatsApp = { phone, booking ->
                    val msg = "Hello from KALIS EVENTS UG management, regarding your event booking #${booking.id} (${booking.eventType}) on ${booking.startDate}."
                    IntentUtils.openWhatsApp(context, msg)
                }
            )
            1 -> AdminEquipmentTab(
                equipmentList = equipmentList,
                onToggleAvailability = onToggleEquipmentAvailability,
                onEditPrice = { equip ->
                    editingPriceEquipment = equip
                    newPriceText = "${equip.pricePerDayUgx}"
                }
            )
            2 -> AdminBroadcastTab(onSendBroadcast = onSendBroadcastNotification)
        }
    }
}

@Composable
fun AdminKpiItem(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(KalisDarkCharcoal, RoundedCornerShape(10.dp))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Column {
            Text(text = title, fontSize = 10.sp, color = TextMuted)
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminBookingsTab(
    bookings: List<BookingEntity>,
    onUpdateStatus: (String, String) -> Unit,
    onCall: (String) -> Unit,
    onWhatsApp: (String, BookingEntity) -> Unit
) {
    val statuses = listOf(
        "PENDING_REVIEW",
        "QUOTED",
        "CONFIRMED",
        "SETUP_DISPATCHED",
        "COMPLETED",
        "CANCELLED"
    )

    if (bookings.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
            Text("No bookings registered in database", color = TextSecondary)
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(bookings, key = { it.id }) { booking ->
                var expanded by remember { mutableStateOf(false) }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = KalisCardSurface),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(KalisBorder, KalisBorder)))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "#${booking.id}",
                                fontWeight = FontWeight.ExtraBold,
                                color = ElectricCyan,
                                fontSize = 14.sp
                            )

                            // Status Dropdown
                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = !expanded }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .menuAnchor()
                                        .background(ElectricBlue.copy(0.15f), RoundedCornerShape(6.dp))
                                        .border(1.dp, ElectricBlue, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = booking.status.replace('_', ' '),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ElectricCyan
                                    )
                                }
                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier.background(KalisDarkCharcoal)
                                ) {
                                    statuses.forEach { st ->
                                        DropdownMenuItem(
                                            text = { Text(st.replace('_', ' '), color = TextPrimary, fontSize = 12.sp) },
                                            onClick = {
                                                onUpdateStatus(booking.id, st)
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "${booking.eventType} • ${booking.eventLocation}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = TextPrimary
                        )

                        Text(
                            text = "Client: ${booking.customerName} (${booking.customerPhone})",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )

                        Text(
                            text = "Dates: ${booking.startDate} (${booking.durationDays} days) • Est: ${KalisRepository.formatUgx(booking.totalAmountUgx)}",
                            fontSize = 11.sp,
                            color = ElectricCyan,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Items: ${booking.equipmentNames}",
                            fontSize = 11.sp,
                            color = TextMuted,
                            lineHeight = 15.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { onCall(booking.customerPhone) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = KalisCardSurfaceElevated, contentColor = TextPrimary),
                                modifier = Modifier.weight(1f).height(34.dp)
                            ) {
                                Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Call Client", fontSize = 11.sp)
                            }

                            Button(
                                onClick = { onWhatsApp(booking.customerPhone, booking) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan, contentColor = KalisBlack),
                                modifier = Modifier.weight(1f).height(34.dp)
                            ) {
                                Text("WhatsApp", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminEquipmentTab(
    equipmentList: List<EquipmentEntity>,
    onToggleAvailability: (String, Boolean) -> Unit,
    onEditPrice: (EquipmentEntity) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(equipmentList, key = { it.id }) { equip ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = KalisCardSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(KalisBorder, KalisBorder)))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = equip.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = equip.category,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = "${KalisRepository.formatUgx(equip.pricePerDayUgx)} / day",
                            fontSize = 12.sp,
                            color = ElectricCyan,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = { onEditPrice(equip) },
                            modifier = Modifier
                                .size(34.dp)
                                .background(KalisDarkCharcoal, RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Price", tint = ElectricCyan, modifier = Modifier.size(16.dp))
                        }

                        Button(
                            onClick = { onToggleAvailability(equip.id, !equip.isAvailable) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (equip.isAvailable) KalisSuccess.copy(0.2f) else KalisError.copy(0.2f),
                                contentColor = if (equip.isAvailable) KalisSuccess else KalisError
                            ),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text(
                                text = if (equip.isAvailable) "In Stock" else "Reserved",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminBroadcastTab(
    onSendBroadcast: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Broadcast Push Notification to Clients",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = TextPrimary
        )
        Text(
            text = "Send real-time alerts regarding booking discounts, new line array systems or stage gear availability.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Broadcast Title", color = TextSecondary) },
            singleLine = true,
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

        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Notification Message Content", color = TextSecondary) },
            maxLines = 4,
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

        Button(
            onClick = {
                if (title.isNotBlank() && message.isNotBlank()) {
                    onSendBroadcast(title, message)
                    title = ""
                    message = ""
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan, contentColor = KalisBlack),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Send Broadcast Now", fontWeight = FontWeight.Bold)
        }
    }
}
