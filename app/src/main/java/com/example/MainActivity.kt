package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.KalisBottomNav
import com.example.ui.components.KalisTopBar
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.BookingScreen
import com.example.ui.screens.CatalogueScreen
import com.example.ui.screens.ContactScreen
import com.example.ui.screens.EquipmentDetailSheet
import com.example.ui.screens.GalleryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NotificationsDialog
import com.example.ui.screens.ReviewsScreen
import com.example.ui.screens.TrackingScreen
import com.example.ui.theme.KalisBlack
import com.example.ui.theme.KalisEventsTheme
import com.example.ui.viewmodel.KalisViewModel
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KalisEventsTheme {
                KalisApp()
            }
        }
    }
}

@Composable
fun KalisApp(
    viewModel: KalisViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val activeUser by viewModel.activeUser.collectAsStateWithLifecycle()
    val filteredEquipment by viewModel.filteredEquipment.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val allBookings by viewModel.allBookings.collectAsStateWithLifecycle()
    val allReviews by viewModel.allReviews.collectAsStateWithLifecycle()
    val allNotifications by viewModel.allNotifications.collectAsStateWithLifecycle()
    val unreadNotifications by viewModel.unreadNotificationsCount.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedEquipment by viewModel.selectedEquipment.collectAsStateWithLifecycle()
    val bookingFormState by viewModel.bookingForm.collectAsStateWithLifecycle()
    val feedbackMessage by viewModel.userFeedbackMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    var showNotificationsDialog by remember { mutableStateOf(false) }

    LaunchedEffect(feedbackMessage) {
        feedbackMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.dismissFeedback()
        }
    }

    // Hardware back button navigation handling
    if (currentScreen != Screen.Home) {
        BackHandler {
            viewModel.navigateTo(Screen.Home)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(KalisBlack),
        containerColor = KalisBlack,
        topBar = {
            KalisTopBar(
                unreadNotifications = unreadNotifications,
                isAdminMode = activeUser?.isAdmin == true,
                onNotificationsClick = { showNotificationsDialog = true },
                onAdminToggleClick = { viewModel.toggleAdminMode() }
            )
        },
        bottomBar = {
            KalisBottomNav(
                currentScreen = currentScreen,
                cartItemCount = cartItems.size,
                onNavigate = { viewModel.navigateTo(it) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(KalisBlack)
        ) {
            when (currentScreen) {
                Screen.Home -> {
                    HomeScreen(
                        featuredEquipment = filteredEquipment.filter { it.featured },
                        recentReviews = allReviews.take(4),
                        onSelectEquipment = { viewModel.selectEquipment(it) },
                        onToggleCartItem = { viewModel.toggleCartItem(it) },
                        isInCart = { eq -> cartItems.any { it.id == eq.id } },
                        onNavigate = { viewModel.navigateTo(it) },
                        onCategoryClick = { category ->
                            viewModel.setSelectedCategory(category)
                        }
                    )
                }

                Screen.Catalogue -> {
                    CatalogueScreen(
                        equipmentList = filteredEquipment,
                        searchQuery = searchQuery,
                        selectedCategory = selectedCategory,
                        cartItems = cartItems,
                        onSearchChange = { viewModel.setSearchQuery(it) },
                        onCategoryChange = { viewModel.setSelectedCategory(it) },
                        onSelectEquipment = { viewModel.selectEquipment(it) },
                        onToggleCartItem = { viewModel.toggleCartItem(it) },
                        onProceedToBooking = { viewModel.navigateTo(Screen.Booking) }
                    )
                }

                Screen.Booking -> {
                    BookingScreen(
                        cartItems = cartItems,
                        formState = bookingFormState,
                        currentUser = activeUser,
                        onUpdateForm = { eventType, eventLocation, customLocation, startDate, endDate, durationDays, estimatedGuests, notes ->
                            viewModel.updateBookingForm(
                                eventType = eventType,
                                eventLocation = eventLocation,
                                customLocation = customLocation,
                                startDate = startDate,
                                endDate = endDate,
                                durationDays = durationDays,
                                estimatedGuests = estimatedGuests,
                                notes = notes
                            )
                        },
                        onRemoveCartItem = { viewModel.toggleCartItem(it) },
                        onSubmitBooking = { viewModel.submitBookingRequest() },
                        onResetSuccess = { viewModel.resetBookingSuccess() },
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                }

                Screen.Tracking -> {
                    TrackingScreen(
                        bookings = allBookings,
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                }

                Screen.Gallery -> {
                    GalleryScreen()
                }

                Screen.Reviews -> {
                    ReviewsScreen(
                        reviews = allReviews,
                        onSubmitReview = { name, role, rating, comment ->
                            viewModel.submitReview(name, role, rating, comment)
                        }
                    )
                }

                Screen.Contact -> {
                    ContactScreen(
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                }

                Screen.Admin -> {
                    AdminScreen(
                        bookings = allBookings,
                        equipmentList = filteredEquipment,
                        onUpdateBookingStatus = { id, status -> viewModel.updateBookingStatus(id, status) },
                        onToggleEquipmentAvailability = { id, isAvail -> viewModel.updateEquipmentAvailability(id, isAvail) },
                        onUpdateEquipmentPrice = { id, price -> viewModel.updateEquipmentPrice(id, price) },
                        onSendBroadcastNotification = { title, msg -> viewModel.sendBroadcastNotification(title, msg) },
                        onToggleAdminMode = { viewModel.toggleAdminMode() }
                    )
                }
            }
        }
    }

    // Equipment Details Modal Sheet
    selectedEquipment?.let { equip ->
        EquipmentDetailSheet(
            equipment = equip,
            isInCart = cartItems.any { it.id == equip.id },
            onDismiss = { viewModel.selectEquipment(null) },
            onToggleCart = { viewModel.toggleCartItem(equip) }
        )
    }

    // Notifications Dialog
    if (showNotificationsDialog) {
        NotificationsDialog(
            notifications = allNotifications,
            onDismiss = { showNotificationsDialog = false },
            onMarkAsRead = { viewModel.markNotificationAsRead(it) },
            onMarkAllAsRead = { viewModel.markAllNotificationsAsRead() },
            onClearAll = { viewModel.clearNotifications() }
        )
    }
}
