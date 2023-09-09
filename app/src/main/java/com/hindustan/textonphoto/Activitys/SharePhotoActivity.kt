package com.hindustan.textonphoto.Activitys

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.hindustan.textonphoto.Activitys.SplashActivity.Companion.Ads
import com.hindustan.textonphoto.Activitys.SplashActivity.Companion.sharePhotoNative
import com.hindustan.textonphoto.GoogleAds.AD
import com.hindustan.textonphoto.R
import com.hindustan.textonphoto.databinding.ActivitySharePhotoBinding
import java.io.File

class SharePhotoActivity : AppCompatActivity() {

    lateinit var binding: ActivitySharePhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_share_photo)

        val SavedPhoto = this.intent.getStringExtra("SavedPhoto")

        if(!SavedPhoto.isNullOrEmpty()){
            val file  =File(SavedPhoto)

            Glide.with(this).load(file).into(binding.ivShareImage)
        }


        binding.ivHome.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnCreateNewPhoto.setOnClickListener {
            startActivity(Intent(this,SelectPhotoActivity::class.java))
            finish()
        }

        binding.btnSharePhoto.setOnClickListener {
            val imageFile = File(SavedPhoto)
            val imageUri = FileProvider.getUriForFile(this, "com.hindustan.textonphoto.fileprovider", imageFile)

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)

            val resInfoList = packageManager.queryIntentActivities(shareIntent, PackageManager.MATCH_DEFAULT_ONLY)
            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            startActivity(Intent.createChooser(shareIntent, "Share Image"))
        }


        if (Ads == true && sharePhotoNative == true){
            AD.loadNativeAds(this, binding.templateSmall)
        }


    }
}