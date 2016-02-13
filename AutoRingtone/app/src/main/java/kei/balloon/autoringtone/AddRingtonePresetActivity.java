package kei.balloon.autoringtone;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by takumi on 2016/02/13.
 */
public class AddRingtonePresetActivity extends AppCompatActivity {

    final static int REQUEST_LOCATION = 1;
    final static int REQUEST_ICON_ID = 2;
    final static int REQUEST_MUSIC_PATH = 3;

    private double lat = 0;
    private double lng = 0;
    private int iconId = R.drawable.ic_help;
    private String musicPath;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ringtone_preset);

        final Context context = this;

        Button location = (Button) findViewById(R.id.add_preset_location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Setting.class);
                startActivityForResult(intent, REQUEST_LOCATION);
            }
        });

        Button fileSelect = (Button) findViewById(R.id.add_preset_sound_file);
        fileSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MusicSelectActivity.class);
                startActivityForResult(intent, REQUEST_MUSIC_PATH);
            }
        });

        ImageButton icon = (ImageButton) findViewById(R.id.add_preset_icon_select);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, IconSelectActivity.class);
                startActivityForResult(intent, REQUEST_ICON_ID);
            }
        });

        Button execute = (Button) findViewById(R.id.add_preset_execute);
        execute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText) findViewById(R.id.add_preset_preset_name)).getText().toString();
                String filePath = ((TextView) findViewById(R.id.add_preset_sound_file_name)).getText().toString();
                Intent intent = new Intent();
                intent.putExtra("presetName", name);
                intent.putExtra("filePath", filePath);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("iconId", iconId);
                setResult(RESULT_OK, intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if (requestCode == REQUEST_LOCATION) {
                // todo: 返ってくるデータが決まったら考える
            } else if (requestCode == REQUEST_ICON_ID) {
                ImageButton icon = (ImageButton) findViewById(R.id.add_preset_icon_select);
                iconId = data.getIntExtra("iconId", R.drawable.ic_help);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    icon.setImageDrawable(
                            getResources().getDrawable(iconId, null));
                } else {
                    icon.setImageDrawable(
                            getResources().getDrawable(iconId));

                }
            } else if(requestCode == REQUEST_MUSIC_PATH) {
                musicPath = data.getStringExtra("path");
                Toast.makeText(this, "path:"+musicPath, Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
