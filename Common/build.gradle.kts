plugins {
    id("net.neoforged.moddev") version "0.1.112"
}

val parchmentMinecraftVersion: String by extra
val parchmentVersion: String by extra
val commonNeoFormVersion: String by extra

dependencies {
    compileOnly(group = "org.spongepowered", name = "mixin", version = "0.8.5")
    compileOnly(group = "io.github.llamalad7", name = "mixinextras-common", version = "0.3.5")
    annotationProcessor(group = "io.github.llamalad7", name = "mixinextras-common", version = "0.3.5")
}

neoForge {
    neoFormVersion.set(commonNeoFormVersion)

    parchment {
        minecraftVersion.set(parchmentMinecraftVersion)
        mappingsVersion.set(parchmentVersion)
    }
}

sourceSets.main {
    resources { srcDir("src/generated/resources/") }
}
