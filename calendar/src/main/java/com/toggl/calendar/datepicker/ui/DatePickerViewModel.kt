package com.toggl.calendar.datepicker.ui

import java.time.DayOfWeek

data class DatePickerViewModel(
    val weekHeaderLabels: List<DayOfWeek>,
    val weeks: List<List<SelectableDate>>,
    val selectedWeek: Int
)

data class SelectableDate(
    val dateLabel: String,
    val isSelected: Boolean,
    val isToday: Boolean
)