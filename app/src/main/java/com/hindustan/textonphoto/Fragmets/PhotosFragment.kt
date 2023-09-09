package com.hindustan.textonphoto.Fragmets

import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hindustan.textonphoto.Adapter.PhotosAdapter
import com.hindustan.textonphoto.R


class PhotosFragment : Fragment() {

    private val photoList = ArrayList<String>()
    private lateinit var rv_photos: RecyclerView
    private lateinit var PhotosAdapter: PhotosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_photos, container, false)

        rv_photos = view.findViewById(R.id.rv_photo)

        if (!::PhotosAdapter.isInitialized) {
            PhotosAdapter = PhotosAdapter(requireActivity(), photoList)
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            getImagePath()

        } else {
            requestPermissions(arrayOf(READ_MEDIA_IMAGES), 2004)
        }

        val layoutManager = GridLayoutManager(
            requireContext(), 3,
            GridLayoutManager.VERTICAL, false
        )

        rv_photos.setHasFixedSize(true)

        rv_photos.layoutManager = layoutManager

        rv_photos.adapter = PhotosAdapter



        return view
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == 2004 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(requireActivity(), "Permissions Granted..", Toast.LENGTH_SHORT).show();

            getImagePath()

            PhotosAdapter.notifyDataSetChanged()
        } else {
            // Permission denied, handle accordingly (e.g., show a message).

        }
    }


    private fun getImagePath() {
        // In this method, we are adding all our image paths
        // to our ArrayList, which we have created.
        Log.d("Tag", "getImagePath: ")

        // On the below line, we are checking if the device has an SD card or not.
        val isSDPresent =
            android.os.Environment.getExternalStorageState() == android.os.Environment.MEDIA_MOUNTED

        if (isSDPresent) {
            // If the SD card is present, we create a new list
            // to get our images' data along with their IDs.
            val columns = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID)

            // On the below line, we create a new string to order our images by.
            val orderBy = MediaStore.Images.Media._ID

            // This method will retrieve all the images
            // from the gallery and store them in a Cursor.
            val cursor = requireContext().contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns,
                null,
                null,
                orderBy
            )

            // Below line retrieves the total number of images.
            val count = cursor?.count ?: 0

            // Loop through the cursor to add the image file paths to the ArrayList.
            for (i in 0 until count) {
                // Move the cursor to the current position.
                cursor?.moveToPosition(i)

                // Retrieve the image file path.
                val dataColumnIndex = cursor?.getColumnIndex(MediaStore.Images.Media.DATA) ?: -1

                // After that, we get the image file path
                // and add that path to our ArrayList.
                photoList.add(cursor!!.getString(dataColumnIndex));
            }

            photoList.reverse()

            PhotosAdapter.notifyDataSetChanged()

            // After adding the data to our ArrayList,
            // we close the cursor.
            cursor?.close()
        }
    }


}