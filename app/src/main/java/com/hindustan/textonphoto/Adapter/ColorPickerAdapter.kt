package com.hindustan.textonphoto.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.hindustan.textonphoto.R

class ColorPickerAdapter internal constructor(private var context: Context,colorPickerColors: List<Int>)  : RecyclerView.Adapter<ColorPickerAdapter.ViewHolder>() {

    private lateinit var onColorPickerClickListener: OnColorPickerClickListener
    private val colorPickerColors: List<Int> = colorPickerColors
    private var selected = true

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        val cv_color:MaterialCardView = itemView.findViewById(R.id.cv_color)

    }

    internal constructor(context: Context) : this(context, getDefaultColors(context)) {
        this.context = context
    }

    interface OnColorPickerClickListener {
        fun onColorPickerClickListener(colorCode: Int)
    }

    fun setOnColorPickerClickListener(onColorPickerClickListener: OnColorPickerClickListener) {
        this.onColorPickerClickListener = onColorPickerClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_color,parent,false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return colorPickerColors.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.cv_color.setCardBackgroundColor(colorPickerColors.get(position))

        holder.cv_color.setOnClickListener {
            if (selected == true){
                holder.cv_color.setStrokeColor(colorPickerColors.get(0))
                selected = false
            }else{
                holder.cv_color.setStrokeColor(colorPickerColors.get(9))
                selected = true
            }

            onColorPickerClickListener.onColorPickerClickListener(colorPickerColors.get(position))
        }
    }

    companion object {
        fun getDefaultColors(context: Context): List<Int> {
            val colorPickerColors = ArrayList<Int>()
            colorPickerColors.add(ContextCompat.getColor((context), R.color.blue))
            colorPickerColors.add(ContextCompat.getColor((context), R.color.brown_color_picker))
            colorPickerColors.add(ContextCompat.getColor((context), R.color.green_color_picker))
            colorPickerColors.add(ContextCompat.getColor((context), R.color.orange_color_picker))
            colorPickerColors.add(ContextCompat.getColor((context), R.color.red_color_picker))
            colorPickerColors.add(ContextCompat.getColor((context), R.color.black))
            colorPickerColors.add(
                ContextCompat.getColor(
                    (context),
                    R.color.red_orange_color_picker
                )
            )
            colorPickerColors.add(
                ContextCompat.getColor(
                    (context),
                    R.color.sky_blue_color_picker
                )
            )
            colorPickerColors.add(ContextCompat.getColor((context), R.color.violet_color_picker))
            colorPickerColors.add(ContextCompat.getColor((context), R.color.white))
            colorPickerColors.add(ContextCompat.getColor((context), R.color.yellow_color_picker))
            colorPickerColors.add(
                ContextCompat.getColor(
                    (context),
                    R.color.yellow_green_color_picker
                )
            )
            return colorPickerColors
        }
    }
}



