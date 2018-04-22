package io.github.rajdeep1008.apkwizard.extras

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.github.rajdeep1008.apkwizard.activities.MainActivity
import java.io.File

/**
 * Created by rajdeep1008 on 22/04/18.
 */
class Utilities {

    companion object {
        val STORAGE_PERMISSION_CODE = 1008

        fun checkPermission(activity: AppCompatActivity): Boolean {
            var permissionGranted = false

            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    val rootView: View = (activity as MainActivity).window.decorView.findViewById(android.R.id.content)
                    Snackbar.make(rootView, "Storage permission required", Snackbar.LENGTH_LONG)
                            .setAction("Allow", {
                                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
                            })
                            .setActionTextColor(Color.WHITE)
                            .show()
                } else {
                    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
                }
            } else {
                permissionGranted = true
            }

            return permissionGranted
        }

        fun checkExternalStorage(): Boolean {
            return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
        }

        fun getAppFolder(): File? {
            var file: File? = null
            if (checkExternalStorage()) {
                file = File(Environment.getExternalStorageDirectory(), "ApkWizard")
                return file
            }
            return file
        }

        fun makeAppDir(){
            val file = getAppFolder()
            if(file != null && !file.exists()){
                file.mkdir()
            }
        }
    }
}