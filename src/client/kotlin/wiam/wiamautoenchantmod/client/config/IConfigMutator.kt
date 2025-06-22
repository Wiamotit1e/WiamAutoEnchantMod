package wiam.wiamautoenchantmod.client.config

interface IConfigMutator {
    fun toggleConfig(): Int
    fun toggleConfig(state: Boolean): Int
    fun toggleRule(): Int
    fun toggleRule(state: Boolean): Int
    fun addRule(rule: EnchantRule): Int
    fun removeRule(index: Int): Int
    fun exchangeRule(index1: Int, index2: Int): Int
    fun reset(): Int
}