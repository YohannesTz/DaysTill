package com.github.yohannestz.daystill.ui.add

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.github.yohannestz.daystill.R
import com.github.yohannestz.daystill.di.appModule
import com.github.yohannestz.daystill.ui.base.composables.DefaultScaffoldWithTopAppBar
import com.github.yohannestz.daystill.ui.base.navigation.NavActionManager
import com.github.yohannestz.daystill.ui.base.util.toFormattedDateString
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplication

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderView(
    navActionManager: NavActionManager
) {
    val viewModel: AddReminderViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val openDatePickerDialog = remember {
        mutableStateOf(false)
    }

    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)

    DefaultScaffoldWithTopAppBar(title = stringResource(R.string.add_reminder), navigateBack = {
        navActionManager.goBack()
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = { text ->
                    viewModel.onEvent(AddReminderEvent.SetTitle(text))
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.add_reminder_title)
                    )
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.note,
                onValueChange = { text ->
                    viewModel.onEvent(AddReminderEvent.SetNote(text))
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.add_reminder_note)
                    )
                },
                minLines = 6,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.date.toFormattedDateString(),
                onValueChange = {},
                label = {
                    Text(
                        text = stringResource(id = R.string.pick_happening_date)
                    )
                },
                leadingIcon = {
                    IconButton(onClick = {
                        openDatePickerDialog.value = true
                    }) {
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
                    }
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .clickable {
                        openDatePickerDialog.value = true
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.onEvent(AddReminderEvent.AddReminder(
                        onReminderAdded = {
                            navActionManager.goBack()
                        }
                    ))
                },
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = stringResource(id = R.string.add_reminder_button),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            if (openDatePickerDialog.value) {
                DatePickerDialog(
                    onDismissRequest = { openDatePickerDialog.value = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.onEvent(
                                    AddReminderEvent.SetDate(
                                        datePickerState.selectedDateMillis ?: 0L
                                    )
                                )
                                openDatePickerDialog.value = false
                            }
                        ) {
                            Text(stringResource(id = R.string.ok))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { openDatePickerDialog.value = false }) {
                            Text(stringResource(id = R.string.cancel))
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
        }
    }
}

@Preview
@Composable
private fun AddReminderViewPreview() {
    val context = LocalContext.current

    KoinApplication(application = {
        androidContext(context)
        modules(appModule)
    }) {
        Surface {
            AddReminderView(navActionManager = NavActionManager(rememberNavController()))
        }
    }
}