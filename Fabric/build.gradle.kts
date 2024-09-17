plugins {
    id("fabric-loom") version "1.7.1"
    id("io.github.goooler.shadow") version "8.1.7"
}

val modId: String by extra
val modName: String by extra
val modGroup: String by extra
val minecraftVersion: String by extra
val parchmentVersion: String by extra
val parchmentMinecraftVersion: String by extra
val fabricVersion: String by extra
val fabricLoaderVersion: String by extra
val epsilonVersion: String by extra

val shadowLibrary: Configuration by configurations.creating

configurations {
    implementation.get().extendsFrom(shadowLibrary)
}

base {
    archivesName.set("${modId}-fabric-${minecraftVersion}")
}

dependencies {
    minecraft(group = "com.mojang", name = "minecraft", version = minecraftVersion)

    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${parchmentMinecraftVersion}:${parchmentVersion}@zip")
    })

    modImplementation(group = "net.fabricmc", name = "fabric-loader", version = fabricLoaderVersion)
    modImplementation(group = "net.fabricmc.fabric-api", name = "fabric-api", version = fabricVersion)

    implementation(group = "com.google.code.findbugs", name = "jsr305", version = "3.0.1")
    implementation(group = "org.jetbrains", name = "annotations", version = "23.0.0")

    compileOnly(project(":Common"))

    shadowLibrary(group = "com.alcatrazescapee", name = "epsilon", version = epsilonVersion)

    // Biome o Plenty
    // modRuntimeOnly(group = "curse.maven", name = "biomes-o-plenty-220318", version = "5726210")
    // modRuntimeOnly(group = "curse.maven", name = "glitchcore-955399", version = "5660741")
    // modRuntimeOnly(group = "curse.maven", name = "terrablender-fabric-565956", version = "5685547")
    // runtimeOnly(group = "com.electronwill.night-config", name = "toml", version = "3.6.7")
    // runtimeOnly(group = "com.electronwill.night-config", name = "core", version = "3.6.7")
    // runtimeOnly(group = "net.jodah", name = "typetools", version = "0.6.3")
}

loom {
    mixin {
        defaultRefmapName.set("${modId}.refmap.json")
    }

    runs {
        configureEach {
            ideConfigGenerated(true)
            runDir("run")
            vmArgs("-XX:+AllowEnhancedClassRedefinition")
        }
        named("client") { client() }
        named("server") { server() }
    }
}

tasks {
    named<JavaCompile>("compileJava") { source(project(":Common").sourceSets.main.get().allSource) }
    named<ProcessResources>("processResources") { from(project(":Common").sourceSets.main.get().resources) }

    jar {
        archiveClassifier.set("slim")
    }
    shadowJar {
        from(sourceSets.main.get().output)
        configurations = listOf(shadowLibrary)
        dependencies {
            exclude(dependency(KotlinClosure1<ResolvedDependency, Boolean>({ moduleGroup != modGroup })))
        }
        relocate("com.alcatrazescapee.epsilon", "${modGroup}.${modId}.epsilon")
    }

    remapJar {
        dependsOn(shadowJar)
        inputFile.set(shadowJar.get().archiveFile)
    }
}
