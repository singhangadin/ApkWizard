package io.github.rajdeep1008.apkwizard

import android.app.SearchManager
import android.content.*
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import io.github.rajdeep1008.apkwizard.adapters.ApkListAdapter
import io.github.rajdeep1008.apkwizard.adapters.PagerAdapter
import io.github.rajdeep1008.apkwizard.utils.Utilities
import io.github.rajdeep1008.apkwizard.fragments.ApkListFragment
import io.github.rajdeep1008.apkwizard.models.Apk
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener, ApkListAdapter.OnContextItemClickListener {
    private lateinit var searchView: SearchView
    private lateinit var contextItemPackageName: String

    private var broadcastReceiver: BroadcastReceiver? = null

    private val userApkList = mutableListOf<Apk>()
    private val systemApkList = mutableListOf<Apk>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        Utilities.checkPermission(this)

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                updateList()
            }
        }

        setupViewPager(mutableListOf(), mutableListOf())

        doAsync {
            val allPackages: List<PackageInfo> = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)

            allPackages.forEach {
                val applicationInfo: ApplicationInfo = it.applicationInfo

                if ((applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0) {
                    val userApk = Apk(
                            applicationInfo,
                            packageManager.getApplicationLabel(applicationInfo).toString(),
                            it.packageName,
                            it.versionName,
                            false)
                    userApkList.add(userApk)
                } else {
                    val systemApk = Apk(
                            applicationInfo,
                            packageManager.getApplicationLabel(applicationInfo).toString(),
                            it.packageName,
                            it.versionName,
                            true)

                    systemApkList.add(systemApk)
                }
            }

            uiThread {
                setupViewPager(userApkList, systemApkList)
                Utilities.updateSortOrder(this@MainActivity, PreferenceManager.getDefaultSharedPreferences(this@MainActivity).getInt(Utilities.PREF_SORT_KEY, 0))
                progress.visibility = View.GONE
            }
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_INSTALL)
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_FULLY_REMOVED)
        intentFilter.addDataScheme("package")
        registerReceiver(broadcastReceiver, intentFilter)

    }

    override fun onDestroy() {
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver)
            broadcastReceiver = null
        }
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            Utilities.STORAGE_PERMISSION_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Utilities.makeAppDir()
                } else {
                    Snackbar.make(find(android.R.id.content), "Permission required to extract apk", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(this)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.sort_name -> Utilities.updateSortOrder(this, Utilities.SORT_ORDER_NAME)
            R.id.sort_install_date -> Utilities.updateSortOrder(this, Utilities.SORT_ORDER_INSTALLATION_DATE)
            R.id.sort_update_date -> Utilities.updateSortOrder(this, Utilities.SORT_ORDER_UPDATE_DATE)
            R.id.sort_size -> Utilities.updateSortOrder(this, Utilities.SORT_ORDER_SIZE)
            R.id.action_about -> startActivity(Intent(this, AboutActivity::class.java))
        }
        return true
    }

    private fun setupViewPager(userApkList: List<Apk>, systemApkList: List<Apk>) {
        container.adapter = PagerAdapter(supportFragmentManager, userApkList, systemApkList)
        tab_bar.setupWithViewPager(container)
    }

    fun updateList() {
        (container.adapter.instantiateItem(container, 0) as ApkListFragment).updateAdapter()
    }

    override fun onQueryTextSubmit(query: String?): Boolean = true

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText?.isEmpty()!!) {
            ((container.adapter.instantiateItem(container, 0) as ApkListFragment).mRecyclerView.adapter as ApkListAdapter).getFilter().filter("")
            ((container.adapter.instantiateItem(container, 1) as ApkListFragment).mRecyclerView.adapter as ApkListAdapter).getFilter().filter("")
        } else {
            ((container.adapter.instantiateItem(container, 0) as ApkListFragment).mRecyclerView.adapter as ApkListAdapter).getFilter().filter(newText.toLowerCase())
            ((container.adapter.instantiateItem(container, 1) as ApkListFragment).mRecyclerView.adapter as ApkListAdapter).getFilter().filter(newText.toLowerCase())
        }
        return false
    }

    override fun onBackPressed() {
        if (!searchView.isIconified) {
            searchView.isIconified = true
        } else {
            super.onBackPressed()
        }
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_launch -> {
                try {
                    startActivity(packageManager.getLaunchIntentForPackage(contextItemPackageName))
                } catch (e: Exception) {
                    Snackbar.make(find(android.R.id.content), "Can't open this app", Snackbar.LENGTH_SHORT).show()
                }
            }
            R.id.action_playstore -> {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$contextItemPackageName")))
                } catch (e: ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$contextItemPackageName")))
                }

            }
        }
        return true
    }

    override fun onItemClicked(packageName: String) {
        contextItemPackageName = packageName
    }
}
