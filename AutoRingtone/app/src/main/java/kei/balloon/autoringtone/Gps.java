package kei.balloon.autoringtone;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.jar.Manifest;

/**
 * Created by subroh0508 on 16/02/13.
 */
public class Gps implements LocationListener{
	public static final double STAY = 0.0, WALK = 2.0, RUN = 5.0, BICYCLE = 9.0, CAR = 16.0, TRAIN = 28.0;
	private static final String DATE_FORMAT = "yyyy/mm/dd HH:mm:ss";

	private Context context;
	private LocationManager locationManager = null;

	private LatLng latlng;
	private String date;
	private double speed;

	private RingtoneChanger rc;

	private int status;

	public Gps(Context c) {
		this(c, null);
	}

	public Gps(Context c, RingtoneChanger rc){
		this.rc = rc;
		context = c;
		locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		latlng = new LatLng(0.0, 0.0);
		date = null;
		speed = 0.0;

		if(!gpsIsEnabled() || !networkIsEnabled()) {
			Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			context.startActivity(settingsIntent);
		}
	}


	public boolean gpsIsEnabled() {
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	public boolean networkIsEnabled() {
		return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}

	//GPSデータの取得開始
	public boolean requestLocation() {
		if(locationManager != null && gpsIsEnabled()) {
			if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED
					&& ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10, this);
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 10, this);
			}

			return true;
		} else {
			return false;
		}
	}

	//GPSデータの取得終了
	public boolean stopUpdate() {
		 if(locationManager != null) {
			 if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED
					 && ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
				 locationManager.removeUpdates(this);
			 }

			 return true;
		 } else {
			 return false;
		 }
	}

	public LatLng getLatLng() {
		if(latlng != null) return latlng;
		else			   return new LatLng(0.0, 0.0);
	}

	public String getDate() {
		if(date != null) return date;
		else 			 return "Not Available";
	}

	public double getSpeed() {
		return speed;
	}

	//GPSから取得した速度情報で移動手段を判別
	public String getTransportation(){
		if(speed == STAY)
			return "stay";
		else if(speed < WALK)
			return "walk";
		else if(speed < RUN)
			return "run";
		else if(speed < BICYCLE)
			return "bicycle";
		//todo 位置情報使って車と電車を区別する
		/*else if(speed < CAR)
			return "car";*/
		else if(speed < TRAIN)
			return "train";
		else
			return "unknown transportation";
	}

	public int getStatus(){
		return status;
	}

	public String getGpsStatus(){
		return android.provider.Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	}

	@Override
	public void onLocationChanged(Location location) {
		latlng = new LatLng(location.getLatitude(), location.getLongitude());
		date = new SimpleDateFormat(DATE_FORMAT).format(location.getTime());
		speed = location.getSpeed();
		rc.setCurrentLocation(latlng, speed); //RingtoneChangerに現在地を設定
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		switch(status){
			case LocationProvider.AVAILABLE:
				status = 1;
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				status = 0;
				break;
			case LocationProvider.OUT_OF_SERVICE:
				status = -1;
				break;
		}

	}
}
