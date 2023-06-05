package com.akaiz.noteapp.feature_note.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.akaiz.noteapp.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.akaiz.noteapp.feature_note.presentation.notes.NoteScreen
import com.akaiz.noteapp.feature_note.presentation.util.Screen
import com.akaiz.noteapp.feature_note.presentation.util.SetupNavGraph
import com.akaiz.noteapp.ui.theme.NoteAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            NoteAppTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navControl = rememberNavController()
                    SetupNavGraph(navHostController = navControl)
                }
            }
        }
    }
}