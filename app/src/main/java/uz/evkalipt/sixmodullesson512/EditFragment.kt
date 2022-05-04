package uz.evkalipt.sixmodullesson512

import android.app.AlertDialog
import android.content.Context
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.*
import uz.evkalipt.sixmodullesson512.adapters.SpinnerAdapter
import uz.evkalipt.sixmodullesson512.databinding.FragmentEditBinding
import uz.evkalipt.sixmodullesson512.databinding.ItemForAlertAddImageBinding
import uz.evkalipt.sixmodullesson512.db.MyDBHelper
import uz.evkalipt.sixmodullesson512.models.Rule
import uz.evkalipt.sixmodullesson512.utils.OnDataPass
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EditFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    lateinit var myDBHelper: MyDBHelper
    lateinit var listSpinner:ArrayList<String>
    lateinit var spinnerAdapter: SpinnerAdapter
    lateinit var currentImagePath: String
    lateinit var photoURI: Uri
    lateinit var filePath: String
    lateinit var binding: FragmentEditBinding
    lateinit var onDataPass: OnDataPass
    var id:Int? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditBinding.inflate(layoutInflater)
        myDBHelper = MyDBHelper(binding.root.context)
        filePath = ""

        var indexSpinner = -1

        binding.backEdit.setOnClickListener {
            findNavController().popBackStack()
            onDataPass.onDataPass(0)
        }

         id = arguments?.getInt("id")
        val rule = myDBHelper.getOne(id!!)

        listSpinner = ArrayList()
        listSpinner.add("Ogohlantiruvchi")
        listSpinner.add("Imtiyozli")
        listSpinner.add("Ta'qiqlovchi")
        listSpinner.add("Buyuruvchi")

        for (i in 0 until listSpinner.size){
            if (rule.str == listSpinner[i]){
                indexSpinner = i
            }
        }

        spinnerAdapter = SpinnerAdapter(listSpinner)
        binding.spinnerEdit.adapter = spinnerAdapter

        binding.imageEdit.setImageURI(Uri.parse(rule.imagePath))
        binding.etTitleEdit.setText(rule.title)
        binding.etDescriptionEdit.setText(rule.description)
        binding.spinnerEdit.setSelection(indexSpinner)

        binding.imageEdit.setOnClickListener {
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
        binding.saveBtEdit.setOnClickListener {
            var filePath2:String = ""
            var title = binding.etTitleEdit.text.toString()
            var description = binding.etDescriptionEdit.text.toString()
            var selectedSpinner = binding.spinnerEdit.selectedItem.toString()

            if (!filePath.trim().equals("")){
                filePath2 = filePath
            }else{
                filePath2 = rule.imagePath!!
            }

            if (!title.trim().equals("") && !description.trim().equals("")) {
                var ruleEdit = Rule(id, title, description, rule.love, selectedSpinner, filePath2)
                myDBHelper.update(ruleEdit)
                Toast.makeText(binding.root.context, "O'zgardi", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }else{
                Toast.makeText(binding.root.context, "Qatorlarni to'ldiring!", Toast.LENGTH_SHORT).show()
            }

        }

//        var a = object :OnBackPressedCallback(true){
//            override fun handleOnBackPressed() {
//                (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId=R.id.home_menu
//
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, a)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        onDataPass.onDataPass(0)
    }

    private fun pickImage() {
        getImageContent.launch("image/*")
    }

    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult
            binding.imageEdit.setImageURI(uri)

            val openInputStream = requireActivity().contentResolver.openInputStream(uri)
            val file = File(binding.root.context.filesDir, "image$id.jpg")
            val fileOutputStream = FileOutputStream(file)
            openInputStream?.copyTo(fileOutputStream)
            openInputStream?.close()
            fileOutputStream.close()
            val absolutePath = file.absolutePath
            filePath = absolutePath
        }

    private val getTakeImageContent =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                binding.imageEdit.setImageURI(photoURI)
                val openInputStream = binding.root.context.contentResolver.openInputStream(photoURI)
                val file = File(binding.root.context.filesDir, "image$id.jpg")
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
        return File.createTempFile("JPEG_$format", ".jpg", externalFilesDir).apply {
            currentImagePath = absolutePath
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onDataPass = context as OnDataPass
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}