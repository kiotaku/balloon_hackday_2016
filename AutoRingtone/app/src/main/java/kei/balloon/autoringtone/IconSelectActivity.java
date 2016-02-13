package kei.balloon.autoringtone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by takumi on 2016/02/13.
 */
public class IconSelectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_select);

        final ListView listView = (ListView) findViewById(R.id.icon_select_list);
        List<List<Map<String,Integer>>> iconIdList;
        final IconSelectItemAdapter iconSelectItemAdapter = new IconSelectItemAdapter(this);

        iconIdList = getIconList();

        iconSelectItemAdapter.setIconIdList(iconIdList);
        listView.setAdapter(iconSelectItemAdapter);
    }

    private List<List<Map<String,Integer>>> getIconList(){
        List<List<Map<String,Integer>>> iconIdList = new ArrayList<>();
        Field[] drawableIDs = R.drawable.class.getFields();

        List<Map<String,Integer>> tmpList = new ArrayList<>();
        for(Field tmp : drawableIDs){
            try {
                String name = tmp.getName();
                if(name.matches("^icon_[[a-z][0-9]_]*$") && !name.matches("^icon_background$")){
                    int value = (Integer) tmp.get(name);
                    Map<String,Integer> tmpMap = new ArrayMap<>();
                    tmpMap.put(name.split("_",2)[1],value);
                    tmpList.add(tmpMap);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if(tmpList.size() == 3){
                iconIdList.add(tmpList);
                tmpList = new ArrayList<>();
            }
        }
        if(tmpList.size() > 0){
            iconIdList.add(tmpList);
        }
        return iconIdList;
    }

    public void canActivityFinish(int iconId){
        Intent intent = new Intent();
        intent.putExtra("iconId",iconId);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}
