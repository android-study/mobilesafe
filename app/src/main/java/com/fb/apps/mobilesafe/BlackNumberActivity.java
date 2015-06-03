package com.fb.apps.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.fb.apps.mobilesafe.adapter.BlackNumberAdapter;
import com.fb.apps.mobilesafe.bean.BlackNumberInfo;
import com.fb.apps.mobilesafe.dao.BlackNumberDao;

import java.util.List;


public class BlackNumberActivity extends Activity {
    private BlackNumberDao dao;
    private ListView list_view;
    private List<BlackNumberInfo> mBlackNumberInfos;
    private  AlertDialog dialog;
    private BlackNumberAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_number);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题
        initData();
        initView();
    }

    private void initData() {
         dao = new BlackNumberDao(this);
        new Thread(){

            @Override
            public void run() {
                //在andoird系统里面所有的耗时操作必须放到子线程
                //获取到所有的黑名单电话号码
                mBlackNumberInfos = dao.findAll();
                //子线程不能刷新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //运行到主线程
                         adapter = new BlackNumberAdapter(BlackNumberActivity.this,mBlackNumberInfos,dao);
                         list_view.setAdapter(adapter);
                    }
                });
            }
        }.start();
    }

    private void initView() {
                Button bt_add_black_number = (Button) findViewById(R.id.btn_black_id);
                bt_add_black_number.setOnClickListener(addonclick);
                list_view = (ListView) findViewById(R.id.black_list_id);
                Button bt_open_setting = (Button) findViewById(R.id.btn_open_setting);
        bt_open_setting.setOnClickListener(openLister);

    }

    /**
     * 打开设置页面
     */
    private  View.OnClickListener openLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openSetting();
        }
    };

    private void openSetting() {
        Intent intent = new Intent(BlackNumberActivity.this,BlackNumberSettingActivity.class);
        startActivity(intent);
    }

    /**
     * 添加黑名单
     */
    private  View.OnClickListener addonclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(BlackNumberActivity.this);
            View view = View.inflate(BlackNumberActivity.this,R.layout.dialog_add_black_number,null);
            final EditText ed_text = (EditText) view.findViewById(R.id.ed_text);
            Button bt_ok = (Button) view.findViewById(R.id.bt_ok);
            Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

            final CheckBox cb_phone = (CheckBox) view.findViewById(R.id.cb_phone);
            final CheckBox cb_sms = (CheckBox) view.findViewById(R.id.cb_sms);

            //确定
            bt_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BlackNumberInfo info = new BlackNumberInfo();
                    String str_phone = ed_text.getText().toString().trim();
                    if(TextUtils.isEmpty(str_phone)){//判断电话号码是否为空
                        Toast.makeText(BlackNumberActivity.this,"请输入电话号码",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    info.setNumber(str_phone);
                    String mode ="";

                    if(cb_phone.isChecked() && cb_sms.isChecked()){//
                        mode ="1";
                    }else if(cb_phone.isChecked()){
                        mode="2";
                    }else if(cb_sms.isChecked()){
                        mode = "3";
                    }else{
                        Toast.makeText(BlackNumberActivity.this,"必须选择一种拦截模式",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    info.setMode(mode);
                    dao.add(str_phone,mode);//添加到数据库
                    //刷新列表
                    mBlackNumberInfos.add(info);
                    adapter.notifyDataSetChanged();//刷新
                    dialog.dismiss();
                }
            });


            bt_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            builder.setView(view);
             dialog = builder.show();
        }
    };
}
