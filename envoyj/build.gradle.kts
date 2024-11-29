plugins {
    id("buildlogic.kotlin-application-conventions")
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.boot:spring-boot")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("io.envoyproxy.controlplane:api:1.0.46")
    implementation("io.envoyproxy.controlplane:server:1.0.46")
    implementation("io.grpc:grpc-netty:1.68.0")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.4")
    implementation("jakarta.servlet:jakarta.servlet-api")
    implementation("com.github.js-cookie:java-cookie:0.0.2")
}


tasks.withType<Test> {
    useJUnitPlatform()
}
