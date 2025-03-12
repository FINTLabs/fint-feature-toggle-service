import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
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
version = System.getenv("VERSION") ?: "0-SNAPSHOT"

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
    implementation(libs.jackson.module.kotlin)
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
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

kapt {
    correctErrorTypes = true
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            testType.set(TestSuiteType.UNIT_TEST)

            targets {
                all {
                    testTask.configure {
                        useJUnitPlatform {
                            excludeTags("integration")
                        }
                        filter {
                            isFailOnNoMatchingTests = false
                        }
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

        val integrationTest by registering(JvmTestSuite::class) {
            testType.set(TestSuiteType.INTEGRATION_TEST)

            sources {
                kotlin {
                    setSrcDirs(files("src/test/kotlin"))
                }
                resources {
                    setSrcDirs(listOf("src/test/resources"))
                }
            }

            dependencies {
                // We can replace direct dependency on test's runtimeClasspath with implementation(project())
                // once https://github.com/gradle/gradle/issues/25269 is resolved

                implementation(sourceSets.test.get().runtimeClasspath)
                implementation(sourceSets.test.get().output)
            }

            targets {
                all {
                    testTask.configure {
                        useJUnitPlatform {
                            includeTags("integration")
                        }
                        filter {
                            isFailOnNoMatchingTests = false
                        }
                        testLogging {
                            events = setOf(TestLogEvent.SKIPPED, TestLogEvent.FAILED)
                            exceptionFormat = TestExceptionFormat.FULL
                            showCauses = true
                            showExceptions = true
                            showStackTraces = true
                        }
                        shouldRunAfter(test)
                    }
                }
            }
        }

        configureEach {
            if (this is JvmTestSuite) {
                useJUnitJupiter()
            }
        }
    }
}

tasks.named("check") {
    dependsOn(testing.suites.named("integrationTest"))
}

apply(from = "https://raw.githubusercontent.com/FINTLabs/fint-buildscripts/master/reposilite.ga.gradle")
