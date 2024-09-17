plugins {
    id("net.neoforged.moddev") version "0.1.112"
}

val modId: String by extra
val modGroup: String by extra
val minecraftVersion: String by extra
val neoForgeVersion: String by extra
val parchmentVersion: String by extra
val parchmentMinecraftVersion: String by extra

base {
    archivesName.set("${modId}-neoforge-${minecraftVersion}")
}

dependencies {
    implementation(project(":Common"))

    // Biome o Plenty
    //runtimeOnly(group = "curse.maven", name = "biomes-o-plenty-220318", version = "5726211")
    //runtimeOnly(group = "curse.maven", name = "glitchcore-955399", version = "5660740")
    //runtimeOnly(group = "curse.maven", name = "terrablender-neoforge-940057", version = "5685546")
}

neoForge {
    version.set(neoForgeVersion)

    addModdingDependenciesTo(sourceSets.test.get())

    parchment {
        minecraftVersion.set(parchmentMinecraftVersion)
        mappingsVersion.set(parchmentVersion)
    }

    runs {
        configureEach {
            jvmArgument("-XX:+AllowEnhancedClassRedefinition")
        }
        register("client") { client() }
        register("server") { server() }
        register("data") {
            data()
            sourceSet = sourceSets.test
            programArguments.addAll("--all", "--mod", modId, "--output", file("../Common/src/generated/resources").absolutePath, "--existing",  file("../Common/src/main/resources").absolutePath)
        }
    }

    mods {
        create(modId) {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets.test.get())
        }
    }
}

tasks {
    named<JavaCompile>("compileJava") { source(project(":Common").sourceSets.main.get().allSource) }
    named<ProcessResources>("processResources") { from(project(":Common").sourceSets.main.get().resources) }
}
