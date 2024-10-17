package com.example.apyblock.presentation.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apyblock.androidComponents.AppBlockingService
import com.example.apyblock.presentation.OnBoardingScreen
import com.example.apyblock.presentation.PermissionScreen
import com.example.apyblock.presentation.viewmodels.MainViewModel
import com.example.apyblock.utils.AccessibilityServiceUtil

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    context : Context
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = if(AccessibilityServiceUtil.isAccessibilityServiceEnabled(context , AppBlockingService::class.java)){
            Screens.permissionScreen.route
        }
        else{
            Screens.onBoardingScreen.route
        }
    ){

        composable(route = Screens.onBoardingScreen.route){
            OnBoardingScreen()
        }

        composable(route = Screens.permissionScreen.route){
            PermissionScreen()
        }

    }
}