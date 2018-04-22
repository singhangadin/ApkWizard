package io.github.rajdeep1008.apkwizard.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import io.github.rajdeep1008.apkwizard.R
import io.github.rajdeep1008.apkwizard.extras.Utilities
import io.github.rajdeep1008.apkwizard.activities.MainActivity
import io.github.rajdeep1008.apkwizard.models.Apk
import org.jetbrains.anko.find

/**
 * Created by rajdeep1008 on 20/04/18.
 */
class ApkListAdapter(var apkList: ArrayList<Apk>, val context: Context) : RecyclerView.Adapter<ApkListAdapter.ApkListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ApkListViewHolder {
        return ApkListViewHolder(LayoutInflater.from(context).inflate(R.layout.apk_item, parent, false), context)
    }

    override fun getItemCount(): Int {
        return apkList.size
    }

    override fun onBindViewHolder(holder: ApkListViewHolder?, position: Int) {
        holder?.mIconImageView?.setImageDrawable(context.packageManager.getApplicationIcon(apkList.get(position).appInfo))
        holder?.mLabelTextView?.text = context.packageManager.getApplicationLabel(apkList.get(position).appInfo).toString()
        holder?.mPackageTextView?.text = apkList.get(position).packageName
    }

    class ApkListViewHolder(view: View, context: Context) : RecyclerView.ViewHolder(view) {

        val mIconImageView: ImageView
        val mLabelTextView: TextView
        val mPackageTextView: TextView
        val mExtractBtn: Button
        val mShareBtn: Button
        val mUninstallBtn: Button
        val mMenuBtn: ImageButton

        init {
            mIconImageView = view.find(R.id.apk_icon_iv)
            mLabelTextView = view.find(R.id.apk_label_tv)
            mPackageTextView = view.find(R.id.apk_package_tv)
            mExtractBtn = view.find(R.id.extract_btn)
            mShareBtn = view.find(R.id.share_btn)
            mUninstallBtn = view.find(R.id.uninstall_btn)
            mMenuBtn = view.find(R.id.menu_btn)

            mIconImageView.setOnClickListener { Log.d("test", "Image clicked") }
            itemView.setOnClickListener { Log.d("test", "Item clicked") }
            mExtractBtn.setOnClickListener {
                if (Utilities.checkPermission(context as MainActivity)) {

                }
            }
        }
    }
}