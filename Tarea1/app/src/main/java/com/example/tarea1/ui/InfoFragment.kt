package com.example.tarea1.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tarea1.R

class InfoFragment : Fragment() {

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private lateinit var tvTexto: TextView
    private lateinit var progress: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tvTexto = view.findViewById(R.id.tvTexto)
        progress = view.findViewById(R.id.progress)
        val btnAnimar = view.findViewById<Button>(R.id.btnAnimar)

        btnAnimar.setOnClickListener { startStop() }
    }

    private fun startStop() {
        if (runnable != null) { // detener
            handler.removeCallbacks(runnable!!); runnable = null
            progress.progress = 0
            tvTexto.text = "(Aún no hay texto)"
            return
        }
        runnable = object : Runnable {
            var p = 0
            override fun run() {
                progress.progress = p
                tvTexto.text = "Cargando: $p%"
                p = (p + 5) % 101
                handler.postDelayed(this, 150)
            }
        }.also { handler.post(it) }
    }

    override fun onDestroyView() {
        runnable?.let { handler.removeCallbacks(it) }
        super.onDestroyView()
    }
}
