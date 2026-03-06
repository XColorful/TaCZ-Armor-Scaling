# TaCZ护甲缩放 | TaCZ Armor Scaling

[中文](#TaCZ护甲缩放) | [English](#tacz-armor-scaling)

# TaCZ护甲缩放

😎[wiki](https://github.com/XColorful/TaCZ-Armor-Scaling/wiki) | 📄[docs](https://github.com/XColorful/TaCZ-Armor-Scaling/tree/HEAD/docs)

本模组可设置任意盔甲**对 TaCZ 子弹**的减伤比例、耐久度损耗百分比，对其他伤害源无效。采用线性比例减伤取代原版盔甲机制，旨在简化 TaCZ 枪包数值配置。

---

`该模组需要安装在服务端`

## 使用说明

### 前置模组

该模组功能从 _自定义大逃杀_ 中拆分出来，需要前置模组：
- 自定义大逃杀 ≥ 0.5.1 [CurseForge](https://www.curseforge.com/minecraft/mc-mods/custom-battleroyale) | [Modrinth](https://modrinth.com/mod/custom-battleroyale)
- 永恒枪械工坊：零（TaCZ） [CurseForge](https://www.curseforge.com/minecraft/mc-mods/timeless-and-classics-zero) | [Modrinth](https://modrinth.com/mod/timeless-and-classics-zero)

### 启用功能

一键设置 _原版护甲穿透_：
- `/armorscaling armorIgnore true`
- `/armorscaling armorIgnore scale 1.0`

启用盔甲比例减伤：
- `/armorscaling damageScaling true`

启用盔甲耐久度比例损耗：
- `/armorscaling durabilityScaling true`

### 配置文件

[护甲缩放配置](https://github.com/XColorful/TaCZ-Armor-Scaling/wiki/Armor-scaling-config)位于 _./config/armorscaling/*.json_
- 修改完后，使用`/armorscaling reload`重新读取
- 使用`/armorscaling config armorscaling switch`切换不同配置文件
- 如在单个文件内存储多个配置，使用`/armorscaling config armorscaling id`选中

# TaCZ Armor Scaling

😎[wiki](https://github.com/XColorful/TaCZ-Armor-Scaling/wiki) | 📄[docs](https://github.com/XColorful/TaCZ-Armor-Scaling/tree/HEAD/docs)

This mod allows for configuring specific damage reduction ratios and durability loss percentages for any armor against **TaCZ bullets**; it is ineffective against other damage sources. Replaces vanilla armor mechanics with linear proportional damage reduction to simplify TaCZ gun pack balancing.

---

`This mod needs to be installed on the server.`

## Usage Instructions

### Prerequisites

This mod's functionality was extracted from _Custom BattleRoyale_. The following dependencies are required:
- Custom BattleRoyale ≥ 0.5.1 [CurseForge](https://www.curseforge.com/minecraft/mc-mods/custom-battleroyale) | [Modrinth](https://modrinth.com/mod/custom-battleroyale)
- Timeless and Classics Zero (TaCZ) [CurseForge](https://www.google.com/search?q=%23%23%23) | [Modrinth](https://www.google.com/search?q=%23%23%23)

### Enabling Features

One-click setup for _Minecraft Armor Piercing_:
- `/armorscaling armorIgnore true`
- `/armorscaling armorIgnore scale 1.0`

Enable proportional armor damage reduction:
- `/armorscaling damageScaling true`

Enable proportional armor durability loss:
- `/armorscaling durabilityScaling true`

### Configuration

[Armor scaling config](https://github.com/XColorful/TaCZ-Armor-Scaling/wiki/Armor-scaling-config#English) is located at _./config/armorscaling/*.json_
- After modifying, use `/armorscaling reload` to reload the configuration.
- Use `/armorscaling config armorscaling switch` to switch between different configuration files.
- If multiple configurations are stored within a single file, use `/armorscaling config armorscaling id` to select.