package com.hindustan.textonphoto.Activitys

import android.app.WallpaperManager
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.hindustan.textonphoto.Activitys.SplashActivity.Companion.Ads
import com.hindustan.textonphoto.Activitys.SplashActivity.Companion.viewPhotoBanner
import com.hindustan.textonphoto.GoogleAds.AD
import com.hindustan.textonphoto.GoogleAds.LoadAds
import com.hindustan.textonphoto.R
import com.hindustan.textonphoto.databinding.ActivityViewPhotoBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ViewPhotoActivity : AppCompatActivity() {

    lateinit var binding:ActivityViewPhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_view_photo)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        AD.loadInterstitialAds(this)

        var fullViewImage = this.intent.getStringExtra("ImageUriFV")

        Log.d("Tag", "onCreate: "+fullViewImage)

        if (!fullViewImage.isNullOrEmpty()){

            Glide.with(this).load(fullViewImage).into(binding.ivFullPhoto)

        }

        binding.llShare.setOnClickListener {

            val imageFile = File(fullViewImage)
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

        binding.llSetWallpaper.setOnClickListener {
            val imageFile = File(fullViewImage) // Replace with the actual file path
            setWallpaper(imageFile)
        }

        binding.ivDelete.setOnClickListener {
            val imageFile = File(fullViewImage) // Replace with the actual file path
            deletePhoto(imageFile)
        }


        if (Ads == true && viewPhotoBanner == true){
            AD.loadAdmobBannerAds(this, binding.adView)
        }

    }

    private fun setWallpaper(imageFile: File) {
        val wallpaperManager = WallpaperManager.getInstance(this)
        try {
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            wallpaperManager.setBitmap(bitmap)
            Toast.makeText(this@ViewPhotoActivity,"Wallpaper set success",Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun deletePhoto(imageFile: File) {
        if (imageFile.exists()) {
            val deleted = imageFile.delete()
            if (deleted) {
                // Delete from MediaStore
                val contentResolver: ContentResolver = applicationContext.contentResolver
                val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                val selection = MediaStore.Images.Media.DATA + "=?"
                val selectionArgs = arrayOf(imageFile.absolutePath)
                contentResolver.delete(uri, selection, selectionArgs)

                Toast.makeText(this@ViewPhotoActivity,"Photo Delete Success",Toast.LENGTH_SHORT).show()

                finish()
            } else {
                // File deletion failed

                Toast.makeText(this@ViewPhotoActivity,"File deletion failed",Toast.LENGTH_SHORT).show()

            }
        } else {
            // File does not exist

            Toast.makeText(this@ViewPhotoActivity,"File does not exist",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        if (Ads == true && SplashActivity.viewPhotoBackInter == true){

            AD.showInterstitialAds(this, null)

        }else{

            finish()

        }
    }

}