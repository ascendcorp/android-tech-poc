//package com.ascendcorp.androidtechpoc.screen.main
//
//import android.R
//import android.content.res.Resources
//import android.media.MediaScannerConnection
//import android.os.Environment
//import android.util.Log
//import java.io.File
//import java.io.FileOutputStream
//import java.io.IOException
//import java.io.InputStream
//import java.io.OutputStream
//
//fun createExternalStoragePublicPicture(resources: Resources) {
//    // Create a path where we will place our picture in the user's
//    // public pictures directory.  Note that you should be careful about
//    // what you place here, since the user often manages these files.  For
//    // pictures and other media owned by the application, consider
//    // Context.getExternalMediaDir().
//    val path = Environment.getExternalStoragePublicDirectory(
//        Environment.DIRECTORY_PICTURES
//    )
//    val file = File(path, "DemoPicture.jpg")
//    try {
//        // Make sure the Pictures directory exists.
//        path.mkdirs()
//        // Very simple code to copy a picture from the application's
//        // resource into the external file.  Note that this code does
//        // no error checking, and assumes the picture is small (does not
//        // try to copy it in chunks).  Note that if external storage is
//        // not currently mounted this will silently fail.
////        val `is`: InputStream = getResources().openRawResource(R.drawable.balloons)
////        val os: OutputStream = FileOutputStream(file)
////        val data = ByteArray(`is`.available())
////        `is`.read(data)
////        os.write(data)
////        `is`.close()
////        os.close()
//        // Tell the media scanner about the new file so that it is
//        // immediately available to the user.
//        MediaScannerConnection.scanFile(
//            this,
//            arrayOf(file.toString()), null
//        ) { path, uri ->
//            Log.i("ExternalStorage", "Scanned $path:")
//            Log.i("ExternalStorage", "-> uri=$uri")
//        }
//    } catch (e: IOException) {
//        // Unable to create file, likely because external storage is
//        // not currently mounted.
//        Log.w("ExternalStorage", "Error writing $file", e)
//    }
//}
