package SOTFECO.augments

import SOTFECO.ReflectionUtils
import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.econ.Industry
import com.fs.starfarer.api.combat.BattleObjectiveAPI
import com.fs.starfarer.api.combat.ShipAPI
import com.fs.starfarer.api.impl.campaign.ids.Industries
import com.fs.starfarer.api.impl.campaign.intel.BaseIntelPlugin
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import com.fs.starfarer.combat.CombatEngine
import com.fs.starfarer.combat.entities.BattleObjective
import niko_SA.SA_settings
import niko_SA.augments.core.stationAttachment
import niko_SA.augments.shieldShunt.Companion.ARMOR_MULT
import niko_SA.augments.shieldShunt.Companion.HULL_MULT
import niko_SA.stringUtils.toPercent
import org.lwjgl.util.vector.Vector2f
import kotlin.math.absoluteValue

class ASBPlatforms: stationAttachment() {

    companion object {
        const val OBJ_ID = "sotfCEO_objective_ASB"

        const val EXTRA_Y_DISTANCE = 2500f
    }

    override fun applyInCombat(station: ShipAPI) {
        val toDeploy = getNumASBSToPlace()
        deployASBs(toDeploy, station)
    }

    private fun deployASBs(num: Int, station: ShipAPI) {
        val owner = station.owner
        val playerside = owner == 0
        val engine = Global.getCombatEngine() as CombatEngine
        val objectives: MutableSet<BattleObjective> = HashSet()
        var yDist = EXTRA_Y_DISTANCE + station.collisionRadius
        if (playerside) yDist = yDist * -1
        if (num <= 0) return
        if (num == 1) {
            val objective = BattleObjective(
                OBJ_ID,
                Vector2f(station.location.x, yDist),
                BattleObjectiveAPI.Importance.NORMAL
            )
            objectives += objective
            engine.addObject(objective)
        } else {
            val initialX = (engine.mapWidth * -0.12f)
            var currX = initialX
            val xOffset = (initialX) / (objectives.size - 1)

            var objsLeft = num
            while (objsLeft-- > 0) {
                val objective = BattleObjective(
                    OBJ_ID,
                    Vector2f(currX, yDist),
                    BattleObjectiveAPI.Importance.NORMAL
                )
                objectives += objective
                engine.addObject(objective)
                currX += xOffset
            }
        }

        finalizeDeployment(objectives, station, owner, playerside)
    }

    private fun finalizeDeployment(
        objectives: MutableSet<BattleObjective>,
        station: ShipAPI,
        owner: Int,
        playerside: Boolean
    ) {
        for (objective in objectives) {
            val existingTime = ReflectionUtils.get("capTime", objective, BattleObjective::class.java) as Float
            //val newTime = existingTime * 2f
            //ReflectionUtils.set("capTime", objective, newTime, BattleObjective::class.java)
            ReflectionUtils.set("capProgress", objective, existingTime + 1, BattleObjective::class.java)
            ReflectionUtils.set("capOwner", objective, owner, BattleObjective::class.java)
        }
    }

    override fun getUnavailableReason(): String? {
        val superValue = super.getUnavailableReason()
        if (superValue != null) return superValue

        val batteries = getBatteries()

        if (batteries == null) return "Market must have ground defenses or heavy batteries present"
        if (!batteries.isFunctional) return "Ground defenses non-functional"

        return null
    }

    override fun canBeRemoved(): Boolean {
        if (!super.canBeRemoved()) return false

        val batteries = getBatteries() ?: return true
        return batteries.isFunctional
    }

    private fun getBatteries(): Industry? {
        if (market == null) return null

        return market!!.getIndustry(Industries.HEAVYBATTERIES) ?: market!!.getIndustry(Industries.GROUNDDEFENSES)
    }

    fun getNumASBSToPlace(): Int {
        val ind = getBatteries() ?: return 0
        var base = 0

        if (ind.spec.hasTag(Industries.TAG_GROUNDDEFENSES)) base += 2
        if (ind.id == Industries.HEAVYBATTERIES) base += 1
        if (ind.isImproved) base += 1

        return base
    }

    override fun getBlueprintValue(): Int {
        return 20000
    }

    override fun getBasicDescription(tooltip: TooltipMakerAPI, expanded: Boolean) {
        super.getBasicDescription(tooltip, expanded)

        tooltip.addPara(
            "Deploys a number of %s around the station that coordinate fire from planetary ground defenses into orbit.",
            5f,
            Misc.getHighlightColor(),
            "ASB uplinks"
        )
        tooltip.addPara(
            "The uplinks are placed %s, are %s by the station's side. They %s by the opposing side, however.",
            5f,
            Misc.getHighlightColor(),
            "behind the station",
            "owned",
            "can be captured"
        )


        tooltip.addPara(
            "The number of uplinks is dependent on the effectiveness of the market's %s. %s provide more, as does an %s installation.",
            10f,
            Misc.getHighlightColor(),
            "ground defenses", "Heavy batteries", "improved"
        ).setHighlightColors(
            Misc.getHighlightColor(),
            Misc.getHighlightColor(),
            Misc.getStoryOptionColor()
        )
        if (market != null && applied) {
            tooltip.setBulletedListMode(BaseIntelPlugin.BULLET)
            val batteries = getBatteries()
            if (batteries == null) {
                tooltip.addPara("No ground defenses are present.", 5f).color = Misc.getNegativeHighlightColor()
            } else {
                tooltip.addPara("Currently coordinating %s.", 5f, Misc.getPositiveHighlightColor(), batteries.currentName)
                val numASBs = getNumASBSToPlace()
                tooltip.addPara("Managing %s ASB uplinks", 5f, Misc.getPositiveHighlightColor(), numASBs.toString())
            }
            tooltip.setBulletedListMode(null)
        }

        tooltip.addPara("Cannot be removed if ground defenses are currently disrupted.", 5f).color = Misc.getGrayColor()
    }
}