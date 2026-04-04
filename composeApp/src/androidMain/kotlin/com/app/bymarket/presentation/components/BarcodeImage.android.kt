package com.app.bymarket.presentation.components

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

actual fun generateBarcode(text: String): ImageBitmap? {
    if (text.isEmpty()) return null
    return try {
        val width = 500
        val height = 150
        val matrix: BitMatrix = MultiFormatWriter().encode(
            text, BarcodeFormat.CODE_128, width, height
        )
        
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        for (y in 0 until height) {
            for (x in 0 until width) {
                bitmap.setPixel(x, y, if (matrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        bitmap.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}
