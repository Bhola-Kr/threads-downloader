package com.rcent.threadsdownloader.web

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil

import com.rcent.threadsdownloader.Utils.NetworkChangeListener
import com.rcent.threadsdownloader.Dialogues.AboutusDialoge
import com.rcent.threadsdownloader.Dialogues.RateUsDialoge
import com.rcent.threadsdownloader.MainActivity
import com.rcent.threadsdownloader.R
import com.rcent.threadsdownloader.databinding.ActivityLinksBinding

class Links : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var urlWeb: String
    private lateinit var binding: ActivityLinksBinding

    private val networkchange: NetworkChangeListener = NetworkChangeListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_links)


        // Toolbar Setup
        toolbar = findViewById(R.id.customToolbar)
        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(
            this@Links, binding.webDrawer,
            toolbar, R.string.open, R.string.close
        )
        binding.webDrawer.addDrawerListener(toggle)
        toggle.syncState()
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
        setToolbar()



        urlWeb = intent.getStringExtra("link").toString()

        binding.webView.loadUrl(urlWeb)
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = object : WebViewClient() {


        }

    }

    fun setAboutUsDialoge() {
        val dashBoardDialog = AboutusDialoge(this@Links)
        dashBoardDialog.setCancelable(false)
        dashBoardDialog.show()
    }


    fun settingRateUsDialoge() {
        val rate = RateUsDialoge(this@Links)
        rate.getWindow()?.setBackgroundDrawable(
            ColorDrawable(
                resources
                    .getColor(android.R.color.white)
            )
        )
        rate.setCancelable(false)
        rate.show()
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun setToolbar() {
        binding.navWeb!!.setNavigationItemSelectedListener { item: MenuItem ->
            val id = item.itemId
            when (id) {
                R.id.menu_home -> {
                    startActivity(Intent(this@Links,MainActivity::class.java))
                    binding.webDrawer!!.closeDrawer(GravityCompat.START)
                    finish()
                }

                R.id.menu_fb -> {
                    if (urlWeb == "https://www.facebook.com/") {
                        binding.webDrawer!!.closeDrawer(GravityCompat.START)

                    } else {
                        finish()
                        binding.webView.loadUrl("https://www.facebook.com/")
                        binding.webView.settings.javaScriptEnabled = true
                        binding.webDrawer!!.closeDrawer(GravityCompat.START)
                    }

                }

                R.id.menu_tiktok -> {

                    if (urlWeb == "https://www.tiktok.com/") {
                        binding.webDrawer!!.closeDrawer(GravityCompat.START)

                    } else {
                        binding.webView.loadUrl("https://www.tiktok.com/")
                        binding.webView.settings.javaScriptEnabled = true
                        binding.webDrawer!!.closeDrawer(GravityCompat.START)
                    }
                }
                R.id.menu_insta -> {

                    if (urlWeb == "https://www.instagram.com/") {
                        binding.webDrawer!!.closeDrawer(GravityCompat.START)

                    } else {
                        binding.webView.loadUrl("https://www.instagram.com/")
                        binding.webView.settings.javaScriptEnabled = true
                        binding.webDrawer!!.closeDrawer(GravityCompat.START)
                    }
                }
                R.id.menu_feedback -> {
                    settingRateUsDialoge()
                    binding.webDrawer!!.closeDrawer(GravityCompat.START)
                }
                R.id.menu_about_us -> {
                    setAboutUsDialoge()
                    binding.webDrawer!!.closeDrawer(GravityCompat.START)
                }
                R.id.menu_exit -> {

                    val intent = Intent(this@Links, MainActivity::class.java)
                    intent.putExtra("exit",true)
                    Toast.makeText(this@Links, "App exit", Toast.LENGTH_LONG).show()
                    binding.webDrawer!!.closeDrawer(GravityCompat.START)
                    startActivity(intent)
                    finish()
                }
            }
            true
        }
    }


    override fun onBackPressed() {

        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            finish()
            super.onBackPressed()
        }
    }

    override fun onStart() {
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkchange, intentFilter)
        super.onStart()
    }

    override fun onStop() {
        unregisterReceiver(networkchange)
        super.onStop()
    }

}