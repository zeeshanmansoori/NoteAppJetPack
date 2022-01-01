package com.zee.noteapp.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zee.noteapp.presentation.addEditNote.AddEditNoteScreen
import com.zee.noteapp.presentation.notes.NoteScreen
import com.zee.noteapp.ui.theme.NoteAppTheme
import com.zee.noteapp.util.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            NoteAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {

                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.NoteScreen.route
                    ) {
                        composable(route = Screen.NoteScreen.route) {
                            NoteScreen(navController = navController)
                        }





                        kotlin.runCatching {
                            composable(
                                // do not put space in routes
                                route = Screen.AddEditNoteScreen.route+"?noteId={noteId}&noteColor={noteColor}" ,
                                arguments = listOf(
                                    navArgument(name = "noteId") {
                                        type = NavType.IntType
                                        defaultValue = -1
                                    },
                                    navArgument(name = "noteColor") {
                                        type = NavType.IntType
                                        defaultValue = -1
                                    }
                                )
                            ) {

                                val color = it.arguments?.getInt("noteColor") ?: -1

                                AddEditNoteScreen(
                                    navController = navController,
                                    noteColor = color
                                )
                            }
                        }.onFailure {

                            Log.d("zeeshan", "onCreate: some error $it")
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NoteAppTheme {
        Greeting("Android")
    }
}