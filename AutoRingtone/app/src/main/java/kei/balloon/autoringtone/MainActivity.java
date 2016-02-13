package kei.balloon.autoringtone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    private ImageView areaIcon; //エリアのアイコンイメージ(メイン画面のやつ )

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Context context = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, IconSelectActivity.class);
                startActivity(intent);
            }
        });

        // todo: test data
        TextView musicStatus = (TextView) findViewById(R.id.music_status);
        TextView areaStatus = (TextView) findViewById(R.id.area_status);
        musicStatus.setText("test music");
        areaStatus.setText("test area");

        areaIcon = (ImageView) findViewById(R.id.area_icon);
        areaIcon.setImageResource(R.drawable.houseicon);

    }


}
