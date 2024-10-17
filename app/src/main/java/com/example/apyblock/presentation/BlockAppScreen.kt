package com.example.apyblock.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import com.example.apyblock.R
import com.example.apyblock.presentation.viewmodels.MainViewModel

@Composable
fun BlockAppScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    navController: NavController
    ) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {

        },
        bottomBar = {
            BottomBar(viewModel = viewModel, navController = navController)
        },
        containerColor = colorResource(id = R.color.background_dark)
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {




        }
    }
}