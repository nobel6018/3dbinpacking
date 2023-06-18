package me.leedo.binpacking3d.domain

class Bin(
    val name: String,
    val width: Double,
    val height: Double,
    val depth: Double,
    val maxWeight: Double,

    val items: MutableList<Item> = mutableListOf(),
    val unfittedItems: MutableList<Item> = mutableListOf(),
) {

    fun putItem(item: Item, pivot: Pivot): Boolean {
        item.position = pivot
        for (rotationType in RotationType.values()) {
            item.rotationType = rotationType
            val dimensions = item.dimension()
            if (width >= pivot[0] + dimensions[0] && height >= pivot[1] + dimensions[1] && depth >= pivot[2] + dimensions[2]) {
                if (items.all { !it.intersect(item) }) {
                    items.add(item)
                    return true
                }
            }
        }

        return false
    }

    fun volume(): Double {
        return width * height * depth
    }

    override fun toString(): String {
        return "$name($width x $height x $depth, max_weight: $maxWeight)"
    }
}
