package SOTFECO

import SOTFECO.SOFTECO_modPlugin.Companion.modId
import com.fs.starfarer.api.Global
import data.scripts.SotfModPlugin
import lunalib.lunaSettings.LunaSettings
import org.lazywizard.lazylib.ext.json.iterator

object SOTFECO_settings {

    //var ABYSSAL_LIGHT_OBJ_ENABLED = true
    var DO_PLAYER_ASB_WARNING = true
    var ENABLE_FAB_OBJECTIVE = false

    fun loadSettings() {
        DO_PLAYER_ASB_WARNING = LunaSettings.getBoolean(modId, "SOTFECO_doPlayerASBWarning")!!
        ENABLE_FAB_OBJECTIVE = LunaSettings.getBoolean(modId, "SOTFECO_enableFabObjective")!!
        if (ENABLE_FAB_OBJECTIVE) {
            Global.getSector()?.memoryWithoutUpdate?.set("\$SOTFECO_enableFabObjective", true)
        } else {
            Global.getSector()?.memoryWithoutUpdate?.unset("\$SOTFECO_enableFabObjective")
        }
        /*ABYSSAL_LIGHT_OBJ_ENABLED = LunaSettings.getBoolean(modId,"SOTFECO_enableAbyssalLightObjs")!!

        if (!ABYSSAL_LIGHT_OBJ_ENABLED) {
            var targetIndex = 0
            var foundTarget = false
            var i = -1
            for (entry in SotfModPlugin.OBJECTIVE_DATA) {
                i++
                if (entry["id"] == "softCEO_hyperspace_dweller_beacon") {
                    foundTarget = true
                    targetIndex = i
                    break
                }
            }

            if (foundTarget) {
                SotfModPlugin.OBJECTIVE_DATA.remove(targetIndex)
            }
        }*/
    }

}
