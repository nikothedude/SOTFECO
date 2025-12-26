package SOTFECO.combatObj

import com.fs.starfarer.api.combat.BattleObjectiveEffect
import com.fs.starfarer.api.combat.ShipAPI
import com.fs.starfarer.api.impl.combat.BaseBattleObjectiveEffect

class SOTFCEO_derelictShipsObj: BaseBattleObjectiveEffect() {

    companion object {
        const val MAX_DP_PERCENT = 0.1f // of total battle size


    }

    override fun advance(amount: Float) {
        TODO("Not yet implemented")
    }

    override fun getStatusItemsFor(ship: ShipAPI?): List<BattleObjectiveEffect.ShipStatusItem?>? {
        TODO("Not yet implemented")
    }

    override fun getLongDescription(): String? {
        TODO("Not yet implemented")
    }
}