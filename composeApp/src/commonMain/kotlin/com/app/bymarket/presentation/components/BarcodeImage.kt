package com.app.bymarket.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BarcodeImage(
    barcode: String,
    modifier: Modifier = Modifier
) {
    val cleanBarcode = remember(barcode) { barcode.trim() }
    
    val bitmap = remember(cleanBarcode) {
        if (cleanBarcode.isNotBlank()) {
            generateBarcode(cleanBarcode)
        } else {
            null
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                // Принудительно белый фон для контраста штрих-кода
                .background(Color.White, RoundedCornerShape(4.dp))
                .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap,
                    contentDescription = "Barcode $cleanBarcode",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit // Изменил на Fit, чтобы не обрезало края
                )
            } else {
                Text(
                    text = if (cleanBarcode.isBlank()) "Пустой код" else "Ошибка генерации",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = cleanBarcode,
            style = MaterialTheme.typography.bodyLarge,
            letterSpacing = 2.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

expect fun generateBarcode(text: String): ImageBitmap?
