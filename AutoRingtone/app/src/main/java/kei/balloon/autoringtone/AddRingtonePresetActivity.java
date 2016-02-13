package kei.balloon.autoringtone;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

/**
 * Created by takumi on 2016/02/13.
 */
public class AddRingtonePresetActivity extends AppCompatActivity {

    private final static int REQUEST_READ_EXTERNAL_STORAGE = 0;

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

        final Activity activity = this;

        checkReadExternalStoragePermission();

        BootstrapButton location = (BootstrapButton) findViewById(R.id.add_preset_location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Setting.class);
                startActivityForResult(intent, REQUEST_LOCATION);
            }
        });

        BootstrapButton fileSelect = (BootstrapButton) findViewById(R.id.add_preset_sound_file);
        fileSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MusicSelectActivity.class);
                startActivityForResult(intent, REQUEST_MUSIC_PATH);
            }
        });

        ImageButton icon = (ImageButton) findViewById(R.id.add_preset_icon_select);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, IconSelectActivity.class);
                startActivityForResult(intent, REQUEST_ICON_ID);
            }
        });

        BootstrapButton execute = (BootstrapButton) findViewById(R.id.add_preset_execute);
        execute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText) findViewById(R.id.add_preset_preset_name)).getText().toString();
                if (!name.isEmpty() && !musicPath.isEmpty() && lat != 0 && lng != 0 && iconId != R.drawable.ic_help) {
                    Intent intent = new Intent();
                    intent.putExtra("presetName", name);
                    intent.putExtra("filePath", musicPath);
                    intent.putExtra("lat", lat);
                    intent.putExtra("lng", lng);
                    intent.putExtra("iconId", iconId);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    new AlertDialog.Builder(activity)
                            .setMessage("入力されていないアイテムがあります")
                            .setPositiveButton("OK", null)
                            .show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {

            if (requestCode == REQUEST_LOCATION) {
                lat = data.getDoubleExtra("lat", 0);
                lng = data.getDoubleExtra("lng", 0);
                ((TextView) findViewById(R.id.add_preset_location_name)).setText(data.getStringExtra("locationName"));
            } else if (requestCode == REQUEST_ICON_ID) {
                ImageButton icon = (ImageButton) findViewById(R.id.add_preset_icon_select);
                iconId = data.getIntExtra("iconId", R.drawable.ic_help);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), iconId);
                icon.setImageBitmap(Bitmap.createScaledBitmap(
                        bitmap,
                        getResources().getDimensionPixelSize(R.dimen.add_ringtone_preset_image_button_dp),
                        getResources().getDimensionPixelSize(R.dimen.add_ringtone_preset_image_button_dp),
                        false));
                iconId = getPresetIconID(iconId);
            } else if(requestCode == REQUEST_MUSIC_PATH) {
                musicPath = data.getStringExtra("path");
                String[] tmp = musicPath.split("/");
                String musicTitle = tmp[tmp.length-1];
                ((TextView) findViewById(R.id.add_preset_sound_file_name)).setText(musicTitle);
            }
            
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkReadExternalStoragePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private int getPresetIconID(int iconId){
        if(iconId == R.drawable.schoolicon) return RingtonePreset.SCHOOL;
        if(iconId == R.drawable.houseicon) return RingtonePreset.HOME;
        if(iconId == R.drawable.workspaceicon) return RingtonePreset.WORKSPACE;
        if(iconId == R.drawable.trainicon) return RingtonePreset.TRAIN;
        return 0;
    }
}
