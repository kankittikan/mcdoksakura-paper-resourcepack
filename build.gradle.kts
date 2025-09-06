plugins {
    `java`
}

group = "com.example"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    // Use adventure text for prompts if needed
    compileOnly("net.kyori:adventure-text-minimessage:4.16.0")
}

tasks.processResources {
    filteringCharset = "UTF-8"
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}


