package com.fb.apps.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.fb.apps.mobilesafe.dao.BlackNumberDao;



public class BlackNmberService extends Service {
    private BlackNumberDao dao;
    private TelephonyManager tm;
    private InnerSmsreceiver receiver ;
    private MyPhoneLister lister;
    public BlackNmberService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Called by the system to notify a Service that it is no longer used and is being removed.  The
     * service should clean up any resources it holds (threads, registered
     * receivers, etc) at this point.  Upon return, there will be no more calls
     * in to this Service object and it is effectively dead.  Do not call this method directly.
     */
    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        receiver = null;
        tm.listen(lister,PhoneStateListener.LISTEN_NONE);
        lister = null;
        super.onDestroy();
    }

    /**
     * Called by the system when the service is first created.  Do not call this method directly.
     */
    @Override
    public void onCreate() {
        super.onCreate();
         dao= new BlackNumberDao(this);
        //获取电话的管理者
         tm= (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
         lister = new MyPhoneLister();
        tm.listen(lister,PhoneStateListener.LISTEN_CALL_STATE);//第二个参数表示电话拨打的状态
         receiver = new InnerSmsreceiver();
        //短信过滤器
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(Integer.MAX_VALUE);
        //注册一个短信广播
        registerReceiver(receiver, filter);
    }

    class InnerSmsreceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("接受到短信");
            Object[] obj = (Object[]) intent.getExtras().get("pdus");
            for (Object object:obj) {
                SmsMessage smsMessage =SmsMessage.createFromPdu((byte[]) object);
                //获取到手机号码
                String number = smsMessage.getOriginatingAddress();
                //根据手机号查询是否在拦截列表内
                String mode = dao.findBlackMode(number);
                if(mode.equals("1")||mode.equals("3")){//短信拦截
                    System.out.println("短信被拦截到了");
                    abortBroadcast();
                }
                //智能拦截
                String body = smsMessage.getMessageBody();
                if(body.contains("yellow")){
                    System.out.println("被哥智能拦截了");
                    abortBroadcast();
                }
            }
        }
    }

    /**
     * 自定义监听器 继承android的电话监听器
     */
    private class MyPhoneLister extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            //第一个表示电话状态，第二个表示电话号码
            super.onCallStateChanged(state, incomingNumber);
            switch (state){
                //闲置状态
                case TelephonyManager.CALL_STATE_IDLE:
                     break ;
                //接通电话状态
                case TelephonyManager.CALL_STATE_OFFHOOK:
                     break ;
                //电话响铃状态
                case TelephonyManager.CALL_STATE_RINGING:
                    String mode = dao.findBlackMode(incomingNumber);
                    if(mode.equals("2")||mode.equals("3")){//手机号拦截
                        System.out.println("电话被拦截到了");
                        endCall();
                    }
                     break ;
                default:
                    break;
            }
        }
    }

    private void endCall() {
            System.out.println("挂断电话");
    }
}
