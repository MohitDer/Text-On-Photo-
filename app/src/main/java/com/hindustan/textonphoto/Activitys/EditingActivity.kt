package com.hindustan.textonphoto.Activitys

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.hindustan.textonphoto.Activitys.Interface.FilterListener
import com.hindustan.textonphoto.Activitys.SplashActivity.Companion.Ads
import com.hindustan.textonphoto.Activitys.SplashActivity.Companion.editPhotoBanner
import com.hindustan.textonphoto.Activitys.SplashActivity.Companion.editPhotoInter
import com.hindustan.textonphoto.Adapter.ColorPickerAdapter
import com.hindustan.textonphoto.Adapter.FilterAdapter
import com.hindustan.textonphoto.Adapter.FontAdapter
import com.hindustan.textonphoto.Adapter.SickerAdapter
import com.hindustan.textonphoto.GoogleAds.AD
import com.hindustan.textonphoto.GoogleAds.LoadAds
import com.hindustan.textonphoto.R
import com.hindustan.textonphoto.databinding.ActivityEditingBinding
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoFilter
import ja.burhanrashid52.photoeditor.SaveFileResult
import ja.burhanrashid52.photoeditor.TextStyleBuilder
import ja.burhanrashid52.photoeditor.ViewType
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException


class EditingActivity : AppCompatActivity(), FilterListener, OnPhotoEditorListener {

    lateinit var binding: ActivityEditingBinding
    lateinit var mPhotoEditor: PhotoEditor
    private val mFilterViewAdapter = FilterAdapter(this, this)
    private var rvFilter = true
    private var rvSticker = true
    lateinit var ImageUri: String;
    lateinit var file: File
    lateinit var textDialog: Dialog
    private var mColorCode = 0
    private var text: String = ""
    private var mpath: String = ""
    private var TAG: String = "Tag"
    private var mTypeface: Typeface = Typeface.DEFAULT
    private val sickerAdapter: SickerAdapter = SickerAdapter(this)


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            com.hindustan.textonphoto.R.layout.activity_editing
        )

        Glide.with(this).setDefaultRequestOptions(RequestOptions())

        val ImageUriGallery = this.intent.getStringExtra("ImageUriGallery")
        val ImageUriPF = this.intent.getStringExtra("ImageUriPF")
        val ImageUriCF = this.intent.getIntExtra("ImageUriCF",0)




        val llmFilters = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvFilter.layoutManager = llmFilters
        binding.rvFilter.adapter = mFilterViewAdapter

        binding.rvStiker.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvStiker.adapter = sickerAdapter
        binding.rvStiker.setHasFixedSize(true)

        sickerAdapter.setStickerListener(object : SickerAdapter.StickerListener {
            override fun onStickerClick(bitmap: Bitmap) {
                mPhotoEditor.addImage(bitmap)
            }

        })


        mPhotoEditor = PhotoEditor.Builder(this, binding.photoEditorView)
            //.setDefaultTextTypeface(mTextRobotoTf)
            //.setDefaultEmojiTypeface(mEmojiTypeFace)
            .build() // build photo editor sdk

        mPhotoEditor.setOnPhotoEditorListener(this)

        if (!ImageUriGallery.isNullOrEmpty()){

            try {
                val inputStream = contentResolver.openInputStream(ImageUriGallery.toUri()!!)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                Glide.with(this)
                    .asBitmap()
                    .load(bitmap)
                    .placeholder(R.drawable.test)
                    .into(binding.photoEditorView.source)

            } catch (e: IOException) {
                e.printStackTrace()
            }



        }else if (!ImageUriPF.isNullOrEmpty()){

            val file = File(ImageUriPF)

            Glide.with(this).load(file).into(binding.photoEditorView.source)

        }else if (ImageUriCF != 0){

            binding.photoEditorView.source.setImageResource(ImageUriCF)

        }


        binding.llFilter.setOnClickListener {
            binding.rvStiker.visibility = View.GONE
            if (rvFilter) {
                binding.rvFilter.animate().alpha(1f).withStartAction {
                    binding.rvFilter.visibility = View.VISIBLE
                }.start()
                rvFilter = false
            } else {

                binding.rvFilter.animate().alpha(0f).withEndAction {
                    binding.rvFilter.visibility = View.GONE
                }.start()

                rvFilter = true
            }


        }

        binding.llSticker.setOnClickListener {
            binding.rvFilter.visibility = View.GONE
            if (rvSticker) {
                binding.rvStiker.animate().alpha(1f).withStartAction {
                    binding.rvStiker.visibility = View.VISIBLE
                }.start()
                rvSticker = false
            } else {

                binding.rvStiker.animate().alpha(0f).withEndAction {
                    binding.rvStiker.visibility = View.GONE
                }.start()

                rvSticker = true
            }


        }

        binding.llText.setOnClickListener {

            binding.rvFilter.visibility = View.GONE
            binding.rvStiker.visibility = View.GONE
            showTextDialog()

        }

        binding.llAddImage.setOnClickListener {
            binding.rvFilter.visibility = View.GONE
            binding.rvStiker.visibility = View.GONE
            openSystemGalleryToSelectAnImage()
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.ivSave.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE), 2002)
            } else {
               saveFile()
            }

        }

        if (Ads == true && editPhotoBanner == true){
            AD.loadAdmobBannerAds(this, binding.adView)
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 2002){
            saveFile()
        }
    }

    @SuppressLint("MissingPermission")
    fun saveFile() {
        val downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val directoryName = "TextOnPhotoDirectory"

        val textOnPhotoDirectory = File(downloadDirectory, directoryName)
        if (!textOnPhotoDirectory.exists()) {
            textOnPhotoDirectory.mkdirs()
        }

        val fileName = System.currentTimeMillis().toString() + ".png"
        val imageFile = File(textOnPhotoDirectory, fileName)

        lifecycleScope.launch {
            val result = mPhotoEditor.saveAsFile(imageFile.absolutePath)
            if (result is SaveFileResult.Success) {
                // Use the media scanner to make the saved image available in the gallery
                MediaScannerConnection.scanFile(
                    applicationContext,
                    arrayOf(imageFile.absolutePath),
                    null
                ) { path, uri ->

                    if (Ads == true){
                        mpath = path
                    }else{
                        val intent = Intent(this@EditingActivity,SharePhotoActivity::class.java)
                        intent.putExtra("SavedPhoto",path)
                        startActivity(intent)
                        finish()
                    }


                }

               Toast.makeText(this@EditingActivity,"Photo Saved",Toast.LENGTH_LONG).show()

                if (Ads == true && editPhotoInter == true){

                    if (SplashActivity.idInter != null) {
                        val adRequest = AdRequest.Builder().build()
                        InterstitialAd.load(this@EditingActivity, SplashActivity.idInter!!, adRequest,
                            object : InterstitialAdLoadCallback() {
                                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                                    AD.mInterstitialAd = interstitialAd
                                    if (AD.mInterstitialAd != null) {
                                        AD.mInterstitialAd?.show(this@EditingActivity)
                                        AD.mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                                            override fun onAdDismissedFullScreenContent() {
                                                AD.loadInterstitialAds(this@EditingActivity)
                                                val intent = Intent(this@EditingActivity,SharePhotoActivity::class.java)
                                                intent.putExtra("SavedPhoto",mpath)
                                                startActivity(intent)
                                                finish()
                                            }

                                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                                AD.loadInterstitialAds(this@EditingActivity)
                                                val intent = Intent(this@EditingActivity,SharePhotoActivity::class.java)
                                                intent.putExtra("SavedPhoto",mpath)
                                                startActivity(intent)
                                                finish()
                                            }
                                        }
                                    } else {
                                        AD.loadInterstitialAds(this@EditingActivity)
                                        val intent = Intent(this@EditingActivity,SharePhotoActivity::class.java)
                                        intent.putExtra("SavedPhoto",mpath)
                                        startActivity(intent)
                                        finish()
                                    }
                                }

                                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                                    Log.d("Tag", loadAdError.toString())
                                    AD.mInterstitialAd = null
                                }
                            })
                    }

                }


            } else {

                Toast.makeText(this@EditingActivity,"Photo Save Failed",Toast.LENGTH_LONG).show()

            }
        }
    }

    fun showTextDialog() {
        textDialog = Dialog(this)

        textDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        textDialog.setContentView(R.layout.item_add_text_dialog)

        textDialog.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent);

        textDialog.show()

        val colorPickerAdapter: ColorPickerAdapter
        val fontAdapter: FontAdapter

        colorPickerAdapter = ColorPickerAdapter(this)
        fontAdapter = FontAdapter(this)

        val rv_Color: RecyclerView = textDialog.findViewById(R.id.rv_color_picker)

        rv_Color.setHasFixedSize(true)

        rv_Color.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        rv_Color.adapter = colorPickerAdapter

        val rv_font: RecyclerView = textDialog.findViewById(R.id.rv_font_picker)

        rv_font.setHasFixedSize(true)

        rv_font.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        rv_font.adapter = fontAdapter


        val et_text: EditText = textDialog.findViewById(R.id.et_add_text)

        val btn_cancel: Button = textDialog.findViewById(R.id.btn_cancel)

        btn_cancel.setOnClickListener {

            textDialog.dismiss()

        }

        colorPickerAdapter.setOnColorPickerClickListener(object :
            ColorPickerAdapter.OnColorPickerClickListener {
            override fun onColorPickerClickListener(colorCode: Int) {


                if (colorCode == 0) {
                    mColorCode = R.color.black
                } else {
                    mColorCode = colorCode
                    et_text.setTextColor(colorCode)
                }


            }
        })

        fontAdapter.setOnFontPickerClickListener(object : FontAdapter.OnFontPickerClickListener {
            override fun onFontPickerClickListener(typeface: Typeface) {
                mTypeface = typeface
            }


        })


        val btn_done: Button = textDialog.findViewById(R.id.btn_done)
        btn_done.setOnClickListener {


            if (et_text.text.isBlank() && et_text.text.isEmpty() && et_text.text.toString() == " ") {
                Toast.makeText(this, "Enter Some Text", Toast.LENGTH_SHORT).show()
            } else {
                text = et_text.text.toString()
                val styleBuilder = TextStyleBuilder()
                styleBuilder.withTextColor(mColorCode)

                mPhotoEditor.addText(mTypeface, text, mColorCode);
            }

            textDialog.dismiss()
        }
    }


    fun showEditTextDialog(rootView: View) {
        textDialog = Dialog(this)

        textDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        textDialog.setContentView(R.layout.item_add_text_dialog)

        textDialog.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent);

        textDialog.show()

        val colorPickerAdapter: ColorPickerAdapter

        val fontAdapter: FontAdapter

        fontAdapter = FontAdapter(this)

        colorPickerAdapter = ColorPickerAdapter(this)

        val rv_Color: RecyclerView = textDialog.findViewById(R.id.rv_color_picker)

        rv_Color.setHasFixedSize(true)

        rv_Color.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        rv_Color.adapter = colorPickerAdapter

        val rv_font: RecyclerView = textDialog.findViewById(R.id.rv_font_picker)

        rv_font.setHasFixedSize(true)

        rv_font.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        rv_font.adapter = fontAdapter


        val et_text: EditText = textDialog.findViewById(R.id.et_add_text)

        val btn_cancel: Button = textDialog.findViewById(R.id.btn_cancel)

        btn_cancel.setOnClickListener {

            textDialog.dismiss()

        }

        colorPickerAdapter.setOnColorPickerClickListener(object :
            ColorPickerAdapter.OnColorPickerClickListener {
            override fun onColorPickerClickListener(colorCode: Int) {


                if (colorCode == null) {
                    mColorCode = R.color.black
                } else {
                    mColorCode = colorCode
                    et_text.setTextColor(colorCode)
                }


            }
        })

        fontAdapter.setOnFontPickerClickListener(object : FontAdapter.OnFontPickerClickListener {
            override fun onFontPickerClickListener(typeface: Typeface) {
                mTypeface = typeface
            }


        })

        val btn_done: Button = textDialog.findViewById(R.id.btn_done)
        btn_done.setOnClickListener {


            if (et_text.text.isBlank() && et_text.text.isEmpty() && et_text.text.toString() == " ") {
                Toast.makeText(this, "Enter Some Text", Toast.LENGTH_SHORT).show()
            } else {
                text = et_text.text.toString()
                val styleBuilder = TextStyleBuilder()
                styleBuilder.withTextColor(mColorCode)
                mPhotoEditor.editText(rootView, mTypeface, text, mColorCode)
            }

            textDialog.dismiss()
        }
    }

    override fun onFilterSelected(photoFilter: PhotoFilter) {
        mPhotoEditor.setFilterEffect(photoFilter)
    }

    override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
        Log.d(
            TAG,
            "onAddViewListener() called with: viewType = [$viewType], numberOfAddedViews = [$numberOfAddedViews]"
        )
    }

    override fun onEditTextChangeListener(rootView: View?, text: String?, colorCode: Int) {
        if (rootView != null) {
            showEditTextDialog(rootView)
        }
    }

    override fun onRemoveViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
        Log.d(
            TAG,
            "onRemoveViewListener() called with: viewType = [$viewType], numberOfAddedViews = [$numberOfAddedViews]"
        )
    }

    override fun onStartViewChangeListener(viewType: ViewType?) {
        Log.d(TAG, "onStartViewChangeListener() called with: viewType = [$viewType]")
    }

    override fun onStopViewChangeListener(viewType: ViewType?) {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType = [$viewType]")
    }

    override fun onTouchSourceImage(event: MotionEvent?) {
        Log.d(TAG, "onTouchView() called with: event = [$event]")
    }

    fun openSystemGalleryToSelectAnImage() {


        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        try {
            startActivityForResult(intent, 104)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                this, "No Gallery APP installed", Toast.LENGTH_LONG
            ).show()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 104) {
            val imageUri: Uri = data?.data!!
            try {
                val inputStream = contentResolver.openInputStream(imageUri)
                val originalBitmap = BitmapFactory.decodeStream(inputStream)

                val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 500, 500, false)

                inputStream?.close()

                mPhotoEditor.addImage(resizedBitmap)

            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }

    }

    override fun onBackPressed() {
        if (Ads == true && SplashActivity.editPhotoBackInter == true){

            AD.showInterstitialAds(this, null)

        }else{

            finish()

        }
    }


}