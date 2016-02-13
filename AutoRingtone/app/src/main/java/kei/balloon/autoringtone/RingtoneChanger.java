package kei.balloon.autoringtone;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by kazuki on 2016/02/13.
 */
public class RingtoneChanger {

    RingtonePreset activePreset;
    LatLng currentLocation;

    public RingtoneChanger(){

    }

    public void setActiveRingtone(){
        //現在地と各Presetの距離比較

        //最小距離をセット
        //範囲内なければ，デフォルト
    }

    public void setLocation(LatLng l){
        currentLocation = l;
    }

}
