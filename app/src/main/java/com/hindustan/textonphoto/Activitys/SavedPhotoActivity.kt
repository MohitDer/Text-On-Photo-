package com.hindustan.textonphoto.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.hindustan.textonphoto.Activitys.SplashActivity.Companion.Ads
import com.hindustan.textonphoto.Activitys.SplashActivity.Companion.savedPhotoBanner
import com.hindustan.textonphoto.Adapter.SavedPhotoAdapter
import com.hindustan.textonphoto.GoogleAds.AD
import com.hindustan.textonphoto.GoogleAds.AD.Companion.loadAdmobBannerAds

import com.hindustan.textonphoto.R
import com.hindustan.textonphoto.databinding.ActivitySavedPhotoBinding
import java.io.File

class SavedPhotoActivity : AppCompatActivity() {

    lateinit var binding:ActivitySavedPhotoBinding
    lateinit var SavedPhotoAdapter:SavedPhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_saved_photo)

        val downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val directoryName = "TextOnPhotoDirectory"

        val textOnPhotoDirectory = File(downloadDirectory, directoryName)

        AD.loadInterstitialAds(this)

        val imageFiles = textOnPhotoDirectory.listFiles { file ->
            file.isFile && file.extension == "png"
        }

        binding.rvSavedPhoto.setHasFixedSize(true)

        binding.rvSavedPhoto.layoutManager = GridLayoutManager(this,3)

        SavedPhotoAdapter = SavedPhotoAdapter(this,imageFiles)

        binding.rvSavedPhoto.adapter =  SavedPhotoAdapter

        SavedPhotoAdapter.notifyDataSetChanged()

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        if (Ads == true && savedPhotoBanner == true){
            loadAdmobBannerAds(this,binding.adView)
        }


    }

    override fun onBackPressed() {
        if (Ads == true && SplashActivity.savedPhotoBackInter == true){

            AD.showInterstitialAds(this, null)

        }else{

            finish()

        }
    }
}