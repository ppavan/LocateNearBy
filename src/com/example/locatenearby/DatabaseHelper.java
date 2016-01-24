package com.example.locatenearby;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;



import com.example.homework8.R;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	private static final String DATABASE_NAME = "place.db";
	private static final int DATABASE_VERSION = 1;
	private static DatabaseHelper helper = null;
	private static final AtomicInteger usageCounter = new AtomicInteger(0);

	private Dao<Place, String> keysDao = null;
	private RuntimeExceptionDao<Place, String> keysRuntimeDao = null;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION,
				R.raw.ormlite_config);
	}

	@Override
	public void onCreate(SQLiteDatabase database,
			ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, Place.class);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database,
			ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, Place.class, true);
			onCreate(database, connectionSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Dao<Place, String> getDao() throws SQLException {
		if (keysDao == null) {
			keysDao = getDao(Place.class);
		}
		return keysDao;
	}

	

	public RuntimeExceptionDao<Place, String> getKeysRuntimeExceptionDao() {
		if (keysRuntimeDao == null) {
			keysRuntimeDao = getRuntimeExceptionDao(Place.class);
		}
		return keysRuntimeDao;
	}

	

	public static synchronized DatabaseHelper getHelper(Context context) {
		if (helper == null) {
			helper = new DatabaseHelper(context);
		}
		usageCounter.incrementAndGet();
		return helper;
	}

	@Override
	public void close() {
		if (usageCounter.decrementAndGet() == 0) {
			super.close();
			keysRuntimeDao = null;
			helper = null;
		}
	}
}

