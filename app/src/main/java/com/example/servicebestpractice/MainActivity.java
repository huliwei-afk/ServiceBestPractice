package com.example.servicebestpractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private DownloadService.DownloadBinder downloadBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadService.DownloadBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startDownload = (Button)findViewById(R.id.start_download);
        Button pauseDownload = (Button)findViewById(R.id.pause_download);
        Button cancelDownload = (Button)findViewById(R.id.cancel_download);
        startDownload.setOnClickListener(this);
        pauseDownload.setOnClickListener(this);
        cancelDownload.setOnClickListener(this);
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        bindService(intent,connection,BIND_AUTO_CREATE);
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.start_download:
                String url = "http://39.134.182.103:6510/shouji.360tpcdn.com/190531/c63afe5408b3e7418b411b091374a905/com.tencent.mobileqq_90026.apk";
                downloadBinder.startDownload(url);
                break;

            case R.id.pause_download:
                downloadBinder.pauseDownload();
                break;

            case R.id.cancel_download:
                downloadBinder.cancelDownload();
                break;

                default:
                    break;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode){
                case 1:
                    if(grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this,"???????????????????????????",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
                    default:
            }
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            unbindService(connection);//?????????????????????????????????
    }
}

