package wiam.wiamautoenchantmod.client.config

data class Config(
    var isThisOn: Boolean,
    var isRulesOpen: Boolean,
    var rules: List<EnchantRule>
) {
    companion object {
        val DEFAULT = Config(
            isThisOn = false,
            isRulesOpen = false,
            rules = EnchantRule.DEFAULT
        )
    }
}