package com.medici.stack.model.data.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.medici.stack.util.LogUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @desc DbHelper
 * @author 李宗好
 * @time:2016年12月20日 下午4:32:11
 */
public class DataSourceHelper extends SQLiteOpenHelper {

	private static final String LOGTAG = LogUtil.makeLogTag(DataSourceHelper.class);

	/**
    * 表格集合
    */
	public static Map<String, String> tableMap;
	
	/**
	 * 创建表
	 */
    private void createTables(SQLiteDatabase db){
    	 for (Entry<String,String> entry : tableMap.entrySet()) {
    		 String tableName = entry.getKey();
    	 		try {
					db.execSQL("create table " +tableName + " "+entry.getValue());
				} catch (SQLException e) {
					Log.e(LOGTAG, "创建表【"+tableName+"】失败！");
					e.printStackTrace();
				}
		 }
     }

    /**
     * @param context
     * @param name
     * @param factory
     * @param version
     */
	public DataSourceHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
		tableMap = new HashMap<>();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTables(db);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {  
        // TODO Auto-generated method stub  
        super.onOpen(db);  
        if (!db.isReadOnly()) {  
            // Enable foreign key constraints 开启外键约束  
            db.execSQL("PRAGMA foreign_keys=ON;");  
        }  
    }  
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

}
