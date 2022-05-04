package uz.evkalipt.sixmodullesson512

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import uz.evkalipt.sixmodullesson512.databinding.FragmentGetOneBinding
import uz.evkalipt.sixmodullesson512.db.MyDBHelper
import uz.evkalipt.sixmodullesson512.utils.OnDataPass

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GetOneFragment : Fragment() {
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
    lateinit var onDataPass: OnDataPass
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGetOneBinding.inflate(layoutInflater)
        myDBHelper = MyDBHelper(binding.root.context)
        val id = arguments?.getInt("id")
        val rule = myDBHelper.getOne(id!!)

        if (!rule.imagePath?.trim().equals("")){
            binding.imageGet.setImageURI(Uri.parse(rule.imagePath))
        }

        binding.tvTitleGet.text = rule.title
        binding.tvDescriptionGet.text = rule.description
        binding.tvToolBar.text = rule.title

        binding.backToolImage.setOnClickListener {
            findNavController().popBackStack()
            onDataPass.onDataPass(0)
        }

//        var a = object :OnBackPressedCallback(true){
//            override fun handleOnBackPressed() {
//                findNavController().popBackStack()
//                onDataPass.onDataPass(0)
//            }
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GetOneFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}