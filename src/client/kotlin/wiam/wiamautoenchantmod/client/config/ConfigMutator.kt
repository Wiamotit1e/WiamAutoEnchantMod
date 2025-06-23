package wiam.wiamautoenchantmod.client.config

import java.util.*

object ConfigMutator: IConfigMutator{
    private val configFileInteraction: IConfigFileInteraction = ConfigFileInteraction
    
    override fun toggleConfig(): Int {
        configFileInteraction.updateConfig { it.isThisOn = !it.isThisOn }
        return 1
    }
    
    override fun toggleConfig(state: Boolean): Int {
        configFileInteraction.updateConfig { it.isThisOn = state }
        return 1
    }
    
    override fun toggleRule(): Int {
        configFileInteraction.updateConfig { it.isRulesOn = !it.isRulesOn }
        return 1
    }
    
    override fun toggleRule(state: Boolean): Int {
        configFileInteraction.updateConfig { it.isRulesOn = state }
        return 1
    }
    
    override fun addRule(rule: EnchantRule): Int {
        configFileInteraction.updateConfig { it.rules.add(rule) }
        return 1
    }
    
    override fun removeRule(index: Int): Int {
        configFileInteraction.updateConfig { it.rules.removeAt(index) }
        return 1
    }
    
    override fun exchangeRule(index1: Int, index2: Int): Int {
        configFileInteraction.updateConfig { Collections.swap(it.rules, index1, index2) }
        return 1
    }
    
    override fun moveRule(fromIndex: Int, toIndex: Int): Int {
        configFileInteraction.updateConfig {
            if (fromIndex == toIndex) return@updateConfig
            val rules = it.rules
            val element = rules.removeAt(fromIndex)
            rules.add(toIndex, element)
        }
        return 1
    }
    
    override fun reset(): Int {
        configFileInteraction.updateConfig { it.resetToDefault() }
        return 1
    }
}