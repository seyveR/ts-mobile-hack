package com.example.hakaton.ui.rezultat

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.hakaton.databinding.FragmentResultatBinding
import com.example.hakaton.ui.SharedViewModel


import androidx.fragment.app.viewModels
import com.example.hakaton.R
import com.squareup.picasso.Picasso


//class ResultFragment : Fragment() {
//
//    private var _binding: FragmentResultatBinding? = null
//    private val binding get() = _binding!!
//
//    private val sharedViewModel: SharedViewModel by activityViewModels()
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentResultatBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        // Получение Uri изображения из Bundle
//        arguments?.getParcelable<Uri>("imageUri")?.let { uri ->
//            binding.resultImageView2.setImageURI(uri)
//        }
//
//        // Наблюдение за изменениями текста в SharedViewModel для Camera2
//        sharedViewModel.camera2Text.observe(viewLifecycleOwner) { text ->
//            binding.zagolovok.text = text
//        }
//
//        // Наблюдение за изменениями текста в SharedViewModel для opisanie
//        sharedViewModel.opisanieText.observe(viewLifecycleOwner) { text ->
//            binding.opisanie2.text = text
//        }
//
//        return root
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}



class ResultFragment : Fragment() {

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

        arguments?.getParcelable<Uri>("imageUri")?.let { uri ->
            Log.d("ResultFragment", "Получен URI изображения: $uri")
            // Используйте Picasso для загрузки и отображения изображения
            Picasso.get().load(uri).into(binding.resultImageView2)
        }



//        // Получение распознанного текста из Bundle
//        arguments?.getString("recognizedText")?.let { text ->
//            // Отображение распознанного текста в элементе с идентификатором "rezultat"
//            binding.rezultat.text = text // Используйте binding для доступа к элементу интерфейса
//        }

        Log.d("ResultFragment", "Получен Bundle: $arguments")
        arguments?.getString("imageCategory")?.let { category ->
            // Отображение категории в элементе с идентификатором "categoryTextView"
            binding.rezultat.text = category
        }




        // Наблюдение за изменениями текста из Camera3
        sharedViewModel.camera3Text.observe(viewLifecycleOwner) { text ->
            binding.zagolovok2.text = text // Предполагается, что у вас есть TextView с id "camera3TextView" для отображения текста из Camera3
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












