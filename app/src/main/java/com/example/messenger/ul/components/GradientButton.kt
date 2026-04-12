package com.example.messenger.ul.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.messenger.ul.theme.Primary40
import com.example.messenger.ul.theme.Primary80
import com.example.messenger.ul.theme.Secondary80
import com.example.messenger.ul.theme.Secondary90

@Composable
fun GradientButton(
    text: String,
    onClick:()-> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    val isActuallyEnabled = enabled && !isLoading
    val gradient = if (enabled) {
        Brush.horizontalGradient(listOf(Primary40, Primary80))
    } else {
        Brush.horizontalGradient(listOf(Secondary90, Secondary80))
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(gradient)
            .clickable(enabled = enabled) { onClick() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ){
        if (isLoading){
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
        } else {
        Text(
            text = text,
            color = if (enabled) Color.White else Color.Gray,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
        }
    }
}