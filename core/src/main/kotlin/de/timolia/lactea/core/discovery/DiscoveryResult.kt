package de.timolia.lactea.core.discovery

data class DiscoveryResult(val all: Collection<DiscoveryClass>) {
    fun requireExactlyOne(name: String): DiscoveryClass {
        check(all.size == 1) {
            ("Require exactly one $name Candidates are: $all")
        }
        return all.iterator().next()
    }
}
