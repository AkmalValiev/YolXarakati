package uz.evkalipt.sixmodullesson512

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.evkalipt.sixmodullesson512.adapters.AdapterForRv
import uz.evkalipt.sixmodullesson512.databinding.FragmentLoveBinding
import uz.evkalipt.sixmodullesson512.db.MyDBHelper
import uz.evkalipt.sixmodullesson512.models.Rule

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LoveFragment : Fragment() {
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
    lateinit var adapterForRv: AdapterForRv
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoveBinding.inflate(layoutInflater)
        myDBHelper = MyDBHelper(binding.root.context)
        val all = myDBHelper.getAll()
        var list = ArrayList<Rule>()
        for (i in 0 until all.size){
            if (all[i].love == 1){
                list.add(all[i])
            }
        }

        adapterForRv = AdapterForRv(list, object :AdapterForRv.OnMyClickListener{
            override fun onClickLove(rule: Rule, position: Int) {
                var love = 0
                var rule1 = Rule(rule.id, rule.title, rule.description, love, rule.str, rule.imagePath)
                myDBHelper.update(rule1)
                list.clear()
                for (i in 0 until myDBHelper.getAll().size){
                    if (myDBHelper.getAll()[i].love == 1){
                        list.add(myDBHelper.getAll()[i])
                    }
                }
                adapterForRv.notifyItemRemoved(position)
                adapterForRv.notifyItemRangeRemoved(position, list.size)

            }

            override fun delete(rule: Rule, position: Int) {
                var alertDialog = AlertDialog.Builder(binding.root.context)
                alertDialog.setMessage("O'chirmoqchimisiz?")
                alertDialog.setPositiveButton("Ha", object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        myDBHelper.delete(rule)
                        list.clear()
                        for (i in 0 until all.size){
                            if (all[i].love == 1){
                                list.add(all[i])
                            }
                        }
                        adapterForRv.notifyItemRemoved(position)
                    }

                })

                alertDialog.setNegativeButton("Yo'q", object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {

                    }

                })
                alertDialog.show()
            }

            override fun edit(rule: Rule) {
                var bundle = Bundle()
                bundle.putInt("id", rule.id!!)
                findNavController().navigate(R.id.editFragment, bundle)
            }

            override fun getOneSetOnClick(rule: Rule) {
                var bundle = Bundle()
                bundle.putInt("id", rule.id!!)
                findNavController().navigate(R.id.getOneFragment, bundle)
            }

        })
        binding.rvLove.adapter = adapterForRv

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoveFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}