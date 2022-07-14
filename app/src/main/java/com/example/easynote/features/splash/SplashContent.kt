package com.example.easynote.features.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easynote.R
import com.example.easynote.base.BaseContent

class SplashContent  : BaseContent() {

    @Composable
    override fun register() {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AppLogo()

                Spacer(modifier = Modifier.height(16.dp))

                AppNameText()

                Spacer(modifier = Modifier.height(4.dp))

                AppDescriptionText()
            }
        }
    }

    @Composable
    private fun AppLogo() {
        Image(
            modifier = Modifier.size(size = 140.dp),
            painter = painterResource(id = R.drawable.img_notes),
            contentDescription = "logo app"
        )
    }

    @Composable
    private fun AppDescriptionText() {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            text = stringResource(id = R.string.app_desc),
            style = TextStyle(
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Light
            )
        )
    }

    @Composable
    private fun AppNameText() {
        Text(
            text = stringResource(id = R.string.app_name),
            style = TextStyle(
                fontSize = 20.sp,
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold
            )
        )
    }

}
