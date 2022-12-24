import java.lang.Integer.max
import kotlin.math.absoluteValue

@Suppress("unused")
class Voxel(val x: Int, val y: Int, val z: Int) {
    constructor(list: List<Int>) : this(list[0], list[1], list[2])
    fun l1Neighbors() : Set<Voxel> {
        return setOf(
            Voxel(x + 1, y, z), Voxel(x - 1, y, z), Voxel(x, y + 1, z),
            Voxel(x, y - 1, z), Voxel(x, y, z + 1), Voxel(x, y, z - 1),
        )
    }
    fun lInfNeighbors() : Set<Voxel> {
        return buildSet {
            for (dx in -1..1) {
                for (dy in -1..1) {
                    for (dz in -1..1) {
                        if (x != 0 || y != 0 || z != 0) {
                            add(Voxel(x + dx, y + dy, z + dz))
                        }
                    }
                }
            }
        }
    }
    infix fun l1(arg: Voxel) : Int = (x - arg.x).absoluteValue + (y - arg.y).absoluteValue + (z - arg.z).absoluteValue
    infix fun lInf(arg: Voxel) : Int = max(
        (x - arg.x).absoluteValue, max(
            (y - arg.y).absoluteValue,
            (z - arg.z).absoluteValue
        )
    )
    operator fun component1() : Int = x
    operator fun component2() : Int = y
    operator fun component3() : Int = z
    operator fun plus(arg: Voxel) = Voxel(x + arg.x, y + arg.y, z + arg.z)
    operator fun minus(arg: Voxel) = Voxel(x - arg.x, y - arg.y, z - arg.z)
    operator fun times(arg: Voxel) = Voxel(x * arg.x, y * arg.y, z * arg.z)
    operator fun compareTo(arg: Voxel) : Int =
        when {
            x > arg.x -> 1
            y > arg.y -> 1
            z > arg.z -> 1
            x == arg.x && y == arg.y && z == arg.z -> 0
            else -> -1
        }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Voxel
        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false
        return true
    }
    override fun hashCode(): Int {
        return if (x < y) {
            if (y < z) {
                val temp = z * z + y
                temp * temp + x
            } else {
                x * x + x + z * z + y
            }
        } else {
            if (y < z) {
                val temp = y * y + y + z
                temp * temp + x
            } else {
                x * x + x + y * y + y + z
            }
        }
    }
    override fun toString(): String = "($x, $y, $z)"
}
