package kei.balloon.autoringtone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by takumi on 2016/02/13.
 */
public class AddRingtonePresetActivity extends AppCompatActivity {

    final static int REQUEST_LOCATION = 1;
    final static int REQUEST_ICON_ID = 2;
    final static int REQUEST_FILE_PATH = 3;

    private double lat = 0;
    private double lng = 0;
    private int iconId = R.drawable.ic_help;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ringtone_preset);

        final Activity activity = this;

        Button location = (Button) findViewById(R.id.add_preset_location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Setting.class);
                startActivityForResult(intent, REQUEST_LOCATION);
            }
        });

        Button fileSelect = (Button) findViewById(R.id.add_preset_sound_file);
        fileSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo: FileSelectActivityを作る
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

        Button execute = (Button) findViewById(R.id.add_preset_execute);
        execute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText) findViewById(R.id.add_preset_preset_name)).getText().toString();
                String filePath = ((TextView) findViewById(R.id.add_preset_sound_file_name)).getText().toString();
                if (!name.isEmpty() && !filePath.isEmpty() && lat == 0 && lng == 0 && iconId == R.drawable.ic_help) {
                    Intent intent = new Intent();
                    intent.putExtra("presetName", name);
                    intent.putExtra("filePath", filePath);
                    intent.putExtra("lat", lat);
                    intent.putExtra("lng", lng);
                    intent.putExtra("iconId", iconId);
                    setResult(RESULT_OK, intent);
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
            } else if (requestCode == REQUEST_FILE_PATH){
                // todo: 返ってくるデータが決まったら考える
            } else if (requestCode == REQUEST_ICON_ID) {
                ImageButton icon = (ImageButton) findViewById(R.id.add_preset_icon_select);
                iconId = data.getIntExtra("iconId", R.drawable.ic_help);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), iconId);
                icon.setImageBitmap(Bitmap.createScaledBitmap(
                        bitmap,
                        getResources().getDimensionPixelSize(R.dimen.add_ringtone_preset_image_button_dp),
                        getResources().getDimensionPixelSize(R.dimen.add_ringtone_preset_image_button_dp),
                        false));
            }
            
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
