package com.habibi.taskmanager.ui.components

import android.text.format.DateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DueDatePickerSection(
    dueDate: Long?,
    onDueDateChange: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }

    val formattedDateString = dueDate?.let { millis ->
        DateFormat.format("d MMMM yyyy HH:mm", Date(millis)).toString()
    } ?: "Set due date"

    // --- Row Displaying Current Due Date ---
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { showDatePicker = true }
            .padding(vertical = 8.dp)
    ) {
        Icon(Icons.Default.Schedule, contentDescription = "Due Date")
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = formattedDateString,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        if (dueDate != null) {
            IconButton(onClick = { onDueDateChange(null) }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove Due Date",
//                    tint = Color.Red.copy(alpha = 0.8f)
                )
            }
        }
    }

    // --- Date Picker ---
    if (showDatePicker) {
        DatePickerModal(
            initialSelectedDateMillis = dueDate,
            onDateSelected = { dateInMillis ->
                selectedDateMillis = dateInMillis
                showTimePicker = true
            },
            onDismiss = { showDatePicker = false }
        )
    }

    // --- Time Picker ---
    if (showTimePicker && selectedDateMillis != null) {
        val calendar = Calendar.getInstance()
        dueDate?.let { calendar.timeInMillis = it }

        val timePickerState = rememberTimePickerState(
            initialHour = calendar.get(Calendar.HOUR_OF_DAY),
            initialMinute = calendar.get(Calendar.MINUTE),
            is24Hour = true
        )

        TimePickerDialog(
            onDismiss = { showTimePicker = false },
            onConfirm = {
                val finalCalendar = Calendar.getInstance().apply {
                    timeInMillis = selectedDateMillis!!
                    set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                    set(Calendar.MINUTE, timePickerState.minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                onDueDateChange(finalCalendar.timeInMillis)
                showTimePicker = false
            },
            content = { TimePicker(state = timePickerState) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    initialSelectedDateMillis: Long?
) {
    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = initialSelectedDateMillis)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Dismiss") }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("OK") }
        },
        text = { content() }
    )
}
