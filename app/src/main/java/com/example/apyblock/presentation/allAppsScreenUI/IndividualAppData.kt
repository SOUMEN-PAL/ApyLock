package com.example.apyblock.presentation.allAppsScreenUI

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.apyblock.R

@Composable
fun IndividualAppData(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.onTapColor)
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){

        Image(
            painter = painterResource(id = R.drawable.blocked_app_on_clicked),
            contentDescription = "",
            modifier = Modifier.weight(2f)
        )

        Text(
            text = "Kuch Bhi",
            modifier = Modifier.weight(6f),
            textAlign = TextAlign.Center,
            color = Color.White
        )

        Column(
            modifier = Modifier.weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Switch(
                checked = true,
                onCheckedChange = {

                }
            )
        }


    }
}


@Preview(showBackground = true)
@Composable
fun IndividualAppDataPreview(){
    IndividualAppData()
}