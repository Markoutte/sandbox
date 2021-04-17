import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "me.markoutte"
version = "1.0"

plugins {
    java
    kotlin("jvm") version "1.4.32"
    id("org.jetbrains.compose") version "0.4.0-build183"
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.hibernate:hibernate-core:5.4.30.Final")
    implementation("com.h2database:h2:1.4.200")
    implementation("com.google.inject:guice:4.0")
    implementation("org.jboss.weld.se:weld-se:2.0.2.Final")
    implementation("org.hibernate:hibernate-validator:5.0.0.Final")
    implementation("javassist:javassist:3.12.1.GA")
    implementation("commons-cli:commons-cli:1.3.1")
    implementation("com.intellij:annotations:12.0")
    implementation("org.openjdk.jmh:jmh-core:1.29")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    testImplementation("junit:junit:4.13")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.4.32")
    compileOnly("org.openjdk.jmh:jmh-generator-annprocess:1.29")
}

java.sourceCompatibility = JavaVersion.VERSION_11

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

compose.desktop {
    application {
        mainClass = "me.markoutte.sandbox.swing.bunnies.BunnyMarkKt"
    }
}