package wiam.wiamautoenchantmod.client


object RegexCache {
    private val cache = mutableMapOf<String, Regex>()
    
    fun getRegex(pattern: String): Regex = cache.getOrPut(pattern) {
        return try {
            pattern.toRegex() // 作为最后一行自动返回
        } catch (e: Exception) {
            WiamUtil.logger.error("Invalid regex: '$pattern'", e)
            Regex("(?!)") // 永远不匹配的容错正则
        }
    }
}