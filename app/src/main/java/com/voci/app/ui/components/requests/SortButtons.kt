package com.voci.app.ui.components.requests

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SortButtons(
    sortOptions: List<SortOption>,              // List of sort options
    selectedSortOption: SortOption,             // Currently selected sort option
    onSortOptionSelected: (SortOption) -> Unit  // Callback to handle sort option selection
) {
    Row {
        // Create a button for each sort option
        sortOptions.forEach { option ->
            Button(
                onClick = { onSortOptionSelected(option) }, // Callback when button is clicked
                // Style
                modifier = Modifier.padding(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (option.label == selectedSortOption.label) {
                        MaterialTheme.colorScheme.primary // Highlight selected button
                    } else {
                        MaterialTheme.colorScheme.surface // Default button color
                    },
                    contentColor = if (option.label == selectedSortOption.label) {
                        MaterialTheme.colorScheme.onPrimary // Highlight selected button
                    } else {
                        MaterialTheme.colorScheme.onSurface // Default button color
                    }

                )
            ) {
                Text(option.label)  // Display the button label
            }
        }
    }
}