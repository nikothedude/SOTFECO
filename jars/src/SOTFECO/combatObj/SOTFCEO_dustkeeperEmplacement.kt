package SOTFECO.combatObj

import SOTFECO.scripts.SOTFECO_flagScript
import com.fs.starfarer.api.Global
import com.fs.starfarer.api.util.Misc
import data.scripts.combat.obj.SotfEmplacementEffect
import SOTFECO.ReflectionUtils
import com.fs.starfarer.combat.entities.BattleObjective

class SOTFCEO_dustkeeperEmplacement: SotfEmplacementEffect() {

    companion object {
        fun announceBetrayal() {
            if (Global.getCombatEngine().customData["\$SOTFCEO_emplacementsBetrayed"] == true) return

            val defectString = "Alert: Dustkeeper emplacements activating and locking out controls"
            Global.getCombatEngine().combatUI.addMessage(
                1,
                Misc.getNegativeHighlightColor(), defectString
            )

            Global.getCombatEngine().customData["\$SOTFCEO_emplacementsBetrayed"] = true
        }
    }

    override fun advance(amount: Float) {
        super.advance(amount)

        val engine = Global.getCombatEngine()
        if (engine.isPaused) return

        if (SOTFECO_flagScript.dustKeepersHostile()) {
            if (objective.owner != 1) {
                announceBetrayal()

                val battleObjective: BattleObjective = objective as? BattleObjective ?: return

                ReflectionUtils.set("capProgress", battleObjective, 1001, BattleObjective::class.java)
                ReflectionUtils.set("capTime", battleObjective, 1000, BattleObjective::class.java)
                ReflectionUtils.set("capOwner", battleObjective, 1, BattleObjective::class.java)
            }
        }
    }
}