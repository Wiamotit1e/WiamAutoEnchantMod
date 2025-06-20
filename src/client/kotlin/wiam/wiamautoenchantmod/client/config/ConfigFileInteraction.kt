package wiam.wiamautoenchantmod.client.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.fabricmc.loader.api.FabricLoader
import java.nio.file.Files
import java.nio.file.Path

object ConfigFileInteraction : IConfigFileInteraction {
    val CONFIG_PATH: Path = FabricLoader.getInstance().configDir.resolve("wiamautoenchant.json")
    val GSON: Gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
    private var currentConfig: Config = Config.DEFAULT
    
    override fun getConfig() = currentConfig.copy()
    
    override fun loadConfigFile(): Config {
        return try {
            if (!Files.exists(CONFIG_PATH)) {
                saveConfigFile() // 创建默认配置文件
                Config.DEFAULT
            } else {
                Files.newBufferedReader(CONFIG_PATH).use { reader ->
                    GSON.fromJson(reader, Config::class.java).also {
                        currentConfig = it
                    }
                }
            }
        } catch (e: Exception) {
            println("Failed to load config: ${e.message}")
            Config.DEFAULT
        }
    }
    
    override fun saveConfigFile() {
        try {
            Files.newBufferedWriter(CONFIG_PATH).use { writer ->
                GSON.toJson(currentConfig, writer)
            }
        } catch (e: Exception) {
            println("Failed to save config: ${e.message}")
        }
    }
    
    // 辅助方法：更新当前配置
    fun updateConfig(newConfig: Config) {
        currentConfig = newConfig
    }
}