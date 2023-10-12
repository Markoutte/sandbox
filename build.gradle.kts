import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "me.markoutte"
version = "1.0"

plugins {
    java
    kotlin("jvm") version "1.8.10"
    id("org.jetbrains.compose") version "1.3.1"
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

val kotlinCoroutineVersion = "1.6.4"
val junitVersion = "5.9.2"
val guavaVersion = "32.1.1-jre"
val fastjsonVersion = "2.0.40"

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
    implementation("org.jetbrains:annotations:24.0.1")
    implementation("com.google.inject:guice:5.1.0")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("commons-cli:commons-cli:1.5.0")
    implementation("net.java.dev.jna:jna-platform:5.13.0")
    implementation("org.openjdk.jmh:jmh-core:1.36")
    implementation("org.openjdk.jmh:jmh-generator-annprocess:1.36")
    implementation("com.formdev:flatlaf:3.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    implementation("com.google.guava:guava:$guavaVersion")
    implementation("com.alibaba.fastjson2:fastjson2:$fastjsonVersion")
}

java.sourceCompatibility = JavaVersion.VERSION_19

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "19"
}

tasks.test {
    useJUnitPlatform()
}
