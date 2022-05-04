package uz.evkalipt.sixmodullesson512

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.github.florent37.runtimepermission.kotlin.askPermission
import uz.evkalipt.sixmodullesson512.adapters.SpinnerAdapter
import uz.evkalipt.sixmodullesson512.databinding.FragmentAddBinding
import uz.evkalipt.sixmodullesson512.databinding.ItemForAlertAddImageBinding
import uz.evkalipt.sixmodullesson512.db.MyDBHelper
import uz.evkalipt.sixmodullesson512.models.Rule
import uz.evkalipt.sixmodullesson512.utils.OnDataPass
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentAddBinding
    lateinit var photoURI: Uri
    lateinit var filePath: String
    lateinit var listSpinner: ArrayList<String>
    lateinit var spinnerAdapter: SpinnerAdapter
    lateinit var myDBHelper: MyDBHelper
    lateinit var onDataPass: OnDataPass
    var maxId = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(layoutInflater)
        myDBHelper = MyDBHelper(binding.root.context)

        val all = myDBHelper.getAll()
        if (all.size > 0) {
            maxId = all[all.size - 1].id!! + 1
        } else {
            maxId = 0
        }

        loadSpinnerList()
        filePath = ""
        spinnerAdapter = SpinnerAdapter(listSpinner)
        binding.spinner.adapter = spinnerAdapter

        binding.backAdd.setOnClickListener {
            findNavController().popBackStack()
            onDataPass.onDataPass(0)
        }

        binding.imageAdd.setOnClickListener {
            var alertDialog = AlertDialog.Builder(binding.root.context)
            val inflate = ItemForAlertAddImageBinding.inflate(layoutInflater)
            alertDialog.setView(inflate.root)
            alertDialog.show()
            inflate.ivCamera.setOnClickListener {
                askPermission(android.Manifest.permission.CAMERA) {
                    var imageFile = createImageFile()
                    photoURI = FileProvider.getUriForFile(
                        binding.root.context,
                        BuildConfig.APPLICATION_ID,
                        imageFile
                    )
                    getTakeImageContent.launch(photoURI)
                }.onDeclined { e ->
                    if (e.hasDenied()) {
                        AlertDialog.Builder(binding.root.context)
                            .setMessage("Qurilmangizda cameradan foydalanishga ruxsat bering!")
                            .setPositiveButton("yes") { dialog, wich ->
                                e.askAgain()
                            }.setNegativeButton("No") { dialog, wich ->
                                dialog.dismiss()
                            }.show()
                    }
                    if (e.hasForeverDenied()) {
                        e.goToSettings()
                    }
                }
            }

            inflate.ivFile.setOnClickListener {

                askPermission(android.Manifest.permission.CAMERA) {
                    pickImage()
                }.onDeclined { e ->
                    if (e.hasDenied()) {
                        AlertDialog.Builder(binding.root.context)
                            .setMessage("Qurilmangizda cameradan foydalanishga ruxsat bering!")
                            .setPositiveButton("yes") { dialog, wich ->
                                e.askAgain()
                            }.setNegativeButton("No") { dialog, wich ->
                                dialog.dismiss()
                            }.show()
                    }
                    if (e.hasForeverDenied()) {
                        e.goToSettings()
                    }
                }
            }
        }

        binding.saveBt.setOnClickListener {
            var title = binding.etTitle.text.toString()
            var description = binding.etDescription.text.toString()
            val selectedItem = binding.spinner.selectedItem.toString()
            var count = 0
            var path = filePath
            if (!title.trim().equals("") && !description.trim().equals("")) {
                var rule = Rule(title, description, count, selectedItem, path)
                myDBHelper.insert(rule)
                Toast.makeText(binding.root.context, "Saqlandi!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
                onDataPass.onDataPass(0)
            } else {
                Toast.makeText(binding.root.context, "Qatorlarni to'ldiring!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

//        var a = object :OnBackPressedCallback(true){
//            override fun handleOnBackPressed() {
//                onDataPass.onDataPass(0)
//                findNavController().popBackStack()
//            }
//
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(a)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        onDataPass.onDataPass(0)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onDataPass = context as OnDataPass
    }

    private fun pickImage() {
        getImageContent.launch("image/*")
    }

    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult
            binding.imageAdd.setImageURI(uri)

            val openInputStream = requireActivity().contentResolver.openInputStream(uri)
            val file = File(binding.root.context.filesDir, "image$maxId.jpg")
            val fileOutputStream = FileOutputStream(file)
            openInputStream?.copyTo(fileOutputStream)
            openInputStream?.close()
            fileOutputStream.close()
            val absolutePath = file.absolutePath
            filePath = absolutePath
        }

    private fun loadSpinnerList() {
        listSpinner = ArrayList()
        listSpinner.add("Ogohlantiruvchi")
        listSpinner.add("Imtiyozli")
        listSpinner.add("Ta'qiqlovchi")
        listSpinner.add("Buyuruvchi")
    }

    private val getTakeImageContent =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                binding.imageAdd.setImageURI(photoURI)

                val openInputStream = binding.root.context.contentResolver.openInputStream(photoURI)
                val file = File(binding.root.context.filesDir, "image$maxId.jpg")
                val fileOutpuStream = FileOutputStream(file)
                openInputStream?.copyTo(fileOutpuStream)
                openInputStream?.close()
                fileOutpuStream.close()
                val fileAbsolutePath = file.absolutePath
                filePath = fileAbsolutePath
            }
        }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val format = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

        val externalFilesDir =
            binding.root.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_$format", ".jpg", externalFilesDir).apply{}
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}