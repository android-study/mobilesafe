package com.fb.apps.mobilesafe.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by FB on 2015/5/25.
 */
public class BlackNumberOpenHelper extends SQLiteOpenHelper {
    public BlackNumberOpenHelper(Context context) {
        //第一个参数是上下文
        //第二个参数是数据库名字
        //第三个参数表示游标工厂
        //第四个参数表示版本号且必须大于1
        super(context, "callsafe.db", null, 1);
    }

    /**
     * 创建数据库
     * mode 表示拦截模式
     * number表示拦截手机号
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table blacknumber(_id integer primary key autoincrement,number varchar(20),mode carchar(2)) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
