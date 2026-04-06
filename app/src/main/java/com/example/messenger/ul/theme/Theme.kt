package com.example.messenger.ul.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Primary40,
    onPrimary = Color.White,
    primaryContainer = Primary90,        // Светло-голубой для пузырей твоих сообщений
    onPrimaryContainer = Primary10,      // Темно-синий текст внутри твоих пузырей

    secondary = Secondary40,
    onSecondary = Color.White,
    secondaryContainer = Secondary90,    // Светло-серый для пузырей собеседника
    onSecondaryContainer = Secondary10,  // Темный текст внутри пузырей собеседника

    tertiary = Tertiary50,
    onTertiary = Color.White,
    tertiaryContainer = Tertiary90,
    onTertiaryContainer = Tertiary10,

    background = Neutral99,              // Бело-серый фон приложения
    onBackground = Neutral10,            // Черный текст
    surface = Neutral99,                 // Фон карточек
    onSurface = Neutral10,
    surfaceVariant = Neutral90,          // Фон полей ввода (Search)
    onSurfaceVariant = Neutral20         // Текст плейсхолдеров
)

private val DarkColorScheme = darkColorScheme(
    primary = Primary80,                 // В темной теме основной цвет светлее
    onPrimary = Primary20,
    primaryContainer = Primary30,        // Темно-синие пузыри сообщений
    onPrimaryContainer = Primary90,      // Светло-голубой текст на них

    secondary = Secondary80,
    onSecondary = Secondary20,
    secondaryContainer = Secondary30,
    onSecondaryContainer = Secondary90,

    tertiary = Tertiary80,
    onTertiary = Tertiary20,
    tertiaryContainer = Tertiary30,
    onTertiaryContainer = Tertiary90,

    background = Neutral10,              // Почти черный фон приложения
    onBackground = Neutral90,            // Светлый текст
    surface = Neutral10,
    onSurface = Neutral90,
    surfaceVariant = Neutral20,          // Темно-серый фон полей ввода
    onSurfaceVariant = Neutral80
)


@Composable
fun MessengerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}