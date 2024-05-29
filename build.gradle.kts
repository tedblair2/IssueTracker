// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.hilt.plugin) apply false
    alias(libs.plugins.ksp.plugin) apply false
    alias(libs.plugins.apollo.plugin) apply false
    alias(libs.plugins.google.services.plugin) apply false
    alias(libs.plugins.parcelable) apply false
    alias(libs.plugins.protobuf.plugin) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}