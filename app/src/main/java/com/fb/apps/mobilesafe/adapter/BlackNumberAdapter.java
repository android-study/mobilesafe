package com.fb.apps.mobilesafe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fb.apps.mobilesafe.R;
import com.fb.apps.mobilesafe.bean.BlackNumberInfo;
import com.fb.apps.mobilesafe.dao.BlackNumberDao;

import java.util.List;

/**
 * Created by FB on 2015/5/25.
 */
public class BlackNumberAdapter extends BaseAdapter{
    private List<BlackNumberInfo> mBlackNumberInfos;
    private BlackNumberDao dao;
    private Context context;
    public BlackNumberAdapter(Context context,List<BlackNumberInfo> mBlackNumberInfos,BlackNumberDao dao) {
        this.mBlackNumberInfos = mBlackNumberInfos;
        this.context =context;
        this.dao = dao;
    }

    @Override
    public int getCount() {
        return mBlackNumberInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mBlackNumberInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_black_number,null);
        }
        TextView tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
        String mode = mBlackNumberInfos.get(position).getMode();
        TextView tv_mode = (TextView) convertView.findViewById(R.id.tv_mode);
        tv_phone.setText(mBlackNumberInfos.get(position).getNumber());
        ImageButton img_delete = (ImageButton) convertView.findViewById(R.id.image_delete);

        if(mode.equals("1")){
            tv_mode.setText("全部拦截");
        }else if(mode.equals("2")){
            tv_mode.setText("电话拦截");
        }else if(mode.equals("3'")){
            tv_mode.setText("短信拦截");

        }
        final BlackNumberInfo  blackNumberInfo = mBlackNumberInfos.get(position);
        //删除
        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = dao.delete(blackNumberInfo.getNumber());//产出电话号码
                if(result){
                    Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                    mBlackNumberInfos.remove(blackNumberInfo);
                    notifyDataSetChanged();//刷新页面
                }else{
                    Toast.makeText(context,"删除失败",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }
}
