//package com.example.hakaton
//
//import android.os.Bundle
//import android.text.TextUtils
//import android.util.Log
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.google.firebase.FirebaseException
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.PhoneAuthCredential
//import com.google.firebase.auth.PhoneAuthProvider
//import java.util.concurrent.TimeUnit
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.content.pm.PackageManager
//import android.telephony.SmsManager
//import android.widget.EditText
//import androidx.appcompat.widget.AppCompatButton
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//
//
//class LoginActivity : AppCompatActivity() {
//
//    private var verificationId: String? = null
//    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
//
//    companion object {
//        private const val MY_PERMISSIONS_REQUEST_SEND_SMS = 0
//    }
//
//    @SuppressLint("ResourceType")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
//
//
//        val buttonStart3 = findViewById<AppCompatButton>(R.id.buttonStart3)
//        val Camera4 = findViewById<EditText>(R.id.Camera4)
//        val buttonStart4 = findViewById<AppCompatButton>(R.id.buttonStart4)
//        val Camera5 = findViewById<EditText>(R.id.Camera5)
//
//        buttonStart3.setOnClickListener {
//            val phoneNumber = Camera4.text.toString()
//            if (TextUtils.isEmpty(phoneNumber)) {
//                Toast.makeText(this, "Введите номер телефона", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            // Применяем анимацию нажатия к кнопке
//            buttonStart3.setBackgroundResource(R.anim.button_press_animation3)
//            sendVerificationCode(phoneNumber)
//        }
//
//
//        buttonStart4.setOnClickListener {
//            val code = Camera5.text.toString()
//            if (TextUtils.isEmpty(code)) {
//                Toast.makeText(this, "Введите код подтверждения", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//        }
//    }
//
//    private fun sendVerificationCode(phoneNumber: String) {
//        Log.d("LoginActivity", "Отправка кода подтверждения на номер: $phoneNumber")
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.SEND_SMS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.SEND_SMS),
//                MY_PERMISSIONS_REQUEST_SEND_SMS
//            )
//        } else {
//        }
//    }
//
//
//}


//class LoginActivity : AppCompatActivity() {
//
//    private var verificationId: String? = null
//    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
//
//    companion object {
//        private const val MY_PERMISSIONS_REQUEST_SEND_SMS = 0
//    }
//
//    @SuppressLint("ResourceType")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
//
//
//        val buttonStart3 = findViewById<AppCompatButton>(R.id.buttonStart3)
//        val Camera4 = findViewById<EditText>(R.id.Camera4)
//        val buttonStart4 = findViewById<AppCompatButton>(R.id.buttonStart4)
//        val Camera5 = findViewById<EditText>(R.id.Camera5)
//
//        buttonStart3.setOnClickListener {
//            val phoneNumber = Camera4.text.toString()
//            if (TextUtils.isEmpty(phoneNumber)) {
//                Toast.makeText(this, "Введите номер телефона", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            // Применяем анимацию нажатия к кнопке
//            buttonStart3.setBackgroundResource(R.anim.button_press_animation3)
//            sendVerificationCode(phoneNumber)
//        }
//
//
//        buttonStart4.setOnClickListener {
//            val code = Camera5.text.toString()
//            if (TextUtils.isEmpty(code)) {
//                Toast.makeText(this, "Введите код подтверждения", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            verifyCode(code)
//        }
//    }
//
//    private fun sendVerificationCode(phoneNumber: String) {
//        Log.d("LoginActivity", "Отправка кода подтверждения на номер: $phoneNumber")
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.SEND_SMS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.SEND_SMS),
//                MY_PERMISSIONS_REQUEST_SEND_SMS
//            )
//        } else {
//            sendSms(phoneNumber, "Ваш код подтверждения: 123456")
//        }
//    }
//
//    private fun sendSms(phoneNumber: String, message: String) {
//        val smsManager = SmsManager.getDefault()
//        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
//    }
//
//    private fun verifyCode(code: String) {
//        Log.d("LoginActivity", "Проверка кода: $code")
//        // Здесь должен быть ваш код для проверки кода подтверждения
//        // Так как мы отправляем SMS напрямую, вам нужно будет реализовать логику проверки кода
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            MY_PERMISSIONS_REQUEST_SEND_SMS -> {
//                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    // Здесь можно повторно вызвать sendVerificationCode, если это необходимо
//                } else {
//                    Toast.makeText(
//                        this,
//                        "Разрешение на отправку SMS не предоставлено",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//                return
//            }
//
//            else -> {
//                // Ignore all other requests.
//            }
//        }
//    }
//}


