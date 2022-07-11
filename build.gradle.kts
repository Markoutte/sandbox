import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "me.markoutte"
version = "1.0"

plugins {
    java
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.compose") version "1.0.1"
    id("org.springframework.boot") version "2.4.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

val kotlinCoroutineVersion = "1.5.2"

repositories {
    mavenLocal()
    google()
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:$kotlinCoroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:$kotlinCoroutineVersion")
    implementation("org.hibernate:hibernate-core:5.4.30.Final")
    implementation("com.h2database:h2:1.4.200")
    implementation("com.google.inject:guice:4.0")
    implementation("javax.enterprise:cdi-api:2.0")
    implementation("org.jboss.weld.se:weld-se-core:4.0.1.Final")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("org.hibernate:hibernate-validator:5.0.0.Final")
    implementation("javassist:javassist:3.12.1.GA")
    implementation("commons-cli:commons-cli:1.3.1")
    implementation("com.intellij:annotations:12.0")
    implementation("org.openjdk.jmh:jmh-core:1.29")
    implementation("net.java.dev.jna:jna-platform:5.8.0")
    testImplementation("junit:junit:4.13")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.4.32")
    compileOnly("org.openjdk.jmh:jmh-generator-annprocess:1.29")
    implementation("com.formdev:flatlaf:2.3")
}

java.sourceCompatibility = JavaVersion.VERSION_11

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}