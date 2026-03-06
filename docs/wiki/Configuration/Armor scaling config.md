[English](#English)

# 护甲缩放配置

## 单个配置

- id：护甲缩放配置唯一id
- name：为该配置命名，可重复
- color：暂时没有功能
```json
{
	"id": 0,
	"name": "Example Armor Scaling config",
	"color": "#FFFFFF",
	"damageScaling": {
		伤害缩放配置
	},
	"durabilityScaling": {
		耐久度缩放配置
	}
}
```

### 伤害缩放配置

- item：物品ID
- scale：基础伤害缩放倍率
```json
"damageScaling": {
	"damageScaleData": [
		{
			"item": "minecraft:iron_helmet",
			"scale": 0.6
		},
		{
			"item": "minecraft:diamond_chestplate",
			"scale": 0.45
		},
		{
			"item": "minecraft:iron_chestplate",
			"scale": 0.6
		},
		{
			"item": "minecraft:chainmail_chestplate",
			"scale": 0.8
		}
	]
}
```

### 耐久度缩放配置

- healthToDurabilityRatio：每 1 点伤害对应的耐久度比例
- useAbsorbedDamageOnly：是否只取伤害缩放的吸收伤害
- headshotMultiplierScaling：是否乘以爆头倍率
- item：物品ID
- damagePercent：耐久度比例上限
- replaceItem：物品损坏后替换的物品，使用[通用刷新配置](https://github.com/XColorful/BattleRoyale/wiki/General-loot-config)
- lootDataId：用于选取`replaceItemLootData`
- replaceItemLootData：可复用的`replaceItem`
```json
"durabilityScaling": {
	"healthToDurabilityRatio": 5,
	"useAbsorbedDamageOnly": true,
	"headshotMultiplierScaling": true,
	"durabilityScaleData": [
		{
			"item": "minecraft:iron_helmet",
			"damagePercent": 150,
			"replaceItem": {
			}
		},
		{
			"item": "minecraft:diamond_chestplate",
			"damagePercent": 250,
			"replaceItem": {
				"lootType": "item",
				"item": "minecraft:chainmail_chestplate",
				"count": 1,
				"nbt": "{Damage:239}"
			}
		},
		{
			"item": "minecraft:chainmail_chestplate",
			"damagePercent": 220,
			"lootDataId": 0
		}
	],
	"replaceItemLootData": [
		{
			"lootDataId": 0,
			"replaceItem": {
				"lootType": "item",
				"item": "minecraft:chainmail_chestplate",
				"count": 1,
				"nbt": "{Damage:239}"
			}
		}
	]
}
```

# English

## Single gamerule config

- gameId: unique armor scaling config id
- gameName: name the config, can be repeated
- color: no function for now
```json
{
	"id": 0,
	"name": "Example Armor Scaling config",
	"color": "#FFFFFF",
	"damageScaling": {
		DAMAGE SCALING CONFIG
	},
	"durabilityScaling": {
		DURABILITY SCALING CONFIG
	}
}
```

### Damage scaling config

- item: item ID
- scale: base damage scaling multiplier
```json
"damageScaling": {
	"damageScaleData": [
		{
			"item": "minecraft:iron_helmet",
			"scale": 0.6
		},
		{
			"item": "minecraft:diamond_chestplate",
			"scale": 0.45
		},
		{
			"item": "minecraft:iron_chestplate",
			"scale": 0.6
		},
		{
			"item": "minecraft:chainmail_chestplate",
			"scale": 0.8
		}
	]
}
```

### Durability scaling config

- healthToDurabilityRatio: durability ratio corresponding to every 1 point of damage
- useAbsorbedDamageOnly: Whether to only use the damage absorbed by scaling
- headshotMultiplierScaling: Whether to multiply by the headshot multiplier
- item: item ID
- damagePercent: maximum durability percentage limit
- replaceItem: the item to replace after the original is broken, uses[General loot config](https://github.com/XColorful/BattleRoyale/wiki/General-loot-config#English)
- lootDataId: used to select `replaceItemLootData`
- replaceItemLootData: reusable `replaceItem` data
```json
"durabilityScaling": {
	"healthToDurabilityRatio": 5,
	"useAbsorbedDamageOnly": true,
	"headshotMultiplierScaling": true,
	"durabilityScaleData": [
		{
			"item": "minecraft:iron_helmet",
			"damagePercent": 150,
			"replaceItem": {
			}
		},
		{
			"item": "minecraft:diamond_chestplate",
			"damagePercent": 250,
			"replaceItem": {
				"lootType": "item",
				"item": "minecraft:chainmail_chestplate",
				"count": 1,
				"nbt": "{Damage:239}"
			}
		},
		{
			"item": "minecraft:chainmail_chestplate",
			"damagePercent": 220,
			"lootDataId": 0
		}
	],
	"replaceItemLootData": [
		{
			"lootDataId": 0,
			"replaceItem": {
				"lootType": "item",
				"item": "minecraft:chainmail_chestplate",
				"count": 1,
				"nbt": "{Damage:239}"
			}
		}
	]
}
```