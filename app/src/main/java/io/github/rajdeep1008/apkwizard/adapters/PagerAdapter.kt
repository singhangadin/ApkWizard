package io.github.rajdeep1008.apkwizard.adapters

import android.content.Context
import android.content.pm.PackageInfo
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import io.github.rajdeep1008.apkwizard.fragments.ApkListFragment
import io.github.rajdeep1008.apkwizard.models.Apk

/**
 * Created by rajdeep1008 on 05/03/18.
 */
class PagerAdapter(fm: FragmentManager, private val context: Context, val userApkList: List<Apk>,val systemApkList: List<Apk>) : FragmentStatePagerAdapter(fm) {

    val tabNames = arrayOf("Installed", "System", "Extracted")
    var mContext: Context? = null
    var mUserApkList: List<Apk>? = null
    var mSystemApkList: List<Apk>? = null

    init {
        mContext = context
        mUserApkList = userApkList
        mSystemApkList = systemApkList
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return ApkListFragment.newInstance(mUserApkList as ArrayList<Apk>)
            1 -> return ApkListFragment.newInstance(mSystemApkList as ArrayList<Apk>)
            2 -> return ApkListFragment.newInstance(mUserApkList as ArrayList<Apk>)
        }
        return ApkListFragment()
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabNames[position]
    }
}