# WiamAutoEnchant Mod

[![Fabric](https://img.shields.io/badge/Minecraft-1.21+-blueviolet?logo=curseforge)](https://fabricmc.net/)
[![License](https://img.shields.io/badge/license-MIT-green)](LICENSE)

Automate your enchanting experience in Minecraft! WiamAutoEnchant intelligently manages your enchanting table operations based on customizable rules, saving you time and resources.

## ✨ Features

- **Smart Enchanting Automation**: Automatically applies enchantments based on your configured rules
- **Custom Rule System**: Create complex rules for different items and enchantments
- **Regex Support**: Use regex patterns for flexible item/enchantment matching
- **In-Game Configuration**: Modify settings without restarting
- **Command Interface**: Full control through intuitive commands
- **Multi-Level Support**: Automatically choose between level 1/3 enchantments

## ⚙️ Installation

1. Install [Fabric Loader](https://fabricmc.net/use/)
2. Download the latest [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api)
3. [Download WiamAutoEnchant mod](https://example.com/download) (replace with actual link)
4. Place the JAR file in your `mods` folder

## 🕹️ Usage

### Basic Commands
```
/autoenchant config reset                               - Reset the config
/autoenchant config toggle_config [true|false]          - Toggle the mod
/autoenchant config toggle_rule [true|false]            - Toggle rule processing
/autoenchant config show_config                         - Show current configuration
/autoenchant config remove_rule <index>                 - Remove the rule in the index
/autoenchant config exchange_rule <index1> <index2>     - Swap the positions of rules
/autoenchant config move_rule <index1> <index2>         - Move the positions of rules
```

### Creating Rules
Format:
```
/autoenchant config add_rule <item_id> <enchantment_id> <level_condition> <action> <is_item_regex> <is_enchantment_regex>
```

Example:
```
/autoenchant config add_rule ".*sword" "minecraft:sharpness" ">2" LEVEL_3 true false
```

### Rule Components
| Parameter | Description | Example |
|-----------|-------------|---------|
| `item_id` | Item ID or regex pattern | `".*leggings"`, `"minecraft:diamond_sword"` |
| `enchantment_id` | Enchantment ID or regex | `"minecraft:protection"`, `".*_protection"` |
| `level_condition` | Level requirement | `">2"`, `"<=3"`, `"==1"` |
| `action` | Enchantment button | `LEVEL_1`, `LEVEL_3` |
| `is_item_regex` | Treat item_id as regex | `true`/`false` |
| `is_enchantment_regex` | Treat enchantment_id as regex | `true`/`false` |

## ⚙️ Configuration

Configuration is stored in `config/wiamautoenchant.json`. Example structure:
```json
{
  "isThisOn": true,
  "isRulesOn": true,
  "rules": [
    {
      "itemId": ".*",
      "enchantmentId": "minecraft:unbreaking",
      "level": ">0",
      "action": "LEVEL_1",
      "isEnchantmentIdRegex": false,
      "isItemIdRegex": true
    },
    {
      "itemId": ".*leggings",
      "enchantmentId": "minecraft:protection",
      "level": ">0",
      "action": "LEVEL_1",
      "isEnchantmentIdRegex": false,
      "isItemIdRegex": true
    }
  ]
}
```

## 🧩 Compatibility

- Minecraft 1.21 (Fabric)
- Compatible with most inventory management mods
- Client-side only (no server installation needed)

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Credits

- Developed by Wiamotit1e and DeepSeek
- Inspired by various automation mods
- Built with FabricMC and Kotlin

---

好的，这是整合后的中文版说明文档：

---

# **WiamAutoEnchant 模组**

[![Fabric](https://img.shields.io/badge/Minecraft-1.21+-blueviolet?logo=curseforge)](https://fabricmc.net/)
[![许可证](https://img.shields.io/badge/license-MIT-green)](LICENSE)

自动化你的 Minecraft 附魔体验！WiamAutoEnchant 基于自定义规则智能管理附魔台操作，节省你的时间和资源。

---

## ✨ **功能亮点**

- **智能附魔自动化**：根据配置规则自动应用附魔
- **自定义规则系统**：为不同物品和附魔创建复杂规则
- **正则表达式支持**：使用正则模式灵活匹配物品/附魔
- **游戏内配置**：无需重启即可修改设置
- **命令控制**：通过直观命令实现完整控制
- **多等级支持**：自动选择 1级/3级 附魔选项

---

## ⚙️ **安装指南**

1. 安装 [Fabric Loader](https://fabricmc.net/use/)
2. 下载最新版 [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api)
3. [下载 WiamAutoEnchant 模组](https://example.com/download) (请替换为实际链接)
4. 将 JAR 文件放入 `mods` 文件夹

---

## 🕹️ **使用说明**

### ▶ 基础命令
```
/autoenchant config reset                               - 重置配置
/autoenchant config toggle_config [true|false]          - 开关模组
/autoenchant config toggle_rule [true|false]            - 开关规则处理
/autoenchant config show_config                         - 显示当前配置
/autoenchant config remove_rule <索引>                  - 删除指定规则
/autoenchant config exchange_rule <索引1> <索引2>       - 交换两条规则位置
/autoenchant config move_rule <索引1> <索引2>           - 移动规则位置
```

### ▶ 创建规则
**命令格式**：
```
/autoenchant config add_rule <物品ID> <附魔ID> <等级条件> <操作类型> <是否正则物品> <是否正则附魔>
```

**示例**：
```
/autoenchant config add_rule ".*sword" "minecraft:sharpness" ">2" LEVEL_3 true false
```

### ▶ 规则参数详解
| 参数 | 说明 | 示例 |
|------|------|------|
| `物品ID` | 物品ID 或 正则表达式 | `".*leggings"`, `"minecraft:diamond_sword"` |
| `附魔ID` | 附魔ID 或 正则表达式 | `"minecraft:protection"`, `".*_protection"` |
| `等级条件` | 等级要求 | `">2"`, `"<=3"`, `"==1"` |
| `操作类型` | 附魔按钮选择 | `LEVEL_1`, `LEVEL_3` |
| `是否正则物品` | 物品ID是否用正则 | `true`/`false` |
| `是否正则附魔` | 附魔ID是否用正则 | `true`/`false` |

---

## ⚙️ **配置文件**

配置文件路径：`config/wiamautoenchant.json`  
**示例结构**：
```json
{
  "isThisOn": true,
  "isRulesOn": true,
  "rules": [
    {
      "itemId": ".*",
      "enchantmentId": "minecraft:unbreaking",
      "level": ">0",
      "action": "LEVEL_1",
      "isEnchantmentIdRegex": false,
      "isItemIdRegex": true
    },
    {
      "itemId": ".*leggings",
      "enchantmentId": "minecraft:protection",
      "level": ">0",
      "action": "LEVEL_1",
      "isEnchantmentIdRegex": false,
      "isItemIdRegex": true
    }
  ]
}
```

---

## 🧩 **兼容性**

- 支持 Minecraft 1.21 (Fabric)
- 兼容大部分物品管理模组
- **纯客户端模组**（无需服务器安装）

---

## 📜 **许可证**

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件。

---

## 🙏 **致谢**

- 开发：Wiamotit1e 和 DeepSeek
- 灵感来源：多种自动化模组
- 技术框架：FabricMC + Kotlin

---
