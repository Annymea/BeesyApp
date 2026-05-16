package com.beesy.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReorderList(
    onSortElementUp: (ListElement) -> (Unit),
    onSortElementDown: (ListElement) -> Unit,
    list: List<ListElement>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier =
            modifier
                .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(list) { item ->
            ReorderListElement(
                onSortUpClicked = { onSortElementUp(item) },
                onSortDownClicked = { onSortElementDown(item) },
                element = item,
            )
        }
    }
}

@Composable
fun ReorderListElement(
    onSortUpClicked: () -> (Unit),
    onSortDownClicked: () -> (Unit),
    element: ListElement,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier.background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp),
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(12.dp),
            text = element.title,
        )
        Row(
            modifier = Modifier.padding(8.dp),
        ) {
            Icon(
                modifier =
                    Modifier.clickable(
                        onClick = {
                            onSortUpClicked()
                        },
                    ),
                imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = "Edit Task",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Icon(
                modifier =
                    Modifier.clickable(
                        onClick = {
                            onSortDownClicked()
                        },
                    ),
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = "Delete Task",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}