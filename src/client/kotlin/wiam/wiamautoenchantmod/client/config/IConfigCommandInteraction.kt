package wiam.wiamautoenchantmod.client.config

interface IConfigCommandInteraction: IConfigMutator {
    fun showConfig(): Int
    fun reloadConfig(): Int
}