package io.github.rajdeep1008.apkwizard.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import io.github.rajdeep1008.apkwizard.R
import io.github.rajdeep1008.apkwizard.DetailActivity
import io.github.rajdeep1008.apkwizard.MainActivity
import io.github.rajdeep1008.apkwizard.utils.Utilities
import io.github.rajdeep1008.apkwizard.models.Apk
import kotlinx.android.synthetic.main.apk_item.view.*
import java.util.*
import android.content.pm.PackageManager


/**
 * Created by rajdeep1008 on 20/04/18.
 */
class ApkListAdapter(var apkList: ArrayList<Apk>, private val context: Context) : RecyclerView.Adapter<ApkListAdapter.ApkListViewHolder>() {

    val mOriginalApkList = apkList
    var mItemClickListener: OnContextItemClickListener? = null

    init {
        mItemClickListener = context as MainActivity
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ApkListViewHolder {
        return ApkListViewHolder(LayoutInflater.from(context).inflate(R.layout.apk_item, parent, false), context, apkList)
    }

    override fun getItemCount(): Int = apkList.size

    override fun onBindViewHolder(holder: ApkListViewHolder?, position: Int) {
        try {
            val app = context.packageManager.getApplicationInfo(apkList[position].packageName, 0)
            val icon = context.packageManager.getApplicationIcon(app)
            val name = context.packageManager.getApplicationLabel(app)

            holder?.mIconImageView?.setImageDrawable(icon)
            holder?.mLabelTextView?.text = name
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        holder?.mPackageTextView?.text = apkList[position].packageName

        if (apkList[position].systemApp)
            holder?.mUninstallBtn?.visibility = View.GONE
    }

    inner class ApkListViewHolder(view: View, context: Context, apkList: ArrayList<Apk>) : RecyclerView.ViewHolder(view) {
        private val mShareBtn: Button = view.share_btn
        private val mMenuBtn: ImageButton = view.menu_btn
        private val mExtractBtn: Button = view.extract_btn

        val mIconImageView: ImageView = view.apk_icon_iv
        val mLabelTextView: TextView = view.apk_label_tv
        val mPackageTextView: TextView = view.apk_package_tv
        val mUninstallBtn: Button = view.uninstall_btn

        init {
            (context as Activity).registerForContextMenu(mMenuBtn)

            itemView.setOnClickListener {
                val transitionName = context.resources.getString(R.string.transition)
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("apk_info", apkList[adapterPosition])

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(context, mIconImageView, transitionName)
                    context.startActivity(intent, activityOptions.toBundle())
                } else {
                    context.startActivity(intent)
                }
            }

            mExtractBtn.setOnClickListener {
                if (Utilities.checkPermission(context as MainActivity)) {
                    Utilities.extractApk(apkList[adapterPosition])
                    val rootView: View = (context).window.decorView.findViewById(android.R.id.content)
                    Snackbar.make(rootView, "${apkList[adapterPosition].appName} apk extracted successfully", Snackbar.LENGTH_LONG).show()
                }
            }

            mShareBtn.setOnClickListener {
                if (Utilities.checkPermission(context as MainActivity)) {
                    val intent = Utilities.getShareableIntent(apkList[adapterPosition])
                    context.startActivity(Intent.createChooser(intent, "Share the apk using"))
                }
            }

            mUninstallBtn.setOnClickListener {
                val uninstallIntent = Intent(Intent.ACTION_UNINSTALL_PACKAGE)
                uninstallIntent.data = Uri.parse("package:" + apkList[adapterPosition].packageName)
                uninstallIntent.putExtra(Intent.EXTRA_RETURN_RESULT, true)
                context.startActivity(uninstallIntent)
            }

            mMenuBtn.setOnClickListener {
                mItemClickListener?.onItemClicked(apkList[adapterPosition].packageName!!)
                context.openContextMenu(mMenuBtn)
                mMenuBtn.setOnCreateContextMenuListener { menu, _, _ ->
                    context.menuInflater.inflate(R.menu.context_menu, menu)
                }
            }
        }
    }

    fun updateData(list: ArrayList<Apk>) {
        apkList = list
        notifyDataSetChanged()
    }

    fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                val results = ArrayList<Apk>()

                if ((constraint != null) and (constraint.toString().isNotEmpty())) {
                    for (apk in mOriginalApkList) {
                        if (apk.appName.toLowerCase().contains(constraint.toString()) or apk.packageName?.toLowerCase()?.contains(constraint.toString())!!) {
                            results.add(apk)
                        }
                    }

                    filterResults.values = results
                    filterResults.count = results.size
                } else {
                    filterResults.values = mOriginalApkList
                    filterResults.count = mOriginalApkList.size
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                apkList = results?.values as ArrayList<Apk>
                notifyDataSetChanged()
            }
        }
    }

    interface OnContextItemClickListener {
        fun onItemClicked(packageName: String)
    }
}