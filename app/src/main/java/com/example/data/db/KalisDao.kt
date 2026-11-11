package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BookingEntity
import com.example.data.model.EquipmentEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.ReviewEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KalisDao {

    // Equipment
    @Query("SELECT * FROM equipment ORDER BY featured DESC, name ASC")
    fun getAllEquipment(): Flow<List<EquipmentEntity>>

    @Query("SELECT * FROM equipment WHERE id = :id")
    suspend fun getEquipmentById(id: String): EquipmentEntity?

    @Query("SELECT * FROM equipment WHERE category = :category ORDER BY name ASC")
    fun getEquipmentByCategory(category: String): Flow<List<EquipmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEquipment(equipment: List<EquipmentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleEquipment(equipment: EquipmentEntity)

    @Update
    suspend fun updateEquipment(equipment: EquipmentEntity)

    @Query("UPDATE equipment SET isAvailable = :isAvailable WHERE id = :id")
    suspend fun updateEquipmentAvailability(id: String, isAvailable: Boolean)

    @Query("UPDATE equipment SET pricePerDayUgx = :price WHERE id = :id")
    suspend fun updateEquipmentPrice(id: String, price: Long)

    @Query("DELETE FROM equipment WHERE id = :id")
    suspend fun deleteEquipment(id: String)

    // Bookings
    @Query("SELECT * FROM bookings ORDER BY createdAt DESC")
    fun getAllBookings(): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE customerEmail = :email ORDER BY createdAt DESC")
    fun getBookingsByCustomer(email: String): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE id = :id")
    suspend fun getBookingById(id: String): BookingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    @Update
    suspend fun updateBooking(booking: BookingEntity)

    @Query("UPDATE bookings SET status = :status WHERE id = :id")
    suspend fun updateBookingStatus(id: String, status: String)

    @Query("DELETE FROM bookings WHERE id = :id")
    suspend fun deleteBooking(id: String)

    // Reviews
    @Query("SELECT * FROM reviews ORDER BY timestamp DESC")
    fun getAllReviews(): Flow<List<ReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<ReviewEntity>)

    // Notifications
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markNotificationAsRead(id: String)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllNotificationsAsRead()

    @Query("DELETE FROM notifications")
    suspend fun clearAllNotifications()

    // Users
    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    fun getActiveUser(): Flow<UserEntity?>

    @Query("SELECT * FROM users ORDER BY name ASC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("UPDATE users SET isLoggedIn = 0")
    suspend fun logOutAllUsers()

    @Query("UPDATE users SET isLoggedIn = 1 WHERE id = :userId")
    suspend fun setActiveUser(userId: String)
}
