package io.github.rajdeep1008.apkwizard.activities

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import io.github.rajdeep1008.apkwizard.R
import io.github.rajdeep1008.apkwizard.adapters.PagerAdapter
import io.github.rajdeep1008.apkwizard.models.Apk
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var tabLayout: TabLayout
    private lateinit var mViewPager: ViewPager

    private val userApkList = mutableListOf<Apk>()
    private val systemApkList = mutableListOf<Apk>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = find(R.id.toolbar)
        progressBar = find(R.id.progress)
        mViewPager = find(R.id.container)
        tabLayout = find(R.id.tab_bar)
        setSupportActionBar(toolbar)

        setupViewPager(userApkList, systemApkList)

        doAsync {
            val allPackages: List<PackageInfo> = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)

            allPackages.forEach {
                val applicationInfo: ApplicationInfo = it.applicationInfo

                if ((applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0) {
                    val userApk = Apk(
                            applicationInfo,
                            it.packageName,
                            it.versionName,
                            false)
                    userApkList.add(userApk)
                } else {
                    val systemApk = Apk(
                            applicationInfo,
                            it.packageName,
                            it.versionName,
                            true)

                    systemApkList.add(systemApk)
                }

                Log.d("test", packageManager.getApplicationLabel(applicationInfo).toString())
            }

            uiThread {
                setupViewPager(userApkList, systemApkList)
                progressBar.visibility = View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }

    fun setupViewPager(userApkList: List<Apk>, systemApkList: List<Apk>) {
        mViewPager.adapter = PagerAdapter(supportFragmentManager, this, userApkList, systemApkList)
        tabLayout.setupWithViewPager(mViewPager)
    }
}
