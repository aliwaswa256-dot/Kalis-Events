package com.example.data.repository

import com.example.data.db.KalisDao
import com.example.data.model.BookingEntity
import com.example.data.model.EquipmentEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.ReviewEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.text.NumberFormat
import java.util.Locale

class KalisRepository(private val dao: KalisDao) {

    val allEquipment: Flow<List<EquipmentEntity>> = dao.getAllEquipment()
    val allBookings: Flow<List<BookingEntity>> = dao.getAllBookings()
    val allReviews: Flow<List<ReviewEntity>> = dao.getAllReviews()
    val allNotifications: Flow<List<NotificationEntity>> = dao.getAllNotifications()
    val activeUser: Flow<UserEntity?> = dao.getActiveUser()
    val allUsers: Flow<List<UserEntity>> = dao.getAllUsers()

    suspend fun initializeSeedDataIfNeeded() {
        val existingEquipment = dao.getAllEquipment().firstOrNull()
        if (existingEquipment.isNullOrEmpty()) {
            dao.insertEquipment(DEFAULT_EQUIPMENT)
        }

        val existingReviews = dao.getAllReviews().firstOrNull()
        if (existingReviews.isNullOrEmpty()) {
            dao.insertReviews(DEFAULT_REVIEWS)
        }

        val existingNotifications = dao.getAllNotifications().firstOrNull()
        if (existingNotifications.isNullOrEmpty()) {
            dao.insertNotifications(DEFAULT_NOTIFICATIONS)
        }

        val currentUser = dao.getActiveUser().firstOrNull()
        if (currentUser == null) {
            dao.insertUser(DEFAULT_USER)
        }

        val existingBookings = dao.getAllBookings().firstOrNull()
        if (existingBookings.isNullOrEmpty()) {
            DEFAULT_BOOKINGS.forEach { dao.insertBooking(it) }
        }
    }

    fun getEquipmentByCategory(category: String): Flow<List<EquipmentEntity>> {
        return dao.getEquipmentByCategory(category)
    }

    suspend fun getEquipmentById(id: String): EquipmentEntity? = dao.getEquipmentById(id)

    suspend fun updateEquipmentAvailability(id: String, isAvailable: Boolean) {
        dao.updateEquipmentAvailability(id, isAvailable)
    }

    suspend fun updateEquipmentPrice(id: String, priceUgx: Long) {
        dao.updateEquipmentPrice(id, priceUgx)
    }

    suspend fun saveEquipment(equipment: EquipmentEntity) {
        dao.insertSingleEquipment(equipment)
    }

    suspend fun deleteEquipment(id: String) {
        dao.deleteEquipment(id)
    }

    suspend fun createBooking(booking: BookingEntity) {
        dao.insertBooking(booking)
        // Add automatic system notification for customer
        dao.insertNotification(
            NotificationEntity(
                id = "notif_${System.currentTimeMillis()}",
                title = "Booking Request Submitted",
                message = "Your request #${booking.id} for ${booking.eventType} at ${booking.eventLocation} has been received. Our team will review and confirm availability.",
                type = "BOOKING",
                timestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun updateBookingStatus(bookingId: String, status: String) {
        dao.updateBookingStatus(bookingId, status)
        dao.insertNotification(
            NotificationEntity(
                id = "notif_status_${System.currentTimeMillis()}",
                title = "Booking Status Update",
                message = "Booking #$bookingId status updated to: ${status.replace('_', ' ')}",
                type = "BOOKING",
                timestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteBooking(id: String) {
        dao.deleteBooking(id)
    }

    suspend fun addReview(review: ReviewEntity) {
        dao.insertReview(review)
    }

    suspend fun markNotificationAsRead(id: String) {
        dao.markNotificationAsRead(id)
    }

    suspend fun markAllNotificationsAsRead() {
        dao.markAllNotificationsAsRead()
    }

    suspend fun clearNotifications() {
        dao.clearAllNotifications()
    }

    suspend fun sendBroadcastNotification(title: String, message: String, type: String = "SYSTEM") {
        dao.insertNotification(
            NotificationEntity(
                id = "broadcast_${System.currentTimeMillis()}",
                title = title,
                message = message,
                type = type,
                timestamp = System.currentTimeMillis(),
                isRead = false
            )
        )
    }

    suspend fun switchUserRole(makeAdmin: Boolean) {
        val user = dao.getActiveUser().firstOrNull() ?: DEFAULT_USER
        val updated = user.copy(isAdmin = makeAdmin)
        dao.insertUser(updated)
    }

    suspend fun updateUserProfile(name: String, email: String, phone: String, company: String) {
        val user = dao.getActiveUser().firstOrNull() ?: DEFAULT_USER
        val updated = user.copy(name = name, email = email, phone = phone, company = company)
        dao.insertUser(updated)
    }

    companion object {
        fun formatUgx(amount: Long): String {
            val formatter = NumberFormat.getNumberInstance(Locale.US)
            return "UGX ${formatter.format(amount)}"
        }

        val DEFAULT_USER = UserEntity(
            id = "user_kalis_01",
            name = "Ali Waswa",
            email = "aliwaswa256@gmail.com",
            phone = "+256 701 445566",
            company = "VIP Event Productions Kampala",
            isAdmin = false,
            isLoggedIn = true
        )

        val DEFAULT_EQUIPMENT = listOf(
            EquipmentEntity(
                id = "eq_sound_01",
                name = "L-Acoustics K2 Concert Line Array",
                category = "Sound Systems",
                pricePerDayUgx = 2800000L,
                imageResName = "img_sound_rig",
                description = "World-class concert audio system designed for major music festivals, stadium crusades and large outdoor celebrations.",
                specifications = "12x K2 Line array tops, 8x SB28 dual 18\" subwoofers, Lab Gruppen HD touring amp racks, Yamaha CL5 digital mixing console, 6x Shure Axient Digital wireless microphones.",
                isAvailable = true,
                quantityInStock = 2,
                rating = 4.95f,
                featured = true
            ),
            EquipmentEntity(
                id = "eq_stage_01",
                name = "Heavy-Duty Ground Support Stage & Truss Roof",
                category = "Stage Setup",
                pricePerDayUgx = 1850000L,
                imageResName = "img_hero_concert",
                description = "Certified modular aluminum stage system with weather-shielding arched roof canopy, heavy load-bearing trusses, and safety guardrails.",
                specifications = "12m x 10m stage deck, variable height from 1.0m to 1.8m, anti-slip marine ply flooring, dual access staircases, rated up to 8,000kg roof suspension load.",
                isAvailable = true,
                quantityInStock = 3,
                rating = 4.9f,
                featured = true
            ),
            EquipmentEntity(
                id = "eq_tent_01",
                name = "Luxury Clear-Span Alpine Marquee (500-Seater)",
                category = "Event Tents",
                pricePerDayUgx = 3500000L,
                imageResName = "img_tent_decor",
                description = "Premium European-style transparent high-peak marquee tent ideal for high-end weddings, VIP introduction ceremonies, and corporate galas.",
                specifications = "20m x 30m clear structure (no internal poles), crystal chandeliers, warm fairy-light ceiling curtain, white silk wall draping, wind-tested aluminum frame.",
                isAvailable = true,
                quantityInStock = 2,
                rating = 5.0f,
                featured = true
            ),
            EquipmentEntity(
                id = "eq_light_01",
                name = "Concert Moving Heads & Beam FX Package",
                category = "Stage Lighting",
                pricePerDayUgx = 1250000L,
                imageResName = "img_stage_lighting",
                description = "Full computerized stage lighting package delivering sharp laser-like aerial beams, color washes, and dramatic atmospheric effects.",
                specifications = "16x Sharpy 7R 230W Beam lights, 12x 54*3W RGBW LED Washers, 4x 4-Eye Audience Blinders, 2x DMX Hazer smoke machines, Avolites Titan Mobile console.",
                isAvailable = true,
                quantityInStock = 4,
                rating = 4.9f,
                featured = true
            ),
            EquipmentEntity(
                id = "eq_dj_01",
                name = "Pioneer DJ CDJ-3000 & DJM-A9 Flagship Set",
                category = "DJ Equipment",
                pricePerDayUgx = 650000L,
                imageResName = "img_sound_rig",
                description = "Industry standard DJ gear for international concerts, clubs and wedding celebrations with pristine sound fidelity.",
                specifications = "2x Pioneer CDJ-3000 flagship players, 1x DJM-A9 4-channel club mixer, pair of active booth monitors, Link hub, flight cases.",
                isAvailable = true,
                quantityInStock = 3,
                rating = 4.95f,
                featured = false
            ),
            EquipmentEntity(
                id = "eq_screen_01",
                name = "P3.91 High-Bright Outdoor LED Screen (5m x 3m)",
                category = "LED Video Walls",
                pricePerDayUgx = 3200000L,
                imageResName = "img_hero_concert",
                description = "Ultra bright daylight-visible video wall for concerts, corporate presentations, live camera feeds and advertising displays.",
                specifications = "15 square meters, 4500 nits brightness, IP65 waterproof front/back, Novastar VX600 4K video processor, HDMI/SDI matrix switcher, rigging truss.",
                isAvailable = true,
                quantityInStock = 2,
                rating = 4.88f,
                featured = true
            ),
            EquipmentEntity(
                id = "eq_tent_02",
                name = "VIP Hexagonal Dome Tent (100-Seater)",
                category = "Event Tents",
                pricePerDayUgx = 1400000L,
                imageResName = "img_tent_decor",
                description = "Modern stylish dome marquee with panoramic windows, ideal for executive VIP lounge, bridal party pavilion, or cocktail areas.",
                specifications = "10m diameter dome footprint, integrated ambient LED perimeter glow, luxury entrance arch, water-tight perimeter drainage.",
                isAvailable = true,
                quantityInStock = 4,
                rating = 4.85f,
                featured = false
            ),
            EquipmentEntity(
                id = "eq_sound_02",
                name = "Corporate Speech & Presentation Audio Rig",
                category = "Sound Systems",
                pricePerDayUgx = 850000L,
                imageResName = "img_sound_rig",
                description = "Discreet, crystal-clear sound setup tailored for corporate annual general meetings, press conferences, and hotel conferences.",
                specifications = "4x Electro-Voice / JBL active column speakers, 6x Sennheiser wireless lapel and handheld microphones, digital audio recorder, feedback eliminator.",
                isAvailable = true,
                quantityInStock = 5,
                rating = 4.8f,
                featured = false
            ),
            EquipmentEntity(
                id = "eq_gen_01",
                name = "60 kVA Silent Event Power Generator",
                category = "Power & Logistics",
                pricePerDayUgx = 900000L,
                imageResName = "img_stage_lighting",
                description = "Whisper-quiet diesel generator ensuring uninterrupted power for sound, lights, and screens throughout outdoor Ugandan venues.",
                specifications = "Caterpillar soundproofed canopy (under 65dB at 7m), automatic voltage regulation (AVR), 3-phase distribution box with 50m heavy feeder cables.",
                isAvailable = true,
                quantityInStock = 2,
                rating = 4.9f,
                featured = false
            )
        )

        val DEFAULT_REVIEWS = listOf(
            ReviewEntity(
                id = "rev_01",
                authorName = "Sarah Kyomugisha",
                authorRole = "Lead Wedding Planner, Kampala",
                rating = 5,
                comment = "KALIS EVENTS delivered beyond expectation at Speke Resort Munyonyo. The 500-seater clear-span dome marquee and ambient fairy lighting transformed the entire reception into pure magic.",
                eventDate = "October 2026",
                timestamp = System.currentTimeMillis() - 86400000L * 4
            ),
            ReviewEntity(
                id = "rev_02",
                authorName = "David Mukasa",
                authorRole = "Festival Director, Uganda Live Nights",
                rating = 5,
                comment = "The L-Acoustics line array and moving beam lighting was top tier. Audio coverage was clean and punchy across the entire cricket oval, and their sound engineers are true professionals.",
                eventDate = "September 2026",
                timestamp = System.currentTimeMillis() - 86400000L * 12
            ),
            ReviewEntity(
                id = "rev_03",
                authorName = "Grace Nabatanzi",
                authorRole = "Corporate Brand Manager, Entebbe",
                rating = 5,
                comment = "Flawless LED video wall and audio conference setup at Serena Hotel Kigo. Timely setup 4 hours before guest arrival and zero technical hitches throughout our 3-day regional summit.",
                eventDate = "August 2026",
                timestamp = System.currentTimeMillis() - 86400000L * 25
            ),
            ReviewEntity(
                id = "rev_04",
                authorName = "Arthur Sempebwa",
                authorRole = "Private Client, Kololo",
                rating = 5,
                comment = "Hired Pioneer DJ CDJ equipment and ambient mood lighting for an exclusive birthday in Kololo. Prompt delivery, professional setup crew, and transparent UGX pricing with no hidden charges.",
                eventDate = "July 2026",
                timestamp = System.currentTimeMillis() - 86400000L * 45
            )
        )

        val DEFAULT_NOTIFICATIONS = listOf(
            NotificationEntity(
                id = "notif_welcome",
                title = "Welcome to KALIS EVENTS UG!",
                message = "Explore Uganda's premier sound systems, stage rigs, luxury tents, and LED screens. Request instant quotes and track your bookings in real time.",
                type = "SYSTEM",
                timestamp = System.currentTimeMillis() - 3600000L * 2,
                isRead = false
            ),
            NotificationEntity(
                id = "notif_promo",
                title = "Mid-Week Booking Discount",
                message = "Enjoy 10% off equipment rentals for Tuesday & Wednesday events in Kampala and Wakiso districts.",
                type = "PROMO",
                timestamp = System.currentTimeMillis() - 86400000L,
                isRead = false
            ),
            NotificationEntity(
                id = "notif_equipment",
                title = "New Stage & Audio Hardware In Stock",
                message = "Pioneer DJ CDJ-3000 setups and high-power Sharpy moving head lights now available for weekend rentals.",
                type = "SYSTEM",
                timestamp = System.currentTimeMillis() - 86400000L * 2,
                isRead = true
            )
        )

        val DEFAULT_BOOKINGS = listOf(
            BookingEntity(
                id = "KAL-UG-8492",
                customerName = "Ali Waswa",
                customerPhone = "+256 701 445566",
                customerEmail = "aliwaswa256@gmail.com",
                eventType = "Music Concert / Festival",
                eventLocation = "Lugogo Cricket Oval, Kampala",
                startDate = "2026-11-20",
                endDate = "2026-11-21",
                durationDays = 2,
                equipmentItemIds = "eq_sound_01,eq_stage_01,eq_light_01",
                equipmentNames = "L-Acoustics K2 Line Array, Heavy-Duty Ground Support Stage, Moving Heads Lighting Package",
                estimatedGuests = 3500,
                notes = "Requires technical crew on site for live band changeovers and sound checks.",
                totalAmountUgx = 11800000L,
                status = "CONFIRMED",
                createdAt = System.currentTimeMillis() - 86400000L * 3
            ),
            BookingEntity(
                id = "KAL-UG-7184",
                customerName = "Ali Waswa",
                customerPhone = "+256 701 445566",
                customerEmail = "aliwaswa256@gmail.com",
                eventType = "Wedding Reception",
                eventLocation = "Speke Resort Munyonyo, Kampala",
                startDate = "2026-12-05",
                endDate = "2026-12-05",
                durationDays = 1,
                equipmentItemIds = "eq_tent_01,eq_dj_01",
                equipmentNames = "Luxury Clear-Span Alpine Marquee (500-Seater), Pioneer DJ Flagship Set",
                estimatedGuests = 450,
                notes = "Fairy light canopy setup to be completed by 10:00 AM.",
                totalAmountUgx = 4150000L,
                status = "PENDING_REVIEW",
                createdAt = System.currentTimeMillis() - 3600000L * 6
            )
        )
    }
}
