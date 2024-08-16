import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.7.0-alpha07")
            implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.5.1")
            implementation("com.formdev:flatlaf:3.5.1")
            implementation("com.squareup.retrofit2:retrofit:2.11.0")
            implementation("com.squareup.retrofit2:converter-gson:2.11.0")
            implementation("com.squareup.okhttp3:okhttp:4.12.0")
            implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
            implementation("io.github.qdsfdhvh:image-loader:1.8.2")
            // optional - Compose Multiplatform Resources Decoder
            implementation("io.github.qdsfdhvh:image-loader-extension-compose-resources:1.8.2")
            // optional - Moko Resources Decoder
            implementation("io.github.qdsfdhvh:image-loader-extension-moko-resources:1.8.2")
            implementation("com.squareup.okio:okio:3.9.0")
            implementation("ognl:ognl:3.4.3")
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation("dev.mobile:dadb:1.2.7")
            implementation("com.google.code.gson:gson:2.11.0")
            implementation("io.github.qdsfdhvh:image-loader-extension-imageio:1.8.2")
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.doctorate.ui.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Exe, TargetFormat.Deb)
            packageName = "Doctorate UI"
            packageVersion = "1.0.0"
            windows{
                iconFile.set(file("data/icon.ico"))
            }
        }
    }
}

gradle.afterProject {
    file("adb").listFiles().forEach {
        it.copyTo(file("build/compose/binaries/main/app/Doctorate UI/adb/${it.name}"), true)
    }
}