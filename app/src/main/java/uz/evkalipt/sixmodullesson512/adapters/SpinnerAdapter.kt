package uz.evkalipt.sixmodullesson512.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import uz.evkalipt.sixmodullesson512.databinding.ItemForSpinnerBinding

class SpinnerAdapter(var list:ArrayList<String>):BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var vh:Vh
        if (convertView==null){
            val inflate =
                ItemForSpinnerBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
            vh = Vh(inflate)
        }else{
            vh = Vh(ItemForSpinnerBinding.bind(convertView))
        }
        vh.itemForSpinnerBinding.tvSpinnerItem.text =list[position]
        return vh.itemView
    }

    inner class Vh{
        var itemView:View
        var itemForSpinnerBinding:ItemForSpinnerBinding

        constructor(itemForSpinnerBinding: ItemForSpinnerBinding) {
            itemView = itemForSpinnerBinding.root
            this.itemForSpinnerBinding = itemForSpinnerBinding
        }
    }
}