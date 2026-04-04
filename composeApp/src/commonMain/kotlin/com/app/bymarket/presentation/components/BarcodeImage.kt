package com.app.bymarket.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp

@Composable
fun BarcodeImage(
    barcode: String,
    modifier: Modifier = Modifier
) {
    val bitmap = remember(barcode) {
        generateBarcode(barcode)
    }

    Box(modifier = modifier.fillMaxWidth().height(80.dp)) {
        bitmap?.let {
            Image(
                bitmap = it,
                contentDescription = "Barcode $barcode",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

expect fun generateBarcode(text: String): ImageBitmap?
