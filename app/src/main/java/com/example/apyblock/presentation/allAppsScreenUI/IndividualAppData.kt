package com.example.apyblock.presentation.allAppsScreenUI

import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apyblock.R
import com.example.apyblock.domain.models.AppDataModel
import com.example.apyblock.presentation.viewmodels.MainViewModel
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IndividualAppData(
    modifier: Modifier = Modifier,
    appData : AppDataModel,
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val packageName = appData.packageName
    val appName = appData.appName
    var appIcon by remember { mutableStateOf<Drawable?>(null) }

    LaunchedEffect(key1 = packageName) { // Trigger when packageName changes
        appIcon = viewModel.getAPPIcon(context, packageName)
    }
    val appIconPainter = rememberDrawablePainter(drawable = appIcon)
    var isBlocked by remember {
        mutableStateOf(appData.blocked)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.onTapColor),
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = colorResource(id = R.color.appGreen),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = appIconPainter,
            contentDescription = "",
            modifier = Modifier.weight(2f)
        )

        Text(
            text = appName,
            modifier = Modifier.weight(6f),
            textAlign = TextAlign.Center,
            color = Color.White,
            fontSize = 18.sp
        )

        Column(
            modifier = Modifier.weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Switch(
                checked = isBlocked,
                onCheckedChange = {
                    //TODO:Add To block List
                    isBlocked = it
                    if(isBlocked){
                        val appDataInstance = AppDataModel(
                            packageName = appData.packageName,
                            appName = appData.appName,
                            blocked = isBlocked,
                            startTime = LocalDate.now().atTime(LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                            endTime = LocalDate.now().atTime(LocalTime.of(23, 59)).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                        )
                        viewModel.addAppData(appDataInstance)
                    }else{
                        viewModel.deleteAppFromBannedList(appData.packageName)
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = colorResource(id = R.color.appGreen),
                    uncheckedTrackColor = colorResource(id = R.color.onTapColor),
                    uncheckedBorderColor = colorResource(id = R.color.appGreen),
                    uncheckedThumbColor = colorResource(id = R.color.appGreen)
                )
            )
        }


    }
}

