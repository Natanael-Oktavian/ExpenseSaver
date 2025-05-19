/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package natanael.oktavian.expensesaver

import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import natanael.oktavian.expensesaver.R.string
import natanael.oktavian.expensesaver.ui.navigation.ExpenseNavHost
import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.material3.DatePicker
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.ui.viewinterop.AndroidView
import natanael.oktavian.expensesaver.data.FilterData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Date
import java.util.UUID

/**
 * Top level composable that represents screens for the application.
 */
@Composable
fun ExpenseApp(navController: NavHostController = rememberNavController()) {
    ExpenseNavHost(navController = navController)
}

/**
 * App bar to display title and conditionally display the back navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {},
    onDateSelected: ((FilterData) -> Unit)? = null
) {
    var startDate by remember { mutableStateOf<Date?>(null) }
    var endDate by remember { mutableStateOf<Date?>(null) }

    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Filled.ArrowBack,
                        contentDescription = stringResource(string.back_button)
                    )
                }
            }
        },
         actions = {
             if (!canNavigateBack) {
                 IconButton(
                     onClick = {
                         showStartPicker = true
                     }
                 ) {
                     Icon(
                         painter = painterResource(R.drawable.filter),
                         contentDescription = "Filter",
                         tint = MaterialTheme.colorScheme.onBackground
                     )
                 }
             }
        },
    )

    if (showStartPicker) {
        DatePickerDialogWithTitle(
            title = "Select Start Date",
            isEndOfDay = false,
            onDateSelected = {
                startDate = it
                showStartPicker = false
                showEndPicker = true
            },
            onDismiss = { showStartPicker = false }
        )
    }

    if (showEndPicker) {
        DatePickerDialogWithTitle(
            title = "Select End Date",
            isEndOfDay = true,
            onDateSelected = {
                endDate = it
                showEndPicker = false
                val filterData = FilterData(startDate ?: Date(),endDate ?: Date())
                onDateSelected?.invoke(filterData)
            },
            onDismiss = { showEndPicker = false }
        )
    }
}

@Composable
fun DatePickerDialogWithTitle(
    title: String,
    isEndOfDay : Boolean,
    onDateSelected: (Date) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    // Hold reference to DatePicker
    var datePicker: DatePicker? = remember { null }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            AndroidView(factory = {
                DatePicker(context).also {
                    datePicker = it
                    it.init(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ) { _, year, month, day ->
                        calendar.set(year, month, day)
                    }
                }
            })
        },
        confirmButton = {
            TextButton(onClick = {
                datePicker?.let {
                    calendar.set(it.year, it.month, it.dayOfMonth)
                    if(!isEndOfDay) {
                        calendar.set(Calendar.HOUR_OF_DAY, 0)
                        calendar.set(Calendar.MINUTE, 0)
                        calendar.set(Calendar.SECOND, 0)
                        calendar.set(Calendar.MILLISECOND, 0)
                    }
                    else{
                        calendar.set(Calendar.HOUR_OF_DAY, 23)
                        calendar.set(Calendar.MINUTE, 59)
                        calendar.set(Calendar.SECOND, 59)
                        calendar.set(Calendar.MILLISECOND, 59)
                    }
                    onDateSelected(calendar.time)
                }
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

