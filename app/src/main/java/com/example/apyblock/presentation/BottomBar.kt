package com.example.apyblock.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.apyblock.presentation.viewmodels.MainViewModel

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    navController: NavController
) {



}


@Preview(showBackground = true)
@Composable
fun BottomBarPreview(){
    BottomBar(viewModel = viewModel(), navController = rememberNavController())
}