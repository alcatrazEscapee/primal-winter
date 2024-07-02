plugins {
    id("net.neoforged.moddev") version "0.1.112"
}

val modId: String by extra
val modGroup: String by extra
val minecraftVersion: String by extra
val neoForgeVersion: String by extra
val parchmentVersion: String by extra
val parchmentMinecraftVersion: String by extra
val epsilonVersion: String by extra
val epsilonVersionRange: String by extra

base {
    archivesName.set("${modId}-neoforge-${minecraftVersion}")
}

dependencies {
    implementation(project(":Common"))
    jarJar(implementation(group = "com.alcatrazescapee", name = "epsilon", version = epsilonVersion)) {
        version {
            strictly(epsilonVersionRange)
            prefer(epsilonVersion)
        }
    }
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
            additionalRuntimeClasspath("com.alcatrazescapee:epsilon:$epsilonVersion")
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
