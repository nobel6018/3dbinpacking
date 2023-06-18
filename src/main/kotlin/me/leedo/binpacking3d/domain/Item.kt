package me.leedo.binpacking3d.domain

import kotlin.math.max
import kotlin.math.min

typealias Pivot = DoubleArray

enum class Axis(val value: Int) {
    WIDTH(0),
    HEIGHT(1),
    DEPTH(2),
}

enum class RotationType {
    WHD, HWD, HDW, DHW, DWH, WDH
}

class Item(
    val name: String,
    val width: Double,
    val height: Double,
    val depth: Double,
    val weight: Double,

    var position: Pivot,
    var rotationType: RotationType,
) {

    fun dimension(): DoubleArray {
        return when (rotationType) {
            RotationType.WHD -> doubleArrayOf(width, height, depth)
            RotationType.HWD -> doubleArrayOf(height, width, depth)
            RotationType.HDW -> doubleArrayOf(height, depth, width)
            RotationType.DHW -> doubleArrayOf(depth, height, width)
            RotationType.DWH -> doubleArrayOf(depth, width, height)
            RotationType.WDH -> doubleArrayOf(width, depth, height)
        }
    }

    fun volume(): Double {
        return width * height * depth
    }

    // checks whether there's an intersection between item i and item it.
    fun intersect(compareItem: Item): Boolean {
        return rectIntersect(this, compareItem, Axis.WIDTH.value, Axis.HEIGHT.value) &&
                rectIntersect(this, compareItem, Axis.HEIGHT.value, Axis.DEPTH.value) &&
                rectIntersect(this, compareItem, Axis.WIDTH.value, Axis.DEPTH.value)
    }

    // checks whether two rectangles from axis x and y of item i1 and i2
    // has intersection or not.
    fun rectIntersect(item1: Item, item2: Item, x: Int, y: Int): Boolean {
        val d1 = item1.dimension()
        val d2 = item2.dimension()

        val cx1 = item1.position[x] + d1[x] / 2
        val cy1 = item1.position[y] + d1[y] / 2
        val cx2 = item2.position[x] + d2[x] / 2
        val cy2 = item2.position[y] + d2[y] / 2

        val ix = max(cx1, cx2) - min(cx1, cx2)
        val iy = max(cy1, cy2) - min(cy1, cy2)

        return ix < (d1[x] + d2[x]) / 2 && iy < (d1[y] + d2[y]) / 2
    }

    override fun toString(): String {
        return "$name($width x $height x $depth, weight: $weight)"
    }
}
