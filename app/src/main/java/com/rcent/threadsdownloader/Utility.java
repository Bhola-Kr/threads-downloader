package com.rcent.threadsdownloader;

import static android.util.DisplayMetrics.DENSITY_DEFAULT;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

    public static final String EMPTY_STRING = "";
    public static int MAX_WIDTH = 220;
    public static int MAX_HEIGHT = 158;
    public static final String HOCKEY_APP_ID = "259c1d2715394654a13957b13f35d94d";
    public static String STREMAILADDREGEX = "^[_A-Za-z0-9]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,4})$"; //EMAIL REGEX
    public static String cameraPermissions[] = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private static String AuthToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2NjIzMDYwOTEsImF1ZCI6IjcyNTU4ODY2NTgiLCJpc3MiOiJwaWNrdXJwYWdlLmNvbSJ9.ieC6e_8GkjJ6ztrVqhm-ELA5F04k_hnunhfuouiiFms";
    private static String mPushwooshRegisterId = "";
    private static String loginUserFcmToken = "", loginUserId = "62c963b191866b70c353d738", loginUserName = "", loginUserImage = "", loginUserGender = "", loginUserCountry = "";
    private static String chatMessage = "", chatTime = "";
    public static final int REQUEST_CAMERA_IMAGE = 100;

    public static String getDeviceId() {
        String deviceId = null;
        if (deviceId == null) {
            String m_szDevIDShort = "35" +
                    (Build.BOARD.length() % 10)
                    + (Build.BRAND.length() % 10)
                    + (Build.CPU_ABI.length() % 10)
                    + (Build.DEVICE.length() % 10)
                    + (Build.MANUFACTURER.length() % 10)
                    + (Build.MODEL.length() % 10)
                    + (Build.PRODUCT.length() % 10);
            String serial = null;
            try {
                serial = Build.class.getField("SERIAL").get(null).toString();
                // Go ahead and return the serial for api => 9
                deviceId = new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
                return deviceId;
            } catch (Exception e) {
                // String needs to be initialized
                serial = "icreon"; // some value
            }
            // Thanks @Joe!
            // http://stackoverflow.com/a/2853253/950427
            // Finally, combine the values we have found by using the UUID class to create a unique identifier
            deviceId = new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
            return deviceId;
        } else {
            return deviceId;
        }
    }

    public static String getDeviceModelName() {
        String deviceModelName;
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            deviceModelName = capitalize(model);
        } else {
            deviceModelName = capitalize(manufacturer) + " " + model;
        }
        return deviceModelName;


    }

    @SuppressLint("HardwareIds")
    public static String getDeviceID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    @SuppressLint("MissingPermission")
    public static String getDeviceName() {
        String deviceName;
        try {
            BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
            deviceName = myDevice.getName();
            if (deviceName == null || deviceName.equals("")) {
                deviceName = "" + getDeviceModelName();
            }
        } catch (Exception e) {
            deviceName = "NA";
        }
        return deviceName;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

    public static String getDeviceManufacturerName() {
        return Build.MANUFACTURER.toLowerCase(Locale.getDefault());
    }

    public static String getAppVersion(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (Exception e) {
            return "1.0";
        }
    }

    public static int getAppVersionCode(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionCode;
        } catch (Exception e) {
            return 1;
        }
    }

    public static int getAppVersionNumber(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionCode;
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * Method will convert the string to base64 format
     *
     * @param txtString, needs to be base64 encoded
     * @return
     */

    public static String convertBase64(String txtString) {
        try {
            return Base64.encodeToString(txtString.getBytes("UTF-8"), Base64.NO_WRAP);
        } catch (Exception e) {
            return "";
        }
    }

    public static String convertToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

//    public static String getAuthToken(Context appContext) {
//        if (AuthToken == null || AuthToken.equals("")) {
//            AuthToken = PreferenceManager.getInstance()
//                    .getStringPreference(appContext,
//                            PreferenceManager.AUTHORIZATION);
//        }
//        return AuthToken;
//    }


    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DENSITY_DEFAULT);
        return px;
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void showSoftKeyboard(Activity activity, View view) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(view, 0);
        }
    }

    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int getWindowWidth(Activity mcontext) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mcontext.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return width;
    }

    /**
     * Method will set the tint color on imageview with colorid
     *
     * @param context
     * @param resourceId
     * @param colourId
     * @return
     */
    public static Drawable setTintOnDrawable(Context context, int resourceId, int colourId) {
        Drawable drawable = AppCompatResources.getDrawable(context, resourceId);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable.mutate(), ContextCompat.getColor(context, colourId));
        return drawable;
    }

    public static String convertTimeStampTodate(String timeStamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d yyyy");
        String dateString = formatter.format(new Date(Long.parseLong(timeStamp) * 1000));
        return dateString;
    }

    public static void share(Context context, String sharingData) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, sharingData);
        context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.app_name)));
    }


    public static Uri getLocalBitmapUri(Bitmap bmp, final Context context) {
        Uri bmpUri = null;
        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            // bmpUri = Uri.fromFile(file);
            bmpUri = FileProvider.getUriForFile(context, "com.careager.suite.fileprovider", file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


    public static void onCallClick(String contact, Context context) {
        try {
            if (contact != null && contact.length() == 10) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", contact, null));
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getLocalizedMessage();
        }
    }

    public static String splitCamelCase(String s) {
        return s.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])", "(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
    }


    public static String getYouTubeId(String youTubeUrl) {
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youTubeUrl);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "error";
        }
    }

    public static String periodCalculator(Integer integer) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -integer);
        String date = DateFormat.getDateInstance().format(calendar.getTime()) + " To ";
        calendar.add(Calendar.DAY_OF_YEAR, +integer);
        date += DateFormat.getDateInstance().format(calendar.getTime());

        return date;
    }

    public static String getSpecificDate(int year, int month, int day) {

        String minDate = year + "-" + month + "-" + day;

        Date dateFormat = null;

        try {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd").parse(minDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFormat);
        return DateFormat.getDateInstance().format(calendar.getTime());
    }

    public static void whatsAppChat(String phone, Context context) {
        try {
            Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=+91" + phone);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    public static void shareApp(Context context) {
        try {
            final String appPackageName = context.getPackageName();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out the App at: https://play.google.com/store/apps/details?id=" + appPackageName);
            sendIntent.setType("text/plain");
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(sendIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void rateApp(Context context) {
        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            myAppLinkToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Log.d("Error", "" + e.getMessage());
        }
    }

    public static void sendEmail(Context context) {

        String[] TO = {"recentchat@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Recent chat email support");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");
        try {
            emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (ActivityNotFoundException ex) {
            Log.d("Error", "" + ex.getMessage());
        }
    }

    public static String getLocalDate(long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date);
    }

    public static String getTime(long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat("h:mm a");
        return format.format(date);
    }
}


