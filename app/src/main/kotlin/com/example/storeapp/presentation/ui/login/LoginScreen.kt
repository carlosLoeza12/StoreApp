package com.example.storeapp.presentation.ui.login

import android.content.Context
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.storeapp.R
import com.example.storeapp.core.components.Loading
import com.example.storeapp.core.components.StoreText
import com.example.storeapp.core.ui.theme.StoreAppTheme
import com.example.storeapp.core.util.LottieAnimation
import com.example.storeapp.core.util.ResponseResult

@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel(), navigateMainScreen : () -> Unit) {

    val state by viewModel.loginState.collectAsState()

    when (state) {

        is ResponseResult.Loading -> {

            Loading()
        }

        is ResponseResult.Success -> {

           navigateMainScreen()
        }

        is ResponseResult.Error -> {

            LottieAnimation(R.raw.error)
        }
    }

    val context: Context = LocalContext.current

    LoginContent(makeLogin = { viewModel.signInWithGoogle(context) })
}

@Composable
fun LoginContent(makeLogin : () -> Unit) {

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(all = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.weight(0.5f))

        StoreText(
            text = stringResource(R.string.login_title),
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        StoreText(
            text = stringResource(R.string.login_subtitle),
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.weight(0.5f))

        Image(
            modifier = Modifier.size(250.dp),
            painter = painterResource(R.drawable.logo_app),
            contentDescription = stringResource(R.string.app_icon_description)
        )

        Spacer(modifier = Modifier.weight(0.5f))

        OutlinedButton(
            onClick = { makeLogin() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = MaterialTheme.shapes.small,
        ) {

            Image(
                painter = painterResource(R.drawable.logo_google),
                contentDescription = stringResource(R.string.google_icon_description),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            StoreText(
                text = stringResource(R.string.login_button_google),
                modifier = Modifier,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        StoreText(
            text = stringResource(R.string.login_terms_privacy),
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.weight(0.5f))
    }
}

@Preview
@Composable
fun LoginScreenPreview() {

    StoreAppTheme {

        LoginContent({})
    }
}

