package com.app.bymarket.presentation.components

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import java.awt.image.BufferedImage

actual fun generateBarcode(text: String): ImageBitmap? {
    return try {
        val width = 500
        val height = 150
        val matrix = MultiFormatWriter().encode(text, BarcodeFormat.CODE_128, width, height)
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        for (x in 0 until width) {
            for (y in 0 until height) {
                image.setRGB(x, y, if (matrix.get(x, y)) java.awt.Color.BLACK.rgb else java.awt.Color.WHITE.rgb)
            }
        }
        image.toComposeImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
