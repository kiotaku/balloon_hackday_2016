package kei.balloon.autoringtone;

import java.net.URI;

/**
 * Created by kazuki on 2016/02/13.
 */
public class RingtonePreset {

    private String name;
    private URI uri;
    private boolean isMoving;
    LatLng location;

    public final static double RANGE = 100;

    public RingtonePreset(String n, URI u, LatLng l){
        uri = u;
        location = l;
        name = n;
    }

    public String getName(){
        return name;
    }

}

