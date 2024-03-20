import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
	`jvm-test-suite`
	`maven-publish`
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlin.plugin.spring)
	alias(libs.plugins.spring.boot)
	alias(libs.plugins.ktlint)
	alias(libs.plugins.versions)
}

group = "no.fint"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

ktlint {
	this.version.set(libs.versions.klint)
}

dependencies {
	implementation(kotlin("stdlib"))
	implementation(kotlin("reflect"))

	implementation(libs.spring.boot.web)

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
	mavenCentral()
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

apply( from = "https://raw.githubusercontent.com/FINTLabs/fint-buildscripts/master/reposilite.fint.ga.gradle")