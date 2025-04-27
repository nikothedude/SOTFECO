package SOTFECO.combatObj

import com.fs.starfarer.api.impl.combat.threat.ConstructionSwarmSystemScript
import com.fs.starfarer.api.util.WeightedRandomPicker

class SOTFCEO_threatSkirmisherSwarm: SOTFCEO_threatConstructionSwarm() {

    override fun getToPickFrom(): Collection<ConstructionSwarmSystemScript.SwarmConstructableVariant> {
        return ConstructionSwarmSystemScript.CONSTRUCTABLE.filter { it.variantId.contains("skirmish") }.shuffled()
    }

}