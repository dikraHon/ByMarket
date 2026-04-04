package com.app.bymarket.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
actual fun BarcodeScanner(
    onResult: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Barcode scanner is not supported on iOS yet.")
    }
}
