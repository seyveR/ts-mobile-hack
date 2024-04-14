package com.example.hakaton.ui.login

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.hakaton.databinding.FragmentResultatBinding
import com.example.hakaton.ui.SharedViewModel




class LoginFragment : Fragment() {

    private var _binding: FragmentResultatBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultatBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Получение Uri обработанного изображения из Bundle
        arguments?.getParcelable<Uri>("imageUri")?.let { uri ->
            binding.resultImageView2.setImageURI(uri)
        }

        // Получение распознанного текста из Bundle
        arguments?.getString("recognizedText")?.let { text ->
            // Отображение распознанного текста в элементе с идентификатором "rezultat"
            binding.rezultat.text = text // Используйте binding для доступа к элементу интерфейса
        }


        // Наблюдение за изменениями текста в SharedViewModel для Camera2
        sharedViewModel.camera2Text.observe(viewLifecycleOwner) { text ->
            binding.zagolovok.text = text
        }

        // Наблюдение за изменениями текста в SharedViewModel для opisanie
        sharedViewModel.opisanieText.observe(viewLifecycleOwner) { text ->
            binding.opisanie2.text = text
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}












