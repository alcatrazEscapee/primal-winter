from mcresources import ResourceManager, clean_generated_resources


def main():
    resource_path = '../src/main/resources'
    clean_generated_resources(resource_path)
    rm = ResourceManager('primalwinter', resource_path)

    rm.lang({
        'itemGroup.primalwinter.blocks': 'Primal Winter',
        'primalwinter.subtitle.wind': 'Wind Blows'
    })

    for block in ('dirt', 'coarse_dirt', 'sand', 'red_sand', 'gravel', 'stone', 'granite', 'diorite', 'andesite', 'white_terracotta', 'orange_terracotta', 'terracotta', 'yellow_terracotta', 'brown_terracotta', 'red_terracotta', 'light_gray_terracotta'):
        rm.blockstate('snowy_' + block) \
            .with_item_model() \
            .with_block_model(textures={
            'side': 'primalwinter:block/snowy_' + block,
            'bottom': 'minecraft:block/' + block,
            'top': 'minecraft:block/snow'
        }, parent='block/cube_bottom_top') \
            .with_block_loot({
            'entries': {
                'type': 'loot_table',
                'name': 'minecraft:blocks/%s' % block
            }
        }) \
            .with_lang(lang('snowy ' + block))

    rm.blockstate('snowy_grass_path', variants={
        '': [{'model': 'primalwinter:block/snowy_grass_path', 'y': y} for y in (None, 90, 180, 270)]
    }, use_default_model=False) \
        .with_item_model() \
        .with_block_model(textures={
        'top': 'minecraft:block/snow',
        'side': 'primalwinter:block/snowy_grass_path_side'
    }, parent='block/grass_path') \
        .with_block_loot({
        'entries': {
            'type': 'loot_table',
            'name': 'minecraft:blocks/grass_path'
        }
    }) \
        .with_lang(lang('snowy grass path'))

    rm.blockstate('snowy_vine', variants=VINE_VARIANTS) \
        .with_block_loot({
        'entries': {
            'type': 'loot_table',
            'name': 'minecraft:blocks/vine'
        }
    }) \
        .with_lang(lang('snowy vine'))
    rm.item_model('snowy_vine', 'block/vine', 'primalwinter:block/snowy_leaves_overlay')

    for suffix in ('_1', '_2', '_3', '_4', '_1u', '_2u', '_3u', '_4u', '_2_opposite', '_2u_opposite', '_u'):
        elements = []
        for suffixes, element_factory in VINE_ELEMENTS.items():
            if suffix in suffixes:
                elements.append(element_factory('#vine', 0))
                elements.append(element_factory('#overlay', 1))
        rm.block('snowy_vine' + suffix).with_block_model({
            'overlay': 'primalwinter:block/snowy_leaves_overlay'
        }, parent='block/vine' + suffix, elements=elements)

    for wood in ('oak', 'dark_oak', 'acacia', 'jungle', 'birch', 'spruce'):
        rm.blockstate('snowy_%s_log' % wood, variants={
            'axis=y': {'model': 'primalwinter:block/snowy_%s_log' % wood},
            'axis=z': {'model': 'primalwinter:block/snowy_%s_log' % wood, 'x': 90},
            'axis=x': {'model': 'primalwinter:block/snowy_%s_log' % wood, 'x': 90, 'y': 90}
        }) \
            .with_item_model() \
            .with_block_model(textures={
            'side': 'primalwinter:block/snowy_%s_log' % wood,
            'end': 'primalwinter:block/snowy_%s_log_top' % wood
        }, parent='block/cube_column') \
            .with_block_loot({
            'entries': {
                'type': 'loot_table',
                'name': 'minecraft:blocks/%s_log' % wood
            }
        }) \
            .with_lang(lang('snowy %s log', wood)) \
            .with_tag('minecraft:%s_logs' % wood) \
            .with_tag('minecraft:logs')
        rm.blockstate('snowy_%s_leaves' % wood) \
            .with_block_model(textures={
            'all': 'block/%s_leaves' % wood,
            'overlay': 'primalwinter:block/snowy_leaves_overlay'
        }, parent='primalwinter:block/snowy_leaves') \
            .with_item_model() \
            .with_block_loot({
            'entries': {
                'type': 'loot_table',
                'name': 'minecraft:blocks/%s_leaves' % wood
            }
        }) \
            .with_lang(lang('snowy %s leaves', wood)) \
            .with_tag('minecraft:%s_leaves' % wood) \
            .with_tag('minecraft:leaves')

    # Template leaves model
    rm.block_model('snowy_leaves', textures={
        'particle': '#all',
    }, elements=[{
        'from': [0, 0, 0],
        'to': [16, 16, 16],
        'faces': dict((face, {'uv': [0, 0, 16, 16], 'texture': '#all' if tint == 0 else '#overlay', 'tintindex': tint, 'cullface': face}) for face in ('down', 'up', 'north', 'south', 'east', 'west'))
    } for tint in (0, None)])

    # Snowy version of particles
    rm.data(('particles', 'snow'), {
        'textures': ['primalwinter:snow_%d' % i for i in range(4)]
    }, 'assets')

    rm.block_tag('animal_spawns_on', 'minecraft:grass', 'minecraft:sand', 'minecraft:snow_block', 'minecraft:snow', 'primalwinter:snowy_dirt', 'primalwinter:snowy_sand')
    rm.block_tag('minecraft:sand', 'primalwinter:snowy_sand')

    rm.flush()


