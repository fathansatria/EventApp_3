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

    private static final String TABLE_PESERTA = "peserta_table";
    private static final String ID = "id";
    private static final String NAMA = "nama";
    private static final String EMAIL = "e_mail";
    private static final String TELEPON = "telepon";




    private String TBL_CREATE_MHS = "create table " + TABLE_PESERTA + " (" +
            ID + " int primary key," +
            NAMA + " text," +
            EMAIL + " text," +
            TELEPON + " text )";


    private static final String TABLE_DAFTAR = "daftar_peserta_table";
    private static final String ID_DAFTAR = "id";
    private static final String ID_EVENT = "id_event";
    private static final String KETERANGAN = "keterangan";
    private static final String EMAIL_DAFTAR = "e_mail";
    private static final String TELEPON_DAFTAR = "telepon";

    private String TBL_CREATE_DAFTAR = "create table " + TABLE_DAFTAR + " (" +
            ID_DAFTAR + " int primary key," +
            NAMA + " text," +
            ID_EVENT + " text," +
            KETERANGAN + " text," +
            EMAIL_DAFTAR + " text," +
            TELEPON_DAFTAR + " text )";

    public DatabaseHelper(Context context)
    {
        super(context,DATABASENAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(TBL_CREATE_MHS);
        sqLiteDatabase.execSQL(TBL_CREATE_DAFTAR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PESERTA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DAFTAR);

        onCreate(sqLiteDatabase);
    }

    public long daftarPeserta(PesertaModel peserta){
        SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(NAMA, peserta.getNamaPeserta());
            values.put(EMAIL, peserta.getEmail());
            values.put(TELEPON, peserta.getPhone());

        long id = db.insert(TABLE_PESERTA, null, values);

        // assigning tags to tbl create mengambil
        db.close();

        return id ;
    }


    public long daftarPesertaEvent (PesertaModel peserta){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAMA, peserta.getNamaPeserta());
        values.put(EMAIL, peserta.getEmail());
        values.put(TELEPON, peserta.getPhone());
        values.put(ID_EVENT, peserta.getId_event());
        values.put(KETERANGAN, peserta.getKeterangan());


        long id = db.insert(TABLE_DAFTAR, null, values);

        // assigning tags to tbl create mengambil
        db.close();

        return id ;
    }

    /*
     * Creating tag
     */


    public ArrayList<PesertaModel> getAllPeserta() {

        ArrayList<PesertaModel> pesertaModels = new ArrayList<PesertaModel>();

        PesertaModel p1 = new PesertaModel();
        p1.setNamaPeserta(" Nama ");
        p1.setEmail(" ");
        p1.setPhone(" ");
        p1.setKeterangan(" ");
        pesertaModels.add(p1);

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

                // adding to todo list
                pesertaModels.add(std);
            } while (c.moveToNext());
        }

        return pesertaModels;
    }

    public ArrayList<String> getAllPesertaName() {

        ArrayList<String> pesertaNames = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + TABLE_PESERTA;
        pesertaNames.add("Nama");
        Log.e(DATABASENAME, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                // adding to todo list
                pesertaNames.add(c.getString(c.getColumnIndex(NAMA)));

            } while (c.moveToNext());
        }

        return pesertaNames;
    }




    public void closeDB() {

        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();

    }

    public ArrayList<PesertaModel> getAllPesertaByEventId(String id) {
        ArrayList<PesertaModel> pesertas = new ArrayList<PesertaModel>();

        String selectQuery = "SELECT  * FROM " + TABLE_DAFTAR + " tm WHERE tm."
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
                c1.setKeterangan(c.getString((c.getColumnIndex(KETERANGAN))));
                c1.setId_event(id);

                // adding to courses
                pesertas.add(c1);

            } while (c.moveToNext());
        }

        return pesertas;

    }

    public void deletePeserta( PesertaModel peserta) {

        SQLiteDatabase db = this.getWritableDatabase();

        // delete row
        db.delete(TABLE_PESERTA, NAMA + " = ? AND " + EMAIL + " = ? ",
                new String[] { peserta.getNamaPeserta(), peserta.getPhone() });
    }

    public void deletePesertaDaftar( PesertaModel peserta) {

        SQLiteDatabase db = this.getWritableDatabase();

        // delete row
        db.delete(TABLE_DAFTAR, NAMA + " = ? AND " + EMAIL + " = ? AND " + ID_EVENT + " = ?",
                new String[] { peserta.getNamaPeserta(), peserta.getEmail(), peserta.getId_event() });
    }

    public ArrayList<PesertaModel> getAllPesertaByName(String nama) {

        ArrayList<PesertaModel> pesertas = new ArrayList<PesertaModel>();

        String selectQuery = "SELECT  * FROM " + TABLE_PESERTA + " tm WHERE tm."
                + NAMA + " = '" + nama + "'";

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
                c1.setKeterangan(c.getString((c.getColumnIndex(KETERANGAN))));

                // adding to courses
                pesertas.add(c1);

            } while (c.moveToNext());
        }

        return pesertas;

    }


}
