package kei.balloon.autoringtone;

import android.content.Context;
import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    private ImageView areaIcon; //エリアのアイコンイメージ(メイン画面のやつ )
    private MainActivity ma;    //このアクティビティ

    public static final int WRITE_SETTINGS = 1;
    private Uri tmpU;
    private RingtoneChanger rc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ma= this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ma, IconSelectActivity.class);
                //startActivity(intent);
                //Intent intent = new Intent(ma, Setting.class);
                startActivity(intent);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // todo: test data
        TextView musicStatus = (TextView) findViewById(R.id.music_status);
        TextView areaStatus = (TextView) findViewById(R.id.area_status);
        musicStatus.setText("test music");
        areaStatus.setText("test area");
        
        areaIcon = (ImageView) findViewById(R.id.area_icon);
        areaIcon.setImageResource(R.drawable.houseicon);

        rc = new RingtoneChanger(this);
        File f = new File("/storage/emulated/0/Music/HAPPY/HAPPY.mp3");
        Uri u = Uri.fromFile(f);
        rc.setRingtone(u);

    }

}