def lang(key: str, *args) -> str:
    return ((key % args) if len(args) > 0 else key).replace('_', ' ').replace('/', ' ').title()


VINE_VARIANTS = {
    'east=false,north=false,south=false,up=false,west=false': {'model': 'primalwinter:block/snowy_vine_1'},
    'east=false,north=false,south=true,up=false,west=false': {'model': 'primalwinter:block/snowy_vine_1'},
    'east=false,north=false,south=false,up=false,west=true': {'model': 'primalwinter:block/snowy_vine_1', 'y': 90},
    'east=false,north=true,south=false,up=false,west=false': {'model': 'primalwinter:block/snowy_vine_1', 'y': 180},
    'east=true,north=false,south=false,up=false,west=false': {'model': 'primalwinter:block/snowy_vine_1', 'y': 270},
    'east=true,north=true,south=false,up=false,west=false': {'model': 'primalwinter:block/snowy_vine_2'},
    'east=true,north=false,south=true,up=false,west=false': {'model': 'primalwinter:block/snowy_vine_2', 'y': 90},
    'east=false,north=false,south=true,up=false,west=true': {'model': 'primalwinter:block/snowy_vine_2', 'y': 180},
    'east=false,north=true,south=false,up=false,west=true': {'model': 'primalwinter:block/snowy_vine_2', 'y': 270},
    'east=true,north=false,south=false,up=false,west=true': {'model': 'primalwinter:block/snowy_vine_2_opposite'},
    'east=false,north=true,south=true,up=false,west=false': {'model': 'primalwinter:block/snowy_vine_2_opposite', 'y': 90},
    'east=true,north=true,south=true,up=false,west=false': {'model': 'primalwinter:block/snowy_vine_3'},
    'east=true,north=false,south=true,up=false,west=true': {'model': 'primalwinter:block/snowy_vine_3', 'y': 90},
    'east=false,north=true,south=true,up=false,west=true': {'model': 'primalwinter:block/snowy_vine_3', 'y': 180},
    'east=true,north=true,south=false,up=false,west=true': {'model': 'primalwinter:block/snowy_vine_3', 'y': 270},
    'east=true,north=true,south=true,up=false,west=true': {'model': 'primalwinter:block/snowy_vine_4'},
    'east=false,north=false,south=false,up=true,west=false': {'model': 'primalwinter:block/snowy_vine_u'},
    'east=false,north=false,south=true,up=true,west=false': {'model': 'primalwinter:block/snowy_vine_1u'},
    'east=false,north=false,south=false,up=true,west=true': {'model': 'primalwinter:block/snowy_vine_1u', 'y': 90},
    'east=false,north=true,south=false,up=true,west=false': {'model': 'primalwinter:block/snowy_vine_1u', 'y': 180},
    'east=true,north=false,south=false,up=true,west=false': {'model': 'primalwinter:block/snowy_vine_1u', 'y': 270},
    'east=true,north=true,south=false,up=true,west=false': {'model': 'primalwinter:block/snowy_vine_2u'},
    'east=true,north=false,south=true,up=true,west=false': {'model': 'primalwinter:block/snowy_vine_2u', 'y': 90},
    'east=false,north=false,south=true,up=true,west=true': {'model': 'primalwinter:block/snowy_vine_2u', 'y': 180},
    'east=false,north=true,south=false,up=true,west=true': {'model': 'primalwinter:block/snowy_vine_2u', 'y': 270},
    'east=true,north=false,south=false,up=true,west=true': {'model': 'primalwinter:block/snowy_vine_2u_opposite'},
    'east=false,north=true,south=true,up=true,west=false': {'model': 'primalwinter:block/snowy_vine_2u_opposite', 'y': 90},
    'east=true,north=true,south=true,up=true,west=false': {'model': 'primalwinter:block/snowy_vine_3u'},
    'east=true,north=false,south=true,up=true,west=true': {'model': 'primalwinter:block/snowy_vine_3u', 'y': 90},
    'east=false,north=true,south=true,up=true,west=true': {'model': 'primalwinter:block/snowy_vine_3u', 'y': 180},
    'east=true,north=true,south=false,up=true,west=true': {'model': 'primalwinter:block/snowy_vine_3u', 'y': 270},
    'east=true,north=true,south=true,up=true,west=true': {'model': 'primalwinter:block/snowy_vine_4u'}
}

