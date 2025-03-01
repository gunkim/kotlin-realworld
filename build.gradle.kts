import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    kotlin("jvm") version "1.9.24" apply false
    kotlin("plugin.spring") version "1.9.24" apply false
    kotlin("plugin.jpa") version "1.9.24" apply false
    id("org.springframework.boot") version "3.3.0" apply false
    id("io.spring.dependency-management") version "1.1.5" apply false
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    configureStandardJarTasks()
    configureKotlinCompile()

    group = "io.github.gunkim.realworld"
    version = "0.0.1"

    repositories {
        mavenCentral()
    }
    dependencies {
        implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
        testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

fun Project.configureKotlinCompile() {
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "21"
        }
    }
}

fun Project.configureStandardJarTasks() {
    tasks.named<Jar>("jar") {
        enabled = true
    }
    tasks.named<BootJar>("bootJar") {
        enabled = false
    }
}
