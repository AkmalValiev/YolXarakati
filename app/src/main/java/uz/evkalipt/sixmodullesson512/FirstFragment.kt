package uz.evkalipt.sixmodullesson512

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.item_for_tab_layout.view.*
import uz.evkalipt.sixmodullesson512.adapters.AdapterForPager
import uz.evkalipt.sixmodullesson512.databinding.FragmentFirstBinding
import uz.evkalipt.sixmodullesson512.databinding.ItemForTabLayoutBinding
import uz.evkalipt.sixmodullesson512.db.MyDBHelper
import uz.evkalipt.sixmodullesson512.models.Category
import uz.evkalipt.sixmodullesson512.models.Rule
import uz.evkalipt.sixmodullesson512.utils.OnDataPass
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FirstFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    lateinit var adapterForPager: AdapterForPager
    lateinit var myDBHelper: MyDBHelper
    lateinit var listCategory:ArrayList<Category>
    lateinit var onDataPass: OnDataPass
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFirstBinding.inflate(layoutInflater)
        myDBHelper = MyDBHelper(binding.root.context)
        setList()
        adapterForPager = AdapterForPager(listCategory, childFragmentManager)
        binding.viewPager.adapter = adapterForPager
        binding.tabLayuot.setupWithViewPager(binding.viewPager)

        var tabCount = binding.tabLayuot.tabCount
        for (i in 0 until tabCount){
            var tabView = ItemForTabLayoutBinding.inflate(layoutInflater)
            binding.tabLayuot.getTabAt(i)?.customView = tabView.root
            tabView.tv1.text = listCategory[i].title
            tabView.tv2.text = listCategory[i].title

            if (i==0){
                tabView.linear2.visibility = View.VISIBLE
            }else{
                tabView.linear2.visibility = View.INVISIBLE
            }
        }

        binding.tabLayuot.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                var tabView = tab?.customView
                tabView?.linear2?.visibility = View.VISIBLE
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                var tabView = tab?.customView
                tabView?.linear2?.visibility = View.INVISIBLE
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding.imageToolBar.setOnClickListener {
            onDataPass.onDataPass(1)
            findNavController().navigate(R.id.addFragment)
        }

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onDataPass = context as OnDataPass
    }

    private fun setList() {
        var dbList = myDBHelper.getAll()

        listCategory = ArrayList()
        var list1 = ArrayList<Rule>()
        for (i in 0 until dbList.size){
            if (dbList[i].str == "Ogohlantiruvchi"){
                list1.add(dbList[i])
            }
        }
        listCategory.add(Category("Ogohlantiruvchi", list1))

        var list2 = ArrayList<Rule>()
        for (i in 0 until dbList.size){
            if (dbList[i].str == "Imtiyozli"){
                list2.add(dbList[i])
            }
        }
        listCategory.add(Category("Imtiyozli", list2))

        var list3 = ArrayList<Rule>()
        for (i in 0 until dbList.size){
            if (dbList[i].str == "Ta'qiqlovchi"){
              list3.add(dbList[i])
            }
        }
        listCategory.add(Category("Ta'qiqlovchi", list3))

        var list4 = ArrayList<Rule>()
        for (i in 0 until dbList.size){
            if (dbList[i].str == "Buyuruvchi"){
                list4.add(dbList[i])
            }
        }
        listCategory.add(Category("Buyuruvchi", list4))
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FirstFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}