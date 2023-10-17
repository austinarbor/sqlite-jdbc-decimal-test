plugins {
    java
    kotlin("jvm") version "1.9.10"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(
        fileTree("libs") {
            include("*.jar")
        }
    )
    testImplementation("org.slf4j:slf4j-api:2.0.9")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
    testImplementation("org.assertj:assertj-core:3.24.2")
}

kotlin {
    jvmToolchain(17)
}


tasks.named<Test>("test") {
    useJUnitPlatform()
}
