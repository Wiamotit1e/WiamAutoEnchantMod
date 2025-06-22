package wiam.wiamautoenchantmod.client.config

interface IConfigFileInteraction {
    fun loadConfigFile(): Config
    fun saveConfigFile(config: Config)
    fun updateConfig(action: (Config) -> Unit)
}