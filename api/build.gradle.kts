import org.gradle.api.tasks.bundling.Jar
import org.springframework.boot.gradle.tasks.bundling.BootJar

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = true
jar.enabled = false

plugins {
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":core"))
    runtimeOnly(project(":core-impl"))

    // web
    implementation("org.springframework.boot:spring-boot-starter-web")
    // serialization
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.2")

    // auth
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    // database
    runtimeOnly("com.mysql:mysql-connector-j:9.0.0")
    runtimeOnly("com.h2database:h2")

    // testing
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jdbc")
}