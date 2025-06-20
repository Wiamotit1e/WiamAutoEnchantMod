package wiam.wiamautoenchantmod.client

import net.minecraft.client.gui.screen.ingame.EnchantmentScreen
import net.minecraft.client.network.ClientPlayerInteractionManager
import net.minecraft.entity.player.PlayerEntity
import wiam.wiamautoenchantmod.client.config.ConfigFileInteraction
import wiam.wiamautoenchantmod.client.config.EnchantRule

object AutoEnchanterController {
    
    private val autoEnchanter: IAutoEnchanter = AutoEnchanter
    private val isThisOn
        get() = ConfigFileInteraction.getConfig().isThisOn
    private val isRulesOpen: Boolean
        get() = ConfigFileInteraction.getConfig().isRulesOpen
    
    fun autoEnchant(screen: EnchantmentScreen, interactionManager: ClientPlayerInteractionManager, player: PlayerEntity, rules: List<EnchantRule>?) {
        if (!isThisOn) return
        if (!isRulesOpen) autoEnchanter.enchantAllItems(screen, interactionManager, player)
        else autoEnchanter.enchantAllItemsWithRules(screen, interactionManager, player, rules!!)
    }
    
}