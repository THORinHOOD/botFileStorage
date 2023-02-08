import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.30"
    kotlin("plugin.spring") version "1.5.30"
}

group = "com.thorinhood"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    jcenter()
    mavenCentral()
    maven {
        url = uri("https://plugins-gradle.org/m2/")
    }
}

configurations.all {
    exclude("org.springframework.boot", "spring-boot-starter-logging")
}

dependencies {
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("com.lmax:disruptor:3.4.2")
    implementation("org.apache.logging.log4j:log4j-api-kotlin:1.0.0")
    implementation("com.github.pengrad:java-telegram-bot-api:5.2.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
//    implementation("org.springframework.data:spring-data-mongodb:3.0.3.RELEASE")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}