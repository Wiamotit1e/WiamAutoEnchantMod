package wiam.wiamautoenchantmod.client.config

import com.google.gson.GsonBuilder
import net.fabricmc.loader.api.FabricLoader
import java.nio.file.Files

object ConfigFileInteraction : IConfigFileInteraction, IConfigProvider {
    private val CONFIG_PATH = FabricLoader.getInstance().configDir.resolve("wiamautoenchant.json")
    private val GSON = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
    private var currentConfig: Config = Config.DEFAULT
    
    override fun getConfig() = currentConfig.deepCopy()
    
    override fun loadConfigFile(): Config {
        return try {
            if (!Files.exists(CONFIG_PATH)) {
                currentConfig = Config.DEFAULT
                saveConfigFile(currentConfig) // 创建默认配置文件
                currentConfig
            } else {
                Files.newBufferedReader(CONFIG_PATH).use { reader ->
                    GSON.fromJson(reader, Config::class.java).also {
                        currentConfig = it
                    }
                }
            }
        } catch (e: Exception) {
            println("Failed to load config: ${e.message}")
            currentConfig = Config.DEFAULT
            currentConfig
        }
    }
    
    override fun saveConfigFile(config: Config) {
        try {
            Files.newBufferedWriter(CONFIG_PATH).use { writer ->
                GSON.toJson(config, writer)
            }
        } catch (e: Exception) {
            println("Failed to save config: ${e.message}")
        }
    }
    
    override fun updateConfig(action :(Config) -> Unit) {
        val newConfig = currentConfig.apply(action)
        saveConfigFile(newConfig)
        currentConfig = newConfig
    }
}