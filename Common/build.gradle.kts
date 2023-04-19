plugins {
    java
    id("org.spongepowered.gradle.vanilla") version "0.2.1-SNAPSHOT"
}

// From gradle.properties
val minecraftVersion: String by extra
val epsilonVersion: String by extra

repositories {
    maven(url = "https://alcatrazescapee.jfrog.io/artifactory/mods")
}

minecraft {
    version(minecraftVersion)
}

dependencies {
    compileOnly(group = "org.spongepowered", name = "mixin", version = "0.8.5")
    compileOnly(group = "com.alcatrazescapee", name = "epsilon", version = epsilonVersion)
}
