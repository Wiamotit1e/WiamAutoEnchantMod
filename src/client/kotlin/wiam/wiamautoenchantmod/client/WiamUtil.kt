package wiam.wiamautoenchantmod.client

import net.minecraft.item.Item
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object WiamUtil {
    
    var counterOfWaitingForResult = 0
    
    var autoEnchantSign = AutoEnchantSign.FINISHING
    
    var logger: Logger = LoggerFactory.getLogger("wiamautoenchantmod")
    
    var excludedItems = linkedSetOf<Item>()
}