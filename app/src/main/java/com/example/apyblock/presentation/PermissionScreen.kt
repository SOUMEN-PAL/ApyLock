package com.example.apyblock.presentation

import android.content.Intent
import android.provider.Settings
import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
import com.example.apyblock.R
import com.example.apyblock.androidComponents.AppBlockingService
import com.example.apyblock.utils.AccessibilityServiceUtil
import kotlinx.coroutines.delay

@Composable
fun PermissionScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var shouldNavigate by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        while (true) {
            if (AccessibilityServiceUtil.isAccessibilityServiceEnabled(context, AppBlockingService::class.java)) {
                shouldNavigate = true
                break
            }
            delay(100)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.background_dark)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.7f))

        Box(modifier = Modifier
            .size(600.dp)
            .weight(8f) , contentAlignment = Alignment.Center){
            Image(painter = painterResource(id = R.drawable.permission), contentDescription = "" , modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .offset(y = (-35).dp))

            Text(
                text = "We need Accessibility\nPermission to help you stay\nfocused by monitoring and\ntemporarily blocking apps like\nInstagram and YouTube during\nyour scheduled time. This will\nhelp you reduce distractions\nand build healthier usage\nhabits.",
                color = Color.White,
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(24.dp)
                    .offset(y = 45.dp, x = (-20).dp)
            )
        }

        Row(
            modifier = Modifier.weight(2f),
            verticalAlignment = Alignment.Bottom
        ) {
            Button(
                onClick = {
                    if(!AccessibilityServiceUtil.isAccessibilityServiceEnabled(context = context , service = AppBlockingService::class.java)){
                            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                            context.startActivity(intent)
                    }

                    if(shouldNavigate){
                        //TODO: navigate to the next screen
                    }

                },
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
                    text = "Grant Access",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.5f))

    }
}


@Preview(showBackground = true)
@Composable
fun PermissionScreenPreview(){
    PermissionScreen()
}