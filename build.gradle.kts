import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.protobuf

plugins {
	java
	jacoco
	id("org.springframework.boot") version "3.5.10"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.sonarqube") version "7.2.2.6593"
	checkstyle
	id("com.google.protobuf") version "0.9.4"
}

group = "com.mysawit"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	runtimeOnly("io.micrometer:micrometer-registry-prometheus")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testRuntimeOnly("com.h2database:h2")

	implementation("org.springframework.boot:spring-boot-starter-security")
	testImplementation("org.springframework.security:spring-security-test")
	implementation("io.jsonwebtoken:jjwt-api:0.12.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")

	implementation("net.devh:grpc-server-spring-boot-starter:3.0.0.RELEASE")
	implementation("io.grpc:grpc-protobuf:1.62.2")
	implementation("io.grpc:grpc-stub:1.62.2")
	compileOnly("org.apache.tomcat:annotations-api:6.0.53")

	testImplementation("net.serenity-bdd:serenity-core:4.2.8")
	testImplementation("net.serenity-bdd:serenity-junit5:4.2.8")
	testImplementation("net.serenity-bdd:serenity-spring:4.2.8")
	testImplementation("net.serenity-bdd:serenity-rest-assured:4.2.8")
	testImplementation("io.rest-assured:rest-assured:5.5.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.named<Checkstyle>("checkstyleMain") {
	source = fileTree("src/main/java")
}

tasks.named<Checkstyle>("checkstyleTest") {
	source = fileTree("src/test/java")
}

tasks.register<Test>("unitTest") {
	description = "Runs the unit tests."
	group = "verification"

	filter {
		excludeTestsMatching("*FunctionalTest")
	}
}

tasks.register<Test>("functionalTest") {
	description = "Runs the functional tests."
	group = "verification"

	filter {
		includeTestsMatching("*FunctionalTest")
	}
}

tasks.test {
	filter {
		excludeTestsMatching("*FunctionalTest")
	}

	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required.set(true)
	}
	classDirectories.setFrom(files(classDirectories.files.map {
		fileTree(it) {
			exclude(
				"**/com/mysawit/mysawit_kebun/grpc/**",
				"**/com/mysawit/mysawit_kebun/controller/KebunSeedController.class"
			)
		}
	}))
}

sonar {
	properties {
		property("sonar.projectKey", "KEL-ADPRO_MySawit-backend-manage-lahan")

		property("sonar.organization", "kel-adpro")

		property("sonar.host.url", "https://sonarcloud.io")

		property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/test/jacocoTestReport.xml")

		property("sonar.exclusions", "**/com/mysawit/mysawit_kebun/grpc/**,**/grpc/**,build/generated/**,**/com/mysawit/mysawit_kebun/controller/KebunSeedController.java")
		property("sonar.coverage.exclusions", "**/com/mysawit/mysawit_kebun/grpc/**,**/grpc/**,build/generated/**,**/com/mysawit/mysawit_kebun/controller/KebunSeedController.java")
	}
}

protobuf {
	protoc {
		artifact = "com.google.protobuf:protoc:3.25.3"
	}
	plugins {
		id("grpc") {
			artifact = "io.grpc:protoc-gen-grpc-java:1.62.2"
		}
	}
	generateProtoTasks {
		all().forEach {
			it.plugins {
				id("grpc") { }
			}
		}
	}
}