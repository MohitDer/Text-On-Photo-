package com.hindustan.textonphoto.Adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.hindustan.textonphoto.R
import java.io.IOException

class SickerAdapter(context: Context) : RecyclerView.Adapter<SickerAdapter.ViewHolder>() {

    private val context: Context = context

    private var mStickerListener: StickerListener? = null

    fun setStickerListener(stickerListener: StickerListener?) {
        mStickerListener = stickerListener
    }

    interface StickerListener {
        fun onStickerClick(bitmap: Bitmap)
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        val imgSticker: ImageView = item.findViewById(R.id.iv_filter)

        val cv_sicker: CardView = item.findViewById(R.id.cv_filter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_filter, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stickerPathList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .asBitmap()
            .load(stickerPathList[position])
            .into(holder.imgSticker)

        holder.imgSticker.setOnClickListener {
            Glide.with(context)
                .asBitmap()
                .load(stickerPathList[position])
                .into(object : CustomTarget<Bitmap?>(256, 256) {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                        mStickerListener!!.onStickerClick(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        }
    }

    companion object {
        // Image Urls from flaticon(https://www.flaticon.com/stickers-pack/food-289)
        private val stickerPathList = arrayOf(
            "https://cdn-icons-png.flaticon.com/256/4392/4392452.png",
            "https://cdn-icons-png.flaticon.com/256/4392/4392455.png",
            "https://cdn-icons-png.flaticon.com/256/4392/4392459.png",
            "https://cdn-icons-png.flaticon.com/256/4392/4392462.png",
            "https://cdn-icons-png.flaticon.com/256/4392/4392465.png",
            "https://cdn-icons-png.flaticon.com/256/4392/4392467.png",
            "https://cdn-icons-png.flaticon.com/256/4392/4392469.png",
            "https://cdn-icons-png.flaticon.com/256/4392/4392471.png",
            "https://cdn-icons-png.flaticon.com/256/4392/4392522.png",
        )
    }

}