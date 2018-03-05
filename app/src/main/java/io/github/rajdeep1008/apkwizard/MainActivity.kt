package io.github.rajdeep1008.apkwizard

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private var mPagerAdapter: PagerAdapter? = null
    private var mViewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        mPagerAdapter = PagerAdapter(supportFragmentManager, this)
        mViewPager = findViewById(R.id.container)
        mViewPager!!.adapter = mPagerAdapter

        val tabLayout = findViewById(R.id.tab_bar) as TabLayout
        tabLayout.setupWithViewPager(mViewPager)
    }
}
