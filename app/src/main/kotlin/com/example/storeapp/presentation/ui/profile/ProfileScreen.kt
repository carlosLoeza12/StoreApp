package com.example.storeapp.presentation.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.storeapp.R
import com.example.storeapp.core.components.Loading
import com.example.storeapp.core.components.StoreAlertDialog
import com.example.storeapp.core.components.StoreAsyncImage
import com.example.storeapp.core.components.StoreText
import com.example.storeapp.core.ui.theme.StoreAppTheme
import com.example.storeapp.core.util.LottieAnimation
import com.example.storeapp.core.util.ResponseResult
import com.example.storeapp.domain.model.User
import com.example.storeapp.domain.model.UserBuilder
import com.example.storeapp.presentation.ui.productDetail.DetailRow

@Composable
fun ProfileTabScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToLogin: () -> Unit
) {

    val profileState by viewModel.profileState.collectAsState()
    val logoutState by viewModel.logoutState.collectAsState()

    when (val currentState: ResponseResult<User> = profileState) {

        is ResponseResult.Loading -> {

            Loading()
        }

        is ResponseResult.Success -> {

            val user: User = currentState.value

            ProfileContent(user, logout = viewModel::logout)
        }

        is ResponseResult.Error -> {

            LottieAnimation(lottieId = R.raw.data_not_found)
        }
    }

    LaunchedEffect(key1 = logoutState) {

        val logoutStateSnapshot: ResponseResult<Boolean>? = logoutState

        if (logoutStateSnapshot is ResponseResult.Success && logoutStateSnapshot.value) {

            navigateToLogin()
        }
    }

    if (logoutState is ResponseResult.Loading) {

        Loading()
    }
}

@Composable
fun ProfileContent(user: User, logout: () -> Unit) {

    var showLogoutDialog: Boolean by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        UserDataContent(user = user)

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 20.dp),
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 1.dp
        )

        Item(iconId = R.drawable.ic_orders, title = stringResource(R.string.profile_item_orders))

        Item(iconId = R.drawable.ic_wishlist, title = stringResource(R.string.profile_item_wishlist))

        Item(iconId = R.drawable.ic_help, title = stringResource(R.string.profile_item_help))

       Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {

                showLogoutDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = MaterialTheme.shapes.small
        ) {

            StoreText(
                text =  stringResource(R.string.profile_logout_button),
                modifier = Modifier,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }

    if(showLogoutDialog) {

        StoreAlertDialog(
            title = stringResource(R.string.profile_logout_dialog_title),
            text = stringResource(R.string.profile_logout_dialog_text),
            onConfirm = {

                showLogoutDialog = false
                logout()
            },
            onDismiss = { showLogoutDialog = false }
        )
    }
}

@Composable
fun UserDataContent(user: User) {

    StoreAsyncImage(
        modifier = Modifier
            .size(230.dp)
            .clip(CircleShape),
        model = user.photoUrl,
        contentScale = ContentScale.Crop
    )

    StoreText(
        text = user.name,
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.onBackground,
        maxLines = 2,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(30.dp))

    DetailRow(label = stringResource(R.string.profile_email_label), value = user.email)

    DetailRow(label = stringResource(R.string.profile_phone_label), value = user.phoneNumber.ifEmpty { stringResource(R.string.profile_phone_na)})
}

@Composable
fun Item(iconId: Int, title: String) {

    Row(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            modifier = Modifier
                .padding(end = 16.dp)
                .size(30.dp),
            painter = painterResource(iconId),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = stringResource(R.string.profile_item_icon_description)
        )

        Column(modifier = Modifier) {

            StoreText(modifier = Modifier.fillMaxWidth(), text = title, style = MaterialTheme.typography.titleSmall)

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                color = MaterialTheme.colorScheme.onBackground,
                thickness = 1.dp
            )
        }
    }
}

@Preview
@Composable
fun ProfileContentPreview() {

    val user: User = UserBuilder()
        .name("Name")
        .email("Email")
        .phoneNumber("1234567890")
        .build()

    StoreAppTheme {

        ProfileContent(user = user, logout = {})
    }
}