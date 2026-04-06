package com.app.bymarket.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun BarcodeScanner(
    onResult: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
)
