package wiam.wiamautoenchantmod.client.config

sealed class ConfigAction {
    // 开关操作
    data class SetConfigState(val state: Boolean) : ConfigAction()
    data class SetRulesState(val state: Boolean) : ConfigAction()
    // 规则管理
    data class AddRule(val rule: EnchantRule) : ConfigAction()
    data class RemoveRule(val index: Int, val rule: EnchantRule) : ConfigAction()
    data class ExchangeRules(val index1: Int, val index2: Int) : ConfigAction()
    data object IndexInvalid: ConfigAction()
    data class ShowConfig(val config: Config) : ConfigAction()
    // 系统操作
    data object ResetConfig : ConfigAction()
}