package io.github.rajdeep1008.apkwizard.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import io.github.rajdeep1008.apkwizard.fragments.ApkListFragment
import io.github.rajdeep1008.apkwizard.models.Apk

/**
 * Created by rajdeep1008 on 05/03/18.
 */
class PagerAdapter(fm: FragmentManager, userApkList: List<Apk>, systemApkList: List<Apk>) : FragmentStatePagerAdapter(fm) {

    private val tabNames = arrayOf("Installed", "System", "Extracted")
    var mUserApkList: List<Apk> = userApkList
    var mSystemApkList: List<Apk> = systemApkList

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ApkListFragment.newInstance(mUserApkList as ArrayList<Apk>)
            1 -> ApkListFragment.newInstance(mSystemApkList as ArrayList<Apk>)
            2 -> ApkListFragment.newInstance(ArrayList())
            else -> ApkListFragment()
        }
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence? = tabNames[position]
}