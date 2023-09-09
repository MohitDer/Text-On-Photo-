package com.hindustan.textonphoto.GoogleAds


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.hindustan.textonphoto.Activitys.SplashActivity.Companion.idBanner
import com.hindustan.textonphoto.Activitys.SplashActivity.Companion.idInter
import com.hindustan.textonphoto.Activitys.SplashActivity.Companion.idNative


open class AD {

    companion object {
        var mInterstitialAd: InterstitialAd? = null

        var adLoader: AdLoader? = null

        var mNativeAd: NativeAd? = null

        fun loadAdmobBannerAds(context: Context, adview: FrameLayout?) {

            if (idBanner != null) {

                val mAdView = AdView(context)

                mAdView.setAdSize(AdSize.BANNER)

                mAdView.adUnitId = idBanner as String

                val adRequest = AdRequest.Builder().build()

                mAdView.loadAd(adRequest)

                mAdView.adListener = object : AdListener() {

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        val adRequest = AdRequest.Builder().build()

                        mAdView.loadAd(adRequest)

                        Log.d("Tag", "onAdFailedToLoad: "+p0.message)
                    }

                    override fun onAdLoaded() {
                        adview!!.addView(mAdView)

                        Log.d("Tag", "onAdLoaded: Loaded")
                    }
                }


            }


        }

        fun loadInterstitialAds(context: Activity) {

            if (idInter != null) {
                val adRequest = AdRequest.Builder().build()
                InterstitialAd.load(context, idInter!!, adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            mInterstitialAd = interstitialAd
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            Log.d("Tag", loadAdError.toString())
                            mInterstitialAd = null
                        }
                    })
            }

        }

        fun showInterstitialAds(context: Activity, intent: Intent?) {
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(context)
                mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        loadInterstitialAds(context)
                        startActivity(context, intent, 0)
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        loadInterstitialAds(context)
                        startActivity(context, intent, 0)
                    }
                }
            } else {
                loadInterstitialAds(context)
                startActivity(context, intent, 0)
            }
        }

        fun startActivity(context: Activity, intent: Intent?, requestCode: Int) {
            if (intent != null) {
                context.startActivityForResult(intent, requestCode)
            } else {
                context.finish()
            }
        }

        fun loadNativeAds(context: Activity,templateView: TemplateView?) {

            if (idNative != null) {
                adLoader = AdLoader.Builder(context, idNative!!)
                    .forNativeAd { nativeAd ->
                        mNativeAd = nativeAd
                    }
                    .withAdListener(object : AdListener() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            Log.i("TagAdmobNative", adError.message)
                        }

                        override fun onAdLoaded() {

                            if (mNativeAd != null && templateView != null){

                                templateView.visibility = View.VISIBLE
                                templateView.setNativeAd(mNativeAd)

                            }else{
                                loadNativeAds(context,null)
                            }

                        }
                    })
                    .withNativeAdOptions(NativeAdOptions.Builder().build())
                    .build()

                adLoader!!.loadAd(AdRequest.Builder().build())
            }

        }

    }


}