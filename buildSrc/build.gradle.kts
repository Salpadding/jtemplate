/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    // Support convention plugins written in Kotlin. Convention plugins are build scripts in 'src/main' that automatically become available as plugins in the main build.
    `kotlin-dsl`
}

repositories {
    // Use the plugin portal to apply community plugins in convention plugins.
    gradlePluginPortal()
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)
    implementation("org.jetbrains.kotlin.plugin.spring:org.jetbrains.kotlin.plugin.spring.gradle.plugin:1.9.22")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.4.0")
    implementation("io.spring.dependency-management:io.spring.dependency-management.gradle.plugin:1.1.6")
}
