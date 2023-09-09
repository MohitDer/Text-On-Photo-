package com.hindustan.textonphoto.Activitys

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hindustan.textonphoto.Activitys.SplashActivity.Companion.Ads
import com.hindustan.textonphoto.Activitys.SplashActivity.Companion.mainInter
import com.hindustan.textonphoto.Activitys.SplashActivity.Companion.mainNative
import com.hindustan.textonphoto.GoogleAds.AD
import com.hindustan.textonphoto.GoogleAds.AD.Companion.loadInterstitialAds
import com.hindustan.textonphoto.GoogleAds.AD.Companion.showInterstitialAds
import com.hindustan.textonphoto.R
import com.hindustan.textonphoto.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var rateDialog:Dialog
    lateinit var feedBackDialog:Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        if (Ads == true){
            binding.ivBlockAds.visibility = View.VISIBLE
        }else{
            binding.ivBlockAds.visibility = View.GONE
        }

        toggle = ActionBarDrawerToggle(this@MainActivity,binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

 

        loadInterstitialAds(this)

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.remove_ads -> {
                    startActivity(Intent(this,RemoveAdsActivity::class.java))
                }
                R.id.rate_app -> {
                    RateApp()
                }
                R.id.feedback -> {
                    feedBack()
                }
                R.id.share_app -> {
                    ShareApp()
                }
                R.id.privacy_policy -> {
                    privacyPolicy()
                }
            }
            true
        }

        binding.ivMore.setOnClickListener(View.OnClickListener {
            binding.drawerLayout.openDrawer(Gravity.LEFT);
        })

        binding.cvSelectPhoto.setOnClickListener {

            if (Ads == true && mainInter == true){
                val intent = Intent(this,SelectPhotoActivity::class.java)
                showInterstitialAds(this,intent)
            }else{
                val intent = Intent(this,SelectPhotoActivity::class.java)
                startActivity(intent)
            }

        }

        binding.cvSavedPhoto.setOnClickListener {
            if (Ads == true && mainInter == true){
                val intent = Intent(this,SavedPhotoActivity::class.java)
                showInterstitialAds(this,intent)
            }else{
                val intent = Intent(this,SavedPhotoActivity::class.java)
                startActivity(intent)
            }
        }

        if (Ads == true && mainNative == true){
            AD.loadNativeAds(this, binding.templateMedium)
        }



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            true
        }
        return super.onOptionsItemSelected(item)
    }

    fun RateApp(){

        rateDialog = Dialog(this)

        rateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        rateDialog.setContentView(R.layout.item_rate_dailog)

        rateDialog.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)

        rateDialog.show()

        val btn_no:TextView = rateDialog.findViewById(R.id.tv_no)
        val btn_yes:TextView = rateDialog.findViewById(R.id.tv_yes)

        btn_no.setOnClickListener {
            rateDialog.dismiss()
        }

        btn_yes.setOnClickListener {
            val uri: Uri = Uri.parse("market://details?id=$packageName")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=$packageName")))
            }
            rateDialog.dismiss()
        }


    }

    fun ShareApp(){
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=$packageName");
        startActivity(Intent.createChooser(intent, "Share"));
    }

    fun privacyPolicy(){
        val uri = Uri.parse("http://www.google.com") // missing 'http://' will cause crashed
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    fun feedBack(){
        
        feedBackDialog = Dialog(this)

        feedBackDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        feedBackDialog.setContentView(R.layout.item_feedback_dialog)

        feedBackDialog.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)

        feedBackDialog.show()
        
        val et_feedkBack: EditText = feedBackDialog.findViewById(R.id.et_feedback)
        
        val btn_cancel: Button = feedBackDialog.findViewById(R.id.btn_cancel)
        
        val btn_done: Button = feedBackDialog.findViewById(R.id.btn_done)
        
        val msg = et_feedkBack.text.toString()
        
        btn_done.setOnClickListener {

            val intent = Intent(
                Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "mohitder44@gmail.com", null
                )
            )

            if (!msg.isNotBlank()){
                intent.putExtra(Intent.EXTRA_TEXT, msg)
            }

            startActivity(Intent.createChooser(intent, "Choose an Email client :"))
            feedBackDialog.dismiss()
            
        }
        
        btn_cancel.setOnClickListener { 
            feedBackDialog.dismiss()
        }
        
      
    }
}