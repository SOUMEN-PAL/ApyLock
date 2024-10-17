package com.example.apyblock.presentation.navigation

sealed class Screens(val route : String) {
    data object onBoardingScreen : Screens("onBoardingScreen")
    data object permissionScreen : Screens("permissionScreen")
    data object blockedAppScreen : Screens("blockedAppScreen")
}