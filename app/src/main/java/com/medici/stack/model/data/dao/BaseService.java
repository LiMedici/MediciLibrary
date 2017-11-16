package com.medici.stack.model.data.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @Desc 处理数据库的增删改查
 * @author 李宗好
 * @time:2017年2月15日上午11:00:14
 */
public class BaseService {

	private SQLiteDatabase db;

	public BaseService(SQLiteOpenHelper dataSourceHelper) {
		db = dataSourceHelper.getWritableDatabase();
	}

	/**
	 * 通过map获取底支传参对象ContentValues
	 * @param params 支持八大基本数据类型+String
	 * @return
	 */
	protected final ContentValues getContentValues(Map<String, Object> params) throws Exception {
		ContentValues values = new ContentValues();
		for (Entry<String, Object> entry : params.entrySet()) {
			values.put(entry.getKey(), entry.getValue().toString());
		}
		return values;

	}

	/**
	 * 保存对象的方法
	 * 
	 * @param tableName
	 * @param params
	 * @return
	 */
	protected final long insert(String tableName, Map<String, Object> params) throws Exception {
		long count = 0;
		try {
			tableName = tableName.toLowerCase();
			if (!DataSourceHelper.tableMap.containsKey(tableName)) {
				String msg = "表【" + tableName + "】不存在!";
				Logger.w(msg);
				throw new Exception(msg);
			}
			db.beginTransaction();// 开启事务
			ContentValues values = getContentValues(params);
			count = db.insert(tableName, null, values);
			db.setTransactionSuccessful();// 设置事务的标志为True
		} catch (Exception e) {
			Logger.e(e,"新增%s对象失败！",tableName);
			throw e;
		} finally {
			db.endTransaction();// 结束事务
		}
		return count;
	}

	/**
	 * 删除带参数的方法
	 * @param tableName
	 * @param whereArgs
	 * @return
	 * @throws Exception
     */
	protected final int delete(String tableName, Map<String, String> whereArgs) throws Exception {
		int count = 0;
		db.beginTransaction();// 开启事务
		try {
			StringBuffer cons = new StringBuffer();
			String[] args = new String[whereArgs.size()];
			int ii = 0;
			for (Entry<String, String> entry : whereArgs.entrySet()) {
				args[ii] = String.valueOf(entry.getValue());
				cons.append(entry.getKey() + " = ?");
				if (ii != args.length - 1) {
					cons.append(" and ");
				}
				ii++;
			}
			count = db.delete(tableName, cons.toString(), args);
			db.setTransactionSuccessful();// 设置事务的标志为True
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			// 结束事务,有两种情况：commit,rollback
			// 事务的提交或回滚是由事务的标志决定的,如果事务的标志为True，事务就会提交，否侧回滚,默认情况下事务的标志为False
			db.endTransaction();
		}
		return count;
	}

	/**
	 * 更新的方法
	 * 
	 * @param tableName
	 * @param params
	 * @return
	 */
	protected final int update(String tableName, Map<String, Object> params, Map<String, String> whereArgs) throws Exception {
		int count = 0;
		try {
			tableName = tableName.toLowerCase();
			if (!DataSourceHelper.tableMap.containsKey(tableName)) {
				String msg = "表【" + tableName + "】不存在!";
				Logger.w(msg);
				throw new Exception(msg);
			}
			db.beginTransaction();// 开启事务
			ContentValues values = getContentValues(params);
			StringBuffer cons = new StringBuffer();
			String[] args = new String[whereArgs.size()];
			int ii = 0;
			for (Entry<String, String> entry : whereArgs.entrySet()) {
				args[ii] = String.valueOf(entry.getValue());
				cons.append(entry.getKey() + " = ?");
				if (ii != args.length - 1) {
					cons.append(" and ");
				}
				ii++;
			}
			count = db.update(tableName, values, cons.toString(), args);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			Logger.e(e,"新增%s对象失败！",tableName);
			throw e;
		} finally {
			db.endTransaction();// 结束事务
		}
		return count;

	}

	/**
	 * 选择查询
	 * 
	 * @param tableName
	 *            查询的表名
	 * @param params
	 *            待拼接的where参数
	 * @return
	 */
	protected final List<Map<String, String>> queryForMap(String tableName, Map<String, String> params) throws Exception {
		StringBuffer cons = null;
		if (null != params) {
			cons = new StringBuffer(" where ");
			int ii = 0;
			int size = params.size();
			for (Entry<String, String> entry : params.entrySet()) {
				String val = entry.getValue();
				val = "'" + val + "'";
				cons.append(entry.getKey() + " = " + val);
				if (ii != size - 1) {
					cons.append(" and ");
				}
				ii++;
			}
		}
		String sql = "select * from " + tableName;
		if (null != cons && !" where ".equals(cons))
			sql += cons.toString();
		return query(sql, null);
	}

	/**
	 * 底层的查询方法
	 * 
	 * @param sql
	 * @param params
	 * 判断条件Value 替换占位符
	 */
	protected final List<Map<String, String>> query(String sql, String[] params) throws Exception {
		Cursor cursor = db.rawQuery(sql, params);
		List<Map<String, String>> list = cursorToList(cursor);
		if (null != cursor) {
			cursor.close();
		}
		return list;
	}
	
	/**
	 * cursor 生成结果的list<Map>
	 * @param cursor
	 * @return
	 */
	protected List<Map<String, String>> cursorToList(Cursor cursor) {
		String[] names = cursor.getColumnNames();
		List<Map<String,String>> list = Lists.newArrayList();
		while (cursor.moveToNext()) {
			Map<String, String> entity = Maps.newHashMap();
			for (String name : names) {
				entity.put(name, cursor.getString(cursor.getColumnIndex(name)));
			}
			list.add(entity);
		}
		return list;
	}

}
