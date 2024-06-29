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
}

loom {
    mixin {
        defaultRefmapName.set("${modId}.refmap.json")
    }

    runs {
        named("client") {
            client()
            configName = "Fabric Client"
            ideConfigGenerated(true)
            runDir("run")
        }
        named("server") {
            server()
            configName = "Fabric Server"
            ideConfigGenerated(true)
            runDir("run")
        }
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
