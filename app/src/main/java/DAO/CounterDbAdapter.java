package DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import Models.PersonModel;

/**
 * Created by Patrik Granec on 23.2.2015.
 */
public class CounterDbAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_SCORE = "score";
    public static final String KEY_SPR = "scorePerRound";
    public static final String KEY_COLOR = "color";

    private static final String TAG = "CounterDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "create table player (_id integer primary key autoincrement, "
                    + "name text not null, "
                    + "score integer not null, "
                    + "scorePerRound text not null, "
                    + "color text not null);";

    private static final String DATABASE_NAME = "gameCounter";
    private static final String DATABASE_TABLE = "player";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS player");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public CounterDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws android.database.SQLException if the database could be neither opened or created
     */
    public CounterDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public void deleteData(){
        mDb.execSQL("delete from "+ DATABASE_TABLE);
    }

    public long addPlayer(PersonModel person){
        int id = person.getId();
        String name = person.getName();
        int color = person.getColor();
        ContentValues args = new ContentValues();
        args.put(KEY_ROWID, id);
        args.put(KEY_NAME, name);
        args.put(KEY_SCORE, 0);
        args.put(KEY_SPR, "");
        args.put(KEY_COLOR, color);

        return mDb.insert(DATABASE_TABLE, null, args);
    }

    /**
     * If the score is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     */
    public boolean updatePlayer(PersonModel person) {
        int id = person.getId();
        String spr = person.getScorePerRoundString();
        ContentValues args = new ContentValues();
        args.put(KEY_SPR, spr);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + id, null) > 0;
    }

    /**
     * Delete the score with the given rowId
     *
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteScore(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all notes in the database
     *
     * @return Cursor over all notes
     */
    public Cursor fetchAllPlayers() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME,
                KEY_SCORE, KEY_SPR, KEY_COLOR}, null, null, null, null, null);
    }
}
