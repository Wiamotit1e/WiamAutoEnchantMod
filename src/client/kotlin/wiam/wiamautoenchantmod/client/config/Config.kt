package wiam.wiamautoenchantmod.client.config

data class Config(var isThisOn: Boolean, var isRulesOn : Boolean, var rules: MutableList<EnchantRule>) {
    
    fun deepCopy() = copy(
        rules = rules.map { it.copy() }.toMutableList()
    )
    
    fun resetToDefault() {
        this.isThisOn = DEFAULT.isThisOn
        this.isRulesOn = DEFAULT.isRulesOn
        this.rules = DEFAULT.rules
    }
    
    companion object {
        val DEFAULT = Config(isThisOn = false, isRulesOn = false, rules = EnchantRule.DEFAULT)
    }
}