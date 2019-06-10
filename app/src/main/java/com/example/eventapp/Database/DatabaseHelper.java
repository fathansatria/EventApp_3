package com.example.eventapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.eventapp.Model.PesertaModel;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final static String DATABASENAME = "DbEvent";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_PESERTA = "mahasiswa_table";
    private static final String ID = "id";
    private static final String ID_EVENT = "id_event";
    private static final String NAMA = "nama";
    private static final String EMAIL = "e_mail";
    private static final String TELEPON = "telepon";




    private String TBL_CREATE_MHS = "create table " + TABLE_PESERTA + " (" +
            ID + " int primary key," +
            NAMA + " text," +
            ID_EVENT + " text," +
            EMAIL + " text," +
            TELEPON + " text )";


    public DatabaseHelper(Context context)
    {
        super(context,DATABASENAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TBL_CREATE_MHS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PESERTA);
        onCreate(sqLiteDatabase);
    }

    public long daftarPeserta(PesertaModel peserta){
        SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(NAMA, peserta.getNamaPeserta());
            values.put(EMAIL, peserta.getEmail());
            values.put(TELEPON, peserta.getPhone());
            values.put(ID_EVENT, peserta.getId_event());


        long id = db.insert(TABLE_PESERTA, null, values);

        // assigning tags to tbl create mengambil
        db.close();

        return id ;
    }

    /*
     * Creating tag
     */


    public List<PesertaModel> getAllPeserta() {

        List<PesertaModel> pesertaModels = new ArrayList<PesertaModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_PESERTA;

        Log.e(DATABASENAME, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                PesertaModel std = new PesertaModel();
                std.setNamaPeserta((c.getString(c.getColumnIndex(NAMA))));
                std.setEmail(c.getString(c.getColumnIndex(EMAIL)));
                std.setPhone(c.getString(c.getColumnIndex(TELEPON)));
                std.setId_event(c.getString(c.getColumnIndex(ID_EVENT)));


                // adding to todo list
                pesertaModels.add(std);
            } while (c.moveToNext());
        }

        return pesertaModels;
    }





    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    public List<PesertaModel> getAllPesertaByEventId(String id) {
        List<PesertaModel> pesertas = new ArrayList<PesertaModel>();

        String selectQuery = "SELECT  * FROM " + TABLE_PESERTA + " tm WHERE tm."
                + ID_EVENT + " = '" + id + "'";

        Log.e(DATABASENAME, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                PesertaModel c1 = new PesertaModel();
                c1.setNamaPeserta(c.getString((c.getColumnIndex(NAMA))));
                c1.setPhone(c.getString((c.getColumnIndex(TELEPON))));
                c1.setEmail(c.getString((c.getColumnIndex(EMAIL))));

                // adding to courses
                pesertas.add(c1);

            } while (c.moveToNext());
        }

        return pesertas;
    }


}
