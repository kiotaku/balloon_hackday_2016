package kei.balloon.autoringtone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;

public class MainActivity extends AppCompatActivity{
    private static final int REQUEST_FINE_LOCATION = 1;
    private static final int REQUEST_COARSE_LOCATION = 2;
    private static final int REQUEST_INTERNET = 3;

    private ImageView areaIcon; //エリアのアイコンイメージ(メイン画面のやつ )
    private MainActivity ma;    //このアクティビティ

    private Gps gps; //位置情報を取得するクラス

    public static final int WRITE_SETTINGS = 1;
    final static int REQUEST_ADD_PRESET = 2;
    private Uri tmpU;
    private RingtoneChanger rc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)){
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

        ma= this;
        rc = new RingtoneChanger(this);

        checkGpsPermission();

        gps = new Gps(this, rc);
        gps.requestLocation();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ma, AddRingtonePresetActivity.class);
                startActivityForResult(intent, REQUEST_ADD_PRESET);
            }
        });

        // todo: test data
        TextView musicStatus = (TextView) findViewById(R.id.music_status);
        TextView areaStatus = (TextView) findViewById(R.id.area_status);
        musicStatus.setText("test music");
        areaStatus.setText("test area");

        areaIcon = (ImageView) findViewById(R.id.area_icon);
        areaIcon.setImageResource(R.drawable.ic_help);

        /*
            //File f = new File("/storage/emulated/0/Music/HAPPY/HAPPY.mp3");
            File f = new File("/storage/emulated/0/Music/01 海色.mp3");
            Uri u = Uri.fromFile(f);
            rc.setRingtone(u);
        */

        /******* PRESET DEBUG *******
        File f;
        Uri u;
        f = new File("/storage/emulated/0/Music/01 海色.mp3");
        u = Uri.fromFile(f);
        RingtonePreset rp1 = new RingtonePreset("八王子?", u, new LatLng(35.6396765, 139.2967701), RingtonePreset.HOME);

        f = new File("/storage/emulated/0/Music/01 海色.mp3");
        u = Uri.fromFile(f);
        RingtonePreset rp2 = new RingtonePreset("秋葉原UDX", u, new LatLng(35.6994511, 139.7726449), RingtonePreset.WORKSPACE);

        rc.addPreset(rp1);
        rc.addPreset(rp2);
         ****************************/

    }

    private void checkGpsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
            if (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_COARSE_LOCATION);
            if (checkSelfPermission(android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{android.Manifest.permission.INTERNET}, REQUEST_INTERNET);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION:
                return;
            case REQUEST_COARSE_LOCATION:
                return;
            case REQUEST_INTERNET:
                return;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ADD_PRESET && resultCode == RESULT_OK){
            RingtonePreset ringtonePreset = new RingtonePreset(
                    data.getStringExtra("presetName"),
                    Uri.fromFile(new File(data.getStringExtra("filePath"))),
                    new LatLng(data.getDoubleExtra("lat", 0), data.getDoubleExtra("lng", 0)),
                    data.getIntExtra("iconId", R.drawable.ic_help)
            );
            rc.addPreset(ringtonePreset);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}