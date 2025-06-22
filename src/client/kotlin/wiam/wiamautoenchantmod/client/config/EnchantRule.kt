package wiam.wiamautoenchantmod.client.config


data class EnchantRule(val itemId:String, val enchantmentId:String, val level:String, val action: Action, val isEnchantmentIdRegex: Boolean = false, val isItemIdRegex: Boolean = true) {
    
    companion object {
        val DEFAULT = mutableListOf(
            EnchantRule(".*leggings", "minecraft:protection", ">0", Action.LEVEL_1),
            EnchantRule("^(?!.*leggings$).+$", "minecraft:blast_protection", ">0", Action.LEVEL_1),
            EnchantRule(".*", "minecraft:knockback", ">1", Action.LEVEL_1),
            EnchantRule(".*", "minecraft:fire_aspect", ">0", Action.LEVEL_1),
            EnchantRule(".*", "minecraft:smite", ">0", Action.LEVEL_1),
            EnchantRule(".*", "minecraft:fire_protection", ">0", Action.LEVEL_1),
            EnchantRule(".*", "minecraft:thorns", ">0", Action.LEVEL_1),
            EnchantRule(".*", "minecraft:blast_protection", "<4", Action.LEVEL_1),
            EnchantRule(".*", "minecraft:protection", "<4", Action.LEVEL_1),
            EnchantRule(".*", "minecraft:sharpness", "<4", Action.LEVEL_1),
            EnchantRule(".*", ".*", ">0", Action.LEVEL_3, true)
        )
    }
    
}