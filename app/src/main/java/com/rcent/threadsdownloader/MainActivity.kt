package com.rcent.threadsdownloader

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil


import com.google.android.gms.ads.*
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.rcent.threadsdownloader.Api.ApiCalls

import com.rcent.threadsdownloader.Utils.NetworkChangeListener
import com.rcent.threadsdownloader.Dialogues.AboutusDialoge
import com.rcent.threadsdownloader.Dialogues.RateUsDialoge
import com.rcent.threadsdownloader.databinding.ActivityMainBinding
import com.rcent.threadsdownloader.web.Links

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var onBackPressed = false
    private var dashBoardInter: InterstitialAd? = null
    private var dashBannerCont: RelativeLayout? = null

    private lateinit var toolbar: Toolbar
    private lateinit var toggle: ActionBarDrawerToggle
    private val networkchange: NetworkChangeListener = NetworkChangeListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (intent.getBooleanExtra("exit", false)) {
            finish()
        }
        // Toolbar Setup
        toolbar = findViewById(R.id.customToolbar)
        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(
            this@MainActivity, binding.drawerLayout,
            toolbar, R.string.open, R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
        setToolbar()
        // Find Views
        dashBannerCont = findViewById(R.id.bannerContainer)
        // nativeContainer = findViewById(R.id.nativeAdContainer);
        // nativeContainer = findViewById(R.id.nativeAdContainer);
        isStoragePermissionGranted()

        // Initialize Ads
        MobileAds.initialize(this) {}

        loadInterstitialAds()
        loadBottomBannerAds()

        binding.btnDownload.setOnClickListener(View.OnClickListener {
            if (binding.etUrl.text.isEmpty()) {
                Toast.makeText(
                    this@MainActivity,
                    "Please Paste Url First for the download",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (binding.etUrl.text.toString()
                    .contains("youtube.com") || binding.etUrl.text.toString().contains("youtu.be")
            ) {
                if (dashBoardInter != null) {
                    dashBoardInter!!.show(this@MainActivity)
                    dashBoardInter!!.setFullScreenContentCallback(object :
                        FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            Toast.makeText(
                                this@MainActivity,
                                "Please wait your Download will begin Shortly",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Implement Nature Interstitial Ad
                            loadInterstitialAds()
                            super.onAdDismissedFullScreenContent()
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            super.onAdFailedToShowFullScreenContent(adError)
                            Toast.makeText(
                                this@MainActivity,
                                "Error: " + adError.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                }
            } else if (binding.etUrl.text.toString().contains("tiktok.com")) {
                val tiktokUrl = binding.etUrl.text.toString()

                if (dashBoardInter != null) {
                    dashBoardInter!!.show(this@MainActivity)
                    dashBoardInter!!.setFullScreenContentCallback(object :
                        FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            Toast.makeText(
                                this@MainActivity,
                                "Please wait your Download will begin Shortly",
                                Toast.LENGTH_SHORT
                            ).show()
                            loadInterstitialAds()
                            val tiktokApicall = ApiCalls()
                            tiktokApicall.DownloadTikTokVideos(
                                this@MainActivity,
                                tikUrl = tiktokUrl.toString()
                            )
                            Toast.makeText(
                                this@MainActivity,
                                "Download started....!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            super.onAdDismissedFullScreenContent()
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            super.onAdFailedToShowFullScreenContent(adError)
                            Toast.makeText(
                                this@MainActivity,
                                "Error: " + adError.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                } else {
                    loadInterstitialAds()
                    val tiktokApicall = ApiCalls()
                    tiktokApicall.DownloadTikTokVideos(
                        this@MainActivity,
                        tikUrl = tiktokUrl.toString()
                    )
                }

            } else if (binding.etUrl.text.toString().contains("www.instagram.com")) {
                if (binding.etUrl.text.toString().contains("reel") || binding.etUrl.text.toString()
                        .contains("stories")
                    || binding.etUrl.text.toString().contains("tv") || binding.etUrl.text.toString()
                        .contains("/p/")
                ) {

                    val InstaUrl = binding.etUrl.text.toString()


                    if (dashBoardInter != null) {
                        dashBoardInter!!.show(this@MainActivity)
                        dashBoardInter!!.setFullScreenContentCallback(object :
                            FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Please wait your Download will begin Shortly",
                                    Toast.LENGTH_SHORT
                                ).show()
                                // Implement Interstitial Ad
                                loadInterstitialAds()
                                // Implement Api
                                val instaApiCall = ApiCalls()
                                instaApiCall.DownloadInstaVideos(
                                    this@MainActivity,
                                    instaUrl = InstaUrl
                                )

                                super.onAdDismissedFullScreenContent()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                super.onAdFailedToShowFullScreenContent(adError)
                                Toast.makeText(
                                    this@MainActivity,
                                    "Error: " + adError.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                    } else {
                        loadInterstitialAds()
                        // Implement Api
                        val instaApiCall = ApiCalls()
                        instaApiCall.DownloadInstaVideos(
                            this@MainActivity,
                            instaUrl = InstaUrl
                        )
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Please Enter Valid Instagram Video Link",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } else if (binding.etUrl.text.toString().contains("www.threads.net")) {
                if (binding.etUrl.text.toString().contains("post") || binding.etUrl.text.toString()
                        .contains("igshid")
                    || binding.etUrl.text.toString()
                        .contains("threads") || binding.etUrl.text.toString()
                        .contains("/p/")
                ) {

                    val threadUrl = binding.etUrl.text.toString()


                    if (dashBoardInter != null) {
                        dashBoardInter!!.show(this@MainActivity)
                        dashBoardInter!!.setFullScreenContentCallback(object :
                            FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Please wait your Download will begin Shortly",
                                    Toast.LENGTH_SHORT
                                ).show()
                                // Implement Interstitial Ad
                                loadInterstitialAds()
                                // Implement Api
                                val instaApiCall = ApiCalls()
                                instaApiCall.DownloadThreadsVideos(
                                    this@MainActivity,
                                    threadUrl = threadUrl
                                )

                                super.onAdDismissedFullScreenContent()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                super.onAdFailedToShowFullScreenContent(adError)
                                Toast.makeText(
                                    this@MainActivity,
                                    "Error: " + adError.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                    } else {
                        val instaApiCall = ApiCalls()
                        instaApiCall.DownloadThreadsVideos(
                            this@MainActivity,
                            threadUrl = threadUrl
                        )
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Please Enter Valid Threads Video Link",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } else if (binding.etUrl.text.toString()
                    .contains("www.facebook.com") || binding.etUrl.text.toString()
                    .contains("fb.watch")
            ) {

                if (binding.etUrl.text.toString().contains("videos")
                    || binding.etUrl.text.toString().contains("reel")
                    || binding.etUrl.text.toString().contains("fb.watch")
                ) {

                    val FbUrl = binding.etUrl.text.toString()

                    if (dashBoardInter != null) {
                        dashBoardInter!!.show(this@MainActivity)
                        dashBoardInter!!.setFullScreenContentCallback(object :
                            FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Please wait your Download will begin Shortly",
                                    Toast.LENGTH_SHORT
                                ).show()
                                // Implement Interstitial Ad
                                loadInterstitialAds()
                                // Implement Api
                                val FbApiCall = ApiCalls()
                                FbApiCall.DownloadFbVideos(this@MainActivity, Fb_Url = FbUrl)

                                super.onAdDismissedFullScreenContent()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                super.onAdFailedToShowFullScreenContent(adError)
                                Toast.makeText(
                                    this@MainActivity,
                                    "Error: " + adError.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                    } else {
                        loadInterstitialAds()
                        // Implement Api
                        val FbApiCall = ApiCalls()
                        FbApiCall.DownloadFbVideos(this@MainActivity, Fb_Url = FbUrl)
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Please Enter Valid Facebook Video Link",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } else {
                Toast.makeText(this@MainActivity, "Please Enter Valid Url", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        binding.fbIcWeb.setOnClickListener {

            val intent = Intent(this, Links::class.java)
            intent.putExtra("link", "https://www.facebook.com/")
            startActivity(intent)

        }

        binding.instaIcWeb.setOnClickListener {

            val intent = Intent(this, Links::class.java)
            intent.putExtra("link", "https://www.instagram.com/")
            startActivity(intent)
        }

        binding.threadsIcWeb.setOnClickListener {
            val intent = Intent(this, Links::class.java)
            intent.putExtra("link", "https://www.threads.net")
            startActivity(intent)
        }
    }

    private fun isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.v(ContentValues.TAG, "Permission is granted")
            } else {
                Log.v(ContentValues.TAG, "Permission is revoked")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(ContentValues.TAG, "Permission is granted")
        }
    }

    @SuppressLint("NonConstantResourceId")
    fun setToolbar() {
        binding.navMain!!.setNavigationItemSelectedListener { item: MenuItem ->
            val id = item.itemId
            when (id) {
                R.id.menu_home -> binding.drawerLayout!!.closeDrawer(GravityCompat.START)

                R.id.menu_fb -> {
                    val intent = Intent(this, Links::class.java)
                    intent.putExtra("link", "https://www.facebook.com/")
                    startActivity(intent)
                    binding.drawerLayout!!.closeDrawer(GravityCompat.START)
                }

                R.id.menu_tiktok -> {
                    val intent = Intent(this, Links::class.java)
                    intent.putExtra("link", "https://www.tiktok.com/")
                    startActivity(intent)
                    binding.drawerLayout!!.closeDrawer(GravityCompat.START)
                }
                R.id.menu_insta -> {
                    val intent = Intent(this, Links::class.java)
                    intent.putExtra("link", "https://www.instagram.com/")
                    startActivity(intent)
                    binding.drawerLayout!!.closeDrawer(GravityCompat.START)
                }
                R.id.menu_feedback -> {
                    settingRateUsDialoge()
                    binding.drawerLayout!!.closeDrawer(GravityCompat.START)
                }
                R.id.menu_about_us -> {
                    setAboutUsDialoge()
                    binding.drawerLayout!!.closeDrawer(GravityCompat.START)
                }
                R.id.menu_exit -> {
                    Toast.makeText(this@MainActivity, "Exiting Application", Toast.LENGTH_LONG)
                        .show()
                    finish()
                    binding.drawerLayout!!.closeDrawer(GravityCompat.START)
                }
            }
            true
        }
    }

    fun settingRateUsDialoge() {
        val rate = RateUsDialoge(this@MainActivity)
        rate.window?.setBackgroundDrawable(
            ColorDrawable(
                resources
                    .getColor(android.R.color.white)
            )
        )
        rate.setCancelable(false)
        rate.show()
    }

    fun setAboutUsDialoge() {
        val dashBoardDialog = AboutusDialoge(this@MainActivity)
        dashBoardDialog.setCancelable(false)
        dashBoardDialog.show()
    }

    private fun loadInterstitialAds() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            "ca-app-pub-3230017247689957/5777739279",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adError.toString().let { Log.d("TAG", it) }
                    dashBoardInter = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d("TAG", "Ad was loaded.")
                    dashBoardInter = interstitialAd
                }
            })
    }

    private fun loadBottomBannerAds() {
        val mAdView: AdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }


    override fun onBackPressed() {
        if (onBackPressed) {
            super.onBackPressed()
        } else {
            onBackPressed = true
            Toast.makeText(this, "Press Again to exit", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ onBackPressed = false }, 3000)
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