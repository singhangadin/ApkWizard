package io.github.rajdeep1008.apkwizard

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

/**
 * Created by rajdeep1008 on 05/03/18.
 */
class PagerAdapter(fm: FragmentManager, private val context: Context) : FragmentStatePagerAdapter(fm) {

    val tabNames = arrayOf("Installed", "System", "Extracted")

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return ApkListFragment()
            1 -> return ApkListFragment()
            2 -> return ApkListFragment()
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