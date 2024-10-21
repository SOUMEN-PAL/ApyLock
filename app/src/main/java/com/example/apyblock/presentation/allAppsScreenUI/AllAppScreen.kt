package com.example.apyblock.presentation.allAppsScreenUI

import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.apyblock.R
import com.example.apyblock.presentation.bottomBar.BottomBar
import com.example.apyblock.presentation.topBar.TopBar
import com.example.apyblock.presentation.viewmodels.MainViewModel
import com.example.apyblock.utils.AllAppsFetchingState
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AllAppScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    navController: NavController
) {
    val allAppDataListState by viewModel.allAppsDataList.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.resetSearchedList()
        viewModel.getAllAppInSystem(context)
    }
    var searchQuery by viewModel.searchQuery
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var isFocused by rememberSaveable { mutableStateOf(viewModel.isSearching.value) }

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
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //Search Bar
            OutlinedTextField(
                value = searchQuery,
                singleLine = true,
                onValueChange = { search ->
                    searchQuery = search
                    viewModel.searchQuery.value = searchQuery
                    viewModel.getAppsContainingLetters(context = context)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                        if (isFocused) {
                            viewModel.isSearching.value = true
                        }
                    }
                    .weight(1.7f),
                trailingIcon = {
                    IconButton(onClick = {
                        if (isFocused) {
                            viewModel.searchQuery.value = ""
                            searchQuery = ""
                            viewModel.resetSearchedList()
                            viewModel.getAllAppInSystem(context)
                            viewModel.isSearching.value = false
                            focusManager.clearFocus()
                        } else {
                            viewModel.isSearching.value = true
                            focusRequester.requestFocus()
                        }
                    }) {
                        if (isFocused) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                modifier = Modifier.size(28.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                },
                label = {
                    Text(
                        text = "Search Application",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = colorResource(id = R.color.onTapColor),
                    unfocusedBorderColor = colorResource(id = R.color.appGreen),
                    unfocusedLabelColor = Color(0xB4AFAFAF),
                    unfocusedTrailingIconColor = colorResource(id = R.color.appGreen),
                    focusedContainerColor = colorResource(id = R.color.onTapColor),
                    focusedBorderColor = colorResource(id = R.color.appGreen),
                    focusedLabelColor = colorResource(id = R.color.appGreen),
                    focusedTrailingIconColor = colorResource(id = R.color.appGreen),
                    cursorColor = colorResource(id = R.color.redApp)
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(9f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                when(allAppDataListState){
                    is AllAppsFetchingState.Error -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = (allAppDataListState as AllAppsFetchingState.Error).e , fontSize = 34.sp , color = Color.White)
                        }
                    }
                    is AllAppsFetchingState.Loading -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is AllAppsFetchingState.Searching -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val searchedAppList = (allAppDataListState as AllAppsFetchingState.Searching).searchedAppList
                            Log.d("appdata" , searchedAppList.toString())
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(24.dp)
                            ) {
                                items(searchedAppList , key = {appData->
                                    appData.packageName
                                }){searchedAppData->

                                    var appIcon by remember { mutableStateOf<Drawable?>(null) }

                                    LaunchedEffect(key1 = searchedAppData.packageName) { // Trigger when packageName changes
                                        appIcon = viewModel.getAPPIcon(context, searchedAppData.packageName)
                                    }

                                    IndividualAppData(appData = searchedAppData, viewModel = viewModel , appIcon = appIcon)
                                }
                            }
                        }
                    }
                    is AllAppsFetchingState.Success -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val allAppList = (allAppDataListState as AllAppsFetchingState.Success).appList
                            Log.d("appdata" , allAppList.toString())
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(24.dp)
                            ) {
                                items(allAppList , key = { appData->
                                    appData.packageName
                                }){allAppData->
                                    var appIcon by remember { mutableStateOf<Drawable?>(null) }

                                    LaunchedEffect(key1 = allAppData.packageName) { // Trigger when packageName changes
                                        appIcon = viewModel.getAPPIcon(context, allAppData.packageName)
                                    }

                                    IndividualAppData(appData = allAppData, viewModel = viewModel , appIcon = appIcon)
                                }
                            }
                        }
                    }
                }
            }


        }





    }
}