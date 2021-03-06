import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "me.markoutte"
version = "1.0"

plugins {
    java
    kotlin("jvm") version "1.4.32"
    id("org.jetbrains.compose") version "0.4.0-build183"
    id("org.springframework.boot") version "2.4.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    implementation("net.java.dev.jna:jna-platform:5.8.0")
    testImplementation("junit:junit:4.13")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.4.32")
    compileOnly("org.openjdk.jmh:jmh-generator-annprocess:1.29")
    compile("org.springframework.boot:spring-boot-starter-webflux")
}

java.sourceCompatibility = JavaVersion.VERSION_11

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}