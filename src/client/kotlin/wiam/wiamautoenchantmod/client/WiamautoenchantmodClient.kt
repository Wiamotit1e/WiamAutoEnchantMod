package wiam.wiamautoenchantmod.client

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.registry.RegistryKeys
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import wiam.wiamautoenchantmod.client.config.Config
import wiam.wiamautoenchantmod.client.config.ConfigFileInteraction

class WiamautoenchantmodClient : ClientModInitializer {
    
    override fun onInitializeClient() {
        WiamUtil.logger.info("[Wiamautoenchantmod] Initialized")
        ConfigFileInteraction.loadConfigFile()
        
        ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher: CommandDispatcher<FabricClientCommandSource?>, registryAccess: CommandRegistryAccess? ->
            dispatcher.register(
                ClientCommandManager.literal("loadautoenchantconfig")
                    .executes { x: CommandContext<FabricClientCommandSource> ->
                        val config: Config = ConfigFileInteraction.loadConfigFile()
                        if (x.source.client.player != null) {
                            x.source.client.player!!.sendMessage(
                                Text.translatable("message.wiamautoenchantmod.config.loaded")
                                    .formatted(Formatting.BLUE), false
                            )
                            x.source.client.player!!.sendMessage(
                                Text.literal(config.toString())
                                    .formatted(Formatting.BLUE), false
                            )
                        }
                        1
                    })
        })
        ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher: CommandDispatcher<FabricClientCommandSource?>, registryAccess: CommandRegistryAccess? ->
            dispatcher.register(
                ClientCommandManager.literal("listallidofenchantmentandenchant")
                    .executes { x: CommandContext<FabricClientCommandSource> ->
                        if (x.source.client.player != null) {
                            val indexedEntries = x.source.world.registryManager.get(RegistryKeys.ENCHANTMENT).indexedEntries
                            var i = 0
                            while (i < indexedEntries.size()) {
                                x.source.client.player!!.sendMessage(
                                    Text.literal(indexedEntries[i]?.idAsString + " -> " + (indexedEntries[i]?.value()?.description?.string))
                                        .formatted(Formatting.BLUE), false
                                )
                                i++
                            }
                        }
                        1
                    })
        })
        
    }
}
