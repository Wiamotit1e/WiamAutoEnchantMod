package wiam.wiamautoenchantmod.client.config

data class EnchantRule(val enchantmentId:String, val level:String, val action: Action) {
    
    companion object {
        val DEFAULT = listOf(
            EnchantRule("minecraft:knockback", ">1", Action.LEVEL_1),
            EnchantRule("minecraft:fire_aspect", ">0", Action.LEVEL_1),
            EnchantRule("minecraft:smite", ">0", Action.LEVEL_1),
            EnchantRule("minecraft:fire_protection", ">0", Action.LEVEL_1),
            EnchantRule("minecraft:thorns", ">0", Action.LEVEL_1),
            EnchantRule("minecraft:sharpness", "<4", Action.LEVEL_1),
            EnchantRule("minecraft:protection", "<4", Action.LEVEL_1),
            EnchantRule("*", ">0", Action.LEVEL_3)
        )
    }
    
}