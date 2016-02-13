package kei.balloon.autoringtone;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by kazuki on 2016/02/13.
 */
public class RingtonePreset {

    private String name;
    private Uri uri;
    private boolean isMoving;
    LatLng location;
    int iconIndex;

    public final static float RANGE = 1000;

    public RingtonePreset(String n, Uri u, LatLng l, int iconIndex){
        uri = u;
        location = l;
        name = n;
        this.iconIndex = iconIndex;
    }

    public LatLng getLatLng(){
        return location;
    }

    public String getName(){
        return name;
    }

    public Uri getUri(){
        return uri;
    }

    public int getIconIndex(){
        return iconIndex;
    }
}

