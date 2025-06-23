package wiam.wiamautoenchantmod.client.config

object ConfigService: IConfigFileInteraction by ConfigFileInteraction, IConfigActionLogger by ChatConfigActionLogger, IConfigProvider by ConfigFileInteraction, IConfigCommandInteraction {
    private val configMutator: IConfigMutator = ConfigMutator
    override fun showConfig(): Int {
        logConfigAction(ConfigAction.ShowConfig(getConfig()))
        return 1
    }
    
    override fun reloadConfig(): Int {
        logConfigAction(ConfigAction.ReloadConfig)
        loadConfigFile()
        return 1
    }
    
    override fun toggleConfig(): Int {
        logConfigAction(ConfigAction.SetConfigState(!getConfig().isThisOn))
        return configMutator.toggleConfig()
    }
    
    override fun toggleConfig(state: Boolean): Int {
        logConfigAction(ConfigAction.SetConfigState(state))
        return configMutator.toggleConfig(state)
    }
    
    override fun toggleRule(): Int {
        logConfigAction(ConfigAction.SetRulesState(!getConfig().isRulesOn))
        return configMutator.toggleRule()
    }
    
    override fun toggleRule(state: Boolean): Int {
        logConfigAction(ConfigAction.SetRulesState(state))
        return configMutator.toggleRule(state)
    }
    
    override fun addRule(rule: EnchantRule): Int {
        logConfigAction(ConfigAction.AddRule(rule))
        return configMutator.addRule(rule)
    }
    
    override fun removeRule(index: Int): Int {
        if ((getConfig().rules.size <= index) || (index < 0)) {
            logConfigAction(ConfigAction.IndexInvalid)
            return 0
        }
        logConfigAction(ConfigAction.RemoveRule(index, getConfig().rules[index]))
        return configMutator.removeRule(index)
    }
    
    override fun exchangeRule(index1: Int, index2: Int): Int {
        if ((getConfig().rules.size <= index1) || (getConfig().rules.size <= index2) || (index1 < 0) || (index2 < 0)) {
            logConfigAction(ConfigAction.IndexInvalid)
            return 0
        }
        logConfigAction(ConfigAction.ExchangeRules(index1, index2))
        return configMutator.exchangeRule(index1, index2)
    }
    
    override fun moveRule(fromIndex: Int, toIndex: Int): Int {
        if ((getConfig().rules.size <= fromIndex) || (getConfig().rules.size <= toIndex) || (fromIndex < 0) || (toIndex < 0)) {
            logConfigAction(ConfigAction.IndexInvalid)
            return 0
        }
        logConfigAction(ConfigAction.MoveRule(fromIndex, toIndex))
        return configMutator.moveRule(fromIndex, toIndex)
    }
    
    override fun reset(): Int {
        logConfigAction(ConfigAction.ResetConfig)
        return configMutator.reset()
    }
    
}