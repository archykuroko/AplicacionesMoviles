package com.example.cyberdream.ui.barrio

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import com.example.cyberdream.R
import com.example.cyberdream.databinding.FragmentAmbienteBarrioBinding
import android.graphics.Color



class AmbienteBarrioFragment : Fragment() {

    private var _b: FragmentAmbienteBarrioBinding? = null
    private val b get() = _b!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _b = FragmentAmbienteBarrioBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // base: mapa del barrio
        b.imgMapa.setImageResource(R.drawable.map_old_downtown)

        // listeners
        b.chkNiebla.setOnCheckedChangeListener { _, _ -> aplicarAmbiente() }
        b.chkLluvia.setOnCheckedChangeListener { _, _ -> aplicarAmbiente() }
        b.seekIntensidad.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, value: Int, fromUser: Boolean) {
                b.lblIntensidad.text = "Intensidad: $value%"
                aplicarAmbiente()
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })

        aplicarAmbiente()
    }

    private fun aplicarAmbiente() {
        val intensidad = b.seekIntensidad.progress / 100f  // 0..1

        val activaNiebla = b.chkNiebla.isChecked
        val activaLluvia = b.chkLluvia.isChecked

        // --- NIEBLA / OVERLAY ---
        if (activaNiebla) {
            val colorOverlay = Color.argb(
                (80 * intensidad).toInt().coerceIn(0, 150), // alpha dinámico
                120, 0, 180 // tono morado neon
            )
            b.overlay.setBackgroundColor(colorOverlay)
            b.overlay.visibility = View.VISIBLE
        } else {
            b.overlay.setBackgroundColor(Color.TRANSPARENT)
            b.overlay.visibility = View.GONE
        }

        // --- LLUVIA ---
        b.rain.visibility = if (activaLluvia) View.VISIBLE else View.GONE
        b.rain.setIntensity(intensidad.coerceAtLeast(0.15f)) // mínimo para no desaparecer
        b.rain.setWind(0.2f)                                // diagonal suave
        b.rain.setActive(activaLluvia)

        // --- TEXTO DE ESTADO ---
        val estado = buildString {
            append("Estado: ")
            when {
                activaNiebla && activaLluvia -> append("niebla + lluvia")
                activaNiebla -> append("niebla")
                activaLluvia -> append("lluvia")
                else -> append("noche limpia")
            }
        }
        b.estado.text = estado
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }

    companion object { fun newInstance() = AmbienteBarrioFragment() }
}