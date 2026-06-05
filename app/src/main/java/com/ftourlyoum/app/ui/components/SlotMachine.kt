package com.ftourlyoum.app.ui.components

// ===================================================================
// SlotMachine.kt — Slot Machine Spinning Animation
// ===================================================================
// Creates a slot-machine-like spinning effect when picking a dish.
// Rapidly cycles through dish names, gradually slows down, then
// reveals the final pick with a spring animation.
// ===================================================================

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ftourlyoum.app.data.Dish
import com.ftourlyoum.app.data.dishes
import kotlinx.coroutines.delay

/**
 * Displays a slot-machine spinning animation that cycles through
 * dishes and lands on the [finalDish].
 *
 * @param finalDish   The dish to display after spinning stops
 * @param isSpinning  Whether the slot machine is actively spinning
 * @param onComplete  Callback invoked when spinning finishes
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SlotMachine(
    finalDish: Dish,
    isSpinning: Boolean,
    onComplete: () -> Unit
) {
    // Track the currently displayed dish index during spinning
    var currentIndex by remember { mutableIntStateOf(0) }
    var currentSpeed by remember { mutableLongStateOf(80L) }

    // ═══════════════════════════════════════════════════════════
    // Spinning Logic: cycle through dishes with increasing delay
    // ═══════════════════════════════════════════════════════════
    LaunchedEffect(isSpinning) {
        if (!isSpinning) return@LaunchedEffect

        // Reset speed at start
        currentSpeed = 80L

        // Spin loop: gradually slow down
        while (currentSpeed < 300L) {
            delay(currentSpeed)
            currentIndex = (currentIndex + 1) % dishes.size
            currentSpeed += 15L
        }

        // Spinning complete
        onComplete()
    }

    // Determine which dish to show
    val displayDish = if (isSpinning) dishes[currentIndex] else finalDish

    // ═══════════════════════════════════════════════════════════
    // Animated content with vertical slide transition
    // ═══════════════════════════════════════════════════════════
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = displayDish,
            transitionSpec = {
                // Slide up new content, slide down old content
                (slideInVertically { -it } + fadeIn()) togetherWith
                    (slideOutVertically { it } + fadeOut()) using
                    SizeTransform(clip = false)
            },
            label = "slotMachine"
        ) { dish ->
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Emoji icon
                Text(
                    text = dish.emoji,
                    fontSize = 40.sp
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Dish name
                Text(
                    text = dish.name,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}