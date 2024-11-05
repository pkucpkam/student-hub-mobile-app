// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false // Firebase Google Services Plugin
}

buildscript {
    repositories {
        google() // Firebase repository
        mavenCentral()
    }

    dependencies {
        // Firebase plugin
        classpath("com.google.gms:google-services:4.4.2")  // Firebase Services plugin for project-level
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.2") //
    }
}
