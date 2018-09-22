package io.github.rajdeep1008.apkwizard

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
        icon_iv.setImageDrawable(packageManager.getApplicationIcon(apk.appInfo))
    }
}
