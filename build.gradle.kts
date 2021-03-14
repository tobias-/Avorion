import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val ktlintVersion = "10.0.0"
    kotlin("jvm") version "1.4.20"
    application
    id("org.openjfx.javafxplugin") version "0.0.9"
    id("org.jlleitschuh.gradle.ktlint") version ktlintVersion
    id("org.jlleitschuh.gradle.ktlint-idea") version ktlintVersion
}

group = "be.olsson"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("be.olsson.avorion.Main")
}

javafx {
    version = "15.0.1"
    modules = listOf("javafx.controls")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    kotlin("reflect", "1.4.20")
    testImplementation("org.jsoup:jsoup:1.13.1")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.11.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.named<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "6.8.3"
}

configurations.all {
    resolutionStrategy {
        force("org.jetbrains.kotlin:kotlin-reflect:1.4.20")
    }
}
