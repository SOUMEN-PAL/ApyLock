package com.example.apyblock.presentation.blockAppScreenUI

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.apyblock.R
import com.example.apyblock.domain.models.AppDataModel
import com.example.apyblock.presentation.viewmodels.MainViewModel
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun BlockedAppItem(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    blockedApp: AppDataModel,
    navController: NavController
) {
    val context = LocalContext.current
    val packageName = blockedApp.packageName
    val appName = blockedApp.appName
    var appIcon by remember { mutableStateOf<Drawable?>(null) }

    LaunchedEffect(key1 = packageName) { // Trigger when packageName changes
        appIcon = viewModel.getAPPIcon(context, packageName)
    }

    val appIconPainter = rememberDrawablePainter(drawable = appIcon)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.onTapColor),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = colorResource(id = R.color.appGreen),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = appIconPainter,
            contentDescription = "",
            modifier = Modifier.weight(2f)
        )

        Column(
            modifier = Modifier.weight(8f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = appName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = {
                        /*TODO*/
                    },
                    colors = ButtonColors(
                        containerColor = colorResource(id = R.color.appGreen),
                        contentColor = Color.White,
                        disabledContentColor = colorResource(id = R.color.appGreen),
                        disabledContainerColor = Color.White,
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Allocate Timer",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

    }

}


