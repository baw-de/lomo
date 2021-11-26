import org.openjfx.gradle.*
import java.lang.reflect.Field
import java.lang.reflect.Method

group = "de.baw"
version = "1.1-beta3-SNAPSHOT"
description = "LoMo"

val releasePlatform: String? by project

plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.0.10"
    id("org.beryx.jlink") version "2.24.4"
    id("net.researchgate.release") version "2.8.1"
}

repositories {
    mavenCentral()
}

javafx {
    version = "17"
    modules("javafx.swing", "javafx.fxml", "javafx.controls")

    overrideJavaFXPlatform(this)
}

dependencies {
    implementation("org.controlsfx:controlsfx:11.1.0")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:3.0.1")
    implementation("org.glassfish.jaxb:jaxb-runtime:3.0.1")

}

application {
    mainModule.set("de.baw.lomo")
    mainClass.set("de.baw.lomo.GuiStart")
//    applicationDefaultJvmArgs = listOf("--add-modules","org.glassfish.jaxb.runtime")
}

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

jlink {

    imageName.set("lomo-${project.version}")

    launcher {
        noConsole = true
    }
    addOptions("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages")
    addOptions("--release-info", "add:LOMO_VERSION=${project.version}")
//    addOptions("--add-modules", "org.glassfish.jaxb.runtime")

    if (releasePlatform != null) {
        targetPlatform(releasePlatform) {

            val jdkUrl = when (releasePlatform) {
                "win-x64" -> "https://download.java.net/java/GA/jdk17.0.1/2a2082e5a09d4267845be086888add4f/12/GPL/openjdk-17.0.1_windows-x64_bin.zip"
                "linux-x64" -> "https://download.java.net/java/GA/jdk17.0.1/2a2082e5a09d4267845be086888add4f/12/GPL/openjdk-17.0.1_linux-x64_bin.tar.gz"
                "linux-aarch64" -> "https://download.java.net/java/GA/jdk17.0.1/2a2082e5a09d4267845be086888add4f/12/GPL/openjdk-17.0.1_linux-aarch64_bin.tar.gz"
                "mac-x64" -> "https://download.java.net/java/GA/jdk17.0.1/2a2082e5a09d4267845be086888add4f/12/GPL/openjdk-17.0.1_macos-x64_bin.tar.gz"
                "mac-aarch64" -> "https://download.java.net/java/GA/jdk17.0.1/2a2082e5a09d4267845be086888add4f/12/GPL/openjdk-17.0.1_macos-aarch64_bin.tar.gz"
                else -> throw InvalidUserDataException("Invalid release platform: $releasePlatform")
            }

            @Suppress("INACCESSIBLE_TYPE")
            setJdkHome(jdkDownload(jdkUrl))
        }
    }
}

tasks.named<org.beryx.jlink.JlinkTask>("jlink") {
    doLast {
        copy {
            from(layout.projectDirectory)
            include("*.md")
            into(imageDirFromName)
        }
        copy {
            from(layout.projectDirectory)
            include("*.txt")
            into("$imageDirFromName/legal/de.baw.lomo/")
        }
    }
}

fun overrideJavaFXPlatform(javaFXOptions: JavaFXOptions) {

    val javafxPlatformOverride = releasePlatform?.substringBefore("-")

    if (javafxPlatformOverride != null) {
        val javafxPlatform: JavaFXPlatform = JavaFXPlatform.values()
            .firstOrNull { it.classifier == javafxPlatformOverride }
            ?: throw IllegalArgumentException(
                "JavaFX platform $javafxPlatformOverride not in list:" +
                        " ${JavaFXPlatform.values().map { it.classifier }}"
            )

        logger.info("Overriding JavaFX platform to {}", javafxPlatform)

        // Override the private platform field
        val platformField: Field = JavaFXOptions::class.java.getDeclaredField("platform")
        platformField.isAccessible = true
        platformField.set(javaFXOptions, javafxPlatform)

        // Invoke the private updateJavaFXDependencies() method
        val updateDeps: Method =
            JavaFXOptions::class.java.getDeclaredMethod("updateJavaFXDependencies")
        updateDeps.isAccessible = true
        updateDeps.invoke(javaFXOptions)
    }
}