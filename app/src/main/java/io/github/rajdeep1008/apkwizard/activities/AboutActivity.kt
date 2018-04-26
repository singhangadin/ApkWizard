package io.github.rajdeep1008.apkwizard.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.method.LinkMovementMethod
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import io.github.rajdeep1008.apkwizard.R
import org.jetbrains.anko.find

class AboutActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        toolbar = find(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        (find(R.id.made_with_love) as TextView).setText("Made with ♥ from India")

        (find(R.id.play_btn) as ImageButton).setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Rajdeep1008")))
        }

        (find(R.id.medium_btn) as ImageButton).setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://medium.com/@rajdeepsingh")))
        }

        (find(R.id.github_btn) as ImageButton).setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/rajdeep1008")))
        }

        (find(R.id.source_code) as TextView).movementMethod = LinkMovementMethod.getInstance()
    }
}
