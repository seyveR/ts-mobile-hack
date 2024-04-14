package com.example.hakaton.ui.introduction

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.hakaton.LoginActivity
import com.example.hakaton.MainActivity
import com.example.hakaton.R
import com.example.hakaton.databinding.FragmentIntroductionBinding
import com.example.hakaton.ui.home.HomeFragment


//class IntroductionFragment : Fragment() {
//
//    private var _binding: FragmentIntroductionBinding? = null
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentIntroductionBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Найти кнопку по ID и установить обработчик нажатия
//        binding.buttonStart.setOnClickListener {
//            val intent = Intent(requireActivity(), MainActivity::class.java)
//            startActivity(intent)
//        }
//
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}


class IntroductionFragment : Fragment() {

    private var _binding: FragmentIntroductionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntroductionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Загружаем анимацию
        val scaleAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.button_press_animation)

        // Устанавливаем анимацию в качестве слушателя нажатия кнопки
        binding.buttonStart.setOnClickListener {
            // Применяем анимацию к кнопке
            binding.buttonStart.startAnimation(scaleAnimation)

            // Ваш код для перехода к LoginActivity
            val intent = Intent(requireActivity(),MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}










