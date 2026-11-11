package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.KalisBlack
import com.example.ui.theme.KalisCardSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.viewmodel.Screen

data class NavItem(
    val screen: Screen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@Composable
fun KalisBottomNav(
    currentScreen: Screen,
    cartItemCount: Int,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem(
            screen = Screen.Home,
            label = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            testTag = "nav_home"
        ),
        NavItem(
            screen = Screen.Catalogue,
            label = "Equipment",
            selectedIcon = Icons.Filled.Inventory2,
            unselectedIcon = Icons.Outlined.Inventory2,
            testTag = "nav_catalogue"
        ),
        NavItem(
            screen = Screen.Booking,
            label = "Book & Quote",
            selectedIcon = Icons.Filled.ReceiptLong,
            unselectedIcon = Icons.Outlined.ReceiptLong,
            testTag = "nav_booking"
        ),
        NavItem(
            screen = Screen.Tracking,
            label = "Bookings",
            selectedIcon = Icons.Filled.Assignment,
            unselectedIcon = Icons.Outlined.Assignment,
            testTag = "nav_tracking"
        ),
        NavItem(
            screen = Screen.Contact,
            label = "Hub",
            selectedIcon = Icons.Filled.MoreHoriz,
            unselectedIcon = Icons.Outlined.MoreHoriz,
            testTag = "nav_hub"
        )
    )

    NavigationBar(
        modifier = modifier,
        containerColor = KalisBlack,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = currentScreen.route == item.screen.route ||
                    (item.screen == Screen.Contact && (currentScreen == Screen.Gallery || currentScreen == Screen.Reviews || currentScreen == Screen.Admin))

            NavigationBarItem(
                modifier = Modifier.testTag(item.testTag),
                selected = isSelected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    if (item.screen == Screen.Booking && cartItemCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = ElectricCyan,
                                    contentColor = KalisBlack
                                ) {
                                    Text(
                                        text = "$cartItemCount",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label
                            )
                        }
                    } else {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label
                        )
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = KalisBlack,
                    selectedTextColor = ElectricCyan,
                    indicatorColor = ElectricCyan,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                )
            )
        }
    }
}
