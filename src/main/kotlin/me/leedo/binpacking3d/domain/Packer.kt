package me.leedo.binpacking3d.domain

class Packer {
    var bins: MutableList<Bin> = ArrayList()
    var items: MutableList<Item> = ArrayList()

    // var unfitItems: MutableList<Item> = ArrayList()
    var totalItems = 0

    fun addBin(bin: Bin) {
        bins.add(bin)
    }

    fun addItem(item: Item) {
        totalItems = items.size + 1
        items.add(item)
    }

    fun packToBin(bin: Bin, item: Item): Boolean {
        // If the bin is empty, try to put the item at the start position.
        if (bin.items.isEmpty()) {
            val response = bin.putItem(item, doubleArrayOf(0.0, 0.0, 0.0))
            if (!response) {
                bin.unfittedItems.add(item)
            }
            return response
        }

        // If the bin is not empty, try to put the item in the next available position.
        for (axis in Axis.values()) {
            if (tryPutItemInAvailablePosition(bin, item, axis)) {
                return true
            }
        }

        // If the item couldn't be fitted in the bin, add it to the bin's unfit items.
        bin.unfittedItems.add(item)
        return false
    }

    private fun tryPutItemInAvailablePosition(bin: Bin, item: Item, axis: Axis): Boolean {
        for (itemInBin in bin.items) {
            val (w, h, d) = itemInBin.dimension()
            val pivot = when (axis) {
                Axis.WIDTH -> doubleArrayOf(itemInBin.position[0] + w, itemInBin.position[1], itemInBin.position[2])
                Axis.HEIGHT -> doubleArrayOf(itemInBin.position[0], itemInBin.position[1] + h, itemInBin.position[2])
                Axis.DEPTH -> doubleArrayOf(itemInBin.position[0], itemInBin.position[1], itemInBin.position[2] + d)
            }

            if (bin.putItem(item, pivot)) {
                return true
            }
        }
        return false
    }

    fun pack(
        biggerFirst: Boolean = false,
        distributeItems: Boolean = true,
    ) {
        bins.sortByDescending { it.volume() }
        items.sortByDescending { it.volume() }

        for (bin in bins) {
            val itemsCopy = ArrayList<Item>(items)
            for (item in itemsCopy) {
                packToBin(bin, item)
            }

            if (distributeItems) {
                for (item in bin.items) {
                    items.remove(item)
                }
            }
        }
    }
}
