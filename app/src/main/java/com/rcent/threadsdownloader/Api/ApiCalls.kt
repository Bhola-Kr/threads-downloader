package com.rcent.threadsdownloader.Api

import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class ApiCalls {

    private var progressDialog: ProgressDialog? = null
    fun DownloadTikTokVideos(context: Context, tikUrl: String) {
        showProgressDialog(context)

        val client = OkHttpClient()

        val request: Request = Request.Builder()
            .url("https://tiktok-downloader-download-tiktok-videos-without-watermark.p.rapidapi.com/vid/index?url=$tikUrl")
            .get()
            .addHeader(
                "X-RapidAPI-Key",
                "6006d97495msh704b5f2cab5cd7ep12eb79jsnabb6766fe157"
            )
            .addHeader(
                "X-RapidAPI-Host",
                "tiktok-downloader-download-tiktok-videos-without-watermark.p.rapidapi.com"
            )
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                hideProgressDialog()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    var myresponse = response.body!!.string()

                    val jObj = JSONObject(myresponse)
                    val jArray = jObj.getJSONArray("video") as JSONArray

                    for (i in 0..0) {

                        val jArray2 = jArray.getString(i)
                        val dlRequest = DownloadManager.Request(Uri.parse(jArray2))
                        dlRequest.setAllowedOverRoaming(true)
                            .setTitle("TikTok00" + System.currentTimeMillis())
                            .setDescription("Recent Media").setNotificationVisibility(
                                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                            )
                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                            .allowScanningByMediaScanner()
                        dlRequest.setAllowedOverMetered(true)
                            .setDestinationInExternalFilesDir(
                                context,
                                Environment.DIRECTORY_DOWNLOADS,
                                "TikTok" + System.currentTimeMillis() + ".mp4"
                            )

                        val downloadManager =
                            context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                        downloadManager.enqueue(dlRequest)
                        hideProgressDialog()

                    }


                }
            }
        })


    }

    fun DownloadThreadsVideos(context: Context, threadUrl: String) {
        showProgressDialog(context)
        val client = OkHttpClient()

        val request: Request = Request.Builder()
            .url("https://us-central1-fir-api-55d80.cloudfunctions.net/api/threads/downloader?url=$threadUrl")
            .get()
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                hideProgressDialog()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    var myresponse = response.body!!.string()
                    var video: String;
                    var type: String;
                    val jsonResponse = JSONObject(myresponse)

//                    val videoUrlsArray = jsonResponse.getJSONArray("video_urls")
//                    val imageUrlsArray = jsonResponse.getJSONArray("image_urls")
//
//                    if (videoUrlsArray.length() <= 0) {
//                        video = imageUrlsArray.getString(0)
//                        println("Image URL: $video")
//                    } else {
//                        val firstVideoObject = videoUrlsArray.getJSONObject(0)
//                        video = firstVideoObject.getString("download_url")
//                    }

                    val contentArray = jsonResponse.getJSONArray("content")
                    if (contentArray.length() > 0) {
                        val contentObject = contentArray.getJSONObject(0)
                        video = contentObject.getString("src")
                        type = contentObject.getString("type")
                        println("Content Type: $type")
                    } else {
                        println("No content found.")
                        return
                    }

                    println("Download URL: $video")

                    val dlRequest = DownloadManager.Request(Uri.parse(video.toString()))
                    dlRequest.setAllowedOverRoaming(true)
                        .setTitle("threads00" + System.currentTimeMillis())
                        .setDescription("Recent Media").setNotificationVisibility(
                            DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                        )
                        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                        .allowScanningByMediaScanner()
                    dlRequest.setAllowedOverMetered(true)
                        .setDestinationInExternalFilesDir(
                            context,
                            Environment.DIRECTORY_DOWNLOADS,
                            "Threads" + System.currentTimeMillis() + if (type == "video") ".mp4" else ".png"
                        )

                    val downloadManagerInsta =
                        context.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
                    downloadManagerInsta.enqueue(dlRequest)
                    hideProgressDialog()

                }

            }
        })


    }


    fun DownloadInstaVideos(context: Context, instaUrl: String) {
        showProgressDialog(context)
        val client = OkHttpClient()

        val request: Request = Request.Builder()
            .url("https://instagram-downloader-download-instagram-videos-stories.p.rapidapi.com/index?url=$instaUrl")
            .get()
            .addHeader(
                "X-RapidAPI-Key",
                "4130ce89c1mshd39543e2a88d47dp1877a7jsn0ed7e153cb4c"
            )
            .addHeader(
                "X-RapidAPI-Host",
                "instagram-downloader-download-instagram-videos-stories.p.rapidapi.com"
            )
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                hideProgressDialog()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    var myresponse = response.body!!.string()

                    val jObj = JSONObject(myresponse)
                    val video = jObj.getString("media")

                    println(video.toString())

                    val dlRequest = DownloadManager.Request(Uri.parse(video.toString()))
                    dlRequest.setAllowedOverRoaming(true)
                        .setTitle("instagram00" + System.currentTimeMillis())
                        .setDescription("Recent Media").setNotificationVisibility(
                            DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                        )
                        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                        .allowScanningByMediaScanner()
                    dlRequest.setAllowedOverMetered(true)
                        .setDestinationInExternalFilesDir(
                            context,
                            Environment.DIRECTORY_DOWNLOADS,
                            "Instagram" + System.currentTimeMillis() + ".mp4"
                        )

                    val downloadManagerInsta =
                        context.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
                    downloadManagerInsta.enqueue(dlRequest)
                    hideProgressDialog()
                }

            }
        })


    }

    fun DownloadFbVideos(context: Context, Fb_Url: String) {

        showProgressDialog(context)
        val client = OkHttpClient()

        val request: Request = Request.Builder()
            .url("https://facebook-reel-and-video-downloader.p.rapidapi.com/app/main.php?url=$Fb_Url")
            .get()
            .addHeader(
                "X-RapidAPI-Key",
                "6006d97495msh704b5f2cab5cd7ep12eb79jsnabb6766fe157"
            )
            .addHeader(
                "X-RapidAPI-Host",
                "facebook-reel-and-video-downloader.p.rapidapi.com"
            )
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                hideProgressDialog()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val myresponse = response.body!!.string()


                    val jObj = JSONObject(myresponse)
                    if (myresponse.contains("Download High Quality") && myresponse.contains("Download Low Quality")) {


                        val video = jObj.getJSONObject("links")

                        val hdVideo = video.getString("Download High Quality")

                        println(hdVideo.toString())

                        val dlRequest = DownloadManager.Request(Uri.parse(hdVideo.toString()))
                        dlRequest.setAllowedOverRoaming(true)
                            .setTitle("FB00" + System.currentTimeMillis())
                            .setDescription("Recent Media").setNotificationVisibility(
                                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                            )
                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                            .allowScanningByMediaScanner()
                        dlRequest.setAllowedOverMetered(true)
                            .setDestinationInExternalFilesDir(
                                context,
                                Environment.DIRECTORY_DOWNLOADS,
                                "Facebook" + System.currentTimeMillis() + ".mp4"
                            )

                        val downloadManagerFbHD =
                            context.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
                        downloadManagerFbHD.enqueue(dlRequest)

                        hideProgressDialog()

                    } else if (myresponse.contains("Download Low Quality") && !myresponse.contains("Download High Quality")) {

                        val video = jObj.getJSONObject("links")

                        val sdVideo = video.getString("Download High Quality")

                        println(sdVideo.toString())

                        val dlRequest = DownloadManager.Request(Uri.parse(sdVideo.toString()))
                        dlRequest.setAllowedOverRoaming(true)
                            .setTitle("FB00" + System.currentTimeMillis())
                            .setDescription("Recent Media").setNotificationVisibility(
                                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                            )
                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                            .allowScanningByMediaScanner()
                        dlRequest.setAllowedOverMetered(true)
                            .setDestinationInExternalFilesDir(
                                context,
                                Environment.DIRECTORY_DOWNLOADS,
                                "Facebook" + System.currentTimeMillis() + ".mp4"
                            )

                        val downloadManagerFbSD =
                            context.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
                        downloadManagerFbSD.enqueue(dlRequest)
                        hideProgressDialog()

                    } else {
                        hideProgressDialog()
                    }

                }
            }

        })


    }

    private fun showProgressDialog(context: Context) {
        // Initialize the progress dialog if it's not already initialized
        if (progressDialog == null) {
            progressDialog = ProgressDialog(context)
            progressDialog?.setMessage("Loading...") // Set the message for the progress dialog
            progressDialog?.setCancelable(false) // Set whether the dialog can be canceled by tapping outside
        }

        // Show the progress dialog
        progressDialog?.show()
    }

    // Function to hide the progress dialog
    private fun hideProgressDialog() {
        progressDialog?.dismiss()
    }


}