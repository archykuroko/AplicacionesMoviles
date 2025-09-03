package com.example.tarea1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tarea1.R

class ButtonsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_buttons, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val btn = view.findViewById<Button>(R.id.btnDemo)
        val imgBtn = view.findViewById<ImageButton>(R.id.imgButton)

        btn.setOnClickListener {
            Toast.makeText(requireContext(), "¡Button presionado!", Toast.LENGTH_SHORT).show()
        }
        imgBtn.setOnClickListener {
            Toast.makeText(requireContext(), "ImageButton hizo algo 😎", Toast.LENGTH_SHORT).show()
        }
    }
}
