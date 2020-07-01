
from mcresources import ResourceManager, clean_generated_resources


def main():
    resource_path = '../src/main/resources'
    clean_generated_resources(resource_path)
    rm = ResourceManager('primalwinter', resource_path)

    rm.lang({
        'primalwinter.subtitle.wind': 'Wind Blows'
    })

    for block in ('dirt', 'coarse_dirt', 'sand', 'red_sand', 'gravel', 'stone', 'granite', 'diorite', 'andesite'):
        rm.blockstate('snowy_' + block) \
            .with_item_model() \
            .with_block_model(textures={
                'side': 'primalwinter:block/snowy_' + block,
                'bottom': 'minecraft:block/' + block,
                'top': 'minecraft:block/snow'
            }, parent='block/cube_bottom_top') \
            .with_block_loot('minecraft:' + block) \
            .with_lang(lang('snowy ' + block))

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
            .with_block_loot('minecraft:%s_log' % wood) \
            .with_lang(lang('snowy %s log', wood)) \
            .with_tag('minecraft:%s_logs' % wood) \
            .with_tag('minecraft:logs')
        rm.blockstate('snowy_%s_leaves' % wood) \
            .with_block_model(textures={
                'all': 'block/%s_leaves' % wood,
                'overlay': 'primalwinter:block/snowy_leaves_overlay'
            }, parent='primalwinter:block/snowy_leaves') \
            .with_item_model() \
            .with_block_loot({'entries': {
                'type': 'loot_table',
                'name': 'minecraft:blocks/%s_leaves' % wood
            }}) \
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
        } for tint in (0, None)
    ])

    # Snowy version of particles
    rm.data(('particles', 'snow'), {
        'textures': ['primalwinter:snow_%d' % i for i in range(4)]
    }, 'assets')

    rm.flush()


def lang(key: str, *args) -> str:
    return ((key % args) if len(args) > 0 else key).replace('_', ' ').replace('/', ' ').title()


if __name__ == '__main__':
    main()
