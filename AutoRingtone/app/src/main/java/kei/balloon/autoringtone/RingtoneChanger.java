package kei.balloon.autoringtone;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kazuki on 2016/02/13.
 */
public class RingtoneChanger {

    RingtonePreset activePreset; //現在の着信プリセット
    Uri defaultPresetUri; //デフォルトプリセットのURI
    List<RingtonePreset> presets; //プリセットリスト
    LatLng currentLocation; //現在地
    double speed;
    boolean isMoving;

    boolean isDefault = true;

    MainActivity context; //MainActivity
    ImageView iconView;
    TextView areaNameView, musicTitleView;
    AudioManager am;
    SharedPreferences saveData;

    //こんすとらくた
    public RingtoneChanger(MainActivity ma){
        context = ma;
        saveData = context.getSharedPreferences("Data", context.MODE_WORLD_READABLE | context.MODE_WORLD_WRITEABLE);
        iconView = (ImageView)context.findViewById(R.id.area_icon);
        areaNameView = (TextView)context.findViewById(R.id.area_status);
        musicTitleView = (TextView)context.findViewById(R.id.music_status);
        presets = new ArrayList<>();

        defaultPresetUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        // AudioManager取得
        am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        loadSaveData();
        Log.d("RingtoneChanger", "PresetSize = " + Integer.toString(presets.size()));
    }

    private void loadSaveData(){
        List<RingtonePreset> savedPreset = new ArrayList<>();
        int presetCount = saveData.getInt("count", 0);
        for (int i=0; i<presetCount; i++){
            String uriS = saveData.getString("Uri" + Integer.toString(i), "");
            File f = new File(uriS);
            Uri uri = Uri.fromFile(f);

            String name = saveData.getString("Name" + Integer.toString(i), "");
            int iconIndex = saveData.getInt("IconIndex" + Integer.toString(i), 1);
            double lat = saveData.getFloat("Lat" + Integer.toString(i), 0.0f);
            double lng = saveData.getFloat("Lng" + Integer.toString(i), 0.0f);
            RingtonePreset rp = new RingtonePreset(name, uri, new LatLng(lat, lng), iconIndex);
            savedPreset.add(rp);
        }
        presets = savedPreset;
    }

    //現在地を設定
    public void setCurrentLocation(LatLng l){
        this.setCurrentLocation(l, 0.0);
    }

    public void setCurrentLocation(LatLng l, double speed){
        currentLocation = l;
        this.speed = speed;
        this.setActivePreset();
        if  (isDefault) Log.d("RingtoneChanger", "Ringtone is Default.");
        else Log.d("RingtoneChanger", activePreset.getName() + " is Active.");
    }

    //プリセットを追加
    public void addPreset(RingtonePreset rp){
        presets.add(rp);
        SharedPreferences.Editor e = saveData.edit();
        e.putInt("count", presets.size());
        int itr = 0;
        for (RingtonePreset p : presets){
            e.putString("Uri" + Integer.toString(itr), p.getUri().getPath());
            e.putString("Name" + Integer.toString(itr), p.getName());
            e.putInt("IconIndex" + Integer.toString(itr), p.getIconIndex());
            e.putFloat("Lat" + Integer.toString(itr), (float) p.getLatLng().latitude);
            e.putFloat("Lng" + Integer.toString(itr), (float) p.getLatLng().longitude);
            itr++;
        }
        e.commit();
    }

    //アクティブなプリセットを設定
    public void setActivePreset(){
        double min = 99999;


        if  (1.0 <= speed){
            /*** debug ***/
            am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            isMoving = true;
            /*************/
        } else {
            isMoving = false;
            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            for (RingtonePreset rp : presets) {
                float[] distance = new float[1];
                Location.distanceBetween(rp.getLatLng().latitude, rp.getLatLng().longitude, currentLocation.latitude,
                        currentLocation.longitude, distance);
                if (distance[0] <= RingtonePreset.RANGE && distance[0] < min) {
                    activePreset = rp;
                    min = distance[0];
                }
            }
            if (min <= RingtonePreset.RANGE) setRingtoneOfActivePreset(); //範囲内に設定されたプリセットがあるなら設定
            else setRingtoneOfDefaultPreset(); //なければデフォルトに設定
        }
        updateState();
    }

    //着信音をアクティブなプリセットに設定
    public void setRingtoneOfActivePreset(){
        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, activePreset.getUri());
        isDefault = false;
    }

    //着信音をデフォルトのプリセットに設定
    public void  setRingtoneOfDefaultPreset(){
        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, defaultPresetUri);
        isDefault = true;
    }

    public void setRingtone(Uri u){
        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, u);
        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION, u);
        isDefault = false;
    }

    public RingtonePreset getActivePreset(){
        return activePreset;
    }

    public boolean isDefault(){
        return isDefault;
    }

    public boolean isMoving(){
        return isMoving;
    }

    public void updateState(){
        if  (!isMoving){
            if  (activePreset == null) return;

            switch (activePreset.getIconIndex()){
                case RingtonePreset.SCHOOL:
                    iconView.setImageResource(R.drawable.schoolicon);
                    break;
                case RingtonePreset.WORKSPACE:
                    iconView.setImageResource(R.drawable.workspaceicon);
                    break;
                case RingtonePreset.HOME:
                    iconView.setImageResource(R.drawable.houseicon);
                    break;
            }
            areaNameView.setText(activePreset.getName());
            String path = activePreset.getUri().getPath();
            String[] tmp = path.split("/");
            String musicTitle = tmp[tmp.length-1];
            musicTitleView.setText(musicTitle);
        } else {
            iconView.setImageResource(R.drawable.onmoveicon);
            areaNameView.setText("移動中...");
            musicTitleView.setText("マナーモード中");
        }

    }
}
