// ===================================================================
// Root-level build.gradle.kts
// ===================================================================
// Top-level build file — plugins are applied in app/build.gradle.kts
// ===================================================================

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}