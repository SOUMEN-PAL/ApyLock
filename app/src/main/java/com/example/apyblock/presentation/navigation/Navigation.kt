package com.example.apyblock.presentation.navigation

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.apyblock.androidComponents.AppBlockingService
import com.example.apyblock.domain.models.AppDataModel
import com.example.apyblock.presentation.blockAppScreenUI.BlockAppScreen
import com.example.apyblock.presentation.OnBoardingScreen
import com.example.apyblock.presentation.PermissionScreen
import com.example.apyblock.presentation.allAppsScreenUI.AllAppScreen
import com.example.apyblock.presentation.appInfoScreenUI.AppInfoScreen
import com.example.apyblock.presentation.viewmodels.MainViewModel
import com.example.apyblock.presentation.viewmodels.TimePickerViewModel
import com.example.apyblock.utils.AccessibilityServiceUtil
import kotlinx.serialization.json.Json

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    timePickerViewModel: TimePickerViewModel,
    context: Context
) {


    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = if (AccessibilityServiceUtil.isAccessibilityServiceEnabled(
                context,
                AppBlockingService::class.java
            )
        ) {
            Screens.blockedAppScreen.route
        } else {
            Screens.onBoardingScreen.route
        }
    ) {

        composable(
            route = Screens.onBoardingScreen.route,
            enterTransition = ::slideIntoRight,
            exitTransition = ::slideOutToLeft
        ) {
            OnBoardingScreen(navController = navController)
        }

        composable(
            route = Screens.permissionScreen.route,
            enterTransition = ::slideIntoLeft,
            exitTransition = ::slideOutToRight
        ) {
            PermissionScreen(navController = navController)
        }

        composable(
            route = Screens.blockedAppScreen.route,
        ) {
            BlockAppScreen(viewModel = viewModel, navController = navController)
        }

        composable(
            route = Screens.addInfoScreen.route + "/{data}",
            arguments = listOf(navArgument("data") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val jsonData = navBackStackEntry.arguments?.getString("data")
            val appDataModel = remember(jsonData) { // Remember the decoded data
                jsonData?.let { Json.decodeFromString<AppDataModel>(it) }
            }

            appDataModel?.let { AppInfoScreen(appData = it , mainViewModel = viewModel , timePickerViewModel = timePickerViewModel , navController = navController) } // Pass the decoded data to AppInfoScreen
        }

        composable(
            route = Screens.allAppDataScreen.route,
        ) {
            AllAppScreen(viewModel = viewModel, navController = navController)
        }

    }
}

fun slideIntoLeft(scope: AnimatedContentTransitionScope<NavBackStackEntry>): EnterTransition {
    return scope.slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(300)
    )
}

fun slideIntoRight(scope: AnimatedContentTransitionScope<NavBackStackEntry>): EnterTransition {
    return scope.slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(300)
    )
}

fun slideOutToLeft(scope: AnimatedContentTransitionScope<NavBackStackEntry>): ExitTransition {
    return scope.slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(300)
    )
}

fun slideOutToRight(scope: AnimatedContentTransitionScope<NavBackStackEntry>): ExitTransition {
    return scope.slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(300)
    )
}

