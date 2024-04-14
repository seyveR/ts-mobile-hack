package com.example.hakaton.ui.dashboard

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hakaton.BoundingBox
import com.example.hakaton.R
import com.example.hakaton.Yolov5TFLiteDetector
import com.example.hakaton.databinding.FragmentDashboardBinding
import com.example.hakaton.ui.SharedViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File

//class DashboardFragment : Fragment() {
//
//    private var _binding: FragmentDashboardBinding? = null
//    private val binding get() = _binding!!
//
//
//    private val sharedViewModel: SharedViewModel by viewModels()
//
//    private val captureImage = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
//        if (success) {
//            sharedViewModel.setImageUri(imageUri)
//        }
//    }
//
//    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//        uri?.let {
//            sharedViewModel.setImageUri(it)
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val dashboardViewModel =
//            ViewModelProvider(this).get(DashboardViewModel::class.java)
//
//        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        binding.Camera.setOnClickListener {
//            // Открытие камеры
//            val imageUri = createImageUri()
//            captureImage.launch(imageUri)
//        }
//
//        binding.galereya.setOnClickListener {
//            // Выбор изображения из галереи
//            selectImage.launch("image/*")
//        }
//
//        return root
//    }
//
//    private fun createImageUri(): Uri {
//        val image = File(requireContext().filesDir, "camera_photos.png")
//        return FileProvider.getUriForFile(
//            requireContext(),
//            "com.example.hakaton.fileprovider",
//            image
//        )
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}






