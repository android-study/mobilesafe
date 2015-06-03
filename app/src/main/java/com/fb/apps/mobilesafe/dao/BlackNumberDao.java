package com.fb.apps.mobilesafe.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fb.apps.mobilesafe.helper.BlackNumberOpenHelper;
import com.fb.apps.mobilesafe.bean.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FB on 2015/5/25.
 */
public class BlackNumberDao {
    private BlackNumberOpenHelper helper;
    public BlackNumberDao(Context context) {
        helper =  new BlackNumberOpenHelper(context);
    }

    public boolean add(String number ,String mode){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number",number);
        values.put("mode",mode);
        long rowID = db.insert("blacknumber",null,values);
        if(rowID == -1){
            return false;
        }
            return true;
    }

    public boolean delete(String number){
         SQLiteDatabase db = helper.getWritableDatabase();
        long rowID = db.delete("blacknumber","number= ? ",new String[]{number});
        //如果当前rowid为0，表示一行都影响不了
        if(rowID == 0){
            return false;
        }
        return true;
    }

    public boolean changeNumberMode(String number,String newmode){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode",newmode);
        long rowID = db.update("blacknumber",values,"number = ?",new String[]{number});
        if(rowID == 0){
            return false;
        }
        return true;
    }

    /**
     * 查询数据库里面所有的黑名单
     * @return
     */
    public List<BlackNumberInfo> findAll(){
        SQLiteDatabase db = helper.getReadableDatabase();
        List<BlackNumberInfo> lists= new ArrayList<BlackNumberInfo>();
        Cursor cursor = db.query("blacknumber", new String[]{"number", "mode"}, null, null, null, null, null);
        while (cursor.moveToNext()){
            BlackNumberInfo info = new BlackNumberInfo();
            info.setNumber(cursor.getString(0));
            info.setMode(cursor.getString(1));
            lists.add(info);
        }
        cursor.close();;
        db.close();
        return  lists;
    }

    /**
     * 根据手机号查询拦截模式
     * @param number
     * @return
     */
    public String findBlackMode(String number){
        String mode ="0";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("blacknumber",new String[]{"mode"},"number=?",new String[]{number},null,null,null);
        if(cursor.moveToNext()){
            mode = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return mode;
    }

}
