package wiam.wiamautoenchantmod.client

import net.minecraft.client.gui.screen.ingame.EnchantmentScreen
import net.minecraft.client.network.ClientPlayerInteractionManager
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKeys
import wiam.wiamautoenchantmod.client.config.Action
import wiam.wiamautoenchantmod.client.config.EnchantRule
import java.util.stream.IntStream

object AutoEnchanter : IAutoEnchanter {
    
    private fun matchRule(rule: EnchantRule, enchantUnit: WEnchantUnit, itemId: String): Action {
//        WiamUtil.logger.info(
//            "{} {} {}, -> {} {}",
//            rule,
//            enchantUnit,
//            itemId,
//            if (rule.isItemIdRegex) {
//                rule.itemId == ".*" || RegexCache.getRegex(rule.itemId).matches(itemId)
//            } else {
//                rule.itemId == itemId
//            },
//            if (rule.isEnchantmentIdRegex) {
//                rule.enchantmentId == ".*" || RegexCache.getRegex(rule.enchantmentId).matches(enchantUnit.id)
//            } else {
//                rule.enchantmentId == enchantUnit.id
//            }
//        )
        
        // 0. 验证物品ID
        val itemMatches = if (rule.isItemIdRegex) {
            rule.itemId == ".*" || RegexCache.getRegex(rule.itemId).matches(itemId)
        } else {
            rule.itemId == itemId
        }
        
        if (!itemMatches) return Action.INVALID
        // 1. 验证附魔ID
        val enchantmentMatches = if (rule.isEnchantmentIdRegex) {
            rule.enchantmentId == ".*" || RegexCache.getRegex(rule.enchantmentId).matches(enchantUnit.id)
        } else {
            rule.enchantmentId == enchantUnit.id
        }
        
        if (!enchantmentMatches) return Action.INVALID
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
            .filter { i: Int -> screen.screenHandler.slots[i].stack.isEnchantable && !screen.screenHandler.slots[i].stack.hasEnchantments() }
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
                .filter { i: Int -> (!WiamUtil.excludedItems.contains(screen.screenHandler.slots[i].stack.item)) && (screen.screenHandler.slots[i].stack.isEnchantable) && (!screen.screenHandler.slots[i].stack.hasEnchantments()) && (screen.screenHandler.slots[i].stack.item !== Items.BOOK) }
                .findFirst()
                .orElse(-1)
            if (emptySlot == -1) {
                emptySlot = IntStream.range(2, screen.screenHandler.slots.size)
                    .filter { i: Int -> (!WiamUtil.excludedItems.contains(screen.screenHandler.slots[i].stack.item)) && (screen.screenHandler.slots[i].stack.isEnchantable) && (!screen.screenHandler.slots[i].stack.hasEnchantments()) && (screen.screenHandler.slots[i].stack.item === Items.BOOK) }
                    .findFirst()
                    .orElse(-1)
            }
            if (emptySlot != -1) {
                EnchantmentScreenInteraction.fillEnchantingSlot(interactionManager, syncId, emptySlot, player)
                WiamUtil.autoEnchantSign = AutoEnchantSign.WAITING
                return
            }
            var slot = IntStream.range(2, screen.screenHandler.slots.size)
                .filter{ i: Int -> (screen.screenHandler.slots[i].stack.isEnchantable) && (!screen.screenHandler.slots[i].stack.hasEnchantments()) && (screen.screenHandler.slots[i].stack.item === Items.BOOK) }
                .findFirst()
                .orElse(-1)
            if (slot == -1) {
                slot = IntStream.range(2, screen.screenHandler.slots.size)
                    .filter{ i: Int -> (screen.screenHandler.slots[i].stack.isEnchantable) && (!screen.screenHandler.slots[i].stack.hasEnchantments()) && (screen.screenHandler.slots[i].stack.item !== Items.BOOK) }
                    .findFirst()
                    .orElse(-1)
            }
            if (slot != -1) {
                EnchantmentScreenInteraction.fillEnchantingSlot(interactionManager, syncId, slot, player)
                enchantLevel1(screen, interactionManager, syncId, player)
                return
            }
            if ((screen.screenHandler.slots[0].stack.isEnchantable) && (!screen.screenHandler.slots[0].stack.hasEnchantments())) {
                enchantLevel1(screen, interactionManager, syncId, player)
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
                player.world.registryManager.getOptional(RegistryKeys.ENCHANTMENT).get().indexedEntries,
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
            else {
                for (it in rules) {
                    val action1 = matchRule(it, wEnchantUnits[2], Registries.ITEM.getId(player.currentScreenHandler.slots[0].stack.item).toString())
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
            }
            if (action == Action.LEVEL_3) {
                enchantLevel3(screen, interactionManager, syncId, player)
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
    
    private fun enchantLevel3(screen: EnchantmentScreen, interactionManager: ClientPlayerInteractionManager, syncId: Int, player: PlayerEntity) {
        var slotOfLapis: Int
        repeat(3) {
            if (screen.screenHandler.slots[1].stack.count >= 3) return@repeat
            slotOfLapis = getSlotOfLapis(screen)
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
    
    private fun enchantLevel1(screen: EnchantmentScreen, interactionManager: ClientPlayerInteractionManager, syncId: Int, player: PlayerEntity) {
        if (screen.screenHandler.slots[1].stack.isEmpty) {
            val slotOfLapis = getSlotOfLapis(screen)
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
    
    private fun getSlotOfLapis(screen: EnchantmentScreen) = IntStream
        .range(2, screen.screenHandler.slots.size)
        .filter { i: Int -> screen.screenHandler.slots[i].stack.item === Items.LAPIS_LAZULI }
        .findFirst()
        .orElse(-1)
}