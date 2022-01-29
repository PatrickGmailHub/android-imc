package co.tiagoaguiar.codelab.fitnesstracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.tiagoaguiar.codelab.myapplication.R;

public class SqlHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "fitness_tracker.db";
    private static final int DB_VERSION = 1;

    private static SqlHelper INSTANCE;

    private SqlHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    static SqlHelper getInstance(Context context) {
        if(INSTANCE == null)
            INSTANCE = new SqlHelper(context);
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE calc (id INTEGER primary key, type_calc TEXT, res DECIMAL, created_date DATETIME)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d("TESTE", "on Upgrade disparado");
    }

    List<Registro> getRegistroList(String type) {
        List<Registro> registros = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM calc WHERE type_calc = ?", new String[]{type});

        try {
            if (cursor.moveToFirst()) {
                do {
                    Registro registro = new Registro();

                    registro.type = cursor.getString(cursor.getColumnIndex("type_calc"));
                    registro.response = cursor.getDouble(cursor.getColumnIndex("res"));
                    registro.createdDate = cursor.getString(cursor.getColumnIndex("created_date"));

                    registros.add(registro);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("SQLite", e.getMessage(), e);
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }

        return registros;
    }

    long addItem(String type, double response) {
        SQLiteDatabase db = getWritableDatabase();

        long calcId = 0;
        try {
            db.beginTransaction();

            ContentValues values = new ContentValues();

            values.put("type_calc", type);
            values.put("res", response);

            SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss", new Locale("pt", "BR"));
            String now = sdf.format(new Date());

            values.put("created_date", now);

            calcId = db.insertOrThrow("calc ", null, values);
            db.setTransactionSuccessful();

        } catch (Exception e) {
            Log.e("SQLite", e.getMessage(), e);
        } finally {
            if(db.isOpen())
                db.endTransaction();
        }
        return calcId;
    }
}
