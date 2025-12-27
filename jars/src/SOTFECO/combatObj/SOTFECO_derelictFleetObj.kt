package SOTFECO.combatObj

import SOTFECO.augments.ASBPlatforms.Companion.OBJ_ID
import SOTFECO.combatObj.SOTFECO_derelictFleetObj.FleetMode.Companion.getFleetMode
import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.BattleAPI
import com.fs.starfarer.api.campaign.FactionAPI
import com.fs.starfarer.api.combat.BattleObjectiveAPI
import com.fs.starfarer.api.combat.BattleObjectiveEffect
import com.fs.starfarer.api.combat.CombatEngineAPI
import com.fs.starfarer.api.combat.ShipAPI
import com.fs.starfarer.api.impl.combat.BaseBattleObjectiveEffect
import com.fs.starfarer.combat.CombatEngine
import com.fs.starfarer.combat.entities.BattleObjective
import org.lazywizard.lazylib.MathUtils
import org.lwjgl.util.vector.Vector2f

class SOTFECO_derelictFleetObj: BaseBattleObjectiveEffect() {

    enum class FleetMode {
        FRIENDLY_TO_PLAYER {
            override fun getValidFactions(battle: BattleAPI): MutableList<FactionAPI> {
                val baseFacs = Global.getSector().allFactions.filter { it.isShowInIntelTab }.toMutableList()
                val finalFacs = ArrayList<FactionAPI>()
                outer@ for (fac in baseFacs) {
                    for (fleet in battle.playerSide) {
                        if (fleet.faction.isHostileTo(fac)) {
                            break@outer
                        }
                    }
                    for (fleet in battle.nonPlayerSide) {
                        if (!fleet.faction.isHostileTo(fac)) {
                            break@outer
                        }
                    }
                    finalFacs += fac
                }

                return finalFacs
            }

            override fun getBaseOrder(): Int {
                return 20
            }
        },
        FRIENDLY_TO_ENEMY {
            override fun getValidFactions(battle: BattleAPI): MutableList<FactionAPI> {
                val baseFacs = Global.getSector().allFactions.filter { it.isShowInIntelTab }.toMutableList()
                val finalFacs = ArrayList<FactionAPI>()
                outer@ for (fac in baseFacs) {
                    for (fleet in battle.playerSide) {
                        if (!fleet.faction.isHostileTo(fac)) {
                            break@outer
                        }
                    }
                    for (fleet in battle.nonPlayerSide) {
                        if (fleet.faction.isHostileTo(fac)) {
                            break@outer
                        }
                    }
                    finalFacs += fac
                }

                return finalFacs
            }

            override fun getBaseOrder(): Int {
                return 8
            }
        },
        OH_SHIT_OH_FUCK {
            override fun getValidFactions(battle: BattleAPI): MutableList<FactionAPI> {
                val baseFacs = Global.getSector().allFactions.filter { it.isShowInIntelTab }.toMutableList()
                val finalFacs = ArrayList<FactionAPI>()
                outer@ for (fac in baseFacs) {
                    for (fleet in battle.playerSide) {
                        if (!fleet.faction.isHostileTo(fac)) {
                            break@outer
                        }
                    }
                    for (fleet in battle.nonPlayerSide) {
                        if (!fleet.faction.isHostileTo(fac)) {
                            break@outer
                        }
                    }
                    finalFacs += fac
                }

                return finalFacs
            }

            override fun getBaseOrder(): Int {
                return 3
            }
        };

        fun getOrder(): Int {
            var order = getBaseOrder()
            val variation = MathUtils.getRandomNumberInRange(-10, 10)
            order += variation

            return order
        }

        open fun canBeUsed(battle: BattleAPI): Boolean {
            return getValidFactions(battle).isNotEmpty()
        }

        abstract fun getValidFactions(battle: BattleAPI): MutableList<FactionAPI>

        abstract fun getBaseOrder(): Int

        companion object {
            fun getFleetModeOrder(): MutableList<FleetMode> {
                val list = entries.sortedBy { it.getOrder() }.toMutableList()
                return list
            }

            fun getFleetMode(battle: BattleAPI): FleetMode? = getFleetModeOrder().firstOrNull { it.canBeUsed(battle) }
        }
    }

    var mode: FleetMode? = null
    val ships: MutableList<ShipAPI> = ArrayList()

    override fun init(engine: CombatEngineAPI?, objective: BattleObjectiveAPI?) {
        super.init(engine, objective)

        mode = getFleetMode(Global.getSector().playerFleet.battle)
        if (mode == null) {
            replace()
            return
        }

        spawnShips()
    }

    private fun spawnShips() {
        TODO("Not yet implemented")
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

    fun replace() {
        val engine = Global.getCombatEngine() as CombatEngine

        val newObj = BattleObjective(
            "nav_buoy",
            Vector2f(objective.location.x, objective.location.y),
            BattleObjectiveAPI.Importance.NORMAL
        )
        engine.addObject(newObj)
        engine.removeObject(objective)
    }
}