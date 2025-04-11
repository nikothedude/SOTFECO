package SOTFECO

import com.fs.starfarer.api.BaseModPlugin
import com.fs.starfarer.api.Global
import lunalib.lunaSettings.LunaSettings
import lunalib.lunaSettings.LunaSettingsListener

class SOFTECO_modPlugin: BaseModPlugin() {

    companion object {
        var lunaLibEnabled = false
        const val modId = "niko_SOTFMoreCombatObjectives"
    }

    override fun onApplicationLoad() {
        lunaLibEnabled = Global.getSettings().modManager.isModEnabled("lunalib")

        if (lunaLibEnabled) {
            LunaSettings.addSettingsListener(settingsChangedListener())
        }

        SOTFECO_settings.loadSettings()
    }

    class settingsChangedListener(): LunaSettingsListener {
        override fun settingsChanged(modID: String) {
            SOTFECO_settings.loadSettings()
        }
    }

}