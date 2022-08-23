package com.example.easynote.util

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easynote.ui.theme.BackgroundLoading

@Composable
fun DefaultText(text: String, fontSize: TextUnit = 16.sp, fontWeight: FontWeight? = null) {
    Text(text = text, fontSize = fontSize, fontWeight = fontWeight)
}

@Composable
fun ArrowBackIcon(clickable: () -> Unit) {
    Icon(imageVector = Icons.Default.ArrowBack,
        contentDescription = "",
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .clickable { clickable() }
    )
}

@Composable
fun Loading(
    loadingState: MutableState<Boolean>,
    backgroundColor: Color = BackgroundLoading
) {
    AnimatedVisibility(
        visible = loadingState.value,
        enter = EnterTransition.None,
        exit = ExitTransition.None
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun TextInputPassword(
    valueState: MutableState<String>,
    labelText: String,
    imeAction: ImeAction = ImeAction.Done,
    maxLength: Int? = null
) {
    val passwordVisible = rememberSaveable {
        mutableStateOf(false)
    }
    TextInputField(
        maxLength = maxLength,
        valueState = valueState,
        labelText = labelText,
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = KeyboardType.Password
        ),
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image =
                if (passwordVisible.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

            // Please provide localized description for accessibility services
            val description = if (passwordVisible.value) "Hide password" else "Show password"

            IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                Icon(imageVector = image, description)
            }
        }
    )
}

@Composable
fun TextInputField(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
    valueState: MutableState<String>,
    labelText: String,
    maxLines: Int = 1,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.Transparent
    ),
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.None,
        imeAction = ImeAction.Next
    ),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    maxLength: Int? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        modifier = modifier,
        value = valueState.value,
        onValueChange = {
            val isValidMaxLength = maxLength != null && it.length <= maxLength
            if (isValidMaxLength || maxLength == null) {
                valueState.value = it
            }
        },
        label = { Text(text = labelText) },
        maxLines = maxLines,
        colors = colors,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        keyboardActions = keyboardActions,
    )
}
