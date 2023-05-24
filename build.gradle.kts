plugins {
    java
    kotlin("jvm") version "1.8.21"
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
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
    testImplementation("org.assertj:assertj-core:3.24.2")
}

kotlin {
    jvmToolchain(17)
}


tasks.named<Test>("test") {
    useJUnitPlatform()
}
