package uz.evkalipt.sixmodullesson512.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.evkalipt.sixmodullesson512.R
import uz.evkalipt.sixmodullesson512.databinding.ItemForRvBinding
import uz.evkalipt.sixmodullesson512.models.Rule

class AdapterForRv(var list:ArrayList<Rule>, var onMyClickListener: OnMyClickListener):RecyclerView.Adapter<AdapterForRv.Vh>() {

    inner class Vh(var itemForRvBinding: ItemForRvBinding):RecyclerView.ViewHolder(itemForRvBinding.root){
        fun onBind(rule: Rule, position: Int){
            if (!rule.imagePath?.trim().equals("")){
                itemForRvBinding.ivRvItem.setImageURI(Uri.parse(rule.imagePath))
            }else{
                itemForRvBinding.ivRvItem.setImageResource(R.drawable.ic_image1122)
            }
            if (rule.title?.length!!>20){
                itemForRvBinding.tvTitleItem.text = rule.title.toString().substring(0,20)+"..."
            }else{
                itemForRvBinding.tvTitleItem.text = rule.title
            }

            if (rule.love == 0){
                itemForRvBinding.imageLoveRed.visibility = View.INVISIBLE
            }else{
                itemForRvBinding.imageLoveRed.visibility = View.VISIBLE
            }

            itemForRvBinding.imageLove.setOnClickListener {
                onMyClickListener.onClickLove(rule, position)
            }

            itemForRvBinding.deleteBt.setOnClickListener {
                onMyClickListener.delete(rule, position)
            }

            itemForRvBinding.editBt.setOnClickListener {
                onMyClickListener.edit(rule)
            }

            itemForRvBinding.root.setOnClickListener {
                onMyClickListener.getOneSetOnClick(rule)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemForRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnMyClickListener{
        fun onClickLove(rule: Rule, position: Int)
        fun delete(rule: Rule, position: Int)
        fun edit(rule: Rule)
        fun getOneSetOnClick(rule: Rule)
    }

}