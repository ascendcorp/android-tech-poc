package com.ascendcorp.androidtechpoc.screen.main

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ascendcorp.androidtechpoc.databinding.ActivityMainBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val bitmap = Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            ActivityMainBinding.inflate(layoutInflater)
                .apply { binding = this }
                .root
        )
        setupView()

        binding.bSave.setOnClickListener {
            saveImageToPublicDirectory2(bitmap)
//            test()
//            test2()
//            galleryLauncher.launch(PickVisualMediaUtil.imageOnly())
//            saveImageToPublicDirectory(Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888))
        }
    }

    private fun setupView() {
        binding.rvTopic.apply {
            adapter = TopicAdapter().apply { items = getTopics() }
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun saveImageToPublicDirectory(bitmap: Bitmap) {
        val fileName = "IMG_" + System.currentTimeMillis() + ".jpg"
        try {
            val savedImageURL = MediaStore.Images.Media.insertImage(
                contentResolver,
                bitmap,
                fileName,
                "Image of $fileName"
            )
            if (savedImageURL != null) {
                val contentUri = Uri.parse(savedImageURL)
                val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                mediaScanIntent.data = contentUri
                sendBroadcast(mediaScanIntent)
                Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Failed to save image1", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to save image2", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImageToPublicDirectory2(bitmap: Bitmap) {
        val directory: File =
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyAppImages")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val fileName = "IMG_" + System.currentTimeMillis() + ".jpg"
        val file: File = File(directory, fileName)

        try {
            FileOutputStream(file).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
                // Notify the media scanner about the new file
                val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                val contentUri = Uri.fromFile(file)
                mediaScanIntent.setData(contentUri)
                sendBroadcast(mediaScanIntent)
                Toast.makeText(this, "Image saved to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun test() {
        FileUtil.createTempFileInCache(this, "IMG_" + System.currentTimeMillis(), ".png")?.let { file ->
            try {
                val bitmap = Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888)
                ByteArrayOutputStream().use { bos ->
                    bitmap?.compress(Bitmap.CompressFormat.PNG, 100, bos)
                    val bitmapData = bos.toByteArray()
                    FileOutputStream(file).use {
                        it.write(bitmapData)
                    }
                }

                MediaScannerConnection.scanFile(
                    this, arrayOf(file.path),
                    null
                ) { path, uri ->
                    // now visible in gallery
                }

                MediaScannerConnection.scanFile(
                    this,
                    arrayOf(file.toString()), null
                ) { path, uri ->
                    Log.i("ExternalStorage", "Scanned $path:")
                    Log.i("ExternalStorage", "-> uri=$uri")
                }
            } catch (e: Exception) {
                Log.i("ExternalStorage", "Exception")
            }
        }
    }


    var mExternalStorageAvailable: Boolean = false
    var mExternalStorageWriteable: Boolean = false

    private fun test2() {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            mExternalStorageWriteable = true
            mExternalStorageAvailable = true
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY == state) {
            mExternalStorageAvailable = true
            mExternalStorageWriteable = false
        } else {
            mExternalStorageWriteable = false
            mExternalStorageAvailable = false
        }
//        handleExternalStorageState(
//            mExternalStorageAvailable,
//            mExternalStorageWriteable
//        )
    }

    fun startWatchingExternalStorage() {

        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED)
        filter.addAction(Intent.ACTION_MEDIA_REMOVED)
        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                Log.i("test", "Storage: " + intent.data)
                updateExternalStorageState()
            }
        }, filter)
        updateExternalStorageState()
    }

    fun updateExternalStorageState() {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            mExternalStorageWriteable = true
            mExternalStorageAvailable = mExternalStorageWriteable
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY == state) {
            mExternalStorageAvailable = true
            mExternalStorageWriteable = false
        } else {
            mExternalStorageWriteable = false
            mExternalStorageAvailable = mExternalStorageWriteable
        }
    }

    private val galleryLauncher = PickVisualMediaUtil.registerForResult(this) {
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.data
                ?.run { Toast.makeText(this@MainActivity, "galleryLauncher done", Toast.LENGTH_LONG).show() }
                ?: Toast.makeText(this, "galleryLauncher failed", Toast.LENGTH_LONG).show()
        }
    }
}

object PickVisualMediaUtil {

    fun imageOnly() = Intent(Intent.ACTION_GET_CONTENT)
        .setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")

    @JvmStatic
    fun registerForResult(componentActivity: ComponentActivity, callback: ActivityResultCallback<ActivityResult>) =
        componentActivity.registerForResult(callback)
}

fun ComponentActivity.registerForResult(callback: ActivityResultCallback<ActivityResult>) =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult(), callback)
