package com.example.capcutexportanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capcutexportanimation.ui.theme.CapCutExportAnimationTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CapCutExportAnimationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedCupCat()
                    }
                }
            }
        }
    }
}

@Composable
fun CupCatAnimation(progress: Float, image: Painter) {
    Box(
        modifier = Modifier
            .size(200.dp, 300.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Text(
            text = "${(progress * 100).toInt()}%",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )

        // Animated progress line around the image
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 8f
            val pathLength = 2 * (size.width + size.height)
            val currentLength = progress * pathLength

            // Draw the white border
            drawRoundRect(
                color = Color.Black,
                size = size,
                style = Stroke(width = strokeWidth),
            )

            if (currentLength <= size.width) {
                // Top edge
                drawLine(
                    start = Offset(0f, 0f),
                    end = Offset(currentLength, 0f),
                    color = Color.White,
                    strokeWidth = strokeWidth
                )
            } else {
                // Complete top edge
                drawLine(
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    color = Color.White,
                    strokeWidth = strokeWidth
                )
            }

            if (currentLength > size.width && currentLength <= size.width + size.height) {
                // Right edge
                drawLine(
                    start = Offset(size.width, 0f),
                    end = Offset(size.width, currentLength - size.width),
                    color = Color.White,
                    strokeWidth = strokeWidth
                )
            } else if (currentLength > size.width + size.height) {
                // Complete right edge
                drawLine(
                    start = Offset(size.width, 0f),
                    end = Offset(size.width, size.height),
                    color = Color.White,
                    strokeWidth = strokeWidth
                )
            }

            if (currentLength > size.width + size.height && currentLength <= 2 * size.width + size.height) {
                // Bottom edge
                drawLine(
                    start = Offset(size.width, size.height),
                    end = Offset(
                        size.width - (currentLength - (size.width + size.height)),
                        size.height
                    ),
                    color = Color.White,
                    strokeWidth = strokeWidth
                )
            } else if (currentLength > 2 * size.width + size.height) {
                // Complete bottom edge
                drawLine(
                    start = Offset(size.width, size.height),
                    end = Offset(0f, size.height),
                    color = Color.White,
                    strokeWidth = strokeWidth
                )
            }

            if (currentLength > 2 * size.width + size.height) {
                // Left edge
                drawLine(
                    start = Offset(0f, size.height),
                    end = Offset(
                        0f,
                        size.height - (currentLength - (2 * size.width + size.height))
                    ),
                    color = Color.White,
                    strokeWidth = strokeWidth
                )
            }
        }
    }
}

@Composable
fun AnimatedCupCat() {
    var start by remember { mutableStateOf(true) }
    var progress by remember { mutableStateOf(0f) }
    var isAnimating by remember { mutableStateOf(false) }
    val image = painterResource(id = R.drawable.image)

    LaunchedEffect(isAnimating) {
        if (isAnimating) {
            while (progress < 1f) {
                delay(20)
                progress += 0.005f
                if (progress > 1f) progress = 1f
            }
        } else {
            progress = 0f
            start = true
        }
    }

    val fillColor = Color(0xFF3A86FF).copy(alpha = progress)
    val doneColor = Color(0xFF4CAF50)

    val buttonColor = when {
        start -> Color.White
        progress < 1f -> fillColor
        else -> doneColor
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        CupCatAnimation(progress = progress, image = image)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isAnimating = !isAnimating
                start = false
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(50)),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
        ) {
            Text(
                text = if (start) "Export" else if (progress < 1f) "Exporting" else "Done",
                color = if (start) Color.Black else Color.White
            )
        }
    }

}


