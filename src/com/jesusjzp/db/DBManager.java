package com.jesusjzp.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.jeremyfeinstein.slidingmenu.example.R;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class DBManager {

	private final int BUFFER_SIZE = 400000;
	public static final String PACKAGE_NAME = "com.jeremyfeinstein.slidingmenu.example";
	public static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME;

	private static final String DB_NAME = "sanwen.db";
	private static final String TABLE_ARTICLE = "ARTICLE";
	private static final String TABLE_HISTORY = "HISTORY";
	private static final String TABLE_FAVORITE = "FAVORITE";

	private SQLiteDatabase database;
	private Cursor mCursor;
	private Context context;

	public DBManager(Context context) {
		this.context = context;
	}

	public void openDatabase() {
		this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
	}

	private SQLiteDatabase openDatabase(String dbfile) {
		try {
			if (!(new File(dbfile).exists())) {
				// import
				InputStream is = this.context.getResources().openRawResource(
						R.raw.sanwen);
				FileOutputStream fos = new FileOutputStream(dbfile);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,
					null);
			return db;
		} catch (FileNotFoundException e) {
			Log.e("Database", "File not found");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("Database", "IO exception");
			e.printStackTrace();
		}
		return null;
	}

	public void closeDatabase() {
		this.database.close();
	}

	/**
	 * Get articles from database according to the type
	 * 
	 * @param type
	 * @param limit
	 * @return
	 */
	public Cursor fetchArticle(String type, int limit) {
		String sql = "select * from " + TABLE_ARTICLE + " " + "where type = \""
				+ type + "\" " + "order by random() limit " + limit + ";";
		// Log.v("sql", sql);
		mCursor = this.database.rawQuery(sql, null);
		mCursor.moveToFirst(); // move pointer to the head of list
		return mCursor;
	}

	public Cursor fetchHistory(int limit) {
		String sql = "select * from " + TABLE_HISTORY
				+ " order by date desc limit " + limit + ";";
		// Log.v("sql", sql);
		mCursor = this.database.rawQuery(sql, null);
		mCursor.moveToFirst(); // move pointer to the head of list
		return mCursor;
	}

	public Cursor fetchFavorite() {
		String sql = "select * from " + TABLE_FAVORITE + " order by date desc;";
		// Log.v("sql", sql);
		mCursor = this.database.rawQuery(sql, null);
		mCursor.moveToFirst(); // move pointer to the head of list
		return mCursor;
	}

	public Cursor fetchHistoryById(String id) {
		String sql = "select * from " + TABLE_HISTORY + " where _id = " + id
				+ ";";
		// Log.v("sql", sql);
		mCursor = this.database.rawQuery(sql, null);
		mCursor.moveToFirst(); // move pointer to the head of list
		return mCursor;
	}

	public Cursor fetchFavoriteById(String id) {
		String sql = "select * from " + TABLE_FAVORITE + " where _id = " + id
				+ ";";
		// Log.v("sql", sql);
		mCursor = this.database.rawQuery(sql, null);
		mCursor.moveToFirst(); // move pointer to the head of list
		return mCursor;
	}

	public boolean insertHistory(String id, String url, String title,
			String status, String type, String abstr) {
		mCursor = fetchHistoryById(id);
		if (mCursor.getCount() == 0) {
			String sql = "insert into " + TABLE_HISTORY + " values(" + id
					+ ", " + "\"" + url + "\", " + "\"" + title + "\", " + "\""
					+ abstr + "\", " + "\"" + status + "\", " + "\"" + type
					+ "\", \"" + getToday() + "\");";
			Log.v("sql insert history", sql);
			this.database.execSQL(sql);
			return true;
		} else
			return false;
	}

	public boolean insertFavorit(String id, String url,
			String title, String status, String type, String abstr) {
		mCursor = fetchFavoriteById(id);
		if (mCursor.getCount() == 0) {
			String sql = "insert into " + TABLE_FAVORITE + " values(" + id
					+ ", " + "\"" + url + "\", " + "\"" + title + "\", " + "\""
					+ abstr + "\", " + "\"" + status + "\", " + "\"" + type
					+ "\", \"" + getToday() + "\");";
			Log.v("sql insert favorite", sql);
			this.database.execSQL(sql);
			return true;
		} else
			return false;
	}

	public void deleteFavoriteById(String id) {
		String sql = "delete from " + TABLE_FAVORITE + " where _id = " + id
				+ ";";
		// Log.v("sql", sql);
		this.database.execSQL(sql);
	}

	public void clearHistory() {
		String sql = "delete from " + TABLE_HISTORY + ";";
		// Log.v("sql", sql);
		this.database.execSQL(sql);
	}

	public void clearFAVORITE() {
		String sql = "delete from " + TABLE_FAVORITE + ";";
		// Log.v("sql", sql);
		this.database.execSQL(sql);
	}

	/*****************************************************
	 * 
	 *****************************************************/
	public String getToday() {
		Calendar cal = Calendar.getInstance();
		return getFormatDate(cal.getTime(), "yyyy-MM-dd HH:mm:ss");
	}

	public static String getFormatDate(java.util.Date currDate, String format) {
		if (currDate == null) {
			return "";
		}
		SimpleDateFormat dtFormatdB = null;
		try {
			dtFormatdB = new SimpleDateFormat(format);
			return dtFormatdB.format(currDate);
		} catch (Exception e) {
			dtFormatdB = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				return dtFormatdB.format(currDate);
			} catch (Exception ex) {
			}
		}
		return null;
	}

}
