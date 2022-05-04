package uz.evkalipt.sixmodullesson512

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.evkalipt.sixmodullesson512.adapters.AdapterForRv
import uz.evkalipt.sixmodullesson512.databinding.FragmentForPagerBinding
import uz.evkalipt.sixmodullesson512.db.MyDBHelper
import uz.evkalipt.sixmodullesson512.models.Category
import uz.evkalipt.sixmodullesson512.models.Rule
import uz.evkalipt.sixmodullesson512.utils.OnDataPass
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"

class ForPagerFragment : Fragment() {

    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    lateinit var adapterForRv: AdapterForRv
    lateinit var myDBHelper: MyDBHelper
    lateinit var onDataPass: OnDataPass
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentForPagerBinding.inflate(layoutInflater)
        myDBHelper = MyDBHelper(binding.root.context)
        var listRule = ArrayList<Rule>()
        for (i in 0 until myDBHelper.getAll().size){
            if (myDBHelper.getAll()[i].str == param1){
                listRule.add(myDBHelper.getAll()[i])
            }
        }
        adapterForRv = AdapterForRv(listRule, object :AdapterForRv.OnMyClickListener{
            override fun onClickLove(rule: Rule, position:Int) {
                var love = -1
                if (rule.love == 0){
                    love =1
                }else{
                    love = 0
                }
                var rule1 = Rule(rule.id, rule.title, rule.description, love, rule.str, rule.imagePath)
                myDBHelper.update(rule1)
                listRule.clear()
                for (i in 0 until myDBHelper.getAll().size){
                    if (myDBHelper.getAll()[i].str == param1){
                        listRule.add(myDBHelper.getAll()[i])
                    }
                }
                adapterForRv.notifyItemRangeChanged(position, listRule.size)
            }

            override fun delete(rule: Rule, position: Int) {
                var alertDialog = AlertDialog.Builder(binding.root.context)
                alertDialog.setMessage("O'chirmoqchimisiz?")
                alertDialog.setPositiveButton("Ha", object :DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        myDBHelper.delete(rule)
                        listRule.clear()
                        for (i in 0 until myDBHelper.getAll().size){
                            if (myDBHelper.getAll()[i].str == param1){
                                listRule.add(myDBHelper.getAll()[i])
                            }
                        }
                        adapterForRv.notifyItemRemoved(position)
                    }

                })

                alertDialog.setNegativeButton("Yo'q", object :DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {

                    }

                })
                alertDialog.show()
            }

            override fun edit(rule: Rule) {
                var bundle = Bundle()
                bundle.putInt("id", rule.id!!)
                findNavController().navigate(R.id.editFragment, bundle)
                onDataPass.onDataPass(1)
            }

            override fun getOneSetOnClick(rule: Rule) {
                var bundle = Bundle()
                bundle.putInt("id", rule.id!!)
                findNavController().navigate(R.id.getOneFragment, bundle)
                onDataPass.onDataPass(1)
            }

        })
        binding.rv.adapter = adapterForRv

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onDataPass = context as OnDataPass
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            ForPagerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}