class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val bundle = Bundle().apply {
                putParcelable("imageUri", it)
            }
            findNavController().navigate(R.id.action_navigation_dashboard_to_navigation_notifications, bundle)
        }
    }

    private val captureImage = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            lastImageUri?.let {
                val bundle = Bundle().apply {
                    putParcelable("imageUri", it)
                }
                findNavController().navigate(R.id.action_navigation_dashboard_to_navigation_notifications, bundle)
            }
        }
    }

    private var lastImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val scaleAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.button_press_animation2)

        binding.Camera.setOnClickListener {
            binding.Camera.startAnimation(scaleAnimation)
            val imageUri = createImageUri()
            lastImageUri = imageUri
            captureImage.launch(imageUri)
        }

        binding.galereya.setOnClickListener {
            binding.galereya.startAnimation(scaleAnimation)
            selectImage.launch("image/*")
        }

        return root
    }

    private fun createImageUri(): Uri {
        val image = File(requireContext().filesDir, "camera_photos.png")
        return FileProvider.getUriForFile(
            requireContext(),
            "com.example.hakaton.fileprovider",
            image
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findNavController().addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.navigation_notifications) {
                hideBottomNavigationView()
            } else {
                showBottomNavigationView()
            }
        }
    }

    private fun hideBottomNavigationView() {
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView?.visibility = View.GONE
    }

    private fun showBottomNavigationView() {
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView?.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




//class DashboardFragment : Fragment() {
//
//    private var _binding: FragmentDashboardBinding? = null
//    private val binding get() = _binding!!
//
//    private lateinit var lastImageUri: Uri
//    private lateinit var captureImage: ActivityResultLauncher<Uri>
//    private lateinit var selectImage: ActivityResultLauncher<String>
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    val scaleAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.button_press_animation2)
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        captureImage = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
//            if (success) {
//                sendImageToServer(lastImageUri)
//            }
//        }
//
//        selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//            uri?.let {
//                sendImageToServer(it)
//            }
//        }
//
//        binding.Camera.setOnClickListener {
//            binding.Camera.startAnimation(scaleAnimation)
//            val imageUri = createImageUri()
//            lastImageUri = imageUri
//            captureImage.launch(imageUri)
//        }
//
//        binding.galereya.setOnClickListener {
//            selectImage.launch("image/*")
//        }
//    }
//
//    private fun createImageUri(): Uri {
//        val image = File(requireContext().filesDir, "camera_photos.png")
//        return FileProvider.getUriForFile(
//            requireContext(),
//            "com.example.hakaton.fileprovider",
//            image
//        )
//    }
//
//    private fun sendImageToServer(uri: Uri) {
//        val storageRef = Firebase.storage.reference
//        val imageRef = storageRef.child("images/${uri.lastPathSegment}")
//
//        imageRef.putFile(uri).addOnSuccessListener {
//            imageRef.downloadUrl.addOnSuccessListener { uri ->
//                val photoUrl = uri.toString()
//                Log.d("DashboardFragment", "URL изображения: $photoUrl")
//                sendPhotoUrlToServer(photoUrl)
//            }.addOnFailureListener {
//                Log.e("DashboardFragment", "Ошибка при получении URL изображения", it)
//            }
//        }.addOnFailureListener {
//            Log.e("DashboardFragment", "Ошибка при загрузке изображения", it)
//        }
//    }
//
//    private fun sendPhotoUrlToServer(photoUrl: String) {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://f185-95-54-231-188.ngrok-free.app/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val apiService = retrofit.create(ApiService::class.java)
//
//        apiService.uploadImage(photoUrl).enqueue(object : Callback<ResponseBody> {
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                if (response.isSuccessful) {
//                    Log.d("DashboardFragment", "Изображение успешно отправлено")
//                } else {
//                    val errorBody = response.errorBody()?.string()
//                    Log.e("DashboardFragment", "Ошибка при отправке изображения: $errorBody")
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Log.e("DashboardFragment", "Ошибка сети: ${t.message}")
//            }
//        })
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    interface ApiService {
//        @GET("predict")
//        fun uploadImage(@Query("photo_url") photoUrl: String): Call<ResponseBody>
//    }
//}



//class DashboardFragment : Fragment() {
//
//    private var _binding: FragmentDashboardBinding? = null
//    private val binding get() = _binding!!
//
//    private lateinit var prices: Interpreter
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        // Инициализация интерпретатора TensorFlow Lite
//        prices = initializePriceModel()
//
//        binding.Camera.setOnClickListener {
//            val imageUri = createImageUri()
//            lastImageUri = imageUri
//            captureImage.launch(imageUri)
//        }
//
//        binding.galereya.setOnClickListener {
//            selectImage.launch("image/*")
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
//    private fun createImageUri(): Uri {
//        val image = File(requireContext().filesDir, "camera_photos.png")
//        return FileProvider.getUriForFile(
//            requireContext(),
//            "com.example.hakaton.fileprovider",
//            image
//        )
//    }
//
//    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//        uri?.let {
//            val processedImageUri = processImageWithPriceModel(it)
//            val bundle = Bundle().apply {
//                putParcelable("imageUri", processedImageUri)
//            }
//            findNavController().navigate(R.id.action_navigation_dashboard_to_navigation_notifications, bundle)
//        }
//    }
//
//    private val captureImage = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
//        if (success) {
//            lastImageUri?.let {
//                val processedImageUri = processImageWithPriceModel(it)
//                val bundle = Bundle().apply {
//                    putParcelable("imageUri", processedImageUri)
//                }
//                findNavController().navigate(R.id.action_navigation_dashboard_to_navigation_notifications, bundle)
//            }
//        }
//    }
//
//    private var lastImageUri: Uri? = null
//
//    private fun processImageWithPriceModel(imageUri: Uri): Uri {
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
//        val output = Array(1) { Array(640) { Array(640) { FloatArray(1) } } }
//        outputBuffer.rewind() // Перемещаем позицию буфера обратно в начало
//        for (i in 0 until 640) {
//            for (j in 0 until 640) {
//                output[0][i][j] = FloatArray(1)
//                output[0][i][j][0] = outputBuffer.getFloat()
//            }
//        }
//        val boundingBoxes = extractBoundingBoxes(output) // Используем адаптированную функцию для извлечения bounding boxes
//        val processedBitmap = drawBoundingBoxes(bitmap, boundingBoxes)
//
//        // Сохраняем обработанное изображение в файл и возвращаем его Uri
//        val processedImageFile = File(requireContext().filesDir, "processed_image.png")
//        val outputStream = FileOutputStream(processedImageFile)
//        processedBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//        outputStream.close()
//        return FileProvider.getUriForFile(
//            requireContext(),
//            "com.example.hakaton.fileprovider",
//            processedImageFile
//        )
//    }
//
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
//    private fun extractBoundingBoxes(output: Array<Array<Array<FloatArray>>>): List<BoundingBox> {
//        val boundingBoxes = mutableListOf<BoundingBox>()
//        // Предполагаем, что выходные данные модели содержат координаты для каждого bounding box
//        // Этот код нужно адаптировать под формат выходных данных вашей модели
//        for (i in 0 until output[0].size) {
//            for (j in 0 until output[0][i].size) {
//                // Пример извлечения координат bounding box
//                // Вам нужно заменить эти значения на реальные координаты из выходных данных модели
//                val x = output[0][i][j][0] // Пример извлечения координат bounding box
//                val y = output[0][i][j][1] // Пример извлечения координат bounding box
//                val x2 = output[0][i][j][2] // Пример извлечения координат bounding box
//                val y2 = output[0][i][j][3] // Пример извлечения координат bounding box
//                boundingBoxes.add(BoundingBox(x, y, x2, y2))
//            }
//        }
//        return boundingBoxes
//    }
//
//
//
//
//
//    private fun drawBoundingBoxes(bitmap: Bitmap, boundingBoxes: List<BoundingBox>): Bitmap {
//        val processedBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(processedBitmap)
//        val paint = Paint().apply {
//            color = Color.RED
//            style = Paint.Style.STROKE
//            strokeWidth = 4f
//        }
//
//        // Рисуем исходное изображение на новом битмапе
//        canvas.drawBitmap(bitmap, 0f, 0f, null)
//
//        // Рисуем каждый bounding box
//        for (box in boundingBoxes) {
//            canvas.drawRect(box.left, box.top, box.right, box.bottom, paint)
//        }
//
//        return processedBitmap
//    }
//
//
//
//
//    companion object {
//        private const val IMAGE_MEAN = 128.0f
//        private const val IMAGE_STD = 128.0f
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}





//class DashboardFragment : Fragment() {
//
//    private var _binding: FragmentDashboardBinding? = null
//    private val binding get() = _binding!!
//
//    private val sharedViewModel: SharedViewModel by viewModels()
//
//    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//        uri?.let {
//            val bundle = Bundle().apply {
//                putParcelable("imageUri", it)
//            }
//            findNavController().navigate(R.id.action_navigation_dashboard_to_navigation_notifications, bundle)
//        }
//    }
//
//    private val captureImage = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
//        if (success) {
//            lastImageUri?.let {
//                val bundle = Bundle().apply {
//                    putParcelable("imageUri", it)
//                }
//                findNavController().navigate(R.id.action_navigation_dashboard_to_navigation_notifications, bundle)
//            }
//        }
//    }
//
//    private var lastImageUri: Uri? = null
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        binding.Camera.setOnClickListener {
//            val imageUri = createImageUri()
//            lastImageUri = imageUri
//            captureImage.launch(imageUri)
//        }
//
//        binding.galereya.setOnClickListener {
//            selectImage.launch("image/*")
//        }
//
//        // Добавление обработчика ввода текста в EditText
//        binding.Camera.setOnEditorActionListener { _, _, _ ->
//            val text = binding.Camera.text.toString()
//            sharedViewModel.setCameraText(text)
//            true
//        }
//
//        return root
//    }
//
//    private fun createImageUri(): Uri {
//        val image = File(requireContext().filesDir, "camera_photos.png")
//        return FileProvider.getUriForFile(
//            requireContext(),
//            "com.example.yourapp.fileprovider",
//            image
//        )
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        findNavController().addOnDestinationChangedListener { _, destination, _ ->
//            if (destination.id == R.id.navigation_notifications) {
//                hideBottomNavigationView()
//            } else {
//                showBottomNavigationView()
//            }
//        }
//    }
//
//    private fun hideBottomNavigationView() {
//        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
//        bottomNavigationView?.visibility = View.GONE
//    }
//
//    private fun showBottomNavigationView() {
//        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
//        bottomNavigationView?.visibility = View.VISIBLE
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}



//class DashboardFragment : Fragment() {
//
//    private var _binding: FragmentDashboardBinding? = null
//    private val binding get() = _binding!!
//
//    private val sharedViewModel: SharedViewModel by viewModels()
//
//    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//        uri?.let {
//            processImage(it)
//        }
//    }
//
//    private val captureImage = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
//        if (success) {
//            lastImageUri?.let {
//                processImage(it)
//            }
//        }
//    }
//
//    private var lastImageUri: Uri? = null
//    private lateinit var yolov5TFLiteDetector: Yolov5TFLiteDetector
//    private lateinit var boxPaint: Paint
//    private lateinit var textPaint: Paint
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
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
//        textPaint = Paint().apply {
//            textSize = 50f
//            color = Color.GREEN
//            style = Paint.Style.FILL
//        }
//
//        binding.Camera.setOnClickListener {
//            val imageUri = createImageUri()
//            lastImageUri = imageUri
//            captureImage.launch(imageUri)
//        }
//
//        binding.galereya.setOnClickListener {
//            selectImage.launch("image/*")
//        }
//
//        return root
//    }
//
//    private fun createImageUri(): Uri {
//        val image = File(requireContext().filesDir, "camera_photos.png")
//        return FileProvider.getUriForFile(
//            requireContext(),
//            "com.example.yourapp.fileprovider",
//            image
//        )
//    }
//
//    private fun processImage(uri: Uri) {
//        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
//        val recognitions = yolov5TFLiteDetector.detect(bitmap)
//        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
//        val canvas = Canvas(mutableBitmap)
//
//        for (recognition in recognitions) {
//            if (recognition.confidence > 0.4) {
//                val location = recognition.location
//                canvas.drawRect(location, boxPaint)
//                canvas.drawText(recognition.labelName + ":" + recognition.confidence, location.left, location.top, textPaint)
//            }
//        }
//
//        // Создаем Bundle для передачи обработанного изображения
//        val bundle = Bundle().apply {
//            putParcelable("imageUri", uri)
//        }
//
//        // Переходим к NotificationsFragment, передавая Bundle
//        findNavController().navigate(R.id.action_navigation_dashboard_to_navigation_notifications, bundle)
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}


//class DashboardFragment : Fragment() {
//
//    private var _binding: FragmentDashboardBinding? = null
//    private val binding get() = _binding!!
//
//    private val sharedViewModel: SharedViewModel by viewModels()
//
//    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//        uri?.let {
//            processImage(it)
//        }
//    }
//
//    private val captureImage = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
//        if (success) {
//            lastImageUri?.let {
//                processImage(it)
//            }
//        }
//    }
//
//    private var lastImageUri: Uri? = null
//    private lateinit var yolov5TFLiteDetector: Yolov5TFLiteDetector
//    private lateinit var boxPaint: Paint
//    private lateinit var textPaint: Paint
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
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
//        textPaint = Paint().apply {
//            textSize = 50f
//            color = Color.GREEN
//            style = Paint.Style.FILL
//        }
//
//        binding.Camera.setOnClickListener {
//            val imageUri = createImageUri()
//            lastImageUri = imageUri
//            captureImage.launch(imageUri)
//        }
//
//        binding.galereya.setOnClickListener {
//            selectImage.launch("image/*")
//        }
//
//        return root
//    }
//
//    private fun createImageUri(): Uri {
//        val image = File(requireContext().filesDir, "camera_photos.png")
//        return FileProvider.getUriForFile(
//            requireContext(),
//            "com.example.yourapp.fileprovider",
//            image
//        )
//    }
//
//    private fun processImage(uri: Uri) {
//        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
//        val recognitions = yolov5TFLiteDetector.detect(bitmap)
//        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
//        val canvas = Canvas(mutableBitmap)
//
//        for (recognition in recognitions) {
//            if (recognition.confidence > 0.4) {
//                val location = recognition.location
//                canvas.drawRect(location, boxPaint)
//                canvas.drawText(recognition.labelName + ":" + recognition.confidence, location.left, location.top, textPaint)
//            }
//        }
//
//        // Загружаем обработанное изображение в ImageView
//        binding.resultImageView22.setImageBitmap(mutableBitmap)
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}









