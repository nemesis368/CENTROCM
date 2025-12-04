buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
    }
}

plugins {
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false

    // 🔥 ACTUALIZADO (antes 8.2.2)
    id("com.android.application") version "8.5.0" apply false
    id("com.android.library") version "8.5.0" apply false
}
