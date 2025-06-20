package wiam.wiamautoenchantmod.client

import net.minecraft.client.network.ClientPlayerInteractionManager
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.screen.slot.SlotActionType

object EnchantmentScreenInteraction {
    
    fun fillLapisSlot(interactionManager: ClientPlayerInteractionManager, syncId: Int, slot: Int, player: PlayerEntity) {
        interactionManager.clickSlot(syncId, slot, 0, SlotActionType.QUICK_MOVE, player)
    }
    fun fillEnchantingSlot(interactionManager: ClientPlayerInteractionManager, syncId: Int, slot: Int, player: PlayerEntity) {
        if (!player.currentScreenHandler.slots[0].stack.isEmpty) {
            interactionManager.clickSlot(syncId, 0, 0, SlotActionType.QUICK_MOVE, player)
            interactionManager.clickSlot(syncId, 0, 0, SlotActionType.PICKUP, player)
            interactionManager.clickSlot(syncId, -999, 0, SlotActionType.PICKUP, player)
        }
        interactionManager.clickSlot(syncId, slot, 0, SlotActionType.QUICK_MOVE, player)
    }
}