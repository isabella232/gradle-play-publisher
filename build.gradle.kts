import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

buildscript {
    repositories.deps()

    dependencies {
        classpath(kotlin("gradle-plugin", embeddedKotlinVersion))
    }
}

plugins {
    `lifecycle-base`
    id("com.github.ben-manes.versions") version "0.27.0"
}

buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"

    publishAlways()
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
}

allprojects {
    repositories.deps()

    afterEvaluate {
        convention.findByType<KotlinProjectExtension>()?.apply {
            sourceSets.configureEach {
                languageSettings.progressiveMode = true
                languageSettings.enableLanguageFeature("NewInference")
                languageSettings.useExperimentalAnnotation(
                        "kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }
    }

    tasks.withType<Test> {
        maxParallelForks = 8

        testLogging {
            events("passed", "failed", "skipped")
            showStandardStreams = true
            setExceptionFormat("full")
        }
    }
}
