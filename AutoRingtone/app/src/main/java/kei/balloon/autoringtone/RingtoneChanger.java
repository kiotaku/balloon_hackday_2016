package kei.balloon.autoringtone;

import android.content.Context;
import android.location.Location;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
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
    AudioManager am;

    //こんすとらくた
    public RingtoneChanger(MainActivity ma){
        context = ma;
        presets = new ArrayList<>();

        defaultPresetUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        // AudioManager取得
        am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
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
    }

    //アクティブなプリセットを設定
    public void setActivePreset(){
        double min = 99999;


        if  (Gps.TRAIN <= speed){
            /*** debug ***/
            am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            isMoving = true;
            return;
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
}
