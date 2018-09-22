package io.github.rajdeep1008.apkwizard

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import io.github.rajdeep1008.apkwizard.R
import io.github.rajdeep1008.apkwizard.models.Apk
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val apk = intent.extras.get("apk_info") as Apk
        try {
            val app = packageManager.getApplicationInfo(apk.packageName, 0)
            icon_iv.setImageDrawable(packageManager.getApplicationIcon(app))
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
}
