package com.alexcao.dexpense.presentation.commons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.alexcao.dexpense.utils.extensions.toTitleCase

@Composable
fun AppHeader(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = navHostController.currentDestination?.route
                ?.substringAfterLast("/")?.toTitleCase()
                ?: "Unknown",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(8.dp)
        )

        // check can pop
        if (navHostController.previousBackStackEntry != null) {
            IconButton(onClick = { navHostController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Close button",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}