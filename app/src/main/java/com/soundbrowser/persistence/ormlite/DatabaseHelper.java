package com.soundbrowser.persistence.ormlite;

import java.io.File;
import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.soundbrowser.R;
import com.soundbrowser.persistence.model.Item;
import com.soundbrowser.persistence.model.Timming;
import com.soundbrowser.persistence.model.Track;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	// name of the database file for your application -- change to something appropriate for your app
	private static final String DATABASE_NAME = "soundbrowser.db";
	// any time you make changes to your database objects, you may have to increase the database version
	private static final int DATABASE_VERSION = 1;

	// the DAO object we use to access the SimpleData table
	//private Dao<SimpleData, Integer> simpleDao = null;
    private Dao<Item, Integer> daoItem;
    private Dao<Track, Integer> daoTrack;
	//private RuntimeExceptionDao<SimpleData, Integer> simpleRuntimeDao = null;
    private RuntimeExceptionDao<Item, Integer> itemRuntimeDao = null;

	public DatabaseHelper(Context context) {
//		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
        super(
            context,
            Environment.getExternalStoragePublicDirectory(".").		// /storage/sdcard0/.
                getAbsolutePath() + File.separator + DATABASE_NAME,
            null,
            DATABASE_VERSION
            //, R.raw.ormlite_config
        );
    }

	/**
	 * This is called when the database is first created. Usually you should call createTable statements here to create
	 * the tables that will store your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			//TableUtils.createTableIfNotExists(connectionSource, SimpleData.class);
            TableUtils.createTableIfNotExists(connectionSource, Item.class);
            TableUtils.createTableIfNotExists(connectionSource, Track.class);
            TableUtils.createTableIfNotExists(connectionSource, Timming.class);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}

        if(true)
            return;

		// here we try inserting data in the on-create as a test
		//RuntimeExceptionDao<SimpleData, Integer> dao = getSimpleDataDao();
		long millis = System.currentTimeMillis();
		// create some entries in the onCreate
		SimpleData simple = new SimpleData(millis);
		//dao.create(simple);
		simple = new SimpleData(millis + 1);
		//dao.create(simple);
		Log.i(DatabaseHelper.class.getName(), "created new entries in onCreate: " + millis);
	}

	/**
	 * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
	 * the various data to match the new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
//		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			//TableUtils.dropTable(connectionSource, SimpleData.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
/*		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
		*/
	}

	/**
	 * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
	 * value.
	 */
/*	public Dao<SimpleData, Integer> getDao() throws SQLException {
		if (simpleDao == null) {
			simpleDao = getDao(SimpleData.class);
		}
		return simpleDao;
	}*/
    public Dao<Item, Integer> getItemDao() throws SQLException {
        if (daoItem == null) {
            daoItem = getDao(Item.class);
        }
        return daoItem;
    }

    public Dao<Track, Integer> getTrackDao() 
      throws SQLException {
        if (daoTrack == null) 
        	daoTrack = getDao(Track.class);
        return daoTrack;
    }

	/**
	 * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our SimpleData class. It will
	 * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
	 */
/*	public RuntimeExceptionDao<SimpleData, Integer> getSimpleDataDao() {
		if (simpleRuntimeDao == null) {
			simpleRuntimeDao = getRuntimeExceptionDao(SimpleData.class);
		}
		return simpleRuntimeDao;
	}*/
    public RuntimeExceptionDao<Item, Integer> getRuntimeItemDao() {
        if (itemRuntimeDao == null) {
            itemRuntimeDao = getRuntimeExceptionDao(Item.class);
        }
        return itemRuntimeDao;
    }

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		daoItem = null;
		itemRuntimeDao = null;
	}
/*
    @Override
    public synchronized SQLiteDatabase getWritableDatabase (){
        return SQLiteDatabase.openDatabase("/sdcard/helloAndroid.db", null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase (){
        return SQLiteDatabase.openDatabase("/sdcard/helloAndroid.db", null, SQLiteDatabase.OPEN_READONLY);
    }
*/
}
