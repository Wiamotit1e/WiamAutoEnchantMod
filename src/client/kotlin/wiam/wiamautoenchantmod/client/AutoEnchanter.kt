package wiam.wiamautoenchantmod.client

import net.minecraft.client.gui.screen.ingame.EnchantmentScreen
import net.minecraft.client.network.ClientPlayerInteractionManager
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.registry.RegistryKeys
import wiam.wiamautoenchantmod.client.config.Action
import wiam.wiamautoenchantmod.client.config.EnchantRule
import java.util.stream.IntStream

object AutoEnchanter : IAutoEnchanter {
    
    private fun matchRule(rule: EnchantRule, enchantUnit: WEnchantUnit): Action {
        // 1. 验证附魔ID
        val enchantName = enchantUnit.id
        if (rule.enchantmentId != "*" && rule.enchantmentId != enchantName) {
            return Action.INVALID
        }
        // 2. 处理等级条件
        val value = enchantUnit.level
        val trimmedCondition = rule.level.trim()
        if (trimmedCondition.isEmpty()) return Action.INVALID
        
        // 3. 运算符匹配逻辑
        val operators = listOf(">=", "<=", "!=", "==", ">", "<")
        for (op in operators) {
            if (trimmedCondition.startsWith(op)) {
                val numberPart = trimmedCondition.substring(op.length).trim()
                val number = numberPart.toIntOrNull() ?: return Action.INVALID
                
                val conditionMet = when (op) {
                    ">"  -> value > number
                    "<"  -> value < number
                    ">=" -> value >= number
                    "<=" -> value <= number
                    "==" -> value == number
                    "!=" -> value != number
                    else -> false
                }
                return if (conditionMet) rule.action else Action.INVALID
            }
        }
        
        // 4. 无运算符时的数字匹配
        val number = trimmedCondition.toIntOrNull() ?: return Action.INVALID
        return if (value == number) rule.action else Action.INVALID
    }
    
    override fun enchantAllItems(screen: EnchantmentScreen, interactionManager: ClientPlayerInteractionManager, player:PlayerEntity) {
        val level = 2
        val syncId = screen.screenHandler.syncId
        val slot = IntStream
            .range(2, screen.screenHandler.slots.size)
            .filter { i: Int -> screen.screenHandler.slots[i].stack.item.enchantability > 0 && !screen.screenHandler.slots[i].stack.hasEnchantments() }
            .findFirst()
            .orElse(-1)
        
        val slotOfLapis = IntStream
            .range(2, screen.screenHandler.slots.size)
            .filter { i: Int -> screen.screenHandler.slots[i].stack.item === Items.LAPIS_LAZULI && screen.screenHandler.slots[i].stack.count > level }
            .findFirst()
            .orElse(-1)
        if (slotOfLapis == -1 && screen.screenHandler.slots[1].stack.count < level + 1) return
        if (WiamUtil.autoEnchantSign == AutoEnchantSign.FINISHING) {
            if (slot == -1) {
                screen.close()
                return
            }
            if (screen.screenHandler.slots[1].stack.count < level + 1)
                EnchantmentScreenInteraction.fillLapisSlot(interactionManager, syncId, slotOfLapis, player)
            
            EnchantmentScreenInteraction.fillEnchantingSlot(interactionManager, syncId, slot, player)
            interactionManager.clickButton(syncId, level)
            WiamUtil.autoEnchantSign = AutoEnchantSign.PROCESSING
        }
        
        if (WiamUtil.autoEnchantSign == AutoEnchantSign.PROCESSING){
            if (WiamUtil.counterOfWaitingForResult >= 5) {
                screen.close()
                return
            }
            
            if (!screen.screenHandler.slots[0].stack.hasEnchantments() && screen.screenHandler.slots[0].stack.item != Items.ENCHANTED_BOOK) {
                WiamUtil.counterOfWaitingForResult++
                return
            }
            WiamUtil.counterOfWaitingForResult = 0
            WiamUtil.autoEnchantSign = AutoEnchantSign.FINISHING
        }
    }
    
