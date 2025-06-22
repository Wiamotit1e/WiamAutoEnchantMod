package wiam.wiamautoenchantmod.client

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import wiam.wiamautoenchantmod.client.config.Action
import java.util.concurrent.CompletableFuture

class ActionArgumentType private constructor() : ArgumentType<Action> {
    
    private val EXAMPLES = listOf("LEVEL_1", "LEVEL_3")
    
    override fun parse(p0: StringReader): Action {
        val string = p0.readString()
        return when (string) {
            "LEVEL_1" -> { Action.LEVEL_1 }
            "LEVEL_3" -> { Action.LEVEL_3 }
            else -> { throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().create() }
        }
    }
    
    override fun getExamples(): Collection<String> {
        return EXAMPLES
    }
    override fun <S> listSuggestions(context: CommandContext<S>?, builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
        if ("LEVEL_1".startsWith(builder.remainingLowerCase)) {
            builder.suggest("LEVEL_1")
        }
        
        if ("LEVEL_3".startsWith(builder.remainingLowerCase)) {
            builder.suggest("LEVEL_3")
        }
        
        return builder.buildFuture()
    }
    
    
    companion object {
        fun action() = ActionArgumentType()
        fun getAction(context: CommandContext<FabricClientCommandSource>, name: String) = context.getArgument(name, Action::class.java)
    }
}