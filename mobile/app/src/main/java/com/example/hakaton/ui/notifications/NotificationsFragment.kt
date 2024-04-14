package com.example.hakaton.ui.notifications


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.hakaton.R
import com.example.hakaton.databinding.FragmentNotificationsBinding
import androidx.navigation.fragment.findNavController
import com.example.hakaton.ui.SharedViewModel
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import com.example.hakaton.BoundingBox
import com.example.hakaton.Yolov5TFLiteDetector
import com.example.hakaton.ui.ReadImageText
import com.google.android.material.snackbar.Snackbar
import com.googlecode.tesseract.android.TessBaseAPI
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder



//class NotificationsFragment : Fragment() {
//
//    private var _binding: FragmentNotificationsBinding? = null
//    private val binding get() = _binding!!
//
//    private val sharedViewModel: SharedViewModel by activityViewModels()
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        // Добавление обработчика ввода текста в EditText Camera2
//        val cameraEditText = root.findViewById<EditText>(R.id.Camera2)
//        cameraEditText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                Log.d("EditTextInput", "Текст изменен: $s")
//                sharedViewModel.setCamera2Text(s.toString())
//            }
//
//            override fun afterTextChanged(s: Editable) {}
//        })
//
//        // Добавление обработчика ввода текста в EditText opisanie
//        val opisanieEditText = root.findViewById<EditText>(R.id.opisanie)
//        opisanieEditText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                Log.d("EditTextInput", "Текст2 изменен: $s")
//                sharedViewModel.setOpisanieText(s.toString())
//            }
//
//            override fun afterTextChanged(s: Editable) {}
//        })
//
//        // Получение Uri изображения из Bundle
//        arguments?.getParcelable<Uri>("imageUri")?.let { uri ->
//            binding.resultImageView.setImageURI(uri)
//        }
//
//        // Добавление обработчика нажатия на кнопку
//        binding.zagruzit.setOnClickListener {
//            // Переход на следующий фрагмент
//            findNavController().navigate(R.id.action_navigation_notifications_to_fragment_resultat)
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

//class NotificationsFragment : Fragment() {
//
//    private var _binding: FragmentNotificationsBinding? = null
//    private val binding get() = _binding!!
//
//    private val sharedViewModel: SharedViewModel by activityViewModels()
//    private lateinit var yolov5TFLiteDetector: Yolov5TFLiteDetector
//    private lateinit var boxPaint: Paint
//    private lateinit var textPaint: Paint
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        // Получение Uri изображения из Bundle
//        arguments?.getParcelable<Uri>("imageUri")?.let { uri ->
//            // Отображение изображения
//            binding.resultImageView.setImageURI(uri)
//        }
//
//        // Добавление обработчика ввода текста в EditText Camera2
//        val cameraEditText = root.findViewById<EditText>(R.id.Camera2)
//        cameraEditText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                Log.d("EditTextInput", "Текст изменен: $s")
//                sharedViewModel.setCamera2Text(s.toString())
//            }
//
//            override fun afterTextChanged(s: Editable) {}
//        })
//
//        // Добавление обработчика ввода текста в EditText opisanie
//        val opisanieEditText = root.findViewById<EditText>(R.id.opisanie)
//        opisanieEditText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                Log.d("EditTextInput", "Текст2 изменен: $s")
//                sharedViewModel.setOpisanieText(s.toString())
//            }
//
//            override fun afterTextChanged(s: Editable) {}
//        })
//
//        // Инициализация детектора и модели
//        yolov5TFLiteDetector = Yolov5TFLiteDetector()
//        yolov5TFLiteDetector.setModelFile("price.tflite")
//        yolov5TFLiteDetector.initialModel(requireContext())
//
//        boxPaint = Paint().apply {
//            strokeWidth = 5f
//            style = Paint.Style.STROKE
//            color = Color.RED
//        }
//
//        // Создание Paint для фона
//        val backgroundPaint = Paint().apply {
//            color = Color.RED
//            style = Paint.Style.FILL
//        }
//
//        // Создание Paint для текста
//        val textPaint = Paint().apply {
//            textSize = 60f
//            color = Color.WHITE // Белые буквы
//            style = Paint.Style.FILL
//        }
//
//        val scaleAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.button_press_animation3)
//
//        // Обработка изображения при нажатии на кнопку
//        binding.zagruzit.setOnClickListener {
//            binding.zagruzit.startAnimation(scaleAnimation)
//            binding.zagruzit.postDelayed({
//            // Получение Uri изображения из Bundle
//            arguments?.getParcelable<Uri>("imageUri")?.let { uri ->
//                val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
//                val recognitions = yolov5TFLiteDetector.detect(bitmap)
//                val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
//                val canvas = Canvas(mutableBitmap)
//
//                for (recognition in recognitions) {
//                    if (recognition.confidence > 0.4) {
//                        val location = recognition.location
//                        canvas.drawRect(location, boxPaint)
//
//
//                        val textBounds = Rect()
//                        textPaint.getTextBounds(recognition.labelName, 0, recognition.labelName.length, textBounds)
//                        canvas.drawRect(location.left, location.top - textBounds.height(), location.right, location.top, backgroundPaint)
//
//                        // Рисуем текст поверх фона
//                        canvas.drawText(recognition.labelName, location.left, location.top, textPaint)
//
//                    }
//                }
//
//
//                val tempFile = File.createTempFile("processed_image", ".png", requireContext().cacheDir)
//                val outStream = FileOutputStream(tempFile)
//                mutableBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
//                outStream.close()
//
//                val tempFileUri = FileProvider.getUriForFile(
//                    requireContext(),
//                    "com.example.hakaton.fileprovider",
//                    tempFile
//                )
//
//                val bundle = Bundle().apply {
//                    putParcelable("imageUri", tempFileUri)
//                }
//
//                // Переходим к ResultFragment, передавая Bundle
//                findNavController().navigate(R.id.action_navigation_notifications_to_fragment_resultat, bundle)
//            }
//                                         }, scaleAnimation.duration)
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





