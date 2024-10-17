package com.example.apyblock.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.apyblock.R
import com.example.apyblock.presentation.viewmodels.MainViewModel

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    navController: NavController
) {
    val selectedButton = viewModel.selectedScreenIndex.intValue
    val navigationBarsPadding = WindowInsets.navigationBars.asPaddingValues()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .imePadding()
            .padding(bottom = navigationBarsPadding.calculateBottomPadding()),
    ) {

        Button(
            onClick = {
                viewModel.selectedScreenIndex.intValue = 0
            },
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            shape = RectangleShape,
            colors = ButtonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Transparent
            )
        ) {

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxSize()
                    .background(
                        color = if (selectedButton == 0) {
                            colorResource(id = R.color.onTapColor)
                        } else {
                            Color.Transparent
                        }
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = if (selectedButton == 0) {
                        painterResource(id = R.drawable.blocked_app_on_clicked)
                    } else {
                        painterResource(id = R.drawable.blocked_app_not_clicked)
                    },
                    contentDescription = "",
                    modifier = Modifier.size(75.dp)
                )
            }


        }

        Button(
            onClick = {
                viewModel.selectedScreenIndex.intValue = 1
            },
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            shape = RectangleShape,
            colors = ButtonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Transparent
            )
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxSize()
                    .background(
                        color = if (selectedButton == 1) {
                            colorResource(id = R.color.onTapColor)
                        } else {
                            Color.Transparent
                        }
                    ),
                contentAlignment = Alignment.Center,

                ) {
                Image(
                    painter = if (selectedButton == 1) {
                        painterResource(id = R.drawable.all_app_clicked)
                    } else {
                        painterResource(id = R.drawable.all_app_not_clicked)
                    },
                    contentDescription = "",
                    modifier = Modifier.size(75.dp)
                )
            }
        }

    }

}

