plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("ch.qos.logback:logback-classic:1.4.11") // Logback implementation
    implementation("org.slf4j:slf4j-api:2.0.9")            // SLF4J API (optional, already bundled)
    implementation("com.rabbitmq:amqp-client:5.16.0")
}

tasks.test {
    useJUnitPlatform()
}
