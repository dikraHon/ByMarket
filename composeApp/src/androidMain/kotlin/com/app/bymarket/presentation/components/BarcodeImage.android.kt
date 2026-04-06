package com.app.bymarket.presentation.components

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import java.util.EnumMap

actual fun generateBarcode(text: String): ImageBitmap? {
    if (text.isBlank()) return null
    
    return try {
        val width = 1000
        val height = 300
        
        val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
        hints[EncodeHintType.MARGIN] = 1 
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
        val matrix: BitMatrix = MultiFormatWriter().encode(
            text,
            BarcodeFormat.CODE_128,
            width,
            height,
            hints
        )
        
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        bitmap.eraseColor(Color.WHITE)
        
        for (x in 0 until width) {
            for (y in 0 until height) {
                if (matrix.get(x, y)) {
                    bitmap.setPixel(x, y, Color.BLACK)
                }
            }
        }
        bitmap.asImageBitmap()
    } catch (e: Exception) {
        println("Barcode Error: ${e.message}")
        null
    }
}
