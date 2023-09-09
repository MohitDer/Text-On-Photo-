package com.hindustan.textonphoto.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hindustan.textonphoto.Activitys.EditingActivity
import com.hindustan.textonphoto.Activitys.SplashActivity.Companion.Ads
import com.hindustan.textonphoto.Activitys.SplashActivity.Companion.selectPhotoInter
import com.hindustan.textonphoto.GoogleAds.AD
import com.hindustan.textonphoto.R
import java.io.File

class PhotosAdapter(requireActivity: FragmentActivity, photoList: ArrayList<String>) : RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

     var context:Context = requireActivity
     var photoList:ArrayList<String> = photoList


    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {

        val iv_photo:ImageView = itemView.findViewById(R.id.iv_photos)
        val cv_photo:CardView = itemView.findViewById(R.id.cv_photos)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosAdapter.ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo,parent,false)

        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: PhotosAdapter.ViewHolder, position: Int) {

       val file = File(photoList.get(position))

        Glide.with(context).load(file).into(holder.iv_photo)

        holder.cv_photo.setOnClickListener {

            if (Ads == true && selectPhotoInter == true){

                val intent = Intent(context,EditingActivity::class.java)

                intent.putExtra("ImageUriPF",photoList.get(position))

                AD.showInterstitialAds(context = context as Activity, intent)
            }else{

                val intent = Intent(context,EditingActivity::class.java)

                intent.putExtra("ImageUriPF",photoList.get(position))

                context.startActivity(intent)
            }


        }


    }

    override fun getItemCount(): Int {
       return photoList.size
    }
}