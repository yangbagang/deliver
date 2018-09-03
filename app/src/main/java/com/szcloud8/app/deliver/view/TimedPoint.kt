package com.szcloud8.app.deliver.view

class TimedPoint(var x: Float, var y: Float) {

    private var timestamp: Long = System.currentTimeMillis()

    fun velocityFrom(start: TimedPoint): Float {
        val velocity = distanceTo(start) / (this.timestamp - start.timestamp)
        return if (velocity != velocity) 0f else velocity
    }

    fun distanceTo(point: TimedPoint): Float {
        return Math.sqrt(Math.pow((point.x - this.x).toDouble(), 2.0) + Math.pow((point.y - this.y).toDouble(), 2.0)).toFloat()
    }

}