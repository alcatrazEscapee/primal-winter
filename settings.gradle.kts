pluginManagement {
    repositories {
        maven(url = "https://maven.minecraftforge.net")
        maven(url = "https://maven.parchmentmc.org")
        maven(url = "https://maven.fabricmc.net/")
        maven(url = "https://repo.spongepowered.org/repository/maven-public/")

        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "org.spongepowered.mixin") {
                useModule("org.spongepowered:mixingradle:${requested.version}")
            }
        }
    }
}

rootProject.name = "PrimalWinter-1.19"
include("Common", "Fabric", "Forge")