package com.example.apyblock

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apyblock.presentation.OnBoardingScreen
import com.example.apyblock.presentation.PermissionScreen
import com.example.apyblock.presentation.navigation.Navigation
import com.example.apyblock.presentation.viewmodels.MainViewModel
import com.example.apyblock.presentation.viewmodels.MainViewModelFactory
import com.example.apyblock.presentation.viewmodels.TimePickerViewModel
import com.example.apyblock.ui.theme.ApyBlockTheme
/*

Use This command if on linux to run emulator smooth ly

DRI_PRIME=1 emulator -avd Pixel_8_API_35



 */

class MainActivity : ComponentActivity() {
    lateinit var mainViewModel: MainViewModel
    lateinit var timePickerViewModel : TimePickerViewModel
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val repository = (application as ApyLockApplication).appRepository
            mainViewModel = viewModel(factory = MainViewModelFactory(repository))
            timePickerViewModel = viewModel()
            ApyBlockTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(timePickerViewModel = timePickerViewModel , viewModel = mainViewModel, context = this@MainActivity , modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

