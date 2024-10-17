package com.example.apyblock.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apyblock.R

@Composable
fun OnBoardingScreen(modifier: Modifier = Modifier) {



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.background_dark)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.weight(1f))

        val pagerState = rememberPagerState(initialPage = 0) {
            3
        }

        var selectedTab by remember {
            mutableIntStateOf(pagerState.currentPage)
        }

        val circleModifiers = listOf(
            Modifier
                .padding(8.dp)
                .then(
                    if (selectedTab == 0) Modifier
                        .border(
                            2.dp,
                            colorResource(id = R.color.appGreen),
                            CircleShape
                        )
                        .size(25.dp)
                    else Modifier.size(20.dp)
                ),
            Modifier
                .padding(8.dp)
                .then(
                    if (selectedTab == 1) Modifier
                        .border(
                            2.dp,
                            colorResource(id = R.color.appGreen),
                            CircleShape
                        )
                        .size(25.dp)
                    else Modifier.size(20.dp)
                ),
            Modifier
                .padding(8.dp)
                .then(
                    if (selectedTab == 2) Modifier
                        .border(
                            2.dp,
                            colorResource(id = R.color.appGreen),
                            CircleShape
                        )
                        .size(25.dp)
                    else Modifier.size(20.dp)
                )
        )

        LaunchedEffect(pagerState.currentPage) {
            selectedTab = pagerState.currentPage
        }

        HorizontalPager(state = pagerState , modifier = Modifier.weight(3f)) { page ->
            Screen(modifier = Modifier, image = page)
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Circle,
                contentDescription = "state",
                tint = colorResource(id = R.color.white),
                modifier = circleModifiers[0],
            )
            Icon(
                imageVector = Icons.Filled.Circle,
                contentDescription = "state",
                tint = colorResource(id = R.color.white),
                modifier = circleModifiers[1]
            )
            Icon(
                imageVector = Icons.Filled.Circle,
                contentDescription = "state",
                tint = colorResource(id = R.color.white),
                modifier = circleModifiers[2]
            )
        }


        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.Bottom
        ) {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(70.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonColors(
                    containerColor = colorResource(id = R.color.appGreen),
                    contentColor = Color.White,
                    disabledContentColor = colorResource(id = R.color.appGreen),
                    disabledContainerColor = Color.White,
                    )
            ) {
                Text(
                    text = "Get Started",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.5f))

    }
}


@Composable
fun Screen(modifier: Modifier , image : Int) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = when(image){
                0-> painterResource(id = R.drawable.productive)
                1-> painterResource(id = R.drawable.disruption)
                2-> painterResource(id = R.drawable.organised)
                else -> {
                    painterResource(id = R.drawable.productive)
                }
            },
            contentDescription = "be productive",
            modifier = Modifier.weight(9f)
        )

        Text(
            text = when(image){
                0-> "Be Productive"
                1-> "Block Distruption"
                2-> "Get Organised"
                else -> {
                    "Kuch Nahi"
                }
            },
            fontSize = 32.sp,
            color = colorResource(id = R.color.white),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
    }
}




@Preview(showBackground = true)
@Composable
fun Screen1Preview() {
    Screen(modifier = Modifier , 1)
}