VINE_ELEMENTS = {
    ('_1', '_3', '_4', '_1u', '_3u', '_4u'): lambda texture, tint: {
        'from': [0, 0, 15.2],
        'to': [16, 16, 15.2],
        'shade': False,
        'faces': {
            'north': {'uv': [0, 0, 16, 16], 'texture': texture, 'tintindex': tint},
            'south': {'uv': [0, 0, 16, 16], 'texture': texture, 'tintindex': tint}
        }
    },
    ('_2', '_3', '_4', '_2u', '_3u', '_4u'): lambda texture, tint: {
        'from': [0, 0, 0.8],
        'to': [16, 16, 0.8],
        'shade': False,
        'faces': {
            'north': {'uv': [0, 0, 16, 16], 'texture': texture, 'tintindex': tint},
            'south': {'uv': [0, 0, 16, 16], 'texture': texture, 'tintindex': tint}
        }
    },
    ('_2', '_3', '_4', '_2u', '_3u', '_4u', '_2_opposite', '_2u_opposite'): lambda texture, tint: {
        'from': [15.2, 0, 0],
        'to': [15.2, 16, 16],
        'shade': False,
        'faces': {
            'west': {'uv': [0, 0, 16, 16], 'texture': texture, 'tintindex': tint},
            'east': {'uv': [0, 0, 16, 16], 'texture': texture, 'tintindex': tint}
        }
    },
    ('_4', '_4u', '_2_opposite', '_2u_opposite'): lambda texture, tint: {
        'from': [0.8, 0, 0],
        'to': [0.8, 16, 16],
        'shade': False,
        'faces': {
            'west': {'uv': [0, 0, 16, 16], 'texture': texture, 'tintindex': tint},
            'east': {'uv': [0, 0, 16, 16], 'texture': texture, 'tintindex': tint}
        }
    },
    ('_1u', '_2u', '_3u', '_4u', '_2u_opposite', '_u'): lambda texture, tint: {
        'from': [0, 15.2, 0],
        'to': [16, 15.2, 16],
        'shade': False,
        'faces': {
            'down': {'uv': [0, 0, 16, 16], 'texture': texture, 'tintindex': tint},
            'up': {'uv': [0, 0, 16, 16], 'texture': texture, 'tintindex': tint}
        }
    }
}

if __name__ == '__main__':
    main()