import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import jxl.Workbook
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val scaleAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.button_press_animation3)

        val locations = readLocationsFromXls(requireContext())
        val districts = locations.map { it.district }.distinct()
        val districtAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, districts)
        binding.Camera3.setAdapter(districtAdapter)

        binding.Camera3.setOnItemClickListener { _, _, position, _ ->
            Log.d("NotificationsFragment", "Выбор района: $position")
            val selectedDistrict = districts[position]
            Log.d("NotificationsFragment", "Выбран район: $selectedDistrict")

            val filteredAddresses = locations.filter { it.district == selectedDistrict }.map { it.address }
            Log.d("NotificationsFragment", "Фильтрованные адреса для района $selectedDistrict: $filteredAddresses")

            Log.d("NotificationsFragment", "Установка адаптера для списка адресов")

            val addressAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, filteredAddresses)
            binding.Camera2.setAdapter(addressAdapter)
        }


        arguments?.getParcelable<Uri>("imageUri")?.let { uri ->
            binding.resultImageView.setImageURI(uri)
        }

        val cameraEditText = root.findViewById<EditText>(R.id.Camera2)
        cameraEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.d("EditTextInput", "Текст изменен: $s")
                sharedViewModel.setCamera2Text(s.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        })

        val opisanieEditText = root.findViewById<EditText>(R.id.opisanie)
        opisanieEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.d("EditTextInput", "Текст2 изменен: $s")
                sharedViewModel.setOpisanieText(s.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        })

        val camera3EditText = root.findViewById<EditText>(R.id.Camera3)
        camera3EditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.d("EditTextInput", "Текст изменен: $s")
                sharedViewModel.setCamera3Text(s.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.zagruzit.setOnClickListener {
            val opisanieEditText = binding.opisanie
            if (opisanieEditText.text.isNullOrEmpty()) {
                // Показываем Snackbar с сообщением
                Snackbar.make(binding.root, "Пожалуйста, укажите адрес и округ", Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                // Если поле заполнено, продолжаем с действием кнопки
                binding.zagruzit.startAnimation(scaleAnimation)
                binding.zagruzit.postDelayed({
                    arguments?.getParcelable<Uri>("imageUri")?.let { uri ->
                        sendImageToServer(uri) { photoUrl, category ->
                            val bundle = Bundle().apply {
                                putParcelable("imageUri", photoUrl)
                                putString("imageCategory", category)
                            }

                            findNavController().navigate(R.id.action_navigation_notifications_to_fragment_resultat, bundle)
                        }
                    }
                }, scaleAnimation.duration)
            }
        }

        return root
    }

    private fun sendImageToServer(uri: Uri, onSuccess: (Uri, String?) -> Unit) {
        val storageRef = Firebase.storage.reference
        val imageRef = storageRef.child("images/${uri.lastPathSegment}")

        imageRef.putFile(uri).addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val photoUrl = uri
                Log.d("NotificationsFragment", "URL изображения: $photoUrl")

                val selectedAddress = binding.Camera2.text.toString()
                val description = binding.opisanie.text.toString()
                val district = binding.Camera3.text.toString()

                val request = ImageUploadRequest(photo_url = photoUrl.toString(), district = district, area = selectedAddress, description = description)
                sendPhotoUrlToServer(request) { category ->
                    onSuccess(photoUrl, category)
                }
            }.addOnFailureListener {
                Log.e("NotificationsFragment", "Ошибка при получении URL изображения", it)
                onSuccess(Uri.EMPTY, null)
            }
        }.addOnFailureListener {
            Log.e("NotificationsFragment", "Ошибка при загрузке изображения", it)
            onSuccess(Uri.EMPTY, null)
        }
    }

    private fun readLocationsFromXls(context: Context): List<Location> {
        val assetManager = context.assets
        val inputStream: InputStream? = try {
            assetManager.open("locations.xls")
        } catch (e: IOException) {
            Log.e("NotificationsFragment", "Ошибка при чтении файла locations.xls", e)
            null
        }

        if (inputStream == null) {
            Log.e("NotificationsFragment", "Файл locations.xls не найден")
            return emptyList()
        }

        val workbook = Workbook.getWorkbook(inputStream)
        val sheet = workbook.getSheet(0)
        val locations = mutableListOf<Location>()

        for (row in 0 until sheet.rows) {
            val district = sheet.getCell(0, row).contents
            val address = sheet.getCell(1, row).contents
            locations.add(Location(district, address))
        }

        workbook.close()
        inputStream.close()
        Log.d("NotificationsFragment", "Загруженные локации: $locations")
        return locations
    }

    data class Location(val district: String, val address: String)

    private fun sendPhotoUrlToServer(request: ImageUploadRequest, onCategoryReceived: (String?) -> Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://7060-95-54-231-188.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        Log.d("NotificationsFragment", "Отправляем запрос на сервер с описанием: ${request.description}")

        apiService.uploadImage(request).enqueue(object : Callback<ServerResponse> {
            override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>) {
                if (response.isSuccessful) {
                    val serverResponse = response.body()
                    if (serverResponse != null) {
                        Log.d("NotificationsFragment", "Категория изображения: ${serverResponse.category}")
                        onCategoryReceived(serverResponse.category)
                    } else {
                        Log.e("NotificationsFragment", "Ответ сервера пуст")
                        onCategoryReceived(null)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("NotificationsFragment", "Ошибка при отправке изображения: $errorBody")
                    onCategoryReceived(null)
                }
            }

            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                Log.e("NotificationsFragment", "Ошибка сети: ${t.message}", t)
                onCategoryReceived(null)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    data class ImageUploadRequest(val photo_url: String, val district: String, val area: String, val description: String)

    data class ServerResponse(val category: String)

    interface ApiService {
        @POST("predict")
        fun uploadImage(@Body request: ImageUploadRequest): Call<ServerResponse>
    }
}







//////////////////////////////////////////                                          РАБОЧИЙ КОД!!!!
//class NotificationsFragment : Fragment() {
//
//    private var _binding: FragmentNotificationsBinding? = null
//    private val binding get() = _binding!!
//
//    private val sharedViewModel: SharedViewModel by activityViewModels()
//    private lateinit var yolov5TFLiteDetector: Yolov5TFLiteDetector
//    private lateinit var boxPaint: Paint
//    private lateinit var textPaint: Paint
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//
//        val locations = readLocationsFromXls(requireContext())
//        val districts = locations.map { it.district }.distinct()
//        val districtAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, districts)
//        binding.Camera3.setAdapter(districtAdapter)
//
//        binding.Camera3.setOnItemClickListener { _, _, position, _ ->
//            val selectedDistrict = districts[position]
//            val filteredAddresses = locations.filter { it.district == selectedDistrict }.map { it.address }
//            val addressAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, filteredAddresses)
//            binding.Camera2.setAdapter(addressAdapter)
//        }
//
//
//        // Получение Uri изображения из Bundle
//        arguments?.getParcelable<Uri>("imageUri")?.let { uri ->
//            // Отображение изображения
//            binding.resultImageView.setImageURI(uri)
//            // Асинхронная отправка изображения на сервер
//            CoroutineScope(Dispatchers.IO).launch {
//                sendImageToServer(uri)
//            }
//        }
//
//        // Добавление обработчика ввода текста в EditText Camera2
//        val cameraEditText = root.findViewById<EditText>(R.id.Camera2)
//        cameraEditText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                Log.d("EditTextInput", "Текст изменен: $s")
//                sharedViewModel.setCamera2Text(s.toString())
//            }
//
//            override fun afterTextChanged(s: Editable) {}
//        })
//
//        // Добавление обработчика ввода текста в EditText opisanie
//        val opisanieEditText = root.findViewById<EditText>(R.id.opisanie)
//        opisanieEditText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                Log.d("EditTextInput", "Текст2 изменен: $s")
//                sharedViewModel.setOpisanieText(s.toString())
//            }
//
//            override fun afterTextChanged(s: Editable) {}
//        })
//
//        // Инициализация детектора и модели
//        yolov5TFLiteDetector = Yolov5TFLiteDetector()
//        yolov5TFLiteDetector.setModelFile("price.tflite")
//        yolov5TFLiteDetector.initialModel(requireContext())
//
//        boxPaint = Paint().apply {
//            strokeWidth = 5f
//            style = Paint.Style.STROKE
//            color = Color.RED
//        }
//
//        // Создание Paint для фона
//        val backgroundPaint = Paint().apply {
//            color = Color.RED
//            style = Paint.Style.FILL
//        }
//
//        // Создание Paint для текста
//        textPaint = Paint().apply {
//            textSize = 60f
//            color = Color.WHITE // Белые буквы
//            style = Paint.Style.FILL
//        }
//
//        val scaleAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.button_press_animation3)
//
//        // Обработка изображения при нажатии на кнопку
//        binding.zagruzit.setOnClickListener {
//            binding.zagruzit.startAnimation(scaleAnimation)
//            binding.zagruzit.postDelayed({
//                // Получение Uri изображения из Bundle
//                arguments?.getParcelable<Uri>("imageUri")?.let { uri ->
//                    val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
//                    val recognitions = yolov5TFLiteDetector.detect(bitmap)
//                    val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
//                    val canvas = Canvas(mutableBitmap)
//
//                    for (recognition in recognitions) {
//                        if (recognition.confidence > 0.4) {
//                            val location = recognition.location
//                            canvas.drawRect(location, boxPaint)
//
//                            val textBounds = Rect()
//                            textPaint.getTextBounds(recognition.labelName, 0, recognition.labelName.length, textBounds)
//                            canvas.drawRect(location.left, location.top - textBounds.height(), location.right, location.top, backgroundPaint)
//
//                            // Рисуем текст поверх фона
//                            canvas.drawText(recognition.labelName, location.left, location.top, textPaint)
//                        }
//                    }
//
//                    val tempFile = File.createTempFile("processed_image", ".png", requireContext().cacheDir)
//                    val outStream = FileOutputStream(tempFile)
//                    mutableBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
//                    outStream.close()
//
//                    val tempFileUri = FileProvider.getUriForFile(
//                        requireContext(),
//                        "com.example.hakaton.fileprovider",
//                        tempFile
//                    )
//
//                    val bundle = Bundle().apply {
//                        putParcelable("imageUri", tempFileUri)
//                    }
//
//                    // Переходим к ResultFragment, передавая Bundle
//                    findNavController().navigate(R.id.action_navigation_notifications_to_fragment_resultat, bundle)
//                }
//            }, scaleAnimation.duration)
//        }
//
//        return root
//    }
//
//    private fun readLocationsFromXls(context: Context): List<Location> {
//        val assetManager = context.assets
//        val inputStream = assetManager.open("locations.xls") // Убедитесь, что файл называется "locations.xls"
//        val workbook = Workbook.getWorkbook(inputStream)
//        val sheet = workbook.getSheet(0)
//        val locations = mutableListOf<Location>()
//
//        for (row in 0 until sheet.rows) {
//            val district = sheet.getCell(0, row).contents
//            val address = sheet.getCell(1, row).contents
//            locations.add(Location(district, address))
//        }
//
//        workbook.close()
//        inputStream.close()
//        return locations
//    }
//
//    data class Location(val district: String, val address: String)
//
//    private fun sendImageToServer(uri: Uri) {
//        val storageRef = Firebase.storage.reference
//        val imageRef = storageRef.child("images/${uri.lastPathSegment}")
//
//        imageRef.putFile(uri).addOnSuccessListener {
//            imageRef.downloadUrl.addOnSuccessListener { uri ->
//                val photoUrl = uri.toString()
//                Log.d("NotificationsFragment", "URL изображения: $photoUrl")
//
//                // Получение выбранного адреса и описания
//                val selectedAddress = binding.Camera2.text.toString()
//                val description = binding.opisanie.text.toString()
//                val district = binding.Camera3.text.toString()
//
//                // Создание запроса с адресом и описанием
//                val request = ImageUploadRequest(photo_url = photoUrl, district = district, area = selectedAddress, description = description)
//                sendPhotoUrlToServer(request)
//            }.addOnFailureListener {
//                Log.e("NotificationsFragment", "Ошибка при получении URL изображения", it)
//            }
//        }.addOnFailureListener {
//            Log.e("NotificationsFragment", "Ошибка при загрузке изображения", it)
//        }
//    }
//
//
//    private fun sendPhotoUrlToServer(request: ImageUploadRequest) {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://982d-95-54-231-188.ngrok-free.app/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val apiService = retrofit.create(ApiService::class.java)
//
//        Log.d("NotificationsFragment", "Отправляем запрос на сервер с описанием: ${request.description}")
//
//        apiService.uploadImage(request).enqueue(object : Callback<ResponseBody> {
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                if (response.isSuccessful) {
//                    Log.d("NotificationsFragment", "Изображение успешно отправлено")
//                } else {
//                    val errorBody = response.errorBody()?.string()
//                    Log.e("NotificationsFragment", "Ошибка при отправке изображения: $errorBody")
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Log.e("NotificationsFragment", "Ошибка сети: ${t.message}")
//            }
//        })
//    }
//
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    data class ImageUploadRequest(val photo_url: String, val district: String, val area: String, val description: String)
//
//    interface ApiService {
//        @POST("predict")
//        fun uploadImage(@Body request: ImageUploadRequest): Call<ResponseBody>
//    }
//}



// РАБОЧИЙ КОД
//class NotificationsFragment : Fragment() {
//
//    private var _binding: FragmentNotificationsBinding? = null
//    private val binding get() = _binding!!
//
//    private val sharedViewModel: SharedViewModel by activityViewModels()
//    private lateinit var yolov5TFLiteDetector: Yolov5TFLiteDetector
//    private lateinit var boxPaint: Paint
//    private lateinit var textPaint: Paint
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        // Получение Uri изображения из Bundle
//        arguments?.getParcelable<Uri>("imageUri")?.let { uri ->
//            // Отображение изображения
//            binding.resultImageView.setImageURI(uri)
//        }
//
//        // Инициализация детектора и модели
//        yolov5TFLiteDetector = Yolov5TFLiteDetector()
//        yolov5TFLiteDetector.setModelFile("price.tflite")
//        yolov5TFLiteDetector.initialModel(requireContext())
//
//        boxPaint = Paint().apply {
//            strokeWidth = 5f
//            style = Paint.Style.STROKE
//            color = Color.RED
//        }
//
//        // Создание Paint для фона
//        val backgroundPaint = Paint().apply {
//            color = Color.RED
//            style = Paint.Style.FILL
//        }
//
//        // Создание Paint для текста
//        val textPaint = Paint().apply {
//            textSize = 100f
//            color = Color.WHITE // Белые буквы
//            style = Paint.Style.FILL
//        }
//
//        val scaleAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.button_press_animation3)
//
//        // Обработка изображения при нажатии на кнопку
//        binding.zagruzit.setOnClickListener {
//            binding.zagruzit.startAnimation(scaleAnimation)
//            binding.zagruzit.postDelayed({
//                // Получение Uri изображения из Bundle
//                arguments?.getParcelable<Uri>("imageUri")?.let { uri ->
//                    val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
//                    val recognitions = yolov5TFLiteDetector.detect(bitmap)
//                    val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
//                    val canvas = Canvas(mutableBitmap)
//
//                    for (recognition in recognitions) {
//                        if (recognition.confidence > 0.4) {
//                            val location = recognition.location
//                            canvas.drawRect(location, boxPaint)
//
//                            val textBounds = Rect()
//                            textPaint.getTextBounds(recognition.labelName, 0, recognition.labelName.length, textBounds)
//                            canvas.drawRect(location.left, location.top - textBounds.height(), location.right, location.top, backgroundPaint)
//
//                            // Рисуем текст поверх фона
//                            canvas.drawText(recognition.labelName, location.left, location.top, textPaint)
//                        }
//                    }
//
//                    // Инициализация ReadImageText для чтения текста с изображения
//                    val readImageText = ReadImageText(requireContext())
//                    val text = readImageText.processImage(mutableBitmap, "rus") // Используйте нужный язык
//
//                    val tempFile = File.createTempFile("processed_image", ".png", requireContext().cacheDir)
//                    val outStream = FileOutputStream(tempFile)
//                    mutableBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
//                    outStream.close()
//
//                    val tempFileUri = FileProvider.getUriForFile(
//                        requireContext(),
//                        "com.example.hakaton.fileprovider",
//                        tempFile
//                    )
//
//                    val bundle = Bundle().apply {
//                        putParcelable("imageUri", tempFileUri)
//                        putString("recognizedText", text) // Добавьте текст в Bundle
//                    }
//
//                    // Переходим к ResultFragment, передавая Bundle
//                    findNavController().navigate(R.id.action_navigation_notifications_to_fragment_resultat, bundle)
//                }
//            }, scaleAnimation.duration)
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

//class NotificationsFragment : Fragment() {
//
//    private var _binding: FragmentNotificationsBinding? = null
//    private val binding get() = _binding!!
//
//    private val sharedViewModel: SharedViewModel by activityViewModels()
//    private lateinit var yolov5TFLiteDetector: Yolov5TFLiteDetector
//    private lateinit var boxPaint: Paint
//    private lateinit var textPaint: Paint
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        // Получение Uri изображения из Bundle
//        arguments?.getParcelable<Uri>("imageUri")?.let { uri ->
//            // Отображение изображения
//            binding.resultImageView.setImageURI(uri)
//        }
//
//        // Инициализация детектора и модели
//        yolov5TFLiteDetector = Yolov5TFLiteDetector()
//        yolov5TFLiteDetector.setModelFile("price.tflite")
//        yolov5TFLiteDetector.initialModel(requireContext())
//
//        boxPaint = Paint().apply {
//            strokeWidth = 5f
//            style = Paint.Style.STROKE
//            color = Color.RED
//        }
//
//        // Создание Paint для фона
//        val backgroundPaint = Paint().apply {
//            color = Color.RED
//            style = Paint.Style.FILL
//        }
//
//        // Создание Paint для текста
//        textPaint = Paint().apply {
//            textSize = 100f
//            color = Color.WHITE // Белые буквы
//            style = Paint.Style.FILL
//        }
//
//        val scaleAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.button_press_animation3)
//
//        // Обработка изображения при нажатии на кнопку
//        binding.zagruzit.setOnClickListener {
//            binding.zagruzit.startAnimation(scaleAnimation)
//            binding.zagruzit.postDelayed({
//                // Получение Uri изображения из Bundle
//                arguments?.getParcelable<Uri>("imageUri")?.let { uri ->
//                    val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
//                    val recognitions = yolov5TFLiteDetector.detect(bitmap)
//                    val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
//                    val canvas = Canvas(mutableBitmap)
//
//                    for (recognition in recognitions) {
//                        if (recognition.confidence > 0.4) {
//                            val location = recognition.location
//                            canvas.drawRect(location, boxPaint)
//
//                            val textBounds = Rect()
//                            textPaint.getTextBounds(recognition.labelName, 0, recognition.labelName.length, textBounds)
//                            canvas.drawRect(location.left, location.top - textBounds.height(), location.right, location.top, backgroundPaint)
//
//                            // Рисуем текст поверх фона
//                            canvas.drawText(recognition.labelName, location.left, location.top, textPaint)
//                        }
//                    }
//
//                    // Инициализация ReadImageText для чтения текста с изображения
//                    val readImageText = ReadImageText(requireContext())
//                    // Предполагаем, что ReadImageText.processImage поддерживает несколько языков через параметр
//                    val languages = listOf("rus", "eng") // Добавьте сюда все языки, которые вы хотите распознать
//                    val recognizedTexts = mutableListOf<String>()
//
//                    for (language in languages) {
//                        val text = readImageText.processImage(mutableBitmap, language)
//                        recognizedTexts.add(text)
//                    }
//
//                    // Теперь recognizedTexts содержит текст, распознанный на всех указанных языках
//                    // Вы можете объединить результаты или обработать их по-разному
//
//                    val tempFile = File.createTempFile("processed_image", ".png", requireContext().cacheDir)
//                    val outStream = FileOutputStream(tempFile)
//                    mutableBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
//                    outStream.close()
//
//                    val tempFileUri = FileProvider.getUriForFile(
//                        requireContext(),
//                        "com.example.hakaton.fileprovider",
//                        tempFile
//                    )
//
//                    val bundle = Bundle().apply {
//                        putParcelable("imageUri", tempFileUri)
//                        // Добавьте текст в Bundle, например, объединив результаты распознавания
//                        putString("recognizedText", recognizedTexts.joinToString(separator = "\n"))
//                    }
//
//                    // Переходим к ResultFragment, передавая Bundle
//                    findNavController().navigate(R.id.action_navigation_notifications_to_fragment_resultat, bundle)
//                }
//            }, scaleAnimation.duration)
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





//class NotificationsFragment : Fragment() {
//
//    private var _binding: FragmentNotificationsBinding? = null
//    private val binding get() = _binding!!
//
//    private val sharedViewModel: SharedViewModel by activityViewModels()
//    private lateinit var prices: Interpreter
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        // Инициализация интерпретатора TensorFlow Lite
//        prices = initializePriceModel() // Инициализация модели
//
//        // Добавление обработчика ввода текста в EditText Camera2
//        val cameraEditText = root.findViewById<EditText>(R.id.Camera2)
//        cameraEditText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                Log.d("EditTextInput", "Текст изменен: $s")
//                sharedViewModel.setCamera2Text(s.toString())
//            }
//
//            override fun afterTextChanged(s: Editable) {}
//        })
//
//        // Добавление обработчика ввода текста в EditText opisanie
//        val opisanieEditText = root.findViewById<EditText>(R.id.opisanie)
//        opisanieEditText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                Log.d("EditTextInput", "Текст2 изменен: $s")
//                sharedViewModel.setOpisanieText(s.toString())
//            }
//
//            override fun afterTextChanged(s: Editable) {}
//        })
//
//        // Получение Uri изображения из Bundle
//        arguments?.getParcelable<Uri>("imageUri")?.let { uri ->
//            binding.resultImageView.setImageURI(uri)
//            processImageWithPriceModel(uri)
//        }
//
//        // Добавление обработчика нажатия на кнопку
//        binding.zagruzit.setOnClickListener {
//            // Переход на следующий фрагмент
//            findNavController().navigate(R.id.action_navigation_notifications_to_fragment_resultat)
//        }
//
//        return root
//    }
//
//    private fun initializePriceModel(): Interpreter {
//        val assetManager = context?.assets
//        val modelFile = assetManager?.open("price.tflite")
//        val byteBuffer = modelFile?.let {
//            val bytes = ByteArray(it.available())
//            it.read(bytes)
//            ByteBuffer.allocateDirect(bytes.size).apply {
//                order(ByteOrder.nativeOrder())
//                put(bytes)
//                position(0)
//            }
//        } ?: throw IllegalStateException("Price model file not found or could not be read.")
//        return Interpreter(byteBuffer)
//    }
//
//    private fun convertBitmapToByteBufferForPrice(bitmap: Bitmap): ByteBuffer {
//        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 640, 640, true)
//        val byteBuffer = ByteBuffer.allocateDirect(4 * 640 * 640 * 3)
//        byteBuffer.order(ByteOrder.nativeOrder())
//        val intValues = IntArray(640 * 640)
//        scaledBitmap.getPixels(intValues, 0, scaledBitmap.width, 0, 0, scaledBitmap.width, scaledBitmap.height)
//        var pixel = 0
//        for (i in 0 until 640) {
//            for (j in 0 until 640) {
//                val `val` = intValues[pixel++]
//                byteBuffer.putFloat(((`val` shr 16 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
//                byteBuffer.putFloat(((`val` shr 8 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
//                byteBuffer.putFloat(((`val` and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
//            }
//        }
//        byteBuffer.flip() // Перемещаем позицию буфера обратно в начало
//        return byteBuffer
//    }
//
//
//
//    private fun processImageWithPriceModel(imageUri: Uri) {
//        // Загрузка и преобразование изображения
//        val bitmap = BitmapFactory.decodeStream(context?.contentResolver?.openInputStream(imageUri))
//        val byteBuffer = convertBitmapToByteBufferForPrice(bitmap)
//
//        // Выполнение модели
//        val outputBuffer = ByteBuffer.allocateDirect(4 * 640 * 640 * 3) // Размер выходных данных модели
//        outputBuffer.order(ByteOrder.nativeOrder())
//        prices.run(byteBuffer, outputBuffer)
//
//        // Обработка результатов
//        // Предполагая, что выходные данные модели представляют собой набор координат bounding boxes
//        // Вам нужно будет адаптировать эту часть в соответствии с форматом выходных данных вашей модели
//        val output = Array(1) { Array(640) { Array(640) { FloatArray(1) } } }
//        outputBuffer.rewind() // Перемещаем позицию буфера обратно в начало
//        for (i in 0 until 640) {
//            for (j in 0 until 640) {
//                // Пример обработки данных, вам нужно будет адаптировать эту часть
//                output[0][i][j] = FloatArray(1)
//                output[0][i][j][0] = outputBuffer.getFloat()
//            }
//        }
//        val boundingBoxes = extractBoundingBoxes(output) // Используем адаптированную функцию для извлечения bounding boxes
//        drawBoundingBoxes(binding.resultImageView, boundingBoxes)
//    }
//
//
//
//    private fun extractBoundingBoxes(output: Array<Array<Array<FloatArray>>>): List<BoundingBox> {
//        val boundingBoxes = mutableListOf<BoundingBox>()
//        // Пример обработки данных, вам нужно будет адаптировать эту часть
//        for (i in 0 until output[0].size) {
//            for (j in 0 until output[0][i].size) {
//                val left = output[0][i][j][0] // Пример извлечения координат bounding box
//                val top = output[0][i][j][0] // Пример извлечения координат bounding box
//                val right = output[0][i][j][0] // Пример извлечения координат bounding box
//                val bottom = output[0][i][j][0] // Пример извлечения координат bounding box
//                boundingBoxes.add(BoundingBox(left, top, right, bottom))
//            }
//        }
//        return boundingBoxes
//    }
//
//
//
//
//    private fun drawBoundingBoxes(imageView: ImageView, boundingBoxes: List<BoundingBox>) {
//        imageView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                // Удаляем слушатель, чтобы он не вызывался повторно
//                imageView.viewTreeObserver.removeOnGlobalLayoutListener(this)
//
//                // Теперь размеры imageView известны и положительны
//                val bitmap = Bitmap.createBitmap(imageView.width, imageView.height, Bitmap.Config.ARGB_8888)
//                val canvas = Canvas(bitmap)
//                val paint = Paint().apply {
//                    color = Color.RED
//                    style = Paint.Style.STROKE
//                    strokeWidth = 4f
//                }
//
//                // Рисуем каждый боудинг бокс
//                for (box in boundingBoxes) {
//                    canvas.drawRect(box.left, box.top, box.right, box.bottom, paint)
//                }
//
//                imageView.setImageBitmap(bitmap)
//            }
//        })
//    }
//
//
//
//    companion object {
//        private const val IMAGE_MEAN = 128.0f
//        private const val IMAGE_STD = 128.0f
//    }
//
//
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}










