package com.krokosha.marvelhero

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.krokosha.marvelhero.ui.theme.MarvelHeroTheme
import com.krokosha.marvelhero.view.CharacterDetailScreen
import com.krokosha.marvelhero.view.CharactersBottomNav
import com.krokosha.marvelhero.view.CollectionScreen
import com.krokosha.marvelhero.view.LibraryScreen
import com.krokosha.marvelhero.viewmodel.CollectionDbViewModel
import com.krokosha.marvelhero.viewmodel.LibraryApiViewModel
import dagger.hilt.android.AndroidEntryPoint

sealed class Destination(val route: String) {
    object Library : Destination("library")
    object Collection : Destination("collection")
    object CharacterDetail : Destination("character/{characterId}") {
        fun createRoute(characterId: Int?) = "character/$characterId"
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val vm by viewModels<LibraryApiViewModel>()
    private val cvm by viewModels<CollectionDbViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarvelHeroTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    CharactersScaffold(
                        navController = navController,
                        vm = vm,
                        cvm = cvm
                    )
                }
            }
        }
    }
}

@Composable
private fun CharactersScaffold(
    navController: NavHostController,
    vm: LibraryApiViewModel,
    cvm: CollectionDbViewModel
) {
    val ctx: Context = LocalContext.current

    Scaffold(
        bottomBar = { CharactersBottomNav(navController = navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Destination.Library.route
        ) {
            composable(Destination.Library.route) {
                LibraryScreen(
                    navController = navController,
                    vm = vm,
                    paddingValues =  paddingValues
                )
            }

            composable(Destination.Collection.route) {
                CollectionScreen(
                    cvm = cvm,
                    navController = navController
                )
            }

            composable(Destination.CharacterDetail.route) { navBackStackEntry ->
                val id = navBackStackEntry.arguments?.getString("characterId")?.toIntOrNull()

                if (id == null) {
                    Toast.makeText(ctx, "Character id is required", Toast.LENGTH_LONG).show()
                } else {
                    vm.retrieveCharacterBy(id = id)

                    CharacterDetailScreen(
                        lvm = vm,
                        cvm = cvm,
                        paddingValues = paddingValues,
                        navController = navController
                    )
                }
            }
        }
    }
}
