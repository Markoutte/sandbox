plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.hibernate:hibernate-core:5.4.30.Final")
    implementation("com.h2database:h2:1.4.200")
    implementation("com.google.inject:guice:4.0")
    implementation("org.jboss.weld.se:weld-se:2.0.2.Final")
    implementation("org.hibernate:hibernate-validator:5.0.0.Final")
    implementation("javassist:javassist:3.12.1.GA")
    implementation("commons-cli:commons-cli:1.3.1")
    implementation("com.intellij:annotations:12.0")
    implementation("org.openjdk.jmh:jmh-core:1.29")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.31")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    testImplementation("junit:junit:4.13")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.4.31")
    compileOnly("org.openjdk.jmh:jmh-generator-annprocess:1.29")
}

java.sourceCompatibility = JavaVersion.VERSION_11

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
