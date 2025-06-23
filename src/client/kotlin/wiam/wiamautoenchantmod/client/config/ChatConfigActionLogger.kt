package wiam.wiamautoenchantmod.client.config

import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.util.stream.IntStream

object ChatConfigActionLogger: IConfigActionLogger {
    
    private val player: ClientPlayerEntity?
        get() = MinecraftClient.getInstance().player
    
    override fun logConfigAction(action: ConfigAction) {
        when (action) {
            is ConfigAction.SetConfigState -> sendMessage("Config state changed to ${action.state}")
            is ConfigAction.SetRulesState -> sendMessage("Rules state changed to ${action.state}")
            
            is ConfigAction.AddRule -> sendMessage("Rule ${action.rule} was added")
            is ConfigAction.RemoveRule -> sendMessage("Rule ${action.rule} was removed")
            is ConfigAction.ExchangeRules -> sendMessage("Rules at positions ${action.index1} and ${action.index2} were swapped")
            
            ConfigAction.ResetConfig -> sendMessage("Config was resetted to default")
            ConfigAction.IndexInvalid -> sendMessage("Index was too invalid!")
            is ConfigAction.ShowConfig -> {
                sendMessage("Config state: ${action.config.isThisOn}  Rule state: ${action.config.isRulesOn}")
                IntStream.range(0, action.config.rules.size).forEach {sendMessage("Rule" + it +  ": ${action.config.rules[it]}") }
            }
            
            ConfigAction.ReloadConfig -> sendMessage("Config reloading was executed")
            is ConfigAction.MoveRule -> sendMessage("Rules at positions ${action.fromIndex} was moved to ${action.toIndex}")
        }
    }
    
    private fun sendMessage(msg: String) = player?.sendMessage(Text.literal(msg).formatted(Formatting.BLUE).formatted(Formatting.BOLD), false)
}