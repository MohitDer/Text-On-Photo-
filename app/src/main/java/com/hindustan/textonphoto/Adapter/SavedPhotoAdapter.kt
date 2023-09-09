package com.hindustan.textonphoto.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hindustan.textonphoto.Activitys.SplashActivity.Companion.Ads
import com.hindustan.textonphoto.Activitys.SplashActivity.Companion.savedPhotoInter
import com.hindustan.textonphoto.Activitys.ViewPhotoActivity
import com.hindustan.textonphoto.GoogleAds.AD
import com.hindustan.textonphoto.R
import java.io.File

class SavedPhotoAdapter(context:Context, list: Array<File>?): RecyclerView.Adapter<SavedPhotoAdapter.ViewHolder>() {

    var context: Context = context
    var list: Array<File> = list!!

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        val iv_photo: ImageView = itemView.findViewById(R.id.iv_photos)
        val cv_photo: CardView = itemView.findViewById(R.id.cv_photos)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo,parent,false)

        return ViewHolder(view);
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(context).load(list.get(position)).into(holder.iv_photo)

        holder.cv_photo.setOnClickListener {

            if (Ads == true && savedPhotoInter == true){
                val intent = Intent(context, ViewPhotoActivity::class.java)

                intent.putExtra("ImageUriFV",list.get(position).toString())

                AD.showInterstitialAds(context as Activity, intent)

            }else{
                val intent = Intent(context, ViewPhotoActivity::class.java)

                intent.putExtra("ImageUriFV",list.get(position).toString())

                context.startActivity(intent)
            }

        }
    }
}