plugins {
    kotlin("jvm") version "2.0.0"
}

group = "ru.serega6531"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
    compilerOptions {
        freeCompilerArgs.add("-Xdebug")
    }
}