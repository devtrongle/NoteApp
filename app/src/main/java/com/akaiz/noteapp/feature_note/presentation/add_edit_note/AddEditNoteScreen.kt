package com.akaiz.noteapp.feature_note.presentation.add_edit_note

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.akaiz.noteapp.feature_note.domain.model.Note
import com.akaiz.noteapp.feature_note.presentation.add_edit_note.components.TransparentHintTextField
import com.akaiz.noteapp.ui.theme.setBackgroundColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteColor: Int,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {

    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value

    val noteBackgroundAnimatable = remember {
        Animatable(
            Color(if (noteColor != -1) noteColor else viewModel.noteColor.value)
        )
    }

    setBackgroundColor(noteBackgroundAnimatable.value)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }

                is AddEditNoteViewModel.UiEvent.SaveNote -> {
                    navController.popBackStack()
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditNoteEvent.SaveNote)
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save note")
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(noteBackgroundAnimatable.value)
                .padding(16.dp),
        ) {
            ColorPicker(viewModel, scope, noteBackgroundAnimatable)
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                isHintVisible = titleState.isHintVisible,
                onValueChange = { txt ->
                    viewModel.onEvent(AddEditNoteEvent.EnteredTitle(txt))
                },
                onFocusChange = { fc ->
                    viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(fc))
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = contentState.text,
                hint = contentState.hint,
                isHintVisible = contentState.isHintVisible,
                onValueChange = { txt ->
                    viewModel.onEvent(AddEditNoteEvent.EnteredContent(txt))
                },
                onFocusChange = { fc ->
                    viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(fc))
                },
                singleLine = false,
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}

@Composable
fun ColorPicker(
    viewModel: AddEditNoteViewModel,
    scope: CoroutineScope,
    noteBackgroundAnimatable: Animatable<Color, AnimationVector4D>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Note.noteColors.forEach { color ->
            val colorInt = color.toArgb()
            Box(modifier = Modifier
                .size(50.dp)
                .shadow(15.dp, CircleShape)
                .clip(CircleShape)
                .background(color = color)
                .border(
                    width = 3.dp,
                    color = if (viewModel.noteColor.value == colorInt) Color.Black else Color.Transparent,
                    shape = CircleShape
                )
                .clickable {
                    scope.launch {
                        noteBackgroundAnimatable.animateTo(
                            targetValue = Color(colorInt),
                            animationSpec = tween(
                                durationMillis = 500
                            )
                        )
                    }
                    viewModel.onEvent(AddEditNoteEvent.ChangeColor(color = colorInt))
                }
            )
        }
    }
}
