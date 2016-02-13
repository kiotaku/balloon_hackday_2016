package kei.balloon.autoringtone;

import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kazuki on 2016/02/13.
 */
public class RingtoneChanger {

    RingtonePreset activePreset; //現在の着信プリセット
    Uri defaultPresetUri; //デフォルトのプリセット
    List<RingtonePreset> presets; //プリセットリスト
    LatLng currentLocation; //現在地

    MainActivity context; //MainActivity

    //こんすとらくた
    public RingtoneChanger(MainActivity ma){
        context = ma;
        presets = new ArrayList<>();

        defaultPresetUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
    }

    //現在地を設定
    public void setLocation(LatLng l){
        currentLocation = l;
    }

    //プリセットを追加
    public void addPreset(RingtonePreset rp){
        presets.add(rp);
    }

    //アクティブなプリセットを設定
    public void setActivePreset(){
        double min = 99999;


        for (RingtonePreset rp : presets){
            float[] distance = new float[1];
            Location.distanceBetween(rp.getLatLng().latitude, rp.getLatLng().longitude, currentLocation.latitude,
                    currentLocation.longitude, distance);
            if  (distance[0] < RingtonePreset.RANGE && distance[0] < min){
                activePreset = rp;
                min = distance[0];
            }
        }

        if  (min <= RingtonePreset.RANGE) setRingtoneOfActivePreset();
        else setRingtoneOfDefaultPreset();

    }

    //着信音をアクティブなプリセットに設定
    public void setRingtoneOfActivePreset(){
        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, activePreset.getUri());
    }

    //着信音をデフォルトのプリセットに設定
    public void  setRingtoneOfDefaultPreset(){
        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, defaultPresetUri);
    }

    public void setRingtone(Uri u){
        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, u);
        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION, u);
    }

    public RingtonePreset getActivePreset(){
        return activePreset;
    }

}
