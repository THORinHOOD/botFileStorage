import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.5.21"
	kotlin("plugin.spring") version "1.5.21"
}

group = "com.benchinc"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	maven {
		url = uri("https://raw.github.com/THORinHOOD/benchLib/mvn-repo/")
	}
}

configurations.all {
	exclude("org.springframework.boot", "spring-boot-starter-logging")
}

dependencies {
	implementation("com.db:benchLib:0.0.10")
	implementation("io.micrometer:micrometer-registry-prometheus")
	implementation("com.lmax:disruptor:3.4.2")
	implementation("org.apache.logging.log4j:log4j-api-kotlin:1.0.0")
	implementation("org.springframework.boot:spring-boot-starter-log4j2")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("com.github.pengrad:java-telegram-bot-api:5.2.0")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
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
