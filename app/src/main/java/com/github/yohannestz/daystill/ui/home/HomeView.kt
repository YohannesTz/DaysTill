package com.github.yohannestz.daystill.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.yohannestz.daystill.R
import com.github.yohannestz.daystill.data.model.Reminder
import com.github.yohannestz.daystill.ui.base.composables.PullToRefreshBox
import com.github.yohannestz.daystill.ui.base.composables.ScaffoldWithTopAppBarNavIcon
import com.github.yohannestz.daystill.ui.base.navigation.NavActionManager
import com.github.yohannestz.daystill.ui.base.navigation.Surface
import com.github.yohannestz.daystill.ui.home.composables.ReminderCard
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    navActionManager: NavActionManager
) {
    val viewModel: HomeViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var reminderToDelete by remember { mutableStateOf<Reminder?>(null) }

    val themeStyle = viewModel.currentTheme.collectAsStateWithLifecycle()

    ScaffoldWithTopAppBarNavIcon(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navActionManager.navigateTo(Surface.AddReminderView.value)
                },
                icon = { Icon(Icons.Filled.Add, null) },
                text = { Text(text = stringResource(id = R.string.add_reminder)) }
            )
        },
        title = stringResource(R.string.home_title),
        themeStyle = themeStyle.value,
        onThemeToggleIconClick = {
            viewModel.onEvent(HomeEvent.ToggleTheme)
        }
    ) { paddingValues ->

        PullToRefreshBox(
            isRefreshing = state.isLoading,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            onRefresh = {
                viewModel.onEvent(HomeEvent.Refresh)
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (!state.error.isNullOrEmpty() && !state.isLoading) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.something_was_wrong),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.error ?: "",
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            viewModel.onEvent(HomeEvent.Refresh)
                        }) {
                            Text(
                                text = stringResource(id = R.string.retry),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.reminders) { reminder ->
                            ReminderCard(
                                reminder = reminder,
                                onEditClick = {
                                    navActionManager.navigateTo(
                                        Surface.EditReminderView.value + "/${reminder.id}"
                                    )
                                },
                                onDeleteClick = {
                                    reminderToDelete = reminder
                                    showDeleteDialog = true
                                },
                                onShowOnHomeScreenChanged = {
                                    viewModel.onEvent(
                                        HomeEvent.UpdateReminder(
                                            reminder.copy(
                                                shouldShowOnHomeScreen = it
                                            )
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text(text = stringResource(R.string.delete_reminder)) },
                text = { Text(text = stringResource(R.string.confirm_delete_message)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            reminderToDelete?.let {
                                viewModel.onEvent(HomeEvent.DeleteReminder(it))
                            }
                            showDeleteDialog = false
                        }
                    ) {
                        Text(text = stringResource(R.string.yes))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDeleteDialog = false }
                    ) {
                        Text(text = stringResource(R.string.no))
                    }
                }
            )
        }
    }
}
