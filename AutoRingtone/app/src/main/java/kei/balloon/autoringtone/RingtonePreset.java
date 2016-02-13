package kei.balloon.autoringtone;

import android.net.Uri;
import com.google.android.gms.maps.model.LatLng;

import java.net.URI;

/**
 * Created by kazuki on 2016/02/13.
 */
public class RingtonePreset {

    private String name;
    private Uri uri;
    private boolean isMoving;
    LatLng location;

    public final static float RANGE = 100;

    public RingtonePreset(String n, Uri u, LatLng l){
        uri = u;
        location = l;
        name = n;
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
}

