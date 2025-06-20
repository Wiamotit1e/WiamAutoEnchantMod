package wiam.wiamautoenchantmod.client

import net.minecraft.enchantment.Enchantment
import net.minecraft.item.ItemStack
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.collection.IndexedIterable

data class WEnchantUnit(val id:String, val level:Int) {
    
    companion object{
        fun of(item: ItemStack): List<WEnchantUnit> {
            return item.enchantments
                .enchantmentEntries
                .stream()
                .map {
                    return@map WEnchantUnit(it.key.idAsString, it.intValue)
                }
                .toList()
        }
        
        fun of(
            indexedIterable: IndexedIterable<RegistryEntry<Enchantment>>,
            enchantmentId: IntArray,
            enchantmentLevel: IntArray
        ): List<WEnchantUnit> {
            val list = ArrayList<WEnchantUnit>()
            // 遍历有效索引（通常0..2）
            for (i in enchantmentId.indices) {
                if (enchantmentId[i] == -1) continue
                val entry = indexedIterable.get(enchantmentId[i])
                list.add(WEnchantUnit(entry?.idAsString!!, enchantmentLevel[i]))
            }
            return list
        }
    }
    
}