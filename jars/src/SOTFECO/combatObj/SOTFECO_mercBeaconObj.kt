package SOTFECO.combatObj

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.combat.BattleObjectiveEffect
import com.fs.starfarer.api.combat.ShipAPI
import com.fs.starfarer.api.impl.campaign.ids.Factions
import com.fs.starfarer.api.impl.campaign.ids.Skills
import com.fs.starfarer.api.impl.combat.BaseBattleObjectiveEffect
import com.fs.starfarer.api.util.WeightedRandomPicker

class SOTFECO_mercBeaconObj: BaseBattleObjectiveEffect() {

    companion object {
        // low bc we smod them and give officers and high cr
        const val MIN_DP = 40f
        const val MAX_DP = 60f

        val hullIdToWeights = mutableMapOf(
            Pair("shade", 10f),
            Pair("afflictor", 10f),
            Pair("harbinger", 5f),
            Pair("doom", 5f),
        )

        fun getPicker(): WeightedRandomPicker<String> {
            val picker = WeightedRandomPicker<String>()
            hullIdToWeights.forEach { picker.add(it.key, it.value) }

            return picker
        }

        fun modifyShip(ship: ShipAPI) {
            val newOfficer = Global.getSector().getFaction(Factions.MERCENARY).createRandomPerson()
            newOfficer.stats.level = 7

            newOfficer.stats.setSkillLevel(Skills.FIELD_MODULATION, 2f)
            newOfficer.stats.setSkillLevel(Skills.COMBAT_ENDURANCE, 2f)
            newOfficer.stats.setSkillLevel(Skills.TARGET_ANALYSIS, 2f)

            newOfficer.stats.setSkillLevel(Skills.HELMSMANSHIP, 1f)
            newOfficer.stats.setSkillLevel(Skills.SYSTEMS_EXPERTISE, 1f)
            newOfficer.stats.setSkillLevel(Skills.ENERGY_WEAPON_MASTERY, 1f)
            newOfficer.stats.setSkillLevel(Skills.DAMAGE_CONTROL, 2f)

            ship.captain = newOfficer

            ship.currentCR = 1f
        }
    }

    override fun advance(amount: Float) {
    }

    override fun getStatusItemsFor(ship: ShipAPI?): List<BattleObjectiveEffect.ShipStatusItem?>? {
        return null
    }

    override fun getLongDescription(): String? {
        TODO("Not yet implemented")
    }
}