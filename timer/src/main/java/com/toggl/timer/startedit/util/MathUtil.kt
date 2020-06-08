package com.toggl.timer.startedit.util

import android.graphics.PointF
import com.toggl.common.Constants.ClockMath.fullCircle
import com.toggl.common.Constants.ClockMath.minutesInAnHour
import com.toggl.common.Constants.ClockMath.quarterOfCircle
import com.toggl.common.Constants.ClockMath.secondsInAMinute
import java.time.Duration

fun Duration.toAngleOnTheDial(): Double = this.toAngle() - quarterOfCircle

fun Duration.toAngle(): Double =
    this.minusHours(this.toHours()).seconds / (minutesInAnHour * secondsInAMinute).toDouble() * fullCircle

fun Double.toPositiveAngle(): Double {
    var angle = this
    while (angle < 0) angle += fullCircle
    return angle
}

fun Double.angleToTime(): Duration {
    val time = this / fullCircle * minutesInAnHour
    val minutes = time.toLong()
    val seconds = ((time - minutes) * secondsInAMinute).toLong()
    return Duration.ofSeconds(minutes * secondsInAMinute + seconds)
}

fun angleBetween(a: PointF, b: PointF): Float = kotlin.math.atan2(a.y - b.y, a.x - b.x)

fun distanceSq(a: PointF, b: PointF): Float {
    val dx = a.x - b.x
    val dy = a.y - b.y
    return dx * dx + dy * dy
}

fun PointF.updateWithPointOnCircumference(center: PointF, angle: Double, radius: Double) {
    this.set(
        (center.x + radius * kotlin.math.cos(angle)).toFloat(),
        (center.y + radius * kotlin.math.sin(angle)).toFloat()
    )
}

fun Double.isBetween(startAngle: Double, endAngle: Double): Boolean {
    val angle = this.toPositiveAngle()
    val positiveStartAngle = startAngle.toPositiveAngle()
    val positiveEndAngle = endAngle.toPositiveAngle()

    return if (positiveStartAngle > positiveEndAngle) positiveStartAngle <= angle || angle <= positiveEndAngle
    else angle in positiveStartAngle..positiveEndAngle
}

fun Int.pingPongClamp(length: Int): Int {
    if (length <= 0) throw IllegalArgumentException("The length for clamping must be at positive integer, $length given.")
    if (this < 0) throw IllegalArgumentException("The clamped number a non-negative integer, $this given.")

    if (length == 1) return 0

    val lengthOfFoldedSequence = 2 * length - 2
    val indexInFoldedSequence = this % lengthOfFoldedSequence
    return if (indexInFoldedSequence < length) indexInFoldedSequence
    else lengthOfFoldedSequence - indexInFoldedSequence
}

fun <T> Array<T>.getPingPongIndexedItem(index: Int) = this[index.pingPongClamp(this.size)]
fun <T> List<T>.getPingPongIndexedItem(index: Int) = this[index.pingPongClamp(this.size)]