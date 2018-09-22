package io.github.rajdeep1008.apkwizard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.LinkMovementMethod
import android.view.View
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        play_btn.setOnClickListener(this)
        medium_btn.setOnClickListener(this)
        github_btn.setOnClickListener(this)

        source_code.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onClick(v: View?) {
        val intent = when(v?.id) {
            R.id.play_btn -> Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Rajdeep1008"))
            R.id.medium_btn -> Intent(Intent.ACTION_VIEW, Uri.parse("https://medium.com/@rajdeepsingh"))
            R.id.github_btn -> Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/rajdeep1008"))
            else -> Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Rajdeep1008"))
        }
        startActivity(intent)
    }
}
