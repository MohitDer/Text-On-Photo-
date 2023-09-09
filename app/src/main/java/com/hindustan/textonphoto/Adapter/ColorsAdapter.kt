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
import com.hindustan.textonphoto.Activitys.SplashActivity
import com.hindustan.textonphoto.GoogleAds.AD
import com.hindustan.textonphoto.R

class ColorsAdapter(requireActivity: FragmentActivity, colorList: ArrayList<Int>) : RecyclerView.Adapter<ColorsAdapter.ViewHolder>() {

     var context: Context = requireActivity
     var colorList:ArrayList<Int> = colorList

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val iv_photo: ImageView = itemView.findViewById(R.id.iv_photos)
        val cv_photo: CardView = itemView.findViewById(R.id.cv_photos)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorsAdapter.ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorsAdapter.ViewHolder, position: Int) {

        Glide.with(context).load(colorList.get(position)).into(holder.iv_photo)

        holder.cv_photo.setOnClickListener {

            if (SplashActivity.Ads == true && SplashActivity.selectPhotoInter == true){

                val intent = Intent(context, EditingActivity::class.java)

                intent.putExtra("ImageUriCF", colorList.get(position))

                AD.showInterstitialAds(context = context as Activity, intent)
            }else{

                val intent = Intent(context, EditingActivity::class.java)

                intent.putExtra("ImageUriCF", colorList.get(position))

                context.startActivity(intent)
            }


        }
    }

    override fun getItemCount(): Int {
       return colorList.size
    }
}