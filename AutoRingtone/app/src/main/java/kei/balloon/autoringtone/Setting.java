package kei.balloon.autoringtone;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by kei on 2016/02/13.
 */
public class Setting extends AppCompatActivity implements View.OnClickListener{

    // GoogleMapオブジェクトの宣言
    private GoogleMap googleMap;

    //画面上にあるボタンなどのView
    private EditText searchETxt;


    //選択した場所のLatLng
    private LatLng targetLatLng;
    private Address targetAddress;
    private String targetName;
    private Button searchBtn;

    Setting set;

    private InputMethodManager inputMethodManager;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_area);

        set = this;

        searchETxt = (EditText) findViewById(R.id.search_edit_txt);
        searchBtn = (Button) findViewById(R.id.go);
        searchBtn.setOnClickListener(this);

        // MapFragmentオブジェクトを取得
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        //キーボード表示を制御するためのオブジェクト
        inputMethodManager =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        try {
            // GoogleMapオブジェクトの取得
            googleMap = mapFragment.getMap();

            // Activityが初回で生成されたとき
            if (savedInstanceState == null) {

                // MapFragmentのオブジェクトをセット
                mapFragment.setRetainInstance(true);

                // 地図の初期設定を行うメソッドの呼び出し
                mapInit();
            }
        }
        // GoogleMapが使用不可のときのためにtry catchで囲っています。
        catch (Exception e) {
        }

    }

    // 地図の初期設定メソッド
    private void mapInit() {

        // 地図タイプ設定
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // 現在位置ボタンの表示を行なう
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);

        // 東京駅の位置、ズーム設定
        CameraPosition camerapos = new CameraPosition.Builder()
                .target(new LatLng(35.681382, 139.766084)).zoom(15.5f).build();

        // 地図の中心の変更する
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(camerapos));
    }



    // 座標から住所文字列へ変換
    public Address nameToAddress(String localeName, Context context) throws IOException
    {
        // 変換実行
        Geocoder coder = new Geocoder(context, Locale.JAPAN);
        List<Address> list_address = coder.getFromLocationName(localeName,5);
        if (!list_address.isEmpty()){
            // 東京駅の位置、ズーム設定
            CameraPosition camerapos = new CameraPosition.Builder()
                    .target(new LatLng(list_address.get(0).getLatitude(), list_address.get(0).getLongitude())).zoom(15.5f).build();
            // 地図の中心の変更する
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(camerapos));
            // ピンを立てる
            LatLng position = new LatLng(list_address.get(0).getLatitude(), list_address.get(0).getLongitude());
            MarkerOptions options = new MarkerOptions();
            options.position(position);
            options.title(list_address.get(0).getFeatureName());
            googleMap.addMarker(options);
            return list_address.get(0);
        }
        return null;
    }

    @Override
    public void onClick(View v) {
            inputMethodManager.hideSoftInputFromWindow(searchETxt.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
            if (!searchETxt.getText().equals(null))
                try {
                    targetAddress = nameToAddress(searchETxt.getText().toString(), set);
                    if (!targetAddress.equals(null)) {
                        targetName = targetAddress.getFeatureName();
                        targetLatLng = new LatLng(targetAddress.getLatitude(), targetAddress.getLongitude());
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(set);
                        // アラートダイアログのタイトルを設定します
                        alertDialogBuilder.setTitle("検索結果");
                        // アラートダイアログのメッセージを設定します
                        alertDialogBuilder.setMessage(targetName + "でいいですか?");
                        // アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
                        alertDialogBuilder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        
                                    }
                                });
                        // アラートダイアログの否定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
                        alertDialogBuilder.setNegativeButton("CANCEL",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        searchETxt.setText("");
                                    }
                                });
                        // アラートダイアログのキャンセルが可能かどうかを設定します
                        alertDialogBuilder.setCancelable(true);
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        // アラートダイアログを表示します
                        alertDialog.show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
    }
}