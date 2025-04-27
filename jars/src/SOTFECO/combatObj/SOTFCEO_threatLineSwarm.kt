package SOTFECO.combatObj

import com.fs.starfarer.api.impl.combat.threat.ConstructionSwarmSystemScript

class SOTFCEO_threatLineSwarm: SOTFCEO_threatConstructionSwarm() {

    override fun getToPickFrom(): Collection<ConstructionSwarmSystemScript.SwarmConstructableVariant> {
        return ConstructionSwarmSystemScript.CONSTRUCTABLE.filter { it.variantId.contains("standoff") }.shuffled()
    }

}