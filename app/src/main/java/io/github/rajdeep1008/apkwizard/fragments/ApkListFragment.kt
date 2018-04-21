package io.github.rajdeep1008.apkwizard.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.rajdeep1008.apkwizard.R
import io.github.rajdeep1008.apkwizard.adapters.ApkListAdapter
import io.github.rajdeep1008.apkwizard.models.Apk
import org.jetbrains.anko.find


class ApkListFragment : Fragment() {

    private lateinit var apkList: ArrayList<Apk>

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
        val mLinearLayoutManager = LinearLayoutManager(activity)
        val mAdapter = ApkListAdapter(apkList, activity)

        mRecyclerView.layoutManager = mLinearLayoutManager
        mRecyclerView.adapter = mAdapter
        return rootView
    }

}
