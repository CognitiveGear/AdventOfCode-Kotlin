val ktorVersion by extra { "2.1.3" }

plugins {
    kotlin("jvm") version "1.7.22"
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.22")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
        testImplementation("junit:junit:4.13.2")
    }
}