package wiam.wiamautoenchantmod.client

import net.minecraft.client.gui.screen.ingame.EnchantmentScreen
import net.minecraft.client.network.ClientPlayerInteractionManager
import net.minecraft.entity.player.PlayerEntity
import wiam.wiamautoenchantmod.client.config.EnchantRule

interface IAutoEnchanter {
    fun enchantAllItems(
        screen: EnchantmentScreen,
        interactionManager: ClientPlayerInteractionManager,
        player: PlayerEntity,
        maxWaitTick: Int
    )
    
    fun enchantAllItemsWithRules(
        screen: EnchantmentScreen,
        interactionManager: ClientPlayerInteractionManager,
        player: PlayerEntity,
        rules: List<EnchantRule>,
        maxWaitTick: Int
    )
}