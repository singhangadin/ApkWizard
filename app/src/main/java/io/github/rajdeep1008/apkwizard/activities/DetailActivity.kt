package io.github.rajdeep1008.apkwizard.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import io.github.rajdeep1008.apkwizard.R
import io.github.rajdeep1008.apkwizard.models.Apk
import org.jetbrains.anko.find

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val apk = intent.extras.get("apk_info") as Apk
        (find(R.id.icon_iv) as ImageView).setImageDrawable(packageManager.getApplicationIcon(apk.appInfo))
    }
}
