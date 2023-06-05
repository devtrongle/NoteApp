package com.akaiz.noteapp.feature_note.presentation.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.akaiz.noteapp.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.akaiz.noteapp.feature_note.presentation.notes.NoteScreen
import com.akaiz.noteapp.feature_note.presentation.splash.SplashScreen

@Composable
fun SetupNavGraph(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.SplashScreen.route
    ) {
        
        composable(route = Screen.SplashScreen.route) {
            SplashScreen(navHostController = navHostController)
        }
        
        composable(route = Screen.NotesScreen.route) {
            NoteScreen(navController = navHostController)
        }
        
        composable(route = Screen.AddEditNoteScreen.route + "?noteId={noteId}&noteColor={noteColor}",
            arguments = listOf(
                navArgument(
                    name = "noteId"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument(
                    name = "noteColor"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            val color = it.arguments?.getInt("noteColor") ?: -1
            AddEditNoteScreen(
                navController = navHostController,
                noteColor = color
            )
        }
    }
}