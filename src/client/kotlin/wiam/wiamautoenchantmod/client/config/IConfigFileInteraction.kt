package wiam.wiamautoenchantmod.client.config

interface IConfigFileInteraction {
    fun getConfig(): Config
    fun loadConfigFile(): Config
    fun saveConfigFile()
}