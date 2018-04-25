package io.github.rajdeep1008.apkwizard.fragments


import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.rajdeep1008.apkwizard.R
import io.github.rajdeep1008.apkwizard.adapters.ApkListAdapter
import io.github.rajdeep1008.apkwizard.extras.Utilities
import io.github.rajdeep1008.apkwizard.models.Apk
import org.jetbrains.anko.find
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class ApkListFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key.equals(Utilities.PREF_SORT_KEY)) {
            updateSortingOrder(sharedPreferences?.getInt(key, 0)!!)
        }
    }

    private lateinit var apkList: ArrayList<Apk>
    private lateinit var mAdapter: ApkListAdapter
    private lateinit var mLinearLayoutManager: LinearLayoutManager

    companion object {
        val APK_ARG: String = "apk-list"

        fun newInstance(apkList: ArrayList<Apk>): ApkListFragment {
            val fragment = ApkListFragment()
            val args = Bundle()
            args.putParcelableArrayList(APK_ARG, apkList)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments.getParcelableArrayList<Apk>(APK_ARG) != null) {
            apkList = arguments.getParcelableArrayList(APK_ARG)
        } else {
            apkList = ArrayList()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_apk_list, container, false)
        val mRecyclerView: RecyclerView = rootView.find(R.id.apk_list_rv)
        mLinearLayoutManager = LinearLayoutManager(activity)
        mAdapter = ApkListAdapter(apkList, activity)

        mRecyclerView.layoutManager = mLinearLayoutManager
        mRecyclerView.adapter = mAdapter

        val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
        preferences.registerOnSharedPreferenceChangeListener(this)

        updateSortingOrder(preferences.getInt(Utilities.PREF_SORT_KEY, 0))

        return rootView
    }

    override fun onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(activity).unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroy()
    }

    fun updateAdapter() {
        mAdapter.notifyDataSetChanged()
    }

    fun updateSortingOrder(order: Int) {
        val tempList: MutableList<Apk> = apkList.toMutableList()
        when (order) {
            Utilities.SORT_ORDER_NAME -> {
                tempList.sortWith(Comparator { p1, p2 ->
                    context.packageManager.getApplicationLabel(p1.appInfo).toString().toLowerCase()
                            .compareTo(context.packageManager.getApplicationLabel(p2.appInfo).toString().toLowerCase())
                })
            }
            Utilities.SORT_ORDER_INSTALLATION_DATE -> {
                tempList.sortWith(Comparator { p1, p2 ->
                    context.packageManager.getPackageInfo(p2.packageName, 0).firstInstallTime.toString()
                            .compareTo(context.packageManager.getPackageInfo(p1.packageName, 0).firstInstallTime.toString())
                })
            }
            Utilities.SORT_ORDER_UPDATE_DATE -> {
                tempList.sortWith(Comparator { p1, p2 ->
                    context.packageManager.getPackageInfo(p2.packageName, 0).lastUpdateTime.toString()
                            .compareTo(context.packageManager.getPackageInfo(p1.packageName, 0).lastUpdateTime.toString())
                })
            }
            Utilities.SORT_ORDER_SIZE -> {
                tempList.sortWith(Comparator { p1, p2 ->
                    (File(p2.appInfo.sourceDir).length()).compareTo(File(p1.appInfo.sourceDir).length())
                })
            }
        }
        mAdapter.updateData(tempList as ArrayList<Apk>)
    }
}
