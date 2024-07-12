package day24

import getResourceAsText
import java.math.MathContext
import kotlin.math.*

private const val TIME_LIMIT = 100_000L

fun main() {
    val stones = getResourceAsText("/day24/input.txt").lines()
        .map { line ->
            line.split(" @ ")
                .map { it.split(",") }
                .flatten()
                .map { it.trim().toLong() }
        }
        .map { PositionAndVelocity(it[0], it[1], it[2], it[3], it[4], it[5]) }

    val solution = stones.combinations()
        .sortedBy { (first, second) -> first.toPositionVector().distance(second.toPositionVector()) }
        .firstNotNullOf { (first, second) ->
            trySolve(stones, first, second)
        }

    val result = solution.px + solution.py + solution.pz

//    val projectile = PositionAndVelocity(24, 13, 10, -3, 1, 2)
    // val stone1 = PositionAndVelocity(20, 19, 15, 1, -5, -3)
    // val stone2 = PositionAndVelocity(18, 19, 22, -1, -1, -2)

//    willHit(projectile, stone2)

    // val result = calculateVelocity(stones, stone1, stone2)
    println(result)
}

private fun trySolve(stones: List<PositionAndVelocity>, first: PositionAndVelocity, second: PositionAndVelocity): PositionAndVelocity? {
    println("Trying $first to $second")

    val stonesToHit = (stones - first - second).toList()

    (2..TIME_LIMIT).forEach { secondHitTime ->
        val advancedSecond = second.advanceTime(secondHitTime).toPositionVector()

        if (advancedSecond.x < 0 || advancedSecond.y < 0 || advancedSecond.z < 0) {
            return null
        }

        (1..<secondHitTime).forEach { firstHitTime ->
            val advancedFirst = first.advanceTime(firstHitTime).toPositionVector()
            val projectileVector = advancedSecond - advancedFirst

            val variations = getVectorVariations(projectileVector)
            variations.forEach { variation ->
                val projectile = advancedFirst.withVelocity(variation)

                if (stonesToHit.all { stone -> willHit(projectile, stone.advanceTime(firstHitTime)) }) {
                    return advancedFirst
                        .withVelocity(variation.inverse())
                        .advanceTime(firstHitTime)
                        .withVelocity(variation)
                }
            }
        }
    }

    return null
}

private fun getVectorVariations(vec: Vector3): List<Vector3> {
    val gcd = gcd(vec.x, gcd(vec.y, vec.z))

    val gcdDivisors = getDivisors(gcd)

    return gcdDivisors
        .map { vec / it }
}

private fun getDivisors(number: Long): Set<Long> {
    val sqrt = sqrt(number.toDouble()).toLong()

    return buildSet {
        (1..sqrt).forEach { candidate ->
            if (number % candidate == 0L) {
                add(candidate)
                add(number / candidate)
            }
        }
    }
}

fun willHit(projectile: PositionAndVelocity, target: PositionAndVelocity): Boolean {
    // https://stackoverflow.com/a/22117046
    // P0 + (t * V0) = P1 + (t * V1)
    // t = (P1 - P0) / (V0 -  V1)

    val a = projectile.toPositionVector() - target.toPositionVector()
    val b = target.toVelocityVector() - projectile.toVelocityVector()

    return a.parallel(b)
}

private fun <T> List<T>.combinations(): List<Pair<T, T>> {
    return this.flatMap { item1 ->
        this.map { item2 ->
            Pair(item1, item2)
        }
    }
        .filter { (first, second) -> first != second }
}

private fun gcd(a: Long, b: Long): Long {
    if (b == 0L) return a
    return gcd(b, a % b)
}

private data class Vector3(
    val x: Long,
    val y: Long,
    val z: Long
) {

    val components = listOf(x, y, z)

    operator fun minus(other: Vector3): Vector3 {
        return Vector3(
            x = x - other.x,
            y = y - other.y,
            z = z - other.z,
        )
    }

    operator fun div(num: Long): Vector3 {
        return Vector3(
            x = x / num,
            y = y / num,
            z = z / num
        )
    }

    fun withVelocity(v: Vector3): PositionAndVelocity =
        PositionAndVelocity(
            px = this.x,
            py = this.y,
            pz = this.z,
            vx = v.x,
            vy = v.y,
            vz = v.z
        )

    fun dot(other: Vector3) = this.x * other.x + this.y * other.y + this.z * other.z

    fun magnitude() = sqrt((x * x + y * y + z * z).toDouble())

    fun parallel(other: Vector3): Boolean {
        val dot = this.dot(other)
        val thisMag = this.magnitude()
        val otherMag = other.magnitude()

        val cosTheta = dot / (thisMag * otherMag)
        val acosTheta = acos(cosTheta)

        return abs(acosTheta) < 0.0001
    }

    fun distance(other: Vector3): Double {
        val dx = other.x.toBigDecimal() - this.x.toBigDecimal()
        val dy = other.y.toBigDecimal() - this.y.toBigDecimal()
        val dz = other.z.toBigDecimal() - this.z.toBigDecimal()
        return (dx * dx + dy * dy + dz * dz).sqrt(MathContext.DECIMAL128).toDouble()
    }

    fun inverse() = Vector3(
        x = -x,
        y = -y,
        z = -z
    )

}

private fun PositionAndVelocity.advanceTime(time: Long): PositionAndVelocity =
    PositionAndVelocity(
        px = this.px + this.vx * time,
        py = this.py + this.vy * time,
        pz = this.pz + this.vz * time,
        vx = this.vx,
        vy = this.vy,
        vz = this.vz
    )

private fun PositionAndVelocity.toPositionVector() =
    Vector3(
        x = this.px,
        y = this.py,
        z = this.pz
    )

private fun PositionAndVelocity.toVelocityVector() =
    Vector3(
        x = this.vx,
        y = this.vy,
        z = this.vz
    )

private fun PositionAndVelocity.withVelocity(vel: Vector3) = this.copy(
    vx = vel.x,
    vy = vel.y,
    vz= vel.z
)
