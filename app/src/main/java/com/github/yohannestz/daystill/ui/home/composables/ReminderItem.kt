package com.github.yohannestz.daystill.ui.home.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.yohannestz.daystill.data.model.Reminder

@Composable
fun ReminderCard(
    reminder: Reminder,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onShowOnHomeScreenChanged: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Checkbox(
                checked = reminder.shouldShowOnHomeScreen,
                onCheckedChange = onShowOnHomeScreenChanged
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(text = reminder.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = reminder.note, style = MaterialTheme.typography.bodyMedium)
            }

            IconButton(onClick = { onEditClick(reminder.id) }) {
                Icon(Icons.Default.Edit, contentDescription = null)
            }

            IconButton(onClick = { onDeleteClick(reminder.id) }) {
                Icon(Icons.Default.Delete, contentDescription = null)
            }
        }

    }
}

@Composable
@Preview
private fun ReminderCardPreview() {
    Surface {
        Column {
            ReminderCard(
                reminder = Reminder(
                    0, "Cool title", "Lorem Ipsum is simply dummy text...", date = 0L
                ),
                onEditClick = {},
                onDeleteClick = {},
                onShowOnHomeScreenChanged = {}
            )

            ReminderCard(
                reminder = Reminder(
                    0, "Cool title", "Lorem Ipsum is simply dummy text...", date = 0L
                ),
                onEditClick = {},
                onDeleteClick = {},
                onShowOnHomeScreenChanged = {}
            )
        }
    }
}