    override fun enchantAllItemsWithRules (screen: EnchantmentScreen, interactionManager: ClientPlayerInteractionManager, player: PlayerEntity, rules: List<EnchantRule>) {
        val syncId = screen.screenHandler.syncId
        if (WiamUtil.autoEnchantSign == AutoEnchantSign.FINISHING) {
            var emptySlot = IntStream.range(2, screen.screenHandler.slots.size)
                .filter { i: Int -> (!WiamUtil.excludedItems.contains(screen.screenHandler.slots[i].stack.item)) && (screen.screenHandler.slots[i].stack.item.enchantability > 0) && (!screen.screenHandler.slots[i].stack.hasEnchantments()) && (screen.screenHandler.slots[i].stack.item !== Items.BOOK) }
                .findFirst()
                .orElse(-1)
            if (emptySlot != -1) {
                EnchantmentScreenInteraction.fillEnchantingSlot(interactionManager, syncId, emptySlot, player)
                WiamUtil.autoEnchantSign = AutoEnchantSign.WAITING
                return
            }
            emptySlot = IntStream.range(2, screen.screenHandler.slots.size)
                .filter { i: Int -> (!WiamUtil.excludedItems.contains(screen.screenHandler.slots[i].stack.item)) && (screen.screenHandler.slots[i].stack.item.enchantability > 0) && (!screen.screenHandler.slots[i].stack.hasEnchantments()) && (screen.screenHandler.slots[i].stack.item === Items.BOOK) }
                .findFirst()
                .orElse(-1)
            if (emptySlot != -1) {
                EnchantmentScreenInteraction.fillEnchantingSlot(interactionManager, syncId, emptySlot, player)
                WiamUtil.autoEnchantSign = AutoEnchantSign.WAITING
                return
            }
            val slot = IntStream.range(2, screen.screenHandler.slots.size)
                .filter{ i: Int -> (screen.screenHandler.slots[i].stack.item.enchantability > 0) && (!screen.screenHandler.slots[i].stack.hasEnchantments()) }
                .findFirst()
                .orElse(-1)
            if (slot != -1) {
                if( screen.screenHandler.slots[1].stack.isEmpty) {
                    val slotOfLapis = IntStream
                        .range(2, screen.screenHandler.slots.size)
                        .filter { i: Int -> screen.screenHandler.slots[i].stack.item === Items.LAPIS_LAZULI }
                        .findFirst()
                        .orElse(-1)
                    if (slotOfLapis == -1) {
                        WiamUtil.autoEnchantSign = AutoEnchantSign.FINALLY
                        return
                    }
                    EnchantmentScreenInteraction.fillLapisSlot(interactionManager, syncId, slotOfLapis, player)
                }
                EnchantmentScreenInteraction.fillEnchantingSlot(interactionManager, syncId, slot, player)
                interactionManager.clickButton(syncId, 0)
                WiamUtil.autoEnchantSign = AutoEnchantSign.PROCESSING
                return
            }
            if ((screen.screenHandler.slots[0].stack.item.enchantability > 0) && (!screen.screenHandler.slots[0].stack.hasEnchantments())) {
                if( screen.screenHandler.slots[1].stack.isEmpty) {
                    val slotOfLapis = IntStream
                        .range(2, screen.screenHandler.slots.size)
                        .filter { i: Int -> screen.screenHandler.slots[i].stack.item === Items.LAPIS_LAZULI }
                        .findFirst()
                        .orElse(-1)
                    if (slotOfLapis == -1) {
                        WiamUtil.autoEnchantSign = AutoEnchantSign.FINALLY
                        return
                    }
                    EnchantmentScreenInteraction.fillLapisSlot(interactionManager, syncId, slotOfLapis, player)
                }
                
                interactionManager.clickButton(syncId, 0)
                WiamUtil.autoEnchantSign = AutoEnchantSign.PROCESSING
                return
            }
            WiamUtil.autoEnchantSign = AutoEnchantSign.FINALLY
            return
        }
        if (WiamUtil.autoEnchantSign == AutoEnchantSign.WAITING) {
            if (WiamUtil.counterOfWaitingForResult >= 5) {
                WiamUtil.counterOfWaitingForResult = 0
                WiamUtil.autoEnchantSign = AutoEnchantSign.FINALLY
                return
            }
            val wEnchantUnits = WEnchantUnit.of(
                player.world.registryManager.get(RegistryKeys.ENCHANTMENT).indexedEntries,
                screen.screenHandler.enchantmentId,
                screen.screenHandler.enchantmentLevel
            )
            
            if (wEnchantUnits.isEmpty()){
                WiamUtil.counterOfWaitingForResult++
                return
            }
            WiamUtil.counterOfWaitingForResult = 0
            
            
            var action = Action.EMPTY
            if (wEnchantUnits.size < 3) action = Action.LEVEL_1
            else for (it in rules) {
                val action1 = matchRule(it, wEnchantUnits[2])
                if (action1 == Action.INVALID) continue
                if (action1 == Action.LEVEL_3) {
                    action = Action.LEVEL_3
                    break
                }
                if (action1 == Action.LEVEL_1) {
                    action = Action.LEVEL_1
                    break
                }
            }
            if (action == Action.LEVEL_3) {
                var slotOfLapis: Int
                if (screen.screenHandler.slots[1].stack.count < 3) {
                    slotOfLapis = IntStream
                        .range(2, screen.screenHandler.slots.size)
                        .filter { i: Int -> screen.screenHandler.slots[i].stack.item === Items.LAPIS_LAZULI }
                        .findFirst()
                        .orElse(-1)
                    if (slotOfLapis == -1) {
                        WiamUtil.autoEnchantSign = AutoEnchantSign.FINALLY
                        return
                    }
                    EnchantmentScreenInteraction.fillLapisSlot(interactionManager, syncId, slotOfLapis, player)
                }
                if (screen.screenHandler.slots[1].stack.count < 3) {
                    slotOfLapis = IntStream
                        .range(2, screen.screenHandler.slots.size)
                        .filter { i: Int -> screen.screenHandler.slots[i].stack.item === Items.LAPIS_LAZULI }
                        .findFirst()
                        .orElse(-1)
                    if (slotOfLapis == -1) {
                        WiamUtil.autoEnchantSign = AutoEnchantSign.FINALLY
                        return
                    }
                    EnchantmentScreenInteraction.fillLapisSlot(interactionManager, syncId, slotOfLapis, player)
                }
                if (screen.screenHandler.slots[1].stack.count < 3) {
                    slotOfLapis = IntStream
                        .range(2, screen.screenHandler.slots.size)
                        .filter { i: Int -> screen.screenHandler.slots[i].stack.item === Items.LAPIS_LAZULI }
                        .findFirst()
                        .orElse(-1)
                    if (slotOfLapis == -1) {
                        WiamUtil.autoEnchantSign = AutoEnchantSign.FINALLY
                        return
                    }
                    EnchantmentScreenInteraction.fillLapisSlot(interactionManager, syncId, slotOfLapis, player)
                }
                if (screen.screenHandler.slots[1].stack.count < 3) {
                    WiamUtil.autoEnchantSign = AutoEnchantSign.FINALLY
                    return
                }
                interactionManager.clickButton(syncId, 2)
                WiamUtil.autoEnchantSign = AutoEnchantSign.PROCESSING
                return
            }
            WiamUtil.excludedItems.add(screen.screenHandler.slots[0].stack.item)
            WiamUtil.autoEnchantSign = AutoEnchantSign.FINISHING
            return
        }
        if (WiamUtil.autoEnchantSign == AutoEnchantSign.PROCESSING) {
            WiamUtil.excludedItems.clear()
            if (WiamUtil.counterOfWaitingForResult >= 5) {
                WiamUtil.counterOfWaitingForResult = 0
                WiamUtil.autoEnchantSign = AutoEnchantSign.FINALLY
                return
            }
            
            if (!screen.screenHandler.slots[0].stack.hasEnchantments() && screen.screenHandler.slots[0].stack.item != Items.ENCHANTED_BOOK){
                WiamUtil.counterOfWaitingForResult++
                return
            }
            WiamUtil.counterOfWaitingForResult = 0
            WiamUtil.autoEnchantSign = AutoEnchantSign.FINISHING
            return
        }
        if (WiamUtil.autoEnchantSign == AutoEnchantSign.FINALLY) {
            screen.close()
            WiamUtil.excludedItems.clear()
            WiamUtil.autoEnchantSign = AutoEnchantSign.FINISHING
            return
        }
    }
}