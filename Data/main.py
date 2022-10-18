from mcresources import ResourceManager, utils
from mcresources.type_definitions import JsonObject


def main():
    mod_id = 'primalwinter'
    root_dir = '../%s/src/main/resources'

    fabric = ResourceManager(mod_id, root_dir % 'Fabric')
    forge = ResourceManager(mod_id, root_dir % 'Forge')
    common = ResourceManager(mod_id, root_dir % 'Common')
    each = (fabric, forge, common)

    # Link tags and lang, generate them to common
    fabric.lang_buffer = forge.lang_buffer = common.lang_buffer
    fabric.tags_buffer = forge.tags_buffer = common.tags_buffer

    for rm in each:
        utils.clean_generated_resources('/'.join(rm.resource_dir))

    common.lang({
        'itemGroup.primalwinter.items': 'Primal Winter',
        'primalwinter.subtitle.wind': 'Wind Blows'
    })

    for block in ('dirt', 'coarse_dirt', 'sand', 'red_sand', 'gravel', 'stone', 'granite', 'diorite', 'andesite', 'white_terracotta', 'orange_terracotta', 'terracotta', 'yellow_terracotta', 'brown_terracotta', 'red_terracotta', 'light_gray_terracotta'):
        b = common.blockstate('snowy_' + block)
        b.with_item_model()
        b.with_block_model(textures={
            'side': 'primalwinter:block/snowy_' + block,
            'bottom': 'minecraft:block/' + block,
            'top': 'minecraft:block/snow'
        }, parent='block/cube_bottom_top')
        b.with_block_loot({
            'entries': {
                'type': 'loot_table',
                'name': 'minecraft:blocks/%s' % block
            }
        })
        b.with_lang(lang('snowy ' + block))

    for block in ('dirt', 'coarse_dirt', 'sand', 'red_sand', 'gravel'):
        common.block_tag('minecraft:mineable/shovel', 'primalwinter:snowy_%s' % block)

    for block in ('stone', 'granite', 'diorite', 'andesite', 'white_terracotta', 'orange_terracotta', 'terracotta', 'yellow_terracotta', 'brown_terracotta', 'red_terracotta', 'light_gray_terracotta'):
        common.block_tag('minecraft:mineable/pickaxe', 'primalwinter:snowy_%s' % block)

    b = common.blockstate('snowy_dirt_path', variants={
        '': [{'model': 'primalwinter:block/snowy_dirt_path', 'y': y} for y in (None, 90, 180, 270)]
    }, use_default_model=False)
    b.with_item_model()
    b.with_block_model(textures={
        'top': 'minecraft:block/snow',
        'side': 'primalwinter:block/snowy_dirt_path_side'
    }, parent='block/dirt_path')
    b.with_block_loot({
        'entries': {
            'type': 'loot_table',
            'name': 'minecraft:blocks/dirt_path'
        }
    })
    b.with_lang(lang('snowy dirt path'))
    b.with_tag('minecraft:mineable/shovel')

    faces = (('up', {'x': 270}), ('north', {}), ('east', {'y': 90}), ('west', {'y': 270}), ('south', {'y': 180}))
    b = common.blockstate_multipart(
        'snowy_vine',
        *(({face: 'true'}, {'model': 'primalwinter:block/snowy_vine', **rot, 'uvlock': True}) for face, rot in faces),
        *(({face: 'false' for face, _ in faces}, {'model': 'primalwinter:block/snowy_vine', **rot, 'uvlock': True}) for _, rot in faces)
    )
    b.with_block_loot({
        'entries': {
            'type': 'loot_table',
            'name': 'minecraft:blocks/vine'
        }
    })
    b.with_lang(lang('snowy vine'))
    b.with_block_model({
        'overlay': 'primalwinter:block/snowy_leaves_overlay'
    }, parent='block/vine', elements=[vine_element('#vine', 0), vine_element('#overlay', None)])
    b.with_tag('minecraft:mineable/axe')
    b.with_tag('minecraft:climbable')
    common.item_model('snowy_vine', 'block/vine', 'primalwinter:block/snowy_leaves_overlay')

    for wood in ('oak', 'dark_oak', 'acacia', 'jungle', 'birch', 'spruce'):
        b = common.blockstate('snowy_%s_log' % wood, variants={
            'axis=y': {'model': 'primalwinter:block/snowy_%s_log' % wood},
            'axis=z': {'model': 'primalwinter:block/snowy_%s_log' % wood, 'x': 90},
            'axis=x': {'model': 'primalwinter:block/snowy_%s_log' % wood, 'x': 90, 'y': 90}
        })
        b.with_item_model()
        b.with_block_model(textures={
            'side': 'primalwinter:block/snowy_%s_log' % wood,
            'end': 'primalwinter:block/snowy_%s_log_top' % wood
        }, parent='block/cube_column')
        b.with_block_loot({
            'entries': {
                'type': 'loot_table',
                'name': 'minecraft:blocks/%s_log' % wood
            }
        })
        b.with_lang(lang('snowy %s log', wood))
        b.with_tag('minecraft:mineable/axe')
        b.with_tag('minecraft:%s_logs' % wood)
        b.with_tag('minecraft:logs')

        b = common.blockstate('snowy_%s_leaves' % wood)
        b.with_block_model(textures={
            'all': 'block/%s_leaves' % wood,
            'overlay': 'primalwinter:block/snowy_leaves_overlay'
        }, parent='primalwinter:block/snowy_leaves')
        b.with_item_model()
        b.with_block_loot({
            'entries': {
                'type': 'loot_table',
                'name': 'minecraft:blocks/%s_leaves' % wood
            }
        })
        b.with_lang(lang('snowy %s leaves', wood))
        b.with_tag('minecraft:mineable/hoe')
        b.with_tag('minecraft:%s_leaves' % wood)
        b.with_tag('minecraft:leaves')

    # Template leaves model
    common.block_model('snowy_leaves', textures={
        'particle': '#all',
    }, elements=[{
        'from': [0, 0, 0],
        'to': [16, 16, 16],
        'faces': dict((face, {'uv': [0, 0, 16, 16], 'texture': '#all' if tint == 0 else '#overlay', 'tintindex': tint, 'cullface': face}) for face in ('down', 'up', 'north', 'south', 'east', 'west'))
    } for tint in (0, None)])

    # Snowy version of particles
    common.data(('particles', 'snow'), {
        'textures': ['primalwinter:snow_%d' % i for i in range(4)]
    }, 'assets')

    common.block_tag('minecraft:animals_spawnable_on', 'minecraft:snow_block', 'minecraft:snow', 'primalwinter:snowy_dirt', 'primalwinter:snowy_sand')

    # Only flush common
    common.flush()


def vine_element(texture: str, tint: int | None) -> JsonObject:
    return {
        'from': [0, 0, 0.8],
        'to': [16, 16, 0.8],
        'shade': False,
        'faces': {
            'north': {'uv': [16, 0, 0, 16], 'texture': texture, 'tintindex': tint},
            'south': {'uv': [0, 0, 16, 16], 'texture': texture, 'tintindex': tint}
        }
    }

def lang(key: str, *args) -> str:
    return ((key % args) if len(args) > 0 else key).replace('_', ' ').replace('/', ' ').title()


if __name__ == '__main__':
    main()
