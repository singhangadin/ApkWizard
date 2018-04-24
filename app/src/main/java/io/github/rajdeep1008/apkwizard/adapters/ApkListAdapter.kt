package io.github.rajdeep1008.apkwizard.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import io.github.rajdeep1008.apkwizard.R
import io.github.rajdeep1008.apkwizard.activities.MainActivity
import io.github.rajdeep1008.apkwizard.extras.Utilities
import io.github.rajdeep1008.apkwizard.models.Apk
import org.jetbrains.anko.find


/**
 * Created by rajdeep1008 on 20/04/18.
 */
class ApkListAdapter(var apkList: ArrayList<Apk>, val context: Context) : RecyclerView.Adapter<ApkListAdapter.ApkListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ApkListViewHolder {
        return ApkListViewHolder(LayoutInflater.from(context).inflate(R.layout.apk_item, parent, false), context, apkList)
    }

    override fun getItemCount(): Int {
        return apkList.size
    }

    override fun onBindViewHolder(holder: ApkListViewHolder?, position: Int) {
        holder?.mIconImageView?.setImageDrawable(context.packageManager.getApplicationIcon(apkList.get(position).appInfo))
        holder?.mLabelTextView?.text = context.packageManager.getApplicationLabel(apkList.get(position).appInfo).toString()
        holder?.mPackageTextView?.text = apkList.get(position).packageName
        if (apkList.get(position).systemApp) holder?.mUninstallBtn?.visibility = View.GONE
    }

    class ApkListViewHolder(view: View, context: Context, apkList: ArrayList<Apk>) : RecyclerView.ViewHolder(view) {

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

            itemView.setOnClickListener {
                //                Log.d("apk", apkList.get(adapterPosition).appInfo.sourceDir)
            }

            mExtractBtn.setOnClickListener {
                if (Utilities.checkPermission(context as MainActivity)) {
                    Utilities.extractApk(apkList.get(adapterPosition))
                    val rootView: View = (context as MainActivity).window.decorView.findViewById(android.R.id.content)
                    Snackbar.make(rootView, "${apkList.get(adapterPosition).appName} apk extracted successfully", Snackbar.LENGTH_LONG).show()
                }
            }

            mShareBtn.setOnClickListener {
                if (Utilities.checkPermission(context as MainActivity)) {
                    val intent = Utilities.getShareableIntent(apkList.get(adapterPosition))
                    context.startActivity(Intent.createChooser(intent, "Share the apk using"))
                }
            }

            mUninstallBtn.setOnClickListener {
                val uninstallIntent = Intent(Intent.ACTION_UNINSTALL_PACKAGE)
                uninstallIntent.setData(Uri.parse("package:" + apkList[adapterPosition].packageName));
                uninstallIntent.putExtra(Intent.EXTRA_RETURN_RESULT, true)
                context.startActivity(uninstallIntent)
            }
        }
    }
}