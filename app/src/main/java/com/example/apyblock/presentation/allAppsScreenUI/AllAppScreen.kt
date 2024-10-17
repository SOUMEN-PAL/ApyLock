package com.example.apyblock.presentation.allAppsScreenUI

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import com.example.apyblock.R
import com.example.apyblock.presentation.bottomBar.BottomBar
import com.example.apyblock.presentation.topBar.TopBar
import com.example.apyblock.presentation.viewmodels.MainViewModel

@Composable
fun AllAppScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    navController: NavController
) {
    val allAppDataList by viewModel.allAppsDataList.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.getAllAppInSystem(context)
    }

    Scaffold (
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomBar(viewModel = viewModel, navController = navController)
        },
        containerColor = colorResource(id = R.color.background_dark)
    ){
        Column(
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
    }
}