
// From gradle.properties
val modName: String by extra
val modAuthor: String by extra
val modId: String by extra
val modGroup: String by extra
val modJavaVersion: String by extra
val modVersion: String = System.getenv("VERSION") ?: "0.0.0-indev"

val minecraftVersion: String by extra
val forgeVersion: String by extra
val parchmentVersion: String by extra
val fabricVersion: String by extra
val fabricLoaderVersion: String by extra
val epsilonVersion: String by extra


subprojects {

    version = modVersion
    group = modGroup

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(JavaLanguageVersion.of(modJavaVersion).asInt())
    }

    tasks.withType<Jar> {
        manifest {
            attributes(mapOf(
                "Implementation-Title" to modName,
                "Implementation-Version" to modVersion,
                "Implementation-Vendor" to modAuthor,
            ))
        }
    }

    // Apply properties from gradle.properties to mod specific files, meaning they don't need to be changed on version update.
    tasks.withType<ProcessResources> {
        inputs.property("version", version)

        filesMatching(listOf("META-INF/mods.toml", "pack.mcmeta", "fabric.mod.json")) {
            expand(mapOf(
                "fabricVersion" to fabricVersion,
                "fabricLoaderVersion" to fabricLoaderVersion,
            ))
        }
    }

    // Disables Gradle's custom module metadata from being published to maven. The metadata includes mapped dependencies which are not reasonably consumable by other mod developers.
    tasks.withType<GenerateModuleMetadata> {
        enabled = false
    }
}