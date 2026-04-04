package com.app.bymarket.presentation.components

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.ImageInfo

actual fun generateBarcode(text: String): ImageBitmap? {
    if (text.isEmpty()) return null
    return try {
        val width = 500
        val height = 150
        val matrix: BitMatrix = MultiFormatWriter().encode(
            text, BarcodeFormat.CODE_128, width, height
        )
        
        val bitmap = Bitmap()
        val imageInfo = ImageInfo(width, height, ColorType.RGBA_8888, ColorAlphaType.PREMUL)
        bitmap.allocPixels(imageInfo)
        
        val pixels = ByteArray(width * height * 4)
        for (y in 0 until height) {
            for (x in 0 until width) {
                val color = if (matrix.get(x, y)) 0x00.toByte() else 0xFF.toByte()
                val offset = (y * width + x) * 4
                pixels[offset] = color
                pixels[offset + 1] = color
                pixels[offset + 2] = color
                pixels[offset + 3] = 0xFF.toByte()
            }
        }
        bitmap.installPixels(imageInfo, pixels, width * 4)
        bitmap.asComposeImageBitmap()
    } catch (e: Exception) {
        null
    }
}
