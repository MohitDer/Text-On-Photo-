package com.hindustan.textonphoto.Activitys

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.AcknowledgePurchaseResponseListener
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.SkuType.INAPP
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.SkuDetailsParams
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.ImmutableList
import com.hindustan.textonphoto.Activitys.Interface.Security
import com.hindustan.textonphoto.Activitys.SplashActivity.Companion.Ads
import com.hindustan.textonphoto.R
import com.hindustan.textonphoto.databinding.ActivityRemoveAdsBinding
import java.io.IOException

class RemoveAdsActivity : AppCompatActivity(){

    lateinit var binding: ActivityRemoveAdsBinding


    lateinit var billingClient:BillingClient



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_remove_ads)

        binding.ivCancel.setOnClickListener {
            finish()
        }

        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        val sharedPreferences = EncryptedSharedPreferences.create(
            "secret_shared_prefs",
            masterKeyAlias,
            this@RemoveAdsActivity,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        val editor = sharedPreferences.edit()

        val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
                    editor.putBoolean("isAdsRemoved", false)
                    editor.apply()
                }
                Toast.makeText(this,"You've success purchased ad removal.",Toast.LENGTH_LONG).show()

            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {

                editor.putBoolean("isAdsRemoved", true)
                editor.apply()
                Toast.makeText(this,"You've Cancel purchased ad removal.",Toast.LENGTH_LONG).show()

            } else {

                editor.putBoolean("isAdsRemoved", true)
                editor.apply()
                Toast.makeText(this,"Something Went Wrong Please Try Latter",Toast.LENGTH_LONG).show()
            }
        }

         billingClient = BillingClient.newBuilder(this)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

        binding.btnBuy.setOnClickListener {

            if (Ads == false){

                Toast.makeText(this,"You've already purchased ad removal.",Toast.LENGTH_LONG).show()

            }else{

                startPurchases()

            }

        }



    }

    fun startPurchases(){
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val queryProductDetailsParams =
                        QueryProductDetailsParams.newBuilder()
                            .setProductList(
                                ImmutableList.of(
                                    QueryProductDetailsParams.Product.newBuilder()
                                        .setProductId("remove_ads")
                                        .setProductType(BillingClient.ProductType.INAPP)
                                        .build()
                                )
                            )
                            .build()

                    billingClient.queryProductDetailsAsync(queryProductDetailsParams) { billingResult, productDetailsList ->
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList != null) {
                            // Assuming you want to purchase the first product from the list
                            val productDetails = productDetailsList[0] // Change this index if needed

                            val productDetailsParamsList = listOf(
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                    .setProductDetails(productDetails)
                                    .build()
                            )

                            val billingFlowParams = BillingFlowParams.newBuilder()
                                .setProductDetailsParamsList(productDetailsParamsList)
                                .build()

                            val launchResult = billingClient.launchBillingFlow(this@RemoveAdsActivity, billingFlowParams)
                            if (launchResult.responseCode != BillingClient.BillingResponseCode.OK) {
                                // Handle the case where the billing flow was not launched successfully
                            }
                        } else {
                            Toast.makeText(this@RemoveAdsActivity,"Something Went Wrong Please Try again",Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                if (Ads == false){

                    Toast.makeText(this@RemoveAdsActivity,"You've already purchased ad removal.",Toast.LENGTH_LONG).show()

                }else{

                    startPurchases()

                }

            }
        })
    }





}