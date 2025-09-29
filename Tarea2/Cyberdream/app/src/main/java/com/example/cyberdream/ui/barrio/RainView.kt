package com.example.cyberdream.ui.barrio

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * Lluvia neon ligera y eficiente.
 * - setActive(true/false) para iniciar/parar
 * - setIntensity(0f..1f) para controlar densidad y velocidad
 * - setWind(-1f..1f) para inclinar (negativo = izquierda, positivo = derecha)
 */
class RainView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private data class Drop(var x: Float, var y: Float, var len: Float, var speed: Float, var alpha: Int)

    private val rnd = Random(System.nanoTime())
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        color = Color.argb(180, 68, 170, 255) // neon azul-violeta
        strokeWidth = 2f * resources.displayMetrics.density
    }

    private var drops = mutableListOf<Drop>()
    private var lastTime = 0L
    private var running = false
    private var intensity = 0.5f           // 0..1
    private var wind = 0.2f                // -1..1 (inclinación)
    private var angleRad = Math.toRadians(70.0).toFloat() // ángulo de caída base

    fun setActive(active: Boolean) {
        if (active == running) return
        running = active
        if (active) {
            lastTime = System.nanoTime()
            postInvalidateOnAnimation()
        }
    }

    fun setIntensity(value: Float) {
        intensity = value.coerceIn(0f, 1f)
        // recalcular densidad objetivo
        ensureDropsCapacity()
    }

    fun setWind(value: Float) {
        wind = value.coerceIn(-1f, 1f)
    }

    private fun ensureDropsCapacity() {
        val area = width * height.toFloat()
        if (area <= 0f) return
        // densidad base: ~0.0015 drops por px2 (ajustado por intensidad)
        val target = (area * 0.0015f * intensity).toInt().coerceIn(0, 350)
        if (drops.size < target) {
            repeat(target - drops.size) { drops.add(randomDrop(spawnAbove = true)) }
        } else if (drops.size > target) {
            drops = drops.take(target).toMutableList()
        }
    }

    private fun randomDrop(spawnAbove: Boolean = false): Drop {
        val w = width.coerceAtLeast(1)
        val h = height.coerceAtLeast(1)
        val len = (12f..28f).random(rnd) * resources.displayMetrics.density
        val speed = (220f..520f).random(rnd) * (0.6f + intensity) // px/s
        val alpha = (120..210).random(rnd)
        val x = rnd.nextFloat() * w
        val y = if (spawnAbove) rnd.nextFloat() * -h * 0.3f else rnd.nextFloat() * h
        return Drop(x, y, len, speed, alpha)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        drops.clear()
        ensureDropsCapacity()
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!running || drops.isEmpty()) return

        val now = System.nanoTime()
        val dt = ((now - lastTime) / 1_000_000_000.0f).coerceIn(0f, 0.033f) // ~30fps cap
        lastTime = now

        // componente diagonal por viento
        val dirX = (cos(angleRad.toDouble()) * wind).toFloat()
        val dirY = sin(angleRad.toDouble()).toFloat()

        drops.forEach { d ->
            paint.alpha = d.alpha
            // desplazamiento
            val vx = d.speed * dirX * dt
            val vy = d.speed * dirY * dt
            d.x += vx
            d.y += vy

            // puntos extremo de la "línea" (trail)
            val nx = d.x - vx * 0.15f
            val ny = d.y - vy * 0.15f
            canvas.drawLine(d.x, d.y, nx, ny - d.len, paint)

            // recircular si sale de la vista
            if (d.y - d.len > height || d.x < -40 || d.x > width + 40) {
                val idx = drops.indexOf(d)
                drops[idx] = randomDrop(spawnAbove = true)
            }
        }

        // tick siguiente frame
        if (running) postInvalidateOnAnimation()
    }

    // Pequeña extensión para rango aleatorio
    private fun ClosedFloatingPointRange<Float>.random(r: Random) =
        start + r.nextFloat() * (endInclusive - start)
}