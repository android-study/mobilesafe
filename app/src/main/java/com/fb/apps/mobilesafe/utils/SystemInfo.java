package com.fb.apps.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;
import java.util.List;
import static android.app.ActivityManager.RunningServiceInfo;

/**
 * 判断当前的黑名单服务是否已经运行
 * Created by FB on 2015/5/26.
 */
public class SystemInfo {
    public static boolean isRunningService(Context context,String systemName){
        // 获取到系统服务
      ActivityManager am= (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> runningServices =  am.getRunningServices(50);//获取手机进程个数
        for (RunningServiceInfo runinfo:runningServices) {
            //获取到服务的名字
            String className = runinfo.service.getClassName();
            if(systemName.equals(className)){//找到了想等的服务及服务已经启动
                return true;
            }
        }
        return false;
    }
}
