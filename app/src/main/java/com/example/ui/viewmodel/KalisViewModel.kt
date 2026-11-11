package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.data.db.KalisDatabase
import com.example.data.model.BookingEntity
import com.example.data.model.EquipmentEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.ReviewEntity
import com.example.data.model.UserEntity
import com.example.data.repository.KalisRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Home")
    object Catalogue : Screen("catalogue", "Catalogue")
    object Booking : Screen("booking", "Book & Quote")
    object Tracking : Screen("tracking", "Tracking")
    object Gallery : Screen("gallery", "Showcase")
    object Reviews : Screen("reviews", "Reviews")
    object Contact : Screen("contact", "Contact")
    object Admin : Screen("admin", "Admin Hub")
}

data class BookingFormState(
    val eventType: String = "Concert / Music Festival",
    val eventLocation: String = "Kampala",
    val customLocation: String = "",
    val startDate: String = "2026-11-25",
    val endDate: String = "2026-11-26",
    val durationDays: Int = 2,
    val estimatedGuests: Int = 1000,
    val notes: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val successBookingId: String? = null
)

class KalisViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: KalisRepository

    init {
        val database = KalisDatabase.getDatabase(application)
        repository = KalisRepository(database.kalisDao())
        viewModelScope.launch {
            repository.initializeSeedDataIfNeeded()
        }
    }

    // Active User
    val activeUser: StateFlow<UserEntity?> = repository.activeUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Current Navigation Screen
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Filters for Equipment Catalogue
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _showOnlyAvailable = MutableStateFlow(false)
    val showOnlyAvailable: StateFlow<String> = MutableStateFlow("")

    // Raw Equipment from DB
    private val rawEquipment: StateFlow<List<EquipmentEntity>> = repository.allEquipment
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered Equipment
    val filteredEquipment: StateFlow<List<EquipmentEntity>> = combine(
        rawEquipment,
        _searchQuery,
        _selectedCategory
    ) { list, query, category ->
        list.filter { item ->
            val matchesCategory = category == "All" || item.category.equals(category, ignoreCase = true)
            val matchesQuery = query.isBlank() ||
                    item.name.contains(query, ignoreCase = true) ||
                    item.description.contains(query, ignoreCase = true) ||
                    item.specifications.contains(query, ignoreCase = true) ||
                    item.category.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Quotation/Booking Cart Items
    private val _cartItems = MutableStateFlow<List<EquipmentEntity>>(emptyList())
    val cartItems: StateFlow<List<EquipmentEntity>> = _cartItems.asStateFlow()

    // Bookings
    val allBookings: StateFlow<List<BookingEntity>> = repository.allBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Reviews
    val allReviews: StateFlow<List<ReviewEntity>> = repository.allReviews
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Notifications
    val allNotifications: StateFlow<List<NotificationEntity>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadNotificationsCount: StateFlow<Int> = repository.allNotifications
        .combine(MutableStateFlow(0)) { list, _ ->
            list.count { !it.isRead }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Selected Equipment for Modal View
    private val _selectedEquipment = MutableStateFlow<EquipmentEntity?>(null)
    val selectedEquipment: StateFlow<EquipmentEntity?> = _selectedEquipment.asStateFlow()

    // Booking Form State
    private val _bookingForm = MutableStateFlow(BookingFormState())
    val bookingForm: StateFlow<BookingFormState> = _bookingForm.asStateFlow()

    // Status Message / Snackbar
    private val _userFeedbackMessage = MutableStateFlow<String?>(null)
    val userFeedbackMessage: StateFlow<String?> = _userFeedbackMessage.asStateFlow()

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun selectEquipment(equipment: EquipmentEntity?) {
        _selectedEquipment.value = equipment
    }

    fun toggleCartItem(equipment: EquipmentEntity) {
        val current = _cartItems.value.toMutableList()
        val exists = current.any { it.id == equipment.id }
        if (exists) {
            current.removeAll { it.id == equipment.id }
            _userFeedbackMessage.value = "Removed ${equipment.name} from quotation"
        } else {
            if (!equipment.isAvailable) {
                _userFeedbackMessage.value = "${equipment.name} is currently reserved/in maintenance"
                return
            }
            current.add(equipment)
            _userFeedbackMessage.value = "Added ${equipment.name} to quotation"
        }
        _cartItems.value = current
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun updateBookingForm(
        eventType: String? = null,
        eventLocation: String? = null,
        customLocation: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        durationDays: Int? = null,
        estimatedGuests: Int? = null,
        notes: String? = null
    ) {
        val current = _bookingForm.value
        _bookingForm.value = current.copy(
            eventType = eventType ?: current.eventType,
            eventLocation = eventLocation ?: current.eventLocation,
            customLocation = customLocation ?: current.customLocation,
            startDate = startDate ?: current.startDate,
            endDate = endDate ?: current.endDate,
            durationDays = durationDays ?: current.durationDays,
            estimatedGuests = estimatedGuests ?: current.estimatedGuests,
            notes = notes ?: current.notes,
            errorMessage = null
        )
    }

    fun submitBookingRequest() {
        val form = _bookingForm.value
        val cart = _cartItems.value

        if (cart.isEmpty()) {
            _bookingForm.value = form.copy(errorMessage = "Please select at least one equipment item or package.")
            return
        }

        val location = if (form.eventLocation == "Other / Custom Venue") {
            if (form.customLocation.isBlank()) {
                _bookingForm.value = form.copy(errorMessage = "Please provide the specific venue or district address in Uganda.")
                return
            }
            form.customLocation.trim()
        } else {
            form.eventLocation
        }

        if (form.durationDays <= 0) {
            _bookingForm.value = form.copy(errorMessage = "Event duration must be at least 1 day.")
            return
        }

        viewModelScope.launch {
            _bookingForm.value = form.copy(isSubmitting = true, errorMessage = null)

            val user = activeUser.value ?: KalisRepository.DEFAULT_USER
            val trackingId = "KAL-UG-${(1000..9999).random()}"
            val itemsTotal = cart.sumOf { it.pricePerDayUgx } * form.durationDays

            val booking = BookingEntity(
                id = trackingId,
                customerName = user.name,
                customerPhone = user.phone,
                customerEmail = user.email,
                eventType = form.eventType,
                eventLocation = location,
                startDate = form.startDate,
                endDate = form.endDate,
                durationDays = form.durationDays,
                equipmentItemIds = cart.joinToString(",") { it.id },
                equipmentNames = cart.joinToString(", ") { it.name },
                estimatedGuests = form.estimatedGuests,
                notes = form.notes,
                totalAmountUgx = itemsTotal,
                status = "PENDING_REVIEW",
                createdAt = System.currentTimeMillis()
            )

            repository.createBooking(booking)
            _cartItems.value = emptyList()
            _bookingForm.value = form.copy(
                isSubmitting = false,
                successBookingId = trackingId,
                errorMessage = null
            )
            _userFeedbackMessage.value = "Booking #$trackingId submitted successfully!"
        }
    }

    fun resetBookingSuccess() {
        _bookingForm.value = _bookingForm.value.copy(successBookingId = null)
    }

    fun submitReview(authorName: String, authorRole: String, rating: Int, comment: String) {
        if (authorName.isBlank() || comment.isBlank()) {
            _userFeedbackMessage.value = "Please enter your name and review comment."
            return
        }
        viewModelScope.launch {
            val review = ReviewEntity(
                id = "rev_${UUID.randomUUID()}",
                authorName = authorName.trim(),
                authorRole = if (authorRole.isNotBlank()) authorRole.trim() else "Event Host",
                rating = rating.coerceIn(1, 5),
                comment = comment.trim(),
                eventDate = "October 2026",
                timestamp = System.currentTimeMillis()
            )
            repository.addReview(review)
            _userFeedbackMessage.value = "Thank you! Your verified review has been submitted."
        }
    }

    fun markNotificationAsRead(id: String) {
        viewModelScope.launch {
            repository.markNotificationAsRead(id)
        }
    }

    fun markAllNotificationsAsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
        }
    }

    fun clearNotifications() {
        viewModelScope.launch {
            repository.clearNotifications()
        }
    }

    fun toggleAdminMode() {
        val current = activeUser.value
        val newAdminStatus = !(current?.isAdmin ?: false)
        viewModelScope.launch {
            repository.switchUserRole(newAdminStatus)
            _userFeedbackMessage.value = if (newAdminStatus) "Switched to Administrator Mode" else "Switched to Customer Mode"
        }
    }

    fun updateEquipmentAvailability(id: String, isAvailable: Boolean) {
        viewModelScope.launch {
            repository.updateEquipmentAvailability(id, isAvailable)
            _userFeedbackMessage.value = "Equipment availability updated"
        }
    }

    fun updateEquipmentPrice(id: String, priceUgx: Long) {
        viewModelScope.launch {
            repository.updateEquipmentPrice(id, priceUgx)
            _userFeedbackMessage.value = "Equipment price updated"
        }
    }

    fun updateBookingStatus(bookingId: String, status: String) {
        viewModelScope.launch {
            repository.updateBookingStatus(bookingId, status)
            _userFeedbackMessage.value = "Booking #$bookingId updated to $status"
        }
    }

    fun sendBroadcastNotification(title: String, message: String) {
        if (title.isBlank() || message.isBlank()) return
        viewModelScope.launch {
            repository.sendBroadcastNotification(title, message, "PROMO")
            _userFeedbackMessage.value = "Notification sent to all clients"
        }
    }

    fun dismissFeedback() {
        _userFeedbackMessage.value = null
    }

    fun getEquipmentDrawableRes(name: String): Int {
        return when (name) {
            "img_hero_concert" -> R.drawable.img_hero_concert
            "img_sound_rig" -> R.drawable.img_sound_rig
            "img_tent_decor" -> R.drawable.img_tent_decor
            "img_stage_lighting" -> R.drawable.img_stage_lighting
            "kalis_logo" -> R.drawable.kalis_logo
            else -> R.drawable.kalis_logo
        }
    }
}
