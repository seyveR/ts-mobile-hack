//package com.example.hakaton
//
//import android.os.Bundle
//import com.google.android.material.bottomnavigation.BottomNavigationView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.navigation.findNavController
//import androidx.navigation.ui.AppBarConfiguration
//import androidx.navigation.ui.setupActionBarWithNavController
//import androidx.navigation.ui.setupWithNavController
//import com.example.hakaton.databinding.ActivityMainBinding
//
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityMainBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val navView: BottomNavigationView = binding.navView
//
//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
//    }
//}


package com.example.hakaton

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.hakaton.ui.SharedViewModel
import com.example.hakaton.ui.introduction.IntroductionFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

//class MainActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Устанавливаем макет для MainActivity
//        setContentView(R.layout.activity_main)
//
//        // Получаем NavController для управления навигацией
//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
//
//        // Создаем AppBarConfiguration, указывая, какие элементы меню должны рассматриваться как верхние уровни назначения
//        // В вашем случае, поскольку вы не хотите использовать нижнюю навигационную панель, вы можете опустить эту часть
//        // или настроить ее в соответствии с вашими потребностями
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
//            )
//        )
//
//        // Настраиваем ActionBar с NavController
//        setupActionBarWithNavController(navController, appBarConfiguration)
//
//        // Удалите или закомментируйте следующую строку, если вы не хотите использовать нижнюю навигационную панель
//        // navView.setupWithNavController(navController)
//    }
//}


//class MainActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, IntroductionFragment())
//                .commitNow()
//        }
//    }
//}

//class MainActivity : AppCompatActivity() {
//
//    private lateinit var navController: NavController
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
//        navController = navHostFragment.navController
//
//        val navView: BottomNavigationView = findViewById(R.id.nav_view)
//        NavigationUI.setupWithNavController(navView, navController)
//
//        // Найти кнопку по ID и установить обработчик нажатия
//        val navBarAnalizButton = navView.menu.findItem(R.id.analiz)
//        navBarAnalizButton.setOnMenuItemClickListener {
//            navController.navigate(R.id.action_navigation_home_to_navigation_dashboard)
//            true
//        }
//        val navBarHomeButton = navView.menu.findItem(R.id.navigation_home)
//        navBarHomeButton.setOnMenuItemClickListener {
//            navController.navigate(R.id.navigation_home)
//            true
//        }
//    }
//}

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        NavigationUI.setupWithNavController(navView, navController)


    }
}















