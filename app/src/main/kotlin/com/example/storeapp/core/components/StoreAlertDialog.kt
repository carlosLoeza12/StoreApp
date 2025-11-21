package com.example.storeapp.core.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.storeapp.R

@Composable
fun StoreAlertDialog(
    title: String,
    text: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmText: String = stringResource(R.string.cart_confirm_button),
    dismissText: String = stringResource(R.string.cart_cancel_button),
    isCancelable: Boolean = true
) {

    AlertDialog(
        onDismissRequest = {

            if (isCancelable) onDismiss()
        },
        title = {

            StoreText(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 2
            )
        },
        text = {

            StoreText(
                text = text,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2
            )
        },
        confirmButton = {

            TextButton(onClick = onConfirm) {

                StoreText(
                    text = confirmText,
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {

            TextButton(onClick = onDismiss) {

                StoreText(text = dismissText, modifier = Modifier)
            }
        }
    )
}