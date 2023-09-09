package com.hindustan.textonphoto.Activitys

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayout
import com.hindustan.textonphoto.Activitys.SplashActivity.Companion.Ads
import com.hindustan.textonphoto.Activitys.SplashActivity.Companion.selectPhotoBackInter
import com.hindustan.textonphoto.Activitys.SplashActivity.Companion.selectPhotoBanner
import com.hindustan.textonphoto.Fragmets.ColorsFragment
import com.hindustan.textonphoto.Fragmets.PhotosFragment
import com.hindustan.textonphoto.GoogleAds.AD
import com.hindustan.textonphoto.R
import com.hindustan.textonphoto.databinding.ActivitySelectPhotoBinding

class SelectPhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectPhotoBinding

    private lateinit var fragmentOne: PhotosFragment

    private lateinit var fragmentTwo: ColorsFragment



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_photo)

        fragmentOne = PhotosFragment()
        fragmentTwo = ColorsFragment()

        AD.loadInterstitialAds(this)

        binding.tabs.addTab(binding.tabs.newTab().setText("Photos"), true)
        binding.tabs.addTab(binding.tabs.newTab().setText("Colors"))

        binding.tabs.getTabAt(0)!!.select();
        replaceFragment(fragmentOne)


        binding.tabs.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab!!.position == 0) {
                    replaceFragment(fragmentOne)
                } else if (tab!!.position == 1) {
                    replaceFragment(fragmentTwo)
                } else {
                    replaceFragment(fragmentOne)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.ivGallery.setOnClickListener {
            openSystemGalleryToSelectAnImage()
        }

        if (Ads == true && selectPhotoBanner == true){
            AD.loadAdmobBannerAds(this, binding.adView)
        }



    }


    fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
        val fm: androidx.fragment.app.FragmentManager = supportFragmentManager
        val ft: androidx.fragment.app.FragmentTransaction = fm.beginTransaction()
        ft.replace(R.id.frame_container, fragment)
        ft.setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()
    }

    fun openSystemGalleryToSelectAnImage() {


        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        try {
            startActivityForResult(intent, 101)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                this, "No Gallery APP installed", Toast.LENGTH_LONG
            ).show()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 101) {

            val imageUri: Uri? = data!!.data

            val intent = Intent(this, EditingActivity::class.java)
            intent.putExtra("ImageUriGallery",imageUri)
            startActivity(intent)



        }

    }

    override fun onBackPressed() {

        if (Ads == true && selectPhotoBackInter == true){

            AD.showInterstitialAds(this, null)

        }else{

            finish()

        }
    }


}