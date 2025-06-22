package wiam.wiamautoenchantmod.client

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess
import wiam.wiamautoenchantmod.client.config.ConfigService
import wiam.wiamautoenchantmod.client.config.EnchantRule

class WiamautoenchantmodClient : ClientModInitializer {
    
    override fun onInitializeClient() {
        WiamUtil.logger.info("[Wiamautoenchantmod] Initialized")
        ConfigService.loadConfigFile()
        
        ClientCommandRegistrationCallback.EVENT.register{ dispatcher: CommandDispatcher<FabricClientCommandSource>?, registryAccess: CommandRegistryAccess? ->
            dispatcher?.register(ClientCommandManager.literal("autoenchant")
                .then(ClientCommandManager.literal("config")
                    .then(ClientCommandManager.literal("toggle_config")
                        .executes {
                            ConfigService.toggleConfig()
                        } // 无参数切换
                        .then(ClientCommandManager.argument("isConfigOn", BoolArgumentType.bool()) // 带参数设置
                            .executes {
                                ConfigService.toggleConfig(BoolArgumentType.getBool(it, "isConfigOn"))
                            }
                        )
                    )
                    .then(ClientCommandManager.literal("toggle_rule")
                        .executes {
                            ConfigService.toggleRule()
                        } // 无参数切换
                        .then(ClientCommandManager.argument("isRuleOn", BoolArgumentType.bool()) // 带参数设置
                            .executes { ConfigService.toggleRule(BoolArgumentType.getBool(it, "isRuleOn")) }))
                    .then(ClientCommandManager.literal("reset")
                        .executes {
                            ConfigService.reset()
                        }
                    )
                    .then(ClientCommandManager.literal("remove_rule")
                        .then(ClientCommandManager.argument("index", IntegerArgumentType.integer())
                            .executes { ConfigService.removeRule(IntegerArgumentType.getInteger(it, "index")
                            )
                            }
                        )
                    )
                    .then(ClientCommandManager.literal("exchange_rule")
                        .then(ClientCommandManager.argument("index1", IntegerArgumentType.integer())
                            .then(ClientCommandManager.argument("index2", IntegerArgumentType.integer())
                                .executes {
                                    ConfigService.exchangeRule(
                                        IntegerArgumentType.getInteger(it, "index1"),
                                        IntegerArgumentType.getInteger(it, "index2")
                                    )
                                }
                            )
                        )
                    )
                    .then(ClientCommandManager.literal("add_rule")
                        .then(ClientCommandManager.argument("item_id", StringArgumentType.string())
                            .then(ClientCommandManager.argument("enchantment_id", StringArgumentType.string())
                                .then(ClientCommandManager.argument("level", StringArgumentType.string())
                                    .then(ClientCommandManager.argument("action", ActionArgumentType.action())
                                        .then(ClientCommandManager.argument("is_enchantment_id_regex", BoolArgumentType.bool())
                                            .then(ClientCommandManager.argument("is_item_id_regex", BoolArgumentType.bool())
                                                .executes {
                                                    ConfigService.addRule(
                                                        EnchantRule(
                                                            itemId = StringArgumentType.getString(it, "item_id"),
                                                            enchantmentId = StringArgumentType.getString(it, "enchantment_id"),
                                                            level = StringArgumentType.getString(it, "level"),
                                                            action = ActionArgumentType.getAction(it, "action"),
                                                            isEnchantmentIdRegex = BoolArgumentType.getBool(it, "is_enchantment_id_regex"),
                                                            isItemIdRegex = BoolArgumentType.getBool(it, "is_item_id_regex")
                                                        )
                                                    )
                                                }
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                    .then(ClientCommandManager.literal("show_config")
                        .executes {
                            ConfigService.showConfig()
                        }
                    )
                )
            )
        }
    }
}
//data class EnchantRule(val itemId:String, val enchantmentId:String, val level:String, val action: Action, val isEnchantmentIdRegex: Boolean = false, val isItemIdRegex: Boolean = true)