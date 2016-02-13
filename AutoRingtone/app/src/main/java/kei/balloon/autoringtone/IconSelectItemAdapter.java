package kei.balloon.autoringtone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by takumi on 2015/12/26.
 */
public class IconSelectItemAdapter extends BaseAdapter {

    IconSelectActivity activity;
    LayoutInflater layoutInflater;
    List<List<Map<String,Integer>>> iconList;
    List<Integer> flags = new ArrayList<>();

    public IconSelectItemAdapter(IconSelectActivity context){
        this.activity = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setIconIdList(List<List<Map<String,Integer>>> iconList) {
        this.iconList = iconList;
        for(int i = 0; i < iconList.size(); i++){
            flags.add(-1);
        }
    }

    public List<Integer> getFlags() {
        return flags;
    }

    @Override
    public int getCount() {
        return iconList.size();
    }

    @Override
    public Object getItem(int position) {
        return iconList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.icon_select_item, parent, false);

        ImageView left = ((ImageView)convertView.findViewById(R.id.icon_select_item_left));
        ImageView center = ((ImageView)convertView.findViewById(R.id.icon_select_item_center));
        ImageView right = ((ImageView)convertView.findViewById(R.id.icon_select_item_right));
        TextView leftName = (TextView)convertView.findViewById(R.id.icon_select_item_left_name);
        TextView centerName = (TextView)convertView.findViewById(R.id.icon_select_item_center_name);
        TextView rightName = (TextView)convertView.findViewById(R.id.icon_select_item_right_name);
        final String leftNameStr;
        final String centerNameStr;
        final String rightNameStr;
        Iterator<String> nameItr;

        int itemValue = iconList.get(position).size();

        if(itemValue > 0) {
            nameItr = iconList.get(position).get(0).keySet().iterator();
            leftNameStr = nameItr.next();
            left.setImageDrawable(activity.getDrawable(iconList.get(position).get(0).get(leftNameStr)));
            left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flags.set(position, 0);
                    activity.canActivityFinish(iconList.get(position).get(0).get(leftNameStr));
                }
            });
            leftName.setText(leftNameStr.replace("_"," "));
        }

        if(itemValue > 1) {
            nameItr = iconList.get(position).get(1).keySet().iterator();
            centerNameStr = nameItr.next();
            center.setImageDrawable(activity.getDrawable(iconList.get(position).get(1).get(centerNameStr)));
            center.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flags.set(position, 1);
                    activity.canActivityFinish(iconList.get(position).get(1).get(centerNameStr));
                }
            });
            centerName.setText(centerNameStr.replace("_"," "));
        }

        if(itemValue > 2) {
            nameItr = iconList.get(position).get(2).keySet().iterator();
            rightNameStr = nameItr.next();
            right.setImageDrawable(activity.getDrawable(iconList.get(position).get(2).get(rightNameStr)));
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flags.set(position, 2);
                    activity.canActivityFinish(iconList.get(position).get(2).get(rightNameStr));
                }
            });
            rightName.setText(rightNameStr.replace("_"," "));
        }
        return convertView;
    }
}
