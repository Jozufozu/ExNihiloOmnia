package com.jozufozu.exnihiloomnia.common.items.tools

import com.google.gson.JsonObject
import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.items.ItemBaseTool
import com.jozufozu.exnihiloomnia.common.registries.RegistryLoader
import net.minecraft.client.resources.I18n
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.*

class ItemMesh(registryName: ResourceLocation, material: ToolMaterial) : ItemBaseTool(registryName, material), IMesh {
    private var effectiveness: HashMap<String, Float>

    init {
        loadMeshTable()
        effectiveness = masterTable.getOrDefault(registryName.resourcePath, HashMap())
    }

    @SideOnly(Side.CLIENT)
    override fun addInformation(stack: ItemStack, worldIn: World?, tooltip: MutableList<String>, flagIn: ITooltipFlag) {
        super.addInformation(stack, worldIn, tooltip, flagIn)

        tooltip.add(I18n.format("exnihiloomnia.drops.effective_list"))
        for ((type, value) in effectiveness) {
            val translationKey = "exnihiloomnia.drops.type.$type"
            var translate = I18n.format(translationKey)

            if (translate == translationKey)
                translate = type
            tooltip.add(I18n.format("exnihiloomnia.drops.effective_entry", translate, value))
        }
    }

    override fun getEffectivenessForType(type: String) = effectiveness.getOrDefault(type, 1.0f)

    companion object {
        lateinit var masterTable: HashMap<String, HashMap<String, Float>>

        fun loadMeshTable() {
            if (::masterTable.isInitialized) return

            RegistryLoader.copySingle("/registries/mesh.json")
            RegistryLoader.loadSingleJson("/registries/mesh.json") { mesh: JsonObject ->
                val table = HashMap<String, HashMap<String, Float>>()

                val entries = mesh.entrySet()

                for (tools in entries) {
                    val toolsValue = tools.value
                    if (!toolsValue.isJsonObject) continue

                    val toolName = tools.key

                    val effectives = HashMap<String, Float>()

                    for (effectiveness in toolsValue.asJsonObject.entrySet()) {
                        val type = effectiveness.key

                        try {
                            val aFloat = effectiveness.value.asJsonPrimitive.asFloat
                            effectives[type] = aFloat
                        } catch (e: Exception) {
                            ExNihilo.log.error("Could not read effectiveness for '$toolName', '$type'")
                        }

                    }
                    table[toolName] = effectives
                }

                masterTable = table
            }
        }
    }
}
