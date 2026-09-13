> 1.21.1neoforge-cgc 提示词

# 

```
现在要将TaCZ-Armor-Scaling从TaCZ改为CGC
- 目前做了一些修改，但最终效果似乎没生效
- 当前IDEA打开了多个窗口，注意主窗口是CGC，修改的代码需要写在Armor scaling里
- TaCZ类到CGC的迁移映射文档在./docs-tacz/TaCZ Migration Mapping.md

armor scaling里的类名可以不用全部调整到CGC的，但是调用要转接到CGC
```

# 

I'll start by reading the migration mapping doc and exploring the current state of the Armor Scaling project.

The doc is in the CGC worktree. Let me read it along with the in-progress changes in Armor Scaling.

Now let me examine the current state of the Armor Scaling changes and its structure.

Now I understand the shape of the change. Let me read the Armor Scaling source tree and the CGC event/projectile classes in parallel.

Now let me read the ArmorScaling core files and CGC's `IGunProjectile`.

The bash grep hung on large dirs. Let me use the Grep tool instead.

Now I see the architecture. Let me verify the CGC event/base-class API and mod id.

Found a likely root cause: the handler is registered against `GunProjectileEvent.class` (via `CustomEventType`) but `ProjectileHitEntityEvent` dispatches on its own class. Let me verify.

Confirmed the registration/dispatch key mismatch. Let me check how CGC itself registers for this event, to follow the established pattern.

CGC's own client handler registers via `CustomEventType` too, so let me check whether the dispatcher is inheritance-aware before concluding.

Now let me confirm the projectile interface hierarchy and the module/build wiring.

Let me read the scaling managers and the compat init to confirm the full flow.

Let me verify the dispatcher classes across CGC's projectile events.

Let me confirm where `ProjectileHitEntityEvent` is posted (server and/or client) before editing.

Now I have the full picture. Let me track the work and apply the fixes.

Now remove the dead mixin and its config entry.

Let me check the root build config for the Curse maven repo, then compile.

Now let me compile the Armor Scaling project.

Build succeeded. Let me verify the changed files have no unresolved references.

Let me confirm the scaling managers (including ArmorIgnoreManager) are actually registered at startup.

Build is clean. Let me record the non-obvious CGC event-dispatcher pitfall I found, then summarize.

迁移已修复并通过编译。问题不在类名，而在两处「调用转接」：

## 根因（为什么"没生效"）

**1. 注册键不匹配（事件根本没收到）**
CGC 的事件分发是按**事件自身类**精确索引的：`EventPoster.postCustomEvent` 走 `event.getEventDispatcher()`，而 `ProjectileHitEntityEvent` 的 dispatcher 是用 `ProjectileHitEntityEvent.class` 建的。但 `CustomEventType.PROJECTILE_HIT_ENTITY_EVENT` 却指向 `GunProjectileEvent.class`（抽象基类），没有继承查找。所以按下 `CustomEventType` 注册的 handler 被存进了另一个 dispatcher，永远不会被调用——注册日志照样打印成功。

**2. ArmorIgnoreEvent 结果被丢弃**
CGC 在 `ProjectileHitEntityEvent` **之后**由 `_ProjectileHit._executeProjectileHitEntity` 按 `iGunProjectile.getArmorIgnorePercent(...)` 拆分普通/穿甲伤害。原 mixin 的职责应改为：把 `ArmorIgnoreEvent` 的结果**写回枪射物的 `armorIgnorePercent`**，而不是只 post 一下就完事。

## 改动

- [TaczEventRegister.java](D:/Github/TaCZ-Armor-Scaling/neoforge-compat/src/main/java/xiao.armorscaling.compat.neoforge/compat/tacz/TaczEventRegister.java) — 改用 `register(bulletListener, ProjectileHitEntityEvent.class)`；`ArmorIgnoreEvent` 被处理时 `iGunProjectile.setArmorIgnorePercent(gunProjectile, clamp(percent, 0, 1))`。
- [Tacz.java](D:/Github/TaCZ-Armor-Scaling/core/src/main/java/xiao/armorscaling/compat/tacz/Tacz.java) — 去掉 `if (true) return;`，反射校验从 TaCZ API 换成实际用到的 CGC API。
- 删除空壳 `_ProjectileHitMixin`（已无职责，且引用 CGC 内部类 `_ProjectileHit`），`armorscaling.mixins.json` 置空。类名 `Tacz*` 按你的要求保留，只把调用转接到 CGC。
- `TaczBulletHurtEvent` 的字段映射（`context.getBaseDamage` / `getHitResult_VictimEntity` / `getGunLocation` 等）保持你已改好的版本，编译通过。

IDEA `build_project`（`D:\Github\TaCZ-Armor-Scaling`）成功，三个文件 `get_file_problems` 无错误。

## 需要你判断的两点

**① 这可能是 CGC 自身的 bug，不只是 ArmorScaling 的**：`CustomEventType.PROJECTILE_HIT_ENTITY_EVENT(GunProjectileEvent.class)` 在 core/src/main/java/dev/xcolorful/customgun/core/api/event/CustomEventType.java:31，与它三个兄弟常量（都指向自己的事件类）不一致。这意味着 CGC 自己的 `GunHurtBobTweak`（ClientEventHandlers.java:36）也是收不到事件的。我在 ArmorScaling 侧用「按类注册」绕开了，你这个枚举要不要在 CGC 里修，由你定。

**② `customgun` 在 mods.toml 里是 `optional`**，但 `TaczEventRegister`/`TaczBulletHurtEvent` 在类签名里直接引用了 CGC 类型，且 `ArmorScalingNeoforge` 构造时就会 `TaczEventRegister.get()`。若 CGC 未安装，这个类加载会 `NoClassDefFoundError`。要改成 required，还是把 CompatApi 改成惰性初始化？我按你的选择来做。

未提交，按规则停在改动等待你 review。

# 

```
你检查一下CGC CustomEventType枚举对应类的问题，我手动修复，armor scaling按照修复后的来写
```

# 

I'll enumerate every `CustomEventType` constant's declared class against the dispatcher class each event actually uses, plus its `getEventType()` return.

One mismatch so far. Let me check the client-side handler registrations and any late-binding of enum classes.

Let me make sure no other core event is missing from the enum or bound to the wrong class.

Audit complete: exactly **one** mismatch. Let me apply the change to Armor Scaling per the fixed version.

## CGC `CustomEventType` 审计结论

全项目共 **21 个枚举常量 / 20 个 `getEventDispatcher()` 站点**，逐一对齐后只有 **1 处不匹配**。

### 匹配的（core 事件，14 个）

`GUN_FIRE_EVENT`→GunFireEvent、`PROJECTILE_HIT_BLOCK_EVENT`→ProjectileHitBlockEvent、`PROJECTILE_HIT_ENTITY_FINISH_EVENT`→ProjectileHitEntityFinishEvent、`PROJECTILE_KILL_ENTITY_EVENT`→ProjectileKillEntityEvent、`SHOOTER_DRAW_EVENT`、`SHOOTER_FIRE_EVENT`、`SHOOTER_GUN_MODIFIER_CACHE_EVENT`、`SHOOTER_INSPECT_EVENT`、`SHOOTER_PREPARE_MELEE_EVENT`→ShooterMeleeEvent、`SHOOTER_RELOAD_EVENT`、`SHOOTER_RELOAD_FEED_EVENT`、`SHOOTER_RELOAD_FINISH_EVENT`、`SHOOTER_SWITCH_FIRE_MODE_EVENT` —— 全部 `eventClass` == 事件自身 dispatcher 的类。

### 客户端延迟绑定（6 个，`null` → `_CustomEventType.mixinClientEventClass()` 注入，正确）

`SWAP_ITEM_WITH_OFFHAND_EVENT`、`BEFORE_RENDER_HAND_EVENT`、`ITEM_IN_HAND_BOB_HURT/VIEW_EVENT`、`LEVEL_BOB_HURT/VIEW_EVENT`。`CUSTOM_EVENT` 保持 `null`（基类默认值，符合设计）。

### 唯一不匹配

[CustomEventType.java:31](D:/Github/Custom-Gun-Continued-1.21.1neoforge/core/src/main/java/dev/xcolorful/customgun/core/api/event/CustomEventType.java:31)

```java
PROJECTILE_HIT_ENTITY_EVENT(GunProjectileEvent.class),   // 抽象基类
```

而 [ProjectileHitEntityEvent.java:116](D:/Github/Custom-Gun-Continued-1.21.1neoforge/core/src/main/java/dev/xcolorful/customgun/core/api/event/projectile/ProjectileHitEntityEvent.java:116) 用的是自身：

```java
getEventDispatcher(ProjectileHitEntityEvent.class)
```

应为：

```java
PROJECTILE_HIT_ENTITY_EVENT(ProjectileHitEntityEvent.class),
```

`GunProjectileEvent` 是抽象类、没有任何事件用它的 dispatcher，所以这条映射是死键。修复后 `getEventType()`（返回该常量）、`_ProjectileHit` 的派发、以及按枚举注册的调用方三者才闭合。

**连带影响**：修好之前，[ClientEventHandlers.java:36](D:/Github/Custom-Gun-Continued-1.21.1neoforge/core/src/main/java/dev/xcolorful/customgun/client/event/custom/ClientEventHandlers.java:36) 里 CGC 自己的 `GunHurtBobTweak` 也是收不到事件的——不是只有 ArmorScaling 受影响。（另外 `ProjectileHitEntityEvent` 目前只以 `McLogicalSide.SERVER` 派发，而 `GunHurtBobTweak.onProjectileHit` 开头 `if (event.getLogicalSide().isServer()) return;`，即使修好枚举它也会立刻返回，这点你可能也要一并看。）

## ArmorScaling 已按修复后写法改回

[TaczEventRegister.java](D:/Github/TaCZ-Armor-Scaling/neoforge-compat/src/main/java/xiao.armorscaling.compat.neoforge/compat/tacz/TaczEventRegister.java) 的注册/注销改回 `CustomEventType.PROJECTILE_HIT_ENTITY_EVENT`（不再按类注册），只留一行注释说明这条不变量。`ArmorIgnoreEvent` 结果写回 `IGunProjectile.setArmorIgnorePercent` 的部分保持不变。

IDEA `build_project` 成功，无错误。

> 注意：这样 ArmorScaling 就**依赖你先修好 CGC 那条枚举**，否则会退回静默失效。如果你想让它对两种情况都免疫，按类注册（`register(bulletListener, ProjectileHitEntityEvent.class)`）是不依赖枚举的写法——现在已经改成枚举版，要不要留个双保险由你定。

改动都在 ArmorScaling 侧，未提交。

# 

```
子弹穿甲似乎没生效，设置为1.0后应该100%无视护甲，直接取armor scaling的减伤比例，你看下CGC对比TaCZ是否漏了哪里或没写对?
```

# 

Good catch to check. Armor-ignore in CGC is implemented via a separate damage type (`CustomDamageType.PIERCER`), so the likely gap is in the damage-type data. Let me verify.

`PIERCE_DAMAGE`/`BYPASS_DAMAGE` tags look declared-but-unused. Let me check what actually applies piercing in CGC, and compare with TaCZ.

TaCZ has `data/minecraft/tags/damage_type/bypasses_armor.json`; CGC doesn't. Let me confirm contents and check CGC's `LivingEntityMixin`.

Let me confirm CGC has no vanilla armor-bypass tag and no consumer for the pierce tag.

Let me verify the vanilla semantics in the actual MC 1.21.1 source via IDEA.

Confirmed in vanilla source: `getDamageAfterArmorAbsorb` skips **both** armor absorption and `hurtArmor` when the damage type is in `minecraft:bypasses_armor`. Let me check whether CGC's other custom tags have consumers.

找到了。**不是 ArmorScaling 的问题，是 CGC 漏了 TaCZ 的伤害类型数据包标签** —— 穿甲伤害源和普通伤害源目前只差一个名字，护甲照样吸收。

## 证据链

**1. TaCZ 靠原版标签实现穿甲**

`D:\Github\TACZ-1.21.1\src\main\resources\data\minecraft\tags\damage_type\bypasses_armor.json`：

```json
{ "replace": false, "values": ["tacz:bullet_ignore_armor", "tacz:bullet_void_ignore_armor"] }
```

（另有 `bypasses_invulnerability.json` → `tacz:bullet_void`, `tacz:bullet_void_ignore_armor`）

**2. CGC 完全没有这个目录**

我在整个 CGC 仓库里搜 `bypasses_armor` / `bypasses_invulnerability`：**0 个 json 命中**，也没有 datagen（无 `TagsProvider`/`GatherDataEvent`）。`data/minecraft/` 目录根本不存在，只有 `data/customgun/`。

**3. CGC 自己的穿甲标签是死标签**

[ModDamageTypes.java:26-27](core/src/main/java/dev/xcolorful/customgun/core/init/registry/ModDamageTypes.java:26) 声明了 `PIERCE_DAMAGE` / `BYPASS_DAMAGE`，`data/customgun/tags/damage_type/pierce_damage.json` 也写了 `customgun:piercer` —— 但全项目**没有任何一处读取它们**（对比 `BULLET_DAMAGE` 就有人在 [_EntityImpactHandler.java:43](core/src/main/java/dev/xcolorful/customgun/core/projectile/impact/_EntityImpactHandler.java:43) 用）。等于穿甲语义没接线。

**4. 原版判定点（已在 neoforge sources 里核对）**

`LivingEntity#getDamageAfterArmorAbsorb`：

```java
if (!damageSource.is(DamageTypeTags.BYPASSES_ARMOR)) {
    this.hurtArmor(damageSource, damageAmount);          // 护甲耐久
    damageAmount = CombatRules.getDamageAfterAbsorb(...); // 护甲减伤
}
```

所以不在 `minecraft:bypasses_armor` 里的伤害类型，**护甲减伤和护甲耐久损耗都会照常生效**。

## 结果

[_ProjectileHit.java:209-210](core/src/main/java/dev/xcolorful/customgun/core/projectile/impact/_ProjectileHit.java:209) 建的 `BULLET` / `PIERCER` 两个 DamageSource，在 CGC 里除了 `message_id` 之外行为完全相同。`armorIgnorePercent = 1.0` 时：

- 普通伤害分支被跳过（`armorIgnorePercent < 1` 为假）✅
- 全部伤害走 `pierceDamage` → 但 `customgun:piercer` 不 bypass armor → **护甲继续吸收** ❌

这正是你看到的"穿甲没生效"。ArmorScaling 侧 `setArmorIgnorePercent` 写的是对的，无需改动。

## 修复（CGC 侧，两个新文件）

`core/src/main/resources/data/minecraft/tags/damage_type/bypasses_armor.json`

```json
{
  "replace": false,
  "values": [
    "customgun:piercer",
    "customgun:overrider"
  ]
}
```

`core/src/main/resources/data/minecraft/tags/damage_type/bypasses_invulnerability.json`

```json
{
  "replace": false,
  "values": [
    "customgun:breaker",
    "customgun:overrider"
  ]
}
```

映射依据（与 TaCZ 一一对应）：

| CGC 伤害类型 | isPierce / isBypass | TaCZ 对应 | 应入标签 |
|---|---|---|---|
| `customgun:bullet` | false / false | `tacz:bullet` | — |
| `customgun:piercer` | true / false | `tacz:bullet_ignore_armor` | bypasses_armor |
| `customgun:breaker` | false / true | `tacz:bullet_void` | bypasses_invulnerability |
| `customgun:overrider` | true / true | `tacz:bullet_void_ignore_armor` | 两个都要 |

你目前的症状只需第一个文件。第二个文件影响的是"无视无敌帧"（`LivingEntity#isInvulnerableTo` 的 `BYPASSES_INVULNERABILITY`），CGC 现在 `_ProjectileHit` 是用 `invulnerableTime = 0` 硬绕的，所以观感上不一定明显，但属于同一处遗漏，建议一并补上。

## 要我怎么做？

如果你按上次那样手动改 CGC，那这里就不动，ArmorScaling 保持现状即可（它只负责写 `armorIgnorePercent`，链路已经通了）。

如果你想让我直接落这两个文件到 CGC，说一声——不过按 CLAUDE.md 我只动当前分支，而这是 CGC 仓库的 `1.21.1neoforge` 分支，需要你明确授权跨仓库改动。另一个备选是在 ArmorScaling 里塞同名标签（数据包标签会跨模组合并，也能生效），但语义上标签该由拥有伤害类型的 CGC 声明，不推荐。

我没有修改任何文件。
