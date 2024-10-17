package com.example.apyblock.presentation.blockAppScreenUI

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.apyblock.R
import com.example.apyblock.presentation.bottomBar.BottomBar
import com.example.apyblock.presentation.topBar.TopBar
import com.example.apyblock.presentation.viewmodels.MainViewModel
import com.example.apyblock.utils.BannedAppFetchingState

@Composable
fun BlockAppScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    navController: NavController
) {

    LaunchedEffect(Unit) {
        viewModel.getBannedApps()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomBar(viewModel = viewModel, navController = navController)
        },
        containerColor = colorResource(id = R.color.background_dark)
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {

            val bannedAppsListState by viewModel.bannedAppListState.collectAsState()

            when (bannedAppsListState) {
                is BannedAppFetchingState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = (bannedAppsListState as BannedAppFetchingState.Error).e,
                            color = Color.White
                        )
                    }
                }

                is BannedAppFetchingState.Loading -> {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = colorResource(id = R.color.appGreen))
                    }

                }

                is BannedAppFetchingState.Success -> {
                    val bannedAppList =
                        (bannedAppsListState as BannedAppFetchingState.Success).appList
                    if (!bannedAppList.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Block,
                                contentDescription = "",
                                tint = Color(0x609B9A9A),
                                modifier = Modifier.size(150.dp)
                            )
                            Text(
                                text = "You Have No Banned Apps",
                                fontSize = 24.sp,
                                color = Color(0x609B9A9A)
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "{ ",
                                    fontSize = 32.sp,
                                    color = colorResource(id = R.color.appGreen),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Blocked Application",
                                    fontSize = 24.sp,
                                    color = Color(0xFFFFFFFF)
                                )
                                Text(
                                    text = " }",
                                    fontSize = 32.sp,
                                    color = colorResource(id = R.color.appGreen),
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {

                            }

                        }
                    }
                }
            }


        }
    }
}