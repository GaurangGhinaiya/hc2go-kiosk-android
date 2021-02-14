package com.example.kiosk.Utill;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;


import com.brother.ptouch.sdk.PrinterInfo;
import com.example.kiosk.R;
import com.example.kiosk.common.Common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class utills {

    public static boolean is_back_home = false;

    public static void scaleAndUnscaleAnimation(View view) {
        ScaleAnimation scal_grow = new ScaleAnimation(0.90f, 1.0f, 0.90f, 1.0f, Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
        scal_grow.setDuration(900);
        scal_grow.setFillAfter(true);
        scal_grow.setRepeatCount(ObjectAnimator.INFINITE);
        scal_grow.setRepeatMode(ObjectAnimator.REVERSE);
        view.startAnimation(scal_grow);
    }

    public static Typeface customTypeface_medium(Context ctx) {
        return Typeface.createFromAsset(ctx.getAssets(), "font/RUBIK-LIGHT.TTF");
    }

    public static Dialog startLoader(final Context context) {
        final Dialog d = new Dialog(context);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        d.setContentView(R.layout.progress);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        d.show();
        d.getWindow().setAttributes(lp);

        return d;
    }


    public static File method_save_bitmap(Bitmap bitmap) {

        File APP_DIRECTORY = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), ".KIOSK");
        if (!APP_DIRECTORY.exists()) {
            APP_DIRECTORY.mkdirs();
            APP_DIRECTORY.mkdir();
        }

        File file2 = new File(APP_DIRECTORY, "bitmap_temp" + ".png");
        try {
            if (file2.exists()) {
                file2.delete();
            }
            OutputStream fileOutputStream = new FileOutputStream(file2);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return file2;
        } catch (IOException e2) {
            e2.printStackTrace();
            return file2;
        }

        return file2;
    }


    public static Bitmap getBitmapFromView2(View view, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else {
            canvas.drawColor(Color.WHITE);
        }

        view.draw(canvas);
        return bitmap;
    }


    public static Bitmap getBitmapFromView(View v, int height_, int width_) {
        v.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        Bitmap bitmap = Bitmap.createBitmap(v.getMeasuredWidth(),
                v.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        v.draw(c);


        int newWidth = 1000;
        int newHeight = height_;


        int sourceWidth = bitmap.getWidth();
        int sourceHeight = bitmap.getHeight();
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, bitmap.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(bitmap, null, targetRect, null);

        return dest;
    }



    public static Dialog stopLoader(final Dialog d) {
        if (d != null && d.isShowing()) {
            d.cancel();
            //d.hide();
        }
        return null;
    }

    public static Boolean isOnline(Context context) {
        boolean connected = false;
        final ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            connected = true;
        } else if (netInfo != null && netInfo.isConnected()
                && cm.getActiveNetworkInfo().isAvailable()) {
            connected = true;
        } else if (netInfo != null && netInfo.isConnected()) {
            try {
                URL url = new URL("http://www.google.com");
                HttpURLConnection urlc = (HttpURLConnection) url
                        .openConnection();
                urlc.setConnectTimeout(3000);
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                    connected = true;
                }
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (cm != null) {
            final NetworkInfo[] netInfoAll = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfoAll) {
                System.out.println("get network type :::" + ni.getTypeName());
                if ((ni.getTypeName().equalsIgnoreCase("WIFI") || ni
                        .getTypeName().equalsIgnoreCase("MOBILE"))
                        && ni.isConnected() && ni.isAvailable()) {
                    connected = true;
                    if (connected) {
                        break;
                    }
                }
            }
        }
        return connected;
    }

    public static Interpolator INTERPOLATOR = new OvershootInterpolator();

    public static void animationPopUp(View view) {
        if (view == null) return;

        view.setScaleY(0.9f);
        view.setScaleX(0.9f);

        view.animate()
                .scaleY(1)
                .scaleX(1)
                .setDuration(40)
                .setInterpolator(INTERPOLATOR)
                .start();
    }

    public static final int PERMISSION_REQUEST_CODE = 200;

    public static boolean Permissions_READ_EXTERNAL_STORAGE(Context context) {
        int Permission_0 = ActivityCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE);
        return Permission_0 == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean Permissions_READ_Location(Context context) {
        int Permission_0 = ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION);
        return Permission_0 == PackageManager.PERMISSION_GRANTED;
    }

    public static void Request_READ_EXTERNAL_STORAGE(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]
                        {
                                WRITE_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION
                        },
                PERMISSION_REQUEST_CODE);
    }

    public static void setIntent(Context context, Class c) {
        Intent i = new Intent(context, c);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public static File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".png",
                storageDir
        );
        return image;
    }

    public static String getTimeAccordingTime_Date(String datetime) {

        if (datetime == null) {
            return "";
        }
        if (datetime.equalsIgnoreCase("")) {
            return "";
        }

        String _messageDate = null;
        try {

            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date msgdate = sdf.parse(datetime);


            SimpleDateFormat format = new SimpleDateFormat("MM / dd / yyyy", Locale.ENGLISH);
            _messageDate = format.format(msgdate);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return _messageDate;
    }

    public static void shareapp(Context context) {
        try {
            if (isOnline(context)) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "" + context.getString(R.string.app_name));
                String sAux = "\nCheck out " + context.getString(R.string.app_name) + " APP.\n";
                sAux = sAux + "Get it for free from: " + "https://play.google.com/store/apps/details?id=" + context.getPackageName();
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                context.startActivity(Intent.createChooser(i, "choose one"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getTimeAccordingDate_chat(String datetime) {
        if (datetime == null) {
            return "";
        }
        if (datetime.equalsIgnoreCase("")) {
            return "";
        }

        String _messageDate = null;
        try {

            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date msgdate = sdf.parse(datetime);


            SimpleDateFormat format = new SimpleDateFormat("hh:mm a | MMM-dd", Locale.ENGLISH);
            _messageDate = format.format(msgdate);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return _messageDate;
    }

    public static String getTimeAccordingTime_Date1(String datetime) {

        if (datetime == null) {
            return "";
        }
        if (datetime.equalsIgnoreCase("")) {
            return "";
        }

        String _messageDate = null;
        try {

            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date msgdate = sdf.parse(datetime);


            SimpleDateFormat format = new SimpleDateFormat("MM/dd", Locale.ENGLISH);
            _messageDate = format.format(msgdate);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return _messageDate;
    }

    public static File saveBitmapToFile(File file) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Log.e("saveBitmapToFile", "null true");
            return file;
        } catch (Exception e) {
            Log.e("saveBitmapToFile", "null false");
            return null;
        }
    }


    public static String formatDateToString(String timeZone) {
        Date date = new Date();
        SimpleDateFormat DATEE_sdf = new SimpleDateFormat("MMMM dd,yyyy");

        if (timeZone == null || "".equalsIgnoreCase(timeZone.trim())) {
            timeZone = Calendar.getInstance().getTimeZone().getID();
        }
        DATEE_sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        String datee = DATEE_sdf.format(date);
        return "" + datee;
    }

    public static String formatDateToString_time(String timeZone) {
        Date date = new Date();
        SimpleDateFormat TIMEE_sdf = new SimpleDateFormat("hh:mm a");
        if (timeZone == null || "".equalsIgnoreCase(timeZone.trim())) {
            timeZone = Calendar.getInstance().getTimeZone().getID();
        }
        TIMEE_sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        String timee = TIMEE_sdf.format(date);
        return "" + timee;
    }

    public static String formatDate_currenttime() {
        Date date = new Date();
        SimpleDateFormat DATEE_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DATEE_sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String datee = DATEE_sdf.format(date);
        return "" + datee;
    }


}
