@file:Suppress("HardCodedStringLiteral")

val releasePlatform: String? by project

plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.beryx.jlink") version "2.26.0"
    id("net.nemerosa.versioning") version "2.15.1"
}

repositories {
    mavenCentral()
}

javafx {
    version = "17"
    modules("javafx.swing", "javafx.fxml", "javafx.controls")

    if (releasePlatform != null && releasePlatform != "local") {
        setPlatform(releasePlatform.takeUnless { releasePlatform?.substringAfter("-") == "x64" }
            ?: releasePlatform?.substringBefore("-"))
    }
}

dependencies {
    implementation("org.controlsfx:controlsfx:11.2.1")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.2")
    implementation("org.glassfish.jaxb:jaxb-runtime:4.0.5")
}

tasks.compileJava {
    options.encoding = "UTF-8"
    options.release.set(11)
}

application {
    mainModule.set("de.baw.lomo")
    mainClass.set("de.baw.lomo.gui.App")
}

tasks.jar {
    manifest {
        attributes(
            "LOMO_VERSION" to "${project.version}",
            "SOURCE" to versioning.info.build + (if (versioning.info.dirty) "-dirty" else "")
        )
    }
}

tasks.register<Copy>("jarRelease") {
    from(tasks.jar)
    from(configurations.runtimeClasspath)
    into(layout.buildDirectory.dir("jars"))
}

jlink {

    launcher {
        noConsole = true
    }
    addOptions("--strip-debug", "--compress", "1", "--no-header-files", "--no-man-pages")
    addOptions("--release-info",
        "add:LOMO_VERSION=${project.version}:OS_CLASSIFIER=${releasePlatform}"
                + ":SOURCE=${versioning.info.build}" + (if (versioning.info.dirty) "-dirty" else ""))

    mergedModule {
        additive = true
        // Needed to consume API with JPype
        requires("jdk.zipfs")
    }

    imageZip.set(layout.buildDirectory.file("${project.name}.zip"))

    targetPlatform("${project.version}-" + (releasePlatform ?: "local")) {

        if (releasePlatform != null && releasePlatform != "local") {
            val jdkUrl = when (releasePlatform) {
                "win-x64" -> "https://api.adoptium.net/v3/binary/latest/17/ga/windows/x64/jdk/hotspot/normal/eclipse?project=jdk"
                "linux-x64" -> "https://api.adoptium.net/v3/binary/latest/17/ga/linux/x64/jdk/hotspot/normal/eclipse?project=jdk"
                "linux-aarch64" -> "https://api.adoptium.net/v3/binary/latest/17/ga/linux/aarch64/jdk/hotspot/normal/eclipse?project=jdk"
                "mac-x64" -> "https://api.adoptium.net/v3/binary/latest/17/ga/mac/x64/jdk/hotspot/normal/eclipse?project=jdk"
                "mac-aarch64" -> "https://api.adoptium.net/v3/binary/latest/17/ga/mac/aarch64/jdk/hotspot/normal/eclipse?project=jdk"
                else -> throw InvalidUserDataException("Invalid release platform: $releasePlatform")
            }

            @Suppress("INACCESSIBLE_TYPE")
            setJdkHome(jdkDownload(jdkUrl,
                closureOf<org.beryx.jlink.util.JdkUtil.JdkDownloadOptions> {
                    downloadDir = layout.buildDirectory.dir("jdks/$releasePlatform").get().toString()
                    archiveExtension = if (releasePlatform == "win-x64") "zip" else "tar.gz"
                }))
        }
    }
}

tasks.named<org.beryx.jlink.JlinkTask>("jlink") {
    doLast {
        copy {
            from(layout.projectDirectory)
            include("*.md")
            into("$imageDir/${project.name}-${project.version}-"
                + (releasePlatform ?: "local"))
        }
        copy {
            from(layout.projectDirectory)
            include("*.txt")
            into("$imageDir/${project.name}-${project.version}-"
                + (releasePlatform ?: "local") + ("/legal/de.baw.lomo/"))
        }
    }
}