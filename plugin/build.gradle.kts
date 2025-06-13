plugins {
    id("com.gradleup.shadow") version "8.3.5"
}

val projectName = rootProject.name
version = project.properties["projectVersion"].toString()

dependencies {
    implementation(project(":api"))

    // DEPENDENCIES

    implementation("net.kyori:adventure-platform-bukkit:4.4.0")
    implementation("net.kyori:adventure-text-minimessage:4.21.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.13.1")
}

tasks {
    assemble {
        dependsOn(shadowJar)
    }

    shadowJar {
        archiveFileName = "${projectName}-${version}.jar"

        relocate("me.gurwi.commandframework", "club.athlas.jobless.libs.commands")
        relocate("net.kyori", "club.athlas.jobless.libs.kyori")
        relocate("okhttp3", "club.athlas.jobless.libs.okhttp3")
        relocate("okio", "club.athlas.jobless.libs.okio")
        relocate("com.google.gson", "club.athlas.jobless.libs.gson")
    }
}