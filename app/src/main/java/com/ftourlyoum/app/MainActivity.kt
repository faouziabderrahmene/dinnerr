package com.ftourlyoum.app

// ===================================================================
// MainActivity.kt — App Entry Point
// ===================================================================
// Sets up the Compose UI with edge-to-edge display and RTL support.
// The entire UI is built in Jetpack Compose — no XML layouts needed.
// ===================================================================

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.ftourlyoum.app.ui.theme.FtourLyoumTheme
import com.ftourlyoum.app.ui.screens.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge rendering (content draws behind system bars)
        enableEdgeToEdge()

        setContent {
            // Apply our custom Material3 theme
            FtourLyoumTheme {
                // Force RTL layout direction for Arabic text
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    MainScreen()
                }
            }
        }
    }
}