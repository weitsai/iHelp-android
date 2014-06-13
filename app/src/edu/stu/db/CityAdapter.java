package edu.stu.db;

import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CityAdapter {
    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;

    public CityAdapter(Context context) {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    public CityAdapter createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public CityAdapter open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>" + mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public Cursor getTestData() {
        try {
            String sql = "SELECT * FROM city";

            Cursor mCur = mDb.rawQuery(sql, null);
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getCityCoordinates(int city_id, int city_group) {
        try {
            String sql = "SELECT * FROM city_bound where city_id = " + city_id
                    + " AND city_group = " + city_group;
            Cursor mCur = mDb.rawQuery(sql, null);
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public String getCityName(int city_id) {
        try {
            if (city_id < 0) {
                return "找不到縣市";
            }
            String sql = "SELECT name FROM city where id = " + city_id;
            Cursor mCur = mDb.rawQuery(sql, null);
            mCur.moveToNext();
            return mCur.getString(mCur.getColumnIndex("name"));
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public String getCityPhoneNumber(int city_id) {
        try {
            if (city_id < 0) {
                return "0988281110";
            }
            String sql = "SELECT phone FROM city where id = " + city_id;
            Cursor mCur = mDb.rawQuery(sql, null);
            mCur.moveToNext();
            return mCur.getString(mCur.getColumnIndex("phone"));
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }
    
    public String getCityPhoneNumber119(int city_id) {
        try {
            if (city_id < 0) {
                return "0988281110";
            }
            String sql = "SELECT phone_119 FROM city where id = " + city_id;
            Cursor mCur = mDb.rawQuery(sql, null);
            mCur.moveToNext();
            return mCur.getString(mCur.getColumnIndex("phone_119"));
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }
    
    public String getCityPhoneNumberByName(String city) {
        try {
            String sql = "SELECT phone FROM city where name = " + "'" + city + "'";
            Cursor mCur = mDb.rawQuery(sql, null);
            mCur.moveToNext();
            return mCur.getString(mCur.getColumnIndex("phone"));
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }
    
    public String getCityPhoneNumberByName119(String city) {
        try {
            String sql = "SELECT phone_119 FROM city where name = " + "'" + city + "'";
            Cursor mCur = mDb.rawQuery(sql, null);
            mCur.moveToNext();
            return mCur.getString(mCur.getColumnIndex("phone_119"));
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }
}
