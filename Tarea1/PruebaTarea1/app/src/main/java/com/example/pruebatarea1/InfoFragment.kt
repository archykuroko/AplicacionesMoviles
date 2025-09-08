package com.example.pruebatarea1

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment

class InfoFragment : Fragment() {

    private val handler = Handler(Looper.getMainLooper())
    private var progress = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    private fun simulateLoading(pb: ProgressBar, tv: TextView) {
        progress = 0
        pb.progress = 0
        tv.text = "Cargando: 0%"
        handler.post(object : Runnable {
            override fun run() {
                progress += 5
                if (progress <= 100) {
                    pb.progress = progress
                    tv.text = "Cargando: ${progress}%"
                    handler.postDelayed(this, 150)
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<TextView>(R.id.txtTitulo).text = "Elementos de información"
        view.findViewById<TextView>(R.id.txtDesc).text = "Muestran contenido o estado al usuario. Ej.: textos, imágenes y progreso."

        view.findViewById<ImageView>(R.id.imgDemo).setImageResource(android.R.drawable.ic_menu_gallery)

        val pb = view.findViewById<ProgressBar>(R.id.progressBar)
        val tvProg = view.findViewById<TextView>(R.id.txtProgreso)
        view.findViewById<Button>(R.id.btnIniciarCarga).setOnClickListener {
            simulateLoading(pb, tvProg)
        }
    }
}
