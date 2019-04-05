package eu.sisik.pixelproc

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import eu.sisik.pixelproc.histo.Histogram
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder
import kotlin.math.roundToInt
import android.content.Intent
import android.widget.Toast
import android.provider.MediaStore
import android.R.attr.data
import android.net.Uri


class MainActivity : AppCompatActivity() {

    var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            uri = savedInstanceState?.getParcelable(KEY_URI)
        }

        initView()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(KEY_URI, uri)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CODE_STORAGE_PERMISSION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, getString(R.string.err_storage_permission), Toast.LENGTH_LONG).show()
            }

            showImagePicker()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            uri = data?.data
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            loadHistogram(bitmap!!)
            ivImg.setImageBitmap(bitmap)
        }
    }

    private fun initView() {
        var bitmap: Bitmap

        if (uri != null) {
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            ivImg.setImageBitmap(bitmap)
        }
        else {
            bitmap = (ivImg.drawable as BitmapDrawable).bitmap
        }

        loadHistogram(bitmap!!)

        ibSelectImage.setOnClickListener {
            if (hasStoragePermission()) {
                showImagePicker()
            } else {
                requestStoragePermission()
            }
        }
    }

    private fun loadHistogram(bitmap: Bitmap) {
        // Select implementation for generating histogram values
//        val (r, g, b, l) = Histogram(bitmap).run { generateKotlin() }
        val (r, g, b, l) = Histogram(bitmap).run { generateCpp() }

        histoView.apply {
            reds = r
            greens = g
            blues = b
            lumas = l
        }
    }

    private fun hasStoragePermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true

        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun showImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Image"), CODE_SELECT_IMAGE)
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun requestStoragePermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), CODE_STORAGE_PERMISSION)
    }

    companion object {
        const val TAG = "MainActivity"
        const val KEY_URI = "key.URI"
        const val CODE_SELECT_IMAGE = 6660
        const val CODE_STORAGE_PERMISSION = 6661
    }
}
