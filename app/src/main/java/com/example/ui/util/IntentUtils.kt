package com.example.ui.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object IntentUtils {

    const val KALIS_PHONE_PRIMARY = "+256701445566"
    const val KALIS_PHONE_SECONDARY = "+256772889900"
    const val KALIS_WHATSAPP_NUMBER = "256701445566"
    const val KALIS_EMAIL = "info@kalisevents.ug"
    const val KALIS_LOCATION_QUERY = "Plot 14 Lugogo Bypass, Kampala, Uganda"
    const val KALIS_LAT_LNG = "geo:0.3324,32.6078?q=Lugogo+Bypass,+Kampala"

    fun openDialer(context: Context, phoneNumber: String = KALIS_PHONE_PRIMARY) {
        try {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open phone dialer: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun openWhatsApp(
        context: Context,
        message: String = "Hello KALIS EVENTS UG, I am inquiring about sound, stage, and event equipment rental in Uganda."
    ) {
        try {
            val encodedMessage = Uri.encode(message)
            val uri = Uri.parse("https://wa.me/$KALIS_WHATSAPP_NUMBER?text=$encodedMessage")
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not launch WhatsApp: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun openGoogleMaps(context: Context) {
        try {
            val gmmIntentUri = Uri.parse(KALIS_LAT_LNG)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                setPackage("com.google.android.apps.maps")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(mapIntent)
        } catch (e: Exception) {
            try {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/maps/search/?api=1&query=Lugogo+Bypass+Kampala+Uganda")
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(browserIntent)
            } catch (e2: Exception) {
                Toast.makeText(context, "Could not open map: ${e2.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun openEmail(context: Context, subject: String = "Event Equipment Inquiry - KALIS EVENTS UG") {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$KALIS_EMAIL")
                putExtra(Intent.EXTRA_SUBJECT, subject)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open email client", Toast.LENGTH_SHORT).show()
        }
    }
}
