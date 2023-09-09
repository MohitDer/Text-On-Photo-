package com.hindustan.textonphoto.Adapter


import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.hindustan.textonphoto.R


class FontAdapter(context:Context): RecyclerView.Adapter<FontAdapter.ViewHolder>() {

    val context:Context = context
    val customTypefaces = ArrayList<Typeface>()
    private var selected = true
    private lateinit var onFontPickerClickListener:OnFontPickerClickListener

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {

        val cv_font:MaterialCardView = itemView.findViewById(R.id.cv_font)
        val tv_font:TextView = itemView.findViewById(R.id.tv_font)

    }

    interface OnFontPickerClickListener {
        fun onFontPickerClickListener(typeface: Typeface)
    }

    fun setOnFontPickerClickListener(onFontPickerClickListener: OnFontPickerClickListener) {
       this.onFontPickerClickListener = onFontPickerClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_font,parent,false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return customTypefaces.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_font.setTypeface(customTypefaces.get(position))
        holder.cv_font.setOnClickListener {
            if (selected == true){
                holder.cv_font.setStrokeColor(ContextCompat.getColor(context,R.color.blue))
                selected = false
            }else{
                holder.cv_font.setStrokeColor(ContextCompat.getColor(context,R.color.white))
                selected = true
            }
            onFontPickerClickListener.onFontPickerClickListener(customTypefaces.get(position))
        }
    }

    private fun setupFilters() {
      customTypefaces.add(ResourcesCompat.getFont(context,R.font.noto)!!)
      customTypefaces.add(ResourcesCompat.getFont(context,R.font.bungee)!!)
      customTypefaces.add(ResourcesCompat.getFont(context,R.font.staat)!!)
    }

    init {
        setupFilters()
    }
}


