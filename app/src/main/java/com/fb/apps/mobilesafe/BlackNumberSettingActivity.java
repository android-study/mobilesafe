package com.fb.apps.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fb.apps.mobilesafe.service.BlackNmberService;
import com.fb.apps.mobilesafe.utils.SystemInfo;

/**
 * 黑名单的设置页面
 * Created by FB on 2015/5/26.
 */
public class BlackNumberSettingActivity extends Activity{
    private RelativeLayout black_number;
    private TextView tv_desc;
    private CheckBox cb_state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_number_setting);
        initUI();
        initDate();
    }

    private void initDate() {
        final Intent black_service = new Intent(this,BlackNmberService.class);
        black_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_state.isChecked()) {
                    cb_state.setChecked(false);
                    tv_desc.setText("已经关闭");
                    stopService(black_service);
                } else {
                    cb_state.setChecked(true);
                    tv_desc.setText("已经开启");
                    startService(black_service);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean result = SystemInfo.isRunningService(this,"com.fb.apps.mobilesafe.service.BlackNmberService");
        if(result){
            cb_state.setChecked(true);
            tv_desc.setText("已经开启");
        }else{
            cb_state.setChecked(false);
            tv_desc.setText("已经关闭");
        }
    }

    private void initUI() {
         black_number = (RelativeLayout) findViewById(R.id.rl_black_number);
         tv_desc = (TextView) findViewById(R.id.tv_desc);
         cb_state= (CheckBox) findViewById(R.id.cb_states);
    }
}
