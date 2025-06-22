package wiam.wiamautoenchantmod.client

import net.minecraft.client.gui.screen.ingame.EnchantmentScreen
import net.minecraft.client.network.ClientPlayerInteractionManager
import net.minecraft.entity.player.PlayerEntity
import wiam.wiamautoenchantmod.client.config.ConfigService
import wiam.wiamautoenchantmod.client.config.EnchantRule

object AutoEnchanterController {
    
    private val autoEnchanter: IAutoEnchanter = AutoEnchanter
    private val isThisOn
        get() = ConfigService.getConfig().isThisOn
    private val isRulesOn: Boolean
        get() = ConfigService.getConfig().isRulesOn
    
    fun autoEnchant(screen: EnchantmentScreen, interactionManager: ClientPlayerInteractionManager, player: PlayerEntity, rules: List<EnchantRule>?) {
        if (!isThisOn) return
        if (!isRulesOn) autoEnchanter.enchantAllItems(screen, interactionManager, player)
        else autoEnchanter.enchantAllItemsWithRules(screen, interactionManager, player, rules!!)
    }
    
}