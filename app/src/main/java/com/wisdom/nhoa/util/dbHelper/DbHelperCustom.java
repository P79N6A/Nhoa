package com.wisdom.nhoa.util.dbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wisdom.nhoa.homepage.model.MsgListModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.util.dbHelper
 * @class describe：
 * @time 2018/8/15 18:09
 * @change
 */
public class DbHelperCustom extends SQLiteOpenHelper {
    private Context mContext;
    private SQLiteDatabase db;
    public static final String DB_NAME = "sqdatabase";
    public static final int DB_VERSION = 1;
    public static final String MSGTITLE = "MSGTITLE";
    public static final String MSGDESCRIPTION = "MSGDESCRIPTION";
    public static final String MESSTYPECODE = "MESSTYPECODE";
    public static final String DATAID = "DATAID";
    public static final String ISREAD = "ISREAD";
    public static final String ID = "_id";

    public DbHelperCustom(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DB_VERSION);
    }

    public DbHelperCustom(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 向数据库添加记录
     */
    public static void insertMessage(String title, String content, String customContentString, SQLiteDatabase sqLiteDatabase, Context context) {
        ContentValues cValue = new ContentValues();
        try {
            if (customContentString != null) {
                JSONObject jsonObject = new JSONObject(customContentString);
                if (jsonObject != null) {
                    cValue.put(MESSTYPECODE, jsonObject.getString("messTypeCode"));
                    cValue.put(DATAID, jsonObject.getString("dataId"));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        cValue.put(MSGTITLE, title);
        cValue.put(MSGDESCRIPTION, content);
        cValue.put(ISREAD, "0");
        sqLiteDatabase.insert("MSG" + SharedPreferenceUtil.getUserInfo(context).getUid().replaceAll("-", ""), null, cValue);
        Log.v("tablename", "MSG" + SharedPreferenceUtil.getUserInfo(context).getUid());
    }
    /**
     * 跟新每条消息额状态
     */
    public static void updata(SQLiteDatabase sqLiteDatabase, Context context, String messTypeCode, String dataId) {
        ContentValues cValue = new ContentValues();
        cValue.put(ISREAD, "1");
        sqLiteDatabase.update("MSG" + SharedPreferenceUtil.getUserInfo(context).getUid().replaceAll("-", ""), cValue, MESSTYPECODE + "=?" + " and " + DATAID + "=?", new String[]{messTypeCode, dataId});
    }
    /**
     * 根据id删除某一条消息
     */
    public static void deleteById(SQLiteDatabase sqLiteDatabase, Context context,  String dataId) {
        sqLiteDatabase.delete("MSG" +SharedPreferenceUtil.getUserInfo(context).getUid().replaceAll("-", ""),DATAID+"=?",new String[]{dataId});
    }
    /**
     * 查询记录
     */
    public static List<MsgListModel> queryList(SQLiteDatabase database, Context context) {
        List<MsgListModel> list = new ArrayList<>();
        Cursor cursor = null;
        if (SharedPreferenceUtil.getUserInfo(context) != null) {
            cursor = database.query("MSG" + SharedPreferenceUtil.getUserInfo(context).getUid().replaceAll("-", ""), null, null, null, null, null, ID + " desc");
            Log.v("tablename", "MSG" + SharedPreferenceUtil.getUserInfo(context).getUid());
        }
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                MsgListModel model = new MsgListModel();
                model.setMessTypeCode(cursor.getString(cursor.getColumnIndex(MESSTYPECODE)));
                model.setIsread(cursor.getInt(cursor.getColumnIndex(ISREAD)));
                model.setMsgdiscription(cursor.getString(cursor.getColumnIndex(MSGDESCRIPTION)));
                model.setDataId(cursor.getString(cursor.getColumnIndex(DATAID)));
                model.setMsgtitle(cursor.getString(cursor.getColumnIndex(MSGTITLE)));
                list.add(model);
            }
            cursor.close();
        }
        return list;
    }
    /**
     * 向数据库查询未读消息
     */
    public static List<MsgListModel> queryListWhere(SQLiteDatabase database, Context context) {
        List<MsgListModel> list = new ArrayList<>();
        Cursor cursor = null;
        if (SharedPreferenceUtil.getUserInfo(context) != null) {
            cursor = database.query("MSG" + SharedPreferenceUtil.getUserInfo(context).getUid().replaceAll("-", ""), null, ISREAD + "=?", new String[]{"0"}, null, null, null);
            Log.v("tablename", "MSG" + SharedPreferenceUtil.getUserInfo(context).getUid().replaceAll("-", ""));
        }
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                MsgListModel model = new MsgListModel();
                model.setMessTypeCode(cursor.getString(cursor.getColumnIndex(MESSTYPECODE)));
                model.setIsread(cursor.getInt(cursor.getColumnIndex(ISREAD)));
                model.setMsgdiscription(cursor.getString(cursor.getColumnIndex(MSGDESCRIPTION)));
                model.setDataId(cursor.getString(cursor.getColumnIndex(DATAID)));
                model.setMsgtitle(cursor.getString(cursor.getColumnIndex(MSGTITLE)));
                list.add(model);
            }
            cursor.close();
        }
        return list;
    }

    /**
     * 创建表的操作
     *
     * @param tableName
     */
    public static void creatTable(String tableName, SQLiteDatabase sqdb) {
        String creatTable = "CREATE TABLE " + "IF NOT EXISTS " + "MSG" + tableName + "( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + //  id
                MSGTITLE + " TEXT," + //  msgtitle
                MSGDESCRIPTION + " TEXT," + // msgdiscription
                MESSTYPECODE + " TEXT," + //  messTypeCode
                DATAID + " TEXT," + //  dataId
                ISREAD + " INTEGER NOT NULL )";
        Log.v("execSQL", creatTable);
        if (sqdb != null) {
            sqdb.execSQL(creatTable);
        }
    }
}
