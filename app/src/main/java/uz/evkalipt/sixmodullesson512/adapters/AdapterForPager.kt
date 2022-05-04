package uz.evkalipt.sixmodullesson512.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import uz.evkalipt.sixmodullesson512.ForPagerFragment
import uz.evkalipt.sixmodullesson512.models.Category

class AdapterForPager(var list:ArrayList<Category>, fragmentManager: FragmentManager):FragmentStatePagerAdapter(fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return ForPagerFragment.newInstance(list[position].title!!)
    }
}