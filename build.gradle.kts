import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    `jvm-test-suite`
    `maven-publish`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.plugin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.versions)
}

group = "no.fintlabs"
version = System.getenv("RELEASE_VERSION") ?: "0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

ktlint {
    this.version.set(libs.versions.ktlint)
}

dependencies {
    implementation(libs.spring.boot.web)
    kapt(libs.spring.boot.configuration.processor)

    implementation(libs.kotlin.reflect)

//    SLF4J
    implementation(libs.slf4j)

//    Jackson
    implementation(libs.jackson.module.kotlin)

//    Unleash
    implementation(libs.unleash)

//    Test
    testImplementation(libs.spring.boot.test)
    testImplementation(libs.springmockk)
    testImplementation(libs.mockk)
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()

            targets {
                all {
                    testTask.configure {
                        testLogging {
                            events = setOf(TestLogEvent.SKIPPED, TestLogEvent.FAILED)
                            exceptionFormat = TestExceptionFormat.FULL
                            showCauses = true
                            showExceptions = true
                            showStackTraces = true
                        }
                    }
                }
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events = setOf(TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        exceptionFormat = TestExceptionFormat.FULL
        showCauses = true
        showExceptions = true
        showStackTraces = true
    }
}

apply(from = "https://raw.githubusercontent.com/FINTLabs/fint-buildscripts/master/reposilite.fint.ga.gradle")
