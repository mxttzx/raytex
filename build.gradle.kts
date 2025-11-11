plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.jetbrains.intellij.platform") version "2.7.1"
    id("org.jetbrains.grammarkit") version "2022.3.2.2"
}

group = "com.raytex"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

// Configure IntelliJ Platform Gradle Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
dependencies {
    implementation("org.scilab.forge:jlatexmath:1.0.7")
    compileOnly("org.apache.xmlgraphics:batik-svggen:1.16")
    compileOnly("org.apache.xmlgraphics:batik-dom:1.16")
    compileOnly("org.apache.xmlgraphics:batik-util:1.16")
    intellijPlatform {
        create("IC", "2025.1.4.1")
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
      // Add necessary plugin dependencies for compilation here, example:
      bundledPlugin("com.intellij.java")
    }
}

sourceSets {
    main {
        java.srcDirs("src/main/java", "gen")
        resources.srcDir("src/main/resources")
    }
}
intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "251"
        }

        changeNotes = """
            Initial version
        """.trimIndent()
    }
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<Copy> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}
