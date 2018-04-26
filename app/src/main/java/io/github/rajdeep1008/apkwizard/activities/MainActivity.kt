package io.github.rajdeep1008.apkwizard.activities

import android.app.SearchManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import io.github.rajdeep1008.apkwizard.R
import io.github.rajdeep1008.apkwizard.adapters.ApkListAdapter
import io.github.rajdeep1008.apkwizard.adapters.PagerAdapter
import io.github.rajdeep1008.apkwizard.extras.Utilities
import io.github.rajdeep1008.apkwizard.fragments.ApkListFragment
import io.github.rajdeep1008.apkwizard.models.Apk
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var toolbar: Toolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var tabLayout: TabLayout
    private lateinit var mViewPager: ViewPager
    private lateinit var searchView: SearchView
    private var broadcastReceiver: BroadcastReceiver? = null

    private val userApkList = mutableListOf<Apk>()
    private val systemApkList = mutableListOf<Apk>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = find(R.id.toolbar)
        progressBar = find(R.id.progress)
        mViewPager = find(R.id.container)
        tabLayout = find(R.id.tab_bar)

        setSupportActionBar(toolbar)
        Utilities.checkPermission(this)

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                updateList()
            }
        }

        setupViewPager(mutableListOf<Apk>(), mutableListOf<Apk>())

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
                progressBar.visibility = View.GONE
            }
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_INSTALL)
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_FULLY_REMOVED)
        intentFilter.addDataScheme("package");
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
            R.id.sort_name -> {
                Utilities.updateSortOrder(this, Utilities.SORT_ORDER_NAME)
            }
            R.id.sort_install_date -> {
                Utilities.updateSortOrder(this, Utilities.SORT_ORDER_INSTALLATION_DATE)
            }
            R.id.sort_update_date -> {
                Utilities.updateSortOrder(this, Utilities.SORT_ORDER_UPDATE_DATE)
            }
            R.id.sort_size -> {
                Utilities.updateSortOrder(this, Utilities.SORT_ORDER_SIZE)
            }
        }
        return true
    }

    fun setupViewPager(userApkList: List<Apk>, systemApkList: List<Apk>) {
        mViewPager.adapter = PagerAdapter(supportFragmentManager, this, userApkList, systemApkList)
        tabLayout.setupWithViewPager(mViewPager)
    }

    fun updateList() {
        (mViewPager.adapter.instantiateItem(mViewPager, 0) as ApkListFragment).updateAdapter()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText?.isEmpty()!!) {
            ((mViewPager.adapter.instantiateItem(mViewPager, 0) as ApkListFragment).mRecyclerView.adapter as ApkListAdapter).getFilter().filter("")
            ((mViewPager.adapter.instantiateItem(mViewPager, 1) as ApkListFragment).mRecyclerView.adapter as ApkListAdapter).getFilter().filter("")
        } else {
            ((mViewPager.adapter.instantiateItem(mViewPager, 0) as ApkListFragment).mRecyclerView.adapter as ApkListAdapter).getFilter().filter(newText.toLowerCase())
            ((mViewPager.adapter.instantiateItem(mViewPager, 1) as ApkListFragment).mRecyclerView.adapter as ApkListAdapter).getFilter().filter(newText.toLowerCase())
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
}
