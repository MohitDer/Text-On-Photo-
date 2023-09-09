package com.hindustan.textonphoto.Activitys

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PurchasesUpdatedListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hindustan.textonphoto.GoogleAds.AD
import com.hindustan.textonphoto.GoogleAds.AD.Companion.loadAdmobBannerAds
import com.hindustan.textonphoto.GoogleAds.AD.Companion.loadInterstitialAds
import com.hindustan.textonphoto.GoogleAds.AD.Companion.loadNativeAds
import com.hindustan.textonphoto.R
import com.hindustan.textonphoto.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {


    lateinit var fade_in: Animation
    lateinit var slide_up: Animation
    lateinit var binding: ActivitySplashBinding
    val progress: Int = 100

    companion object {

        var AppOpen: Boolean? = null
        var Ads: Boolean? = true
        var editPhotoBackInter: Boolean? = null
        var editPhotoBanner: Boolean? = null
        var editPhotoInter: Boolean? = null
        var mainInter: Boolean? = null
        var mainNative: Boolean? = null
        var savedPhotoBackInter: Boolean? = null
        var savedPhotoBanner: Boolean? = null
        var savedPhotoInter: Boolean? = null
        var selectPhotoBackInter: Boolean? = null
        var selectPhotoBanner: Boolean? = null
        var selectPhotoInter: Boolean? = null
        var sharePhotoBackInter: Boolean? = null
        var sharePhotoInter: Boolean? = null
        var sharePhotoNative: Boolean? = null
        var viewPhotoBackInter: Boolean? = null
        var viewPhotoBanner: Boolean? = null
        var viewPhotoInter: Boolean? = null
        var idAppOpen: String? = null
        var idBanner: String? = null
        var idInter: String? = null
        var idNative: String? = null
        var backCount: Int? = null
        var startCount: Int? = null

    }

    lateinit var database: FirebaseDatabase
    lateinit var ref: DatabaseReference
    var AD: AppOpenAd? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        slide_up = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        binding.tvTextOnPhoto.alpha = 1f


        binding.tvTextOnPhoto.startAnimation(fade_in)

        Handler(Looper.getMainLooper()).postDelayed({

            binding.tvAddText.alpha = 1f

            binding.tvAddText.startAnimation(slide_up)

            binding.pbSplash.alpha = 1f

            binding.pbSplash.startAnimation(slide_up)

        }, 1000)

        var timeprogrees: Int = 0;

        while (timeprogrees < progress) {
            timeprogrees += 5
            binding.pbSplash.progress = timeprogrees
        }

        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        val sharedPreferences = EncryptedSharedPreferences.create(
            "secret_shared_prefs",
            masterKeyAlias,
            this,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

        Ads = sharedPreferences.getBoolean("isAdsRemoved", true)

        MobileAds.initialize(this)
        FirebaseApp.initializeApp(this)

        database = FirebaseDatabase.getInstance()

        ref = database.getReference("Ads")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                AppOpen = snapshot.child("AppOpen").getValue(Boolean::class.java)
                editPhotoBackInter =
                    snapshot.child("editPhotoBackInter").getValue(Boolean::class.java)
                editPhotoBanner = snapshot.child("editPhotoBanner").getValue(Boolean::class.java)
                editPhotoInter = snapshot.child("editPhotoInter").getValue(Boolean::class.java)
                mainInter = snapshot.child("mainInter").getValue(Boolean::class.java)
                mainNative = snapshot.child("mainNative").getValue(Boolean::class.java)
                savedPhotoBackInter =
                    snapshot.child("savedPhotoBackInter").getValue(Boolean::class.java)
                savedPhotoBanner = snapshot.child("savedPhotoBanner").getValue(Boolean::class.java)
                savedPhotoInter = snapshot.child("savedPhotoInter").getValue(Boolean::class.java)
                selectPhotoBackInter =
                    snapshot.child("selectPhotoBackInter").getValue(Boolean::class.java)
                selectPhotoBanner =
                    snapshot.child("selectPhotoBanner").getValue(Boolean::class.java)
                selectPhotoInter = snapshot.child("selectPhotoInter").getValue(Boolean::class.java)
                sharePhotoBackInter =
                    snapshot.child("sharePhotoBackInter").getValue(Boolean::class.java)
                sharePhotoInter = snapshot.child("sharePhotoInter").getValue(Boolean::class.java)
                sharePhotoNative = snapshot.child("sharePhotoNative").getValue(Boolean::class.java)
                viewPhotoBackInter =
                    snapshot.child("viewPhotoBackInter").getValue(Boolean::class.java)
                viewPhotoBanner = snapshot.child("viewPhotoBanner").getValue(Boolean::class.java)
                viewPhotoInter = snapshot.child("viewPhotoInter").getValue(Boolean::class.java)
                idAppOpen = snapshot.child("idAppOpen").getValue(String::class.java)
                idBanner = snapshot.child("idBanner").getValue(String::class.java)
                idInter = snapshot.child("idInter").getValue(String::class.java)
                idNative = snapshot.child("idNative").getValue(String::class.java)
                backCount = snapshot.child("backCount").getValue(Int::class.java)
                startCount = snapshot.child("startCount").getValue(Int::class.java)

                loadAdmobBannerAds(this@SplashActivity, null)
                loadInterstitialAds(this@SplashActivity)
                loadNativeAds(this@SplashActivity, null)

                startMainScreen()

                Log.d("Tag", "onDataChange: ")

            }

            override fun onCancelled(error: DatabaseError) {

                Log.d("Tag", "onCancelled: " + error.message)
            }

        })


    }

    fun startMainScreen() {

        if (Ads == true && AppOpen == true){
            val request = AdRequest.Builder().build()
            AppOpenAd.load(this, idAppOpen!!, request, object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    Log.i("Tag", loadAdError.toString())
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                override fun onAdLoaded(appOpenAd: AppOpenAd) {
                    super.onAdLoaded(appOpenAd)
                    AD = appOpenAd
                    AD!!.show(this@SplashActivity)
                    AD!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent()
                            val intent = Intent(this@SplashActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            })

        }else{
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        }


}


