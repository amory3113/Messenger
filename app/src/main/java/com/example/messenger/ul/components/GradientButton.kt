package com.example.messenger.ul.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun GradientButton(text: String, onClick:()-> Unit, modifier: Modifier = Modifier){
    val gradient = Brush.horizontalGradient(
        colors = listOf(
            Primary40,
            Primary80
        )
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(gradient)
            .clickable { onClick() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}