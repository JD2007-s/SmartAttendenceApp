// This file defines the plugins used by the entire project.
// It applies them to all subprojects (like the :app module).
plugins {
    // Alias for the Android Application plugin
    id("com.android.application") version "8.4.1" apply false
    // Alias for the Kotlin Android plugin
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false
}