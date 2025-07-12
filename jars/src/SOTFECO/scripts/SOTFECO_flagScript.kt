package SOTFECO.scripts

import SOTFECO.SOTFECO_settings
import SOTFECO.SOTFECO_settings.IGNORE_PREVIOUS_ENCOUNTER_REQS
import com.fs.starfarer.api.EveryFrameScript
import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.*
import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.characters.AbilityPlugin
import com.fs.starfarer.api.characters.PersonAPI
import com.fs.starfarer.api.combat.EngagementResultAPI
import com.fs.starfarer.api.util.IntervalUtil
import com.fs.starfarer.api.util.Misc
import data.scripts.campaign.ids.SotfIDs
import java.awt.Container

class SOTFECO_flagScript: EveryFrameScript, CampaignEventListener {

    companion object {
        fun playerInAbyss(): Boolean {
            val playerFleet = Global.getSector().playerFleet ?: return false
            if (!playerFleet.isInHyperspace) return false

            val abyss = Misc.getHyperspaceTerrainPlugin()?.abyssPlugin ?: return false
            return abyss.isInAbyss(playerFleet)
        }

        fun checkIntrusionNodeGenericFlag() {
            SOTFECO_settings.toggleFlag("\$SOTFECO_canSpawnIntrusionNode", canSpawnGenericIntrusionNode())
        }

        fun canSpawnGenericIntrusionNode(): Boolean {
            if (Global.getSector().memoryWithoutUpdate.getBoolean("\$SOTFECO_debugGenericIntrusionNode")) return true
            if (dustKeepersHostile()) return false
            if (!inProxyLocation()) return false

            return false
        }

        fun dustKeepersHostile(): Boolean {
            if (Global.getSector().memoryWithoutUpdate.contains(SotfIDs.MEM_DUSTKEEPER_HATRED)) return true
            if (Global.getSector().getFaction(SotfIDs.DUSTKEEPERS).relToPlayer.isHostile) return true

            return false
        }

        fun inProxyLocation(playerLocation: LocationAPI? = Global.getSector()?.playerFleet?.containingLocation): Boolean {
            if (playerLocation == null) return false
            for (market in Global.getSector().economy.getMarkets(playerLocation)) {
                if (market.hasCondition(SotfIDs.CONDITION_PROXYPATROLS) && market.getCondition(SotfIDs.CONDITION_PROXYPATROLS).plugin.showIcon()) return true
            }
            return false
        }
    }

    val interval = IntervalUtil(0.2f, 0.3f) // days

    override fun isDone(): Boolean = false

    override fun runWhilePaused(): Boolean = false
    override fun advance(amount: Float) {
        val days = Misc.getDays(amount)
        interval.advance(days)
        if (interval.intervalElapsed()) {
            updateAbyssFlag()
            updateMonsterFlag()
            checkIntrusionNodeGenericFlag()
        }
    }

    private fun updateMonsterFlag() {
        SOTFECO_settings.toggleFlag("\$SOTF_ECOencounteredDweller", IGNORE_PREVIOUS_ENCOUNTER_REQS || Global.getSector().playerMemoryWithoutUpdate.getBoolean("\$encounteredDweller"))
        SOTFECO_settings.toggleFlag("\$SOTF_ECOencounteredThreat", IGNORE_PREVIOUS_ENCOUNTER_REQS || Global.getSector().playerMemoryWithoutUpdate.getBoolean("\$encounteredThreat"))
    }

    private fun updateAbyssFlag() {
        SOTFECO_settings.toggleFlag("\$SOTFECO_playerInAbyss", playerInAbyss())
    }

    override fun reportPlayerOpenedMarket(market: MarketAPI?) {
        return
    }

    override fun reportPlayerClosedMarket(market: MarketAPI?) {
        return
    }

    override fun reportPlayerOpenedMarketAndCargoUpdated(market: MarketAPI?) {
        return
    }

    override fun reportEncounterLootGenerated(
        plugin: FleetEncounterContextPlugin?,
        loot: CargoAPI?
    ) {
        return
    }

    override fun reportPlayerMarketTransaction(transaction: PlayerMarketTransaction?) {
        return
    }

    override fun reportBattleOccurred(
        primaryWinner: CampaignFleetAPI?,
        battle: BattleAPI?
    ) {
        return
    }

    override fun reportBattleFinished(
        primaryWinner: CampaignFleetAPI?,
        battle: BattleAPI?
    ) {
        return
    }

    override fun reportPlayerEngagement(result: EngagementResultAPI?) {
        return
    }

    override fun reportFleetDespawned(
        fleet: CampaignFleetAPI?,
        reason: CampaignEventListener.FleetDespawnReason?,
        param: Any?
    ) {
        return
    }

    override fun reportFleetSpawned(fleet: CampaignFleetAPI?) {
        return
    }

    override fun reportFleetReachedEntity(
        fleet: CampaignFleetAPI?,
        entity: SectorEntityToken?
    ) {
        return
    }

    override fun reportFleetJumped(
        fleet: CampaignFleetAPI?,
        from: SectorEntityToken?,
        to: JumpPointAPI.JumpDestination?
    ) {
        if (fleet != Global.getSector().playerFleet) return
        checkIntrusionNodeGenericFlag()
        updateAbyssFlag()
    }

    override fun reportShownInteractionDialog(dialog: InteractionDialogAPI?) {
        return
    }

    override fun reportPlayerReputationChange(faction: String?, delta: Float) {
        if (faction == null) return
        if (faction == SotfIDs.DUSTKEEPERS) checkIntrusionNodeGenericFlag()
    }

    override fun reportPlayerReputationChange(person: PersonAPI?, delta: Float) {
        return
    }

    override fun reportPlayerActivatedAbility(
        ability: AbilityPlugin?,
        param: Any?
    ) {
        return
    }

    override fun reportPlayerDeactivatedAbility(
        ability: AbilityPlugin?,
        param: Any?
    ) {
        return
    }

    override fun reportPlayerDumpedCargo(cargo: CargoAPI?) {
        return
    }

    override fun reportPlayerDidNotTakeCargo(cargo: CargoAPI?) {
        return
    }

    override fun reportEconomyTick(iterIndex: Int) {
        return
    }

    override fun reportEconomyMonthEnd() {
        return
    }
}