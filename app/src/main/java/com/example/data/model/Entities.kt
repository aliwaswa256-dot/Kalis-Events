package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "equipment")
data class EquipmentEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val pricePerDayUgx: Long,
    val imageResName: String,
    val description: String,
    val specifications: String,
    val isAvailable: Boolean = true,
    val quantityInStock: Int = 1,
    val rating: Float = 4.9f,
    val featured: Boolean = false
)

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey val id: String,
    val customerName: String,
    val customerPhone: String,
    val customerEmail: String,
    val eventType: String,
    val eventLocation: String,
    val startDate: String,
    val endDate: String,
    val durationDays: Int,
    val equipmentItemIds: String,
    val equipmentNames: String,
    val estimatedGuests: Int,
    val notes: String,
    val totalAmountUgx: Long,
    val status: String = "PENDING_REVIEW", // PENDING_REVIEW, QUOTED, CONFIRMED, SETUP_DISPATCHED, COMPLETED, CANCELLED
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey val id: String,
    val authorName: String,
    val authorRole: String,
    val rating: Int,
    val comment: String,
    val eventDate: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val title: String,
    val message: String,
    val type: String, // BOOKING, PROMO, SYSTEM
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val company: String = "",
    val isAdmin: Boolean = false,
    val isLoggedIn: Boolean = true
)
