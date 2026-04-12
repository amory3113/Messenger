package com.example.messenger.ul.feature.auth

import android.R.attr.fontWeight
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.messenger.R
import com.example.messenger.ul.components.GradientButton
import com.example.messenger.ul.navigation.Screen
import com.example.messenger.ul.theme.Primary30
import com.example.messenger.ul.theme.Primary40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController, viewModel: AuthViewModel = viewModel()){
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    )
    val passwordsMatch = password == confirmPassword && confirmPassword.isNotEmpty()
    val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPasswordStrong = password.length >= 8 &&
            password.any { it.isUpperCase() } &&
            password.any { it.isLowerCase() } &&
            password.any { it.isDigit() } &&
            password.any { !it.isLetterOrDigit() }
    val isFormValid = fullName.isNotBlank() &&
            isEmailValid &&
            nickname.isNotBlank() &&
            isPasswordStrong &&
            passwordsMatch
    val context = LocalContext.current
    val authState by viewModel.authState.collectAsState()
    LaunchedEffect(authState) {
        when(authState){
            is AuthState.Success -> {
                navController.navigate(Screen.Main.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
            is AuthState.Error ->{
                val errorMessage = (authState as AuthState.Error).message
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 48.dp)
    ) {
        item{
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(color = Primary40, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ){
                Text(
                    "M", style = MaterialTheme.typography.headlineMedium.copy(color = Color.White)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.create_acc),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(id = R.string.lets_get_started),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(64.dp))
        }

        item {
            OutlinedTextField(
                value = fullName, onValueChange = { fullName = it },
                placeholder = { Text(stringResource(id = R.string.full_name)) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                shape = RoundedCornerShape(12.dp), singleLine = true, colors = textFieldColors
            )

            OutlinedTextField(
                value = email, onValueChange = { email = it },
                placeholder = { Text(stringResource(id = R.string.email)) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth().padding(bottom = if (email.isNotEmpty() && !isEmailValid) 4.dp else 12.dp),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = textFieldColors,
                isError = email.isNotEmpty() && !isEmailValid
            )
            if (email.isNotEmpty() && !isEmailValid) {
                Text(
                    text = "Неверный формат электронной почты",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 12.dp, start = 16.dp).fillMaxWidth()
                )
            }

            OutlinedTextField(
                value = nickname, onValueChange = { nickname = it },
                placeholder = { Text(stringResource(id = R.string.nickname)) },
                leadingIcon = { Icon(Icons.Default.AccountBox, contentDescription = null) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                shape = RoundedCornerShape(12.dp), singleLine = true, colors = textFieldColors
            )

            OutlinedTextField(
                value = password, onValueChange = { password = it },
                placeholder = { Text(stringResource(id = R.string.password)) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = "Toggle password visibility")
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                shape = RoundedCornerShape(12.dp), singleLine = true, colors = textFieldColors,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = password.isNotEmpty() && !isPasswordStrong
            )
            if (password.isNotEmpty() && !isPasswordStrong) {
                Text(
                    text = "Пароль должен быть от 8 символов, содержать заглавные, строчные буквы, цифры и спецсимвол.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            OutlinedTextField(
                value = confirmPassword, onValueChange = { confirmPassword = it },
                placeholder = { Text(stringResource(id = R.string.confirm_password)) },
                trailingIcon = {
                    val image = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = image, contentDescription = "Toggle confirm password visibility")
                    }
                },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                shape = RoundedCornerShape(12.dp), singleLine = true, colors = textFieldColors,
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = confirmPassword.isNotEmpty() && !passwordsMatch
            )
        }
        item {
            val isLoading = authState is AuthState.Loading
            GradientButton(
                text = stringResource(id = R.string.sign_up),
                enabled = isFormValid,
                isLoading = isLoading,
                onClick = {
                    viewModel.signUp(
                        email = email,
                        password = password,
                        fullName = fullName,
                        nickname = nickname
                    )
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = buildAnnotatedString {
                    append(stringResource(id = R.string.already_have_acc))
                    append(" ")
                    withStyle(style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    ){
                        append(stringResource(id = R.string.login))
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable{navController.popBackStack() }
            )
        }

    }
}