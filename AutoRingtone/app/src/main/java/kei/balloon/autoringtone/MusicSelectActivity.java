package kei.balloon.autoringtone;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.menu.ListMenuItemView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by subroh0508 on 16/02/13.
 */
public class MusicSelectActivity extends Activity {
	private final static int REQUEST_READ_EXTERNAL_STORAGE = 1;
	ArrayAdapter<String> adapter;
	List<String> pathList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_select);

		ListView listView = (ListView)findViewById(R.id.music_select_list);
		adapter = new ArrayAdapter<String>(this, R.layout.music_select_item);

		setMusicList();

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("path",pathList.get(position));
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
	}

	private void setMusicList(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
				requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
		}

		ContentResolver resolver = this.getContentResolver();
		Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
		cursor.moveToFirst();
		do {
			adapter.add(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
			pathList.add(cursor.getString(cursor.getColumnIndex("_data")));
		} while(cursor.moveToNext());
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case REQUEST_READ_EXTERNAL_STORAGE:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

				} else {

					// permission denied, boo! Disable the
					// functionality that depends on this permission.
				}
				return;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
}
