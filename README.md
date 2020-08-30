![Primal Winter Splash Image](https://github.com/alcatrazEscapee/primal-winter/blob/1.15.x/img/splash.png?raw=true)

_A horrible accident has left the world - the **entire** world - a frozen wasteland. Unending snowstorms rage across the land, from forests, to plains, to jungles, to deserts. The air is thick with fog and wind howls across the landscape. Will you be able to survive in this primordial winter?_

### Availability

Primal Winter is available for the following Minecraft and mod loader versions:

- 1.16.1: Fabric only, Forge is blocked by [MinecraftForge#6994](https://github.com/MinecraftForge/MinecraftForge/pull/6994)
- 1.15.2: Forge

### Features

This mod will turn your entire world into a frozen wasteland. It adds several new blocks, and has many client side aesthetic tweaks to immerse yourself in the winter wonderland you now inhabit.

![Example of Winter Landscape](https://github.com/alcatrazEscapee/primal-winter/blob/1.15.x/img/savanna.png?raw=true)

- A thick white fog obscures your view when you are outside.
- The weather is always snowy and thundering, and there's more snow including extra particle effects and sounds.
- Most surface blocks are replaced with snowy variants. These can be mined to obtain their vanilla counterparts.
- Polar bears and strays now spawn everywhere.
- Ice spikes, icebergs, and packed ice pools appear sporadically in all biomes, snow and ice placement has been improved and now layers more intuitively and can creep into caves and under overhangs such as trees during world generation.


### Configuration


**Biomes**

This mod *heavily* modifies all the biomes in the game. Most mod's biomes should work fine with this. However, this means that biomes in the End or the Nether for example, need to be special cased to not turn into winter biomes. For this, there is a **Common** config option which excludes specific biomes:

```toml
#A list of biome IDs that will not be forcibly converted to frozen wastelands. Any changes requires a MC restart to take effect.
nonWinterBiomes = ["minecraft:nether", "minecraft:end_barrens", "minecraft:end_highlands", "minecraft:end_midlands", "minecraft:the_end", "minecraft:the_void"]
```

**Weather**

This mod also sets the entire world to an endless storm, by setting `/gamerule doWeatherCycle false`, and setting the weather to thunderstorm. It also disables the `/weather` command by default, but that can be re-enabled in the **Common** config option:
```toml
#Should the vanilla /weather be disabled? Any changes require a world restart to take effect.
disableWeatherCommand = true
```

**Client**

All the client side changes (fog density, fog color, snow particle density, fast snow particles, sky render tweaks, and wind and snow sounds) are configurable individually. See the specifics in the client config file for more details.

**Tags**

Primal winter adds two **Block** tags in order to do advanced entity spawning during world generation (Since vanilla entity spawning does not naturally occur on the snowy versions of blocks). These are:

- `primalwinter:animal_spawns_on`, which is used for all standard passive animal mobs. It includes grass, snowy dirt, sand, snowy sand and snow blocks.
- `primalwinter:turtle_spawns_on`, which is used for turtle spawning. It includes sand and snowy sand.

![Another Example of Winter Landscape](https://github.com/alcatrazEscapee/primal-winter/blob/1.15.x/img/jungle.png?raw=true)

### Credit

Thanks to EERussianGuy for making all the wood logs and leaf textures. Thanks to various people in the TerraFirmaCraft discord for their feedback and ideas.


Hope you enjoy, and stay warm!

~ AlcatrazEscapee
