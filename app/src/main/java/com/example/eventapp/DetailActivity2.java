package com.example.eventapp;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eventapp.Adapter.PesertaRecyclerAdapter;
//import com.example.eventapp.Adapter.SpinnerAdapter;
import com.example.eventapp.Database.DatabaseHelper;
import com.example.eventapp.Model.Item;
import com.example.eventapp.Model.PesertaModel;
import com.example.eventapp.Model.Utilities;
import com.example.eventapp.Model.apiInterface;
import com.example.eventapp.Service.apiService;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.eventapp.Model.Utilities.isEmailValid;


public class DetailActivity2 extends AppCompatActivity {

    private TextView tv_eventTitle,tv_content,tv_author;
    private ImageView iv_eventImage;
    private RecyclerView recyclerView;
    private Button btn_harga;
    private DatabaseHelper db;
    private static PesertaRecyclerAdapter pesertaRecyclerAdapter;
    apiInterface apiI;
    apiService apiInit;
    private String event_id;
    private ArrayList<PesertaModel> eventPesertaModels;
    private EditText et_nama, et_telp, et_email, et_keterangan;
    private Spinner spinner;
    private RelativeLayout formPesertaBaru;
    private String newName, newTelp, newEmail, newKeterangan;
    private LinearLayout emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);

        //tv_eventTitle = findViewById(R.id.tv_eventTitle);
        tv_content = findViewById(R.id.tv_content2);
        tv_author = findViewById(R.id.author2);
        iv_eventImage = findViewById(R.id.iv_event_image);
        btn_harga = findViewById(R.id.btn_harga);
        recyclerView = findViewById(R.id.peserta_recycler_view2);
        emptyView = findViewById(R.id.empty_peserta1);
        Button btnTambahPeserta = findViewById(R.id.btn_tambah_peserta2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }

        });


        apiInit = new apiService();
        apiI = apiService.getClient().create(apiInterface.class);
        db = new DatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        if(extras != null){

            event_id = extras.getString("eventId");

        }
        else{
            event_id = "-9999";
        }


        eventPesertaModels = db.getAllPesertaByEventId(event_id);

        if (eventPesertaModels.size() == 0){

            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else{

            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        pesertaRecyclerAdapter = new PesertaRecyclerAdapter(eventPesertaModels, this, db);
        recyclerView.setAdapter(pesertaRecyclerAdapter);
        recyclerView.setHasFixedSize(true);


        btnTambahPeserta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPopUp();

            }
        });

        seeDetail(event_id);



    }

    public void showPopUp(){

        final Dialog popUp = new Dialog(DetailActivity2.this);

        try{
            popUp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        }
        catch (Exception e){
            //nothing
        }

        popUp.setContentView(R.layout.add_peserta_popup);
        popUp.setCancelable(true);

        Button btnCancel = popUp.findViewById(R.id.btn_cancel);
        Button btnDaftar = popUp.findViewById(R.id.btn_daftar);
        et_nama = popUp.findViewById(R.id.et_nama);
        et_telp = popUp.findViewById(R.id.et_telepon);
        et_email = popUp.findViewById(R.id.et_email);
        et_keterangan = popUp.findViewById(R.id.et_keterangan);
        //spinner = popUp.findViewById(R.id.spin_nama);
        formPesertaBaru = popUp.findViewById(R.id.et_form);

        et_nama.setFocusable(true);
        et_nama.setFocusableInTouchMode(true);
        et_nama.setCursorVisible(true);

        et_email.setFocusable(true);
        et_email.setFocusableInTouchMode(true);
        et_email.setCursorVisible(true);

        et_telp.setFocusable(true);
        et_telp.setFocusableInTouchMode(true);
        et_telp.setCursorVisible(true);

        et_keterangan.setFocusable(true);
        et_keterangan.setFocusableInTouchMode(true);
        et_keterangan.setCursorVisible(true);


        final ArrayList<PesertaModel> pesertaModels = db.getAllPeserta();
        final ArrayList<String> nama = new ArrayList<>();
        nama.add(" Nama ");
        nama.add(" Daftar Peserta Baru ");


        for (PesertaModel pesertaModel : pesertaModels){

            nama.add(pesertaModel.getNamaPeserta());

        }

        //init Spinner
        spinner = popUp.findViewById(R.id.spin_nama);
        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nama);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

//        final SpinnerAdapter spinnerAdapter = new SpinnerAdapter(DetailActivity2.this,android.R.layout.simple_spinner_item, pesertaModels);
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = nama.get(position);


                if(selectedItem.equals(" Nama "))
                {
                    formPesertaBaru.setVisibility(View.GONE);

                }
                else if(selectedItem.equals(" Daftar Peserta Baru ")){

                    formPesertaBaru.setVisibility(View.VISIBLE);

                }
                else {

                    formPesertaBaru.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.GONE);

                    try{

                        PesertaModel p = pesertaModels.get(position);

                        et_nama.setText(p.getNamaPeserta());
                        et_email.setText(p.getEmail());
                        et_telp.setText(p.getPhone());
                        et_keterangan.setText(p.getKeterangan());


                    }
                    catch (NullPointerException a)
                    {

                        et_nama.setText("");
                        et_email.setText("");
                        et_telp.setText("");
                        et_keterangan.setText("");
                    }

                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {
                //nothing
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp.dismiss();
            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                newName = et_nama.getText().toString();
                newTelp = et_telp.getText().toString();
                newEmail = et_email.getText().toString();
                newKeterangan = et_keterangan.getText().toString();


                if(newName.equals("")){

                    Toast.makeText(getApplicationContext(), " Please fill all requirements ",Toast.LENGTH_LONG).show();
                }
                else if (newTelp.equals("")){

                    Toast.makeText(getApplicationContext(), " Please fill all requirements ",Toast.LENGTH_LONG).show();
                }
                else if (newEmail.equals("")){

                    Toast.makeText(getApplicationContext(), " Please fill all requirements ",Toast.LENGTH_LONG).show();
                }
                else if (!(isEmailValid(newEmail))){

                    Toast.makeText(getApplicationContext(), " Please input valid email ",Toast.LENGTH_LONG).show();

                }
                else
                {
                    PesertaModel c1 = new PesertaModel();
                    c1.setNamaPeserta(newName);
                    c1.setEmail(newEmail);
                    c1.setPhone(newTelp);
                    c1.setId_event(event_id);
                    c1.setKeterangan(newKeterangan);


                    if (FilterPesertaDaftar(c1)){

                        db.daftarPesertaEvent(c1);

                        if (FilterPeserta(c1)){

                            db.daftarPeserta(c1);

                        }

                        eventPesertaModels.add(c1);
                        popUp.dismiss();
                        pesertaRecyclerAdapter.notifyDataSetChanged();

                        if (eventPesertaModels.size() == 0){

                            emptyView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                        else{

                            emptyView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                        }

                        Toast.makeText(getApplicationContext(), "Registrasi Berhasil", Toast.LENGTH_LONG).show();
                    }
                    else {

                        Toast.makeText(getApplicationContext(), " Anda Sudah Terdaftar di Event Ini ", Toast.LENGTH_LONG).show();

                    }

                    db.closeDB();

                }

            }
        });

        popUp.show();


    }

    public Boolean FilterPesertaDaftar(PesertaModel peserta){


        ArrayList<PesertaModel> pesertas = db.getAllPesertaByEventId(event_id);

        for ( PesertaModel p1 : pesertas){

            if(p1.getNamaPeserta().equals(peserta.getNamaPeserta()) && p1.getEmail().equals(peserta.getEmail())){

                Toast.makeText(getApplicationContext(),"Anda Sudah Terdaftar Pada Event Ini",Toast.LENGTH_LONG).show();
                return false;
            }


        }



        return true;
    }

    public Boolean FilterPeserta(PesertaModel peserta){


        ArrayList<PesertaModel> pesertas = db.getAllPeserta();

        for ( PesertaModel p1 : pesertas){

            if(p1.getNamaPeserta().equals(peserta.getNamaPeserta()) && p1.getEmail().equals(peserta.getEmail())){

                return false;
            }


        }

        return true;
    }

    public void seeDetail(String event_id) {

        Call<Item> Itemmm;
        Itemmm = apiI.getDetail(event_id);


        Itemmm.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {

                // hapus html syntax
                String content = response.body().getContent();
                content = content.replaceAll("<p>", "");
                content = content.replaceAll("</p>", "\n");
                content = content.replaceAll("<b>", "");
                content = content.replaceAll("</b>", "\n");

                content = content.replaceAll("                  ", "\n");

                if(response.body() == null) {

                    Toast.makeText(getApplicationContext(), "Data Not Found", Toast.LENGTH_SHORT).show();

                }else {

                    String harga;
                    try{

                        harga = Utilities.FCurrency(Long.parseLong(response.body().getHarga().get(0).getHarga()))+" ,-";

                    }catch(Exception e){

                        harga = " ";
                    }

                    //tv_eventTitle.setText(response.body().getTitle());
                    tv_content.setText(content);
                    tv_author.setText(response.body().getAuthor());
                    btn_harga.setText(harga);
                    Glide.with(getApplicationContext()).load(response.body().getImage()).into(iv_eventImage);


                }
            }
            @Override
            public void onFailure(Call<Item> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "Get Data Failed", Toast.LENGTH_LONG).show();

            }
        });

    }

    //refresh data
    public static void notifyAdapter(){

        pesertaRecyclerAdapter.notifyDataSetChanged();

    }


}
