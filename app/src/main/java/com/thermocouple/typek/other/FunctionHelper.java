package com.thermocouple.typek.other;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.thermocouple.typek.R;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class FunctionHelper {
    private AppCompatActivity activity;

    public FunctionHelper() {
    }

    public FunctionHelper(AppCompatActivity activity) {
        this.activity = activity;
    }

    // Fungsi ini digunakan untuk berpindah ke activity lain / page lain
    public void startIntent(Class destination, boolean clearIntent, boolean finish,
                            Map<String, String> paramList) {
        Intent intent = new Intent(activity, destination);
        if (clearIntent)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        else
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (paramList != null) {
            for (Map.Entry<String, String> data : paramList.entrySet()) {
                String key = data.getKey();
                String value = data.getValue();

                intent.putExtra(key, value);
            }
        }

        activity.startActivity(intent);
        if (finish)
            activity.finish();
    }

    // Fungsi ini digunakan untuk berpindah ke activity lain namun saat kembali ke activity sebelumnya
    // memberikan suatu nilai
    public void startIntentForResult(Class destination, Map<String, String> paramList, int code) {
        Intent intent = new Intent(activity, destination);

        if (paramList != null) {
            for (Map.Entry<String, String> data : paramList.entrySet()) {
                String key = data.getKey();
                String value = data.getValue();

                intent.putExtra(key, value);
            }
        }
        activity.startActivityForResult(intent, code);
    }

    // Fungsi ini digunakan untuk menyimpan session sesuai dengan key dan value yang diinginkan
    public void saveSession(String name, String value) {
        PreferenceManager.getDefaultSharedPreferences(activity).edit().putString(name, value).apply();
    }

    // Fungsi ini digunakan untuk mengambil data session sesuai dengan key yang diinginkan
    public String getSession(String key) {
        return PreferenceManager.getDefaultSharedPreferences(activity).getString(key, null);
    }

    // Fungsi ini digunakan untuk membersihkan session / proses logout aplikasi
    public void clearSession() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    // Fungsi ini digunakan untuk mengenkripsi base 64 suatu String
    public String encryptString(String text) {
        byte[] data;
        String base64 = "";
        try {
            data = text.getBytes(StandardCharsets.UTF_8);
            base64 = Base64.encodeToString(data, Base64.NO_WRAP);
        } catch (Exception e) {
        }
        return base64;
    }

    // Fungsi ini digunakan untuk convert format waktu menjadi jam, menit, detik
    public float convertTime(final String input, final String type) {
        String[] parts = input.split(":");
        float tempHasil = 0;

        if (type.equalsIgnoreCase("hour")) {
            float hour = Float.parseFloat(parts[0]);
            float minute = Float.parseFloat(parts[1]) / 60;
            float second = Float.parseFloat(parts[2]) / 3600;

            tempHasil = hour + minute + second;
        } else if (type.equalsIgnoreCase("minute")) {
            float hour = Float.parseFloat(parts[0]) * 60;
            float minute = Float.parseFloat(parts[1]);
            float second = Float.parseFloat(parts[2]) / 60;

            tempHasil = hour + minute + second;
        } else if (type.equalsIgnoreCase("second")) {
            float hour = Float.parseFloat(parts[0]) * 3600;
            float minute = Float.parseFloat(parts[1]) * 60;
            float second = Float.parseFloat(parts[2]);

            tempHasil = hour + minute + second;
        }

        return tempHasil;
    }

    // Fungsi ini digunakan untuk mengubah format tanggal sesuai dengan keinginan
    public String formattingDate(String oldFormat, String format, String time) {
        Date date = null;
        String tempFormat = "dd MMMM yyyy";
        String fromFormat = "yyyy-MM-dd HH:mm:ss";
        String newFormat = "";
        try {
            if (!format.isEmpty())
                tempFormat = format;
            if (!oldFormat.isEmpty())
                fromFormat = oldFormat;

            if (time != null && !time.isEmpty()) {
                date = new SimpleDateFormat(fromFormat).parse(time);
                newFormat = new SimpleDateFormat(tempFormat).format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return newFormat;
    }

    // Fungsi ini digunakan untuk menampilkan atau menutup loading dari SwipeRefreshLayout
    public void showLoadingSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout, boolean isShow) {
        if (isShow) {
            if (!swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(true);
            }
        } else {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    // Fungsi ini digunakan untuk melakukan pengecekan apakah posisi listview berada di atas
    public void setEnabledSwipeRefreshListView(final SwipeRefreshLayout swipeRefreshLayout,
                                               final ListView listView, final String type) {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (listView.getChildAt(0) != null) {
                    boolean isTop = false;
                    if (listView.getChildCount() == 0)
                        isTop = true;
                    else if (type.equalsIgnoreCase("collapseToolbar"))
                        isTop = listView.getChildAt(0).getTop() >= 0 && listView.getChildAt(0).getTop() >= 20;
                    else
                        isTop = listView.getChildAt(0).getTop() >= 0;

                    swipeRefreshLayout.setEnabled(isTop);
                }
            }
        });
    }

    // Fungsi ini digunakan untuk melakukan pengecekan apakah posisi scrollview berada di atas
    public void setEnabledSwipeRefreshScrollView(final SwipeRefreshLayout swipeRefreshLayout,
                                                 final ScrollView scrollView) {
        final ViewTreeObserver.OnScrollChangedListener onScrollChangedListener = new
                ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if (scrollView.getScrollY() == 0) {
                            swipeRefreshLayout.setEnabled(true);
                        } else
                            swipeRefreshLayout.setEnabled(false);
                    }
                };

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            private ViewTreeObserver observer;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (observer == null) {
                    observer = scrollView.getViewTreeObserver();
                    observer.addOnScrollChangedListener(onScrollChangedListener);
                } else if (!observer.isAlive()) {
                    observer.removeOnScrollChangedListener(onScrollChangedListener);
                    observer = scrollView.getViewTreeObserver();
                    observer.addOnScrollChangedListener(onScrollChangedListener);
                }
                return false;
            }
        });
    }

    // Fungsi ini digunakan untuk menampilkan popup biasa yang berisi judul dan deskripsi
    public void popupDialog(String title, String message, final boolean isFinishActivity) {
        try {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (i) {
                        case DialogInterface.BUTTON_NEGATIVE:
                            dialogInterface.dismiss();
                            if (isFinishActivity)
                                activity.finish();
                            return;
                        default:
                            return;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle((CharSequence) title);
            builder.setCancelable(false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setMessage(Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT));
            } else {
                builder.setMessage(Html.fromHtml(message));
            }

            builder.setNegativeButton((CharSequence) "Tutup", dialogClickListener);
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    Fungsi ini digunakan untuk menampilkan popup konfirmasi
    public void popupConfirm(String title, DialogInterface.OnClickListener dialogClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle((CharSequence) "Peringatan ! ");
        builder.setCancelable(false);
        builder.setMessage((CharSequence) title)
                .setPositiveButton((CharSequence) "Ya", dialogClickListener)
                .setNegativeButton((CharSequence) "Tidak", dialogClickListener).show();
    }

    // Fungsi ini digunakan untuk membuat height listview menjadi dinamis
    public void setListViewHeightBasedOnChildren(final ListView listView) {
        listView.post(new Runnable() {
            @Override
            public void run() {
                ListAdapter listAdapter = listView.getAdapter();
                if (listAdapter == null) {
                    return;
                }
                int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
                int listWidth = listView.getMeasuredWidth();
                for (int i = 0; i < listAdapter.getCount(); i++) {
                    View listItem = listAdapter.getView(i, null, listView);
                    listItem.measure(
                            View.MeasureSpec.makeMeasureSpec(listWidth, View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    totalHeight += listItem.getMeasuredHeight();
                    Log.d("listItemHeight" + listItem.getMeasuredHeight(), "___________");
                }
                ViewGroup.LayoutParams params = listView.getLayoutParams();
                params.height = (int) ((totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1))));
                listView.setLayoutParams(params);
                listView.requestLayout();
            }
        });
    }

    // Fungsi ini digunakan untuk setup progress dialog / loading sesuai dengan context
    public void setupProgressDialog(ProgressDialog pDialog, String title) {
        pDialog.setMessage(title);
        pDialog.setCancelable(false);
    }

    // Fungsi ini digunakan untuk menampilkan progress dialog / loading
    public void showProgressDialog(ProgressDialog pDialog, boolean show) {
        if (show) {
            if (!pDialog.isShowing()) {
                pDialog.show();
            }
        } else {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }

    // Fungsi ini digunakan untuk menampilkan pesan berupa toast
    public void showToast(String message, String duration) {
        if (duration.equalsIgnoreCase("short"))
            Toast.makeText(activity.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(activity.getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    // Fungsi ini  digunakan untuk mengecek apakah device terkoneksi dengan internet atau tidak
    public boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) activity.getSystemService(activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    public float convertDpToPixel(float dp) {
        return dp * ((float) activity.getResources().getDisplayMetrics().densityDpi
                / DisplayMetrics.DENSITY_DEFAULT);
    }

    public float convertPixelsToDp(float px) {
        return px / ((float) activity.getResources().getDisplayMetrics().densityDpi
                / DisplayMetrics.DENSITY_DEFAULT);
    }

    // Fungsi ini digunakan untuk membuat height pada grid view menyesuaikan dengan value nya
    public void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if (items > columns) {
            x = items / columns;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
    }


    // Fungsi ini digunakan untuk memnuculkan notifikasi pada status bar device
    public void showNotification(Context context, Intent notificationIntent, Map<String, String> param) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = context.getString(R.string.channelID);
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        int icon = R.drawable.logo;

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.addLine(param.get("message"));
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(icon)
                .setContentTitle(param.get("title"))
                .setContentText(param.get("message"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setTicker(param.get("title"))
                .setGroup("com.android.example.WORK_EMAIL")
                .setStyle(inboxStyle)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), icon));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        notificationIntent.putExtra(key, value);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

        notificationManager.notify(notificationId, mBuilder.build());
    }

    public View inflateView(int layout) {
        View view;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null);
        return view;
    }
}
