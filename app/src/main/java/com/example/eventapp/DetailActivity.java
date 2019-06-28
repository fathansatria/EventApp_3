package com.example.eventapp;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.eventapp.Adapter.SpinnerAdapter;
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


public class DetailActivity extends AppCompatActivity {

    private String newName, newTelp, newEmail, newKeterangan;
    private DatabaseHelper db;
    private ArrayList<PesertaModel> pesertaModels;
    private String event_id;
    private BottomSheetBehavior bottomSheetBehavior;


    //init api
    private apiService apiInit = new apiService();
    private TextView tv_event_Title,tv_content,tv_author,tv_harga;
    apiInterface apiI = apiService.getClient().create(apiInterface.class);
    private ImageView event_image;
    private RecyclerView recyclerView;
    public static PesertaRecyclerAdapter pesertaRecyclerAdapter;
    private LinearLayout emptyView;
    private Spinner spinner;
    private RelativeLayout formPesertaBaru;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        LinearLayout bottom_model = findViewById(R.id.bottom_model);
        Button btnTambah =  findViewById(R.id.btn_tambah_peserta);
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_model);
        tv_content = findViewById(R.id.tv_content);
        tv_author = findViewById(R.id.author);
        tv_event_Title = findViewById(R.id.title);
        tv_harga =  bottom_model.findViewById(R.id.tv_harga);
        event_image = findViewById(R.id.iv_event_image);
        emptyView = bottom_model.findViewById(R.id.empty_peserta);


        recyclerView =  findViewById(R.id.peserta_recycler_view);
        recyclerView.setHasFixedSize(true);


        db = new DatabaseHelper(this);

        //ambil event id dari main activity

        Bundle extras = getIntent().getExtras();
        if(extras != null){

            event_id = extras.getString("eventId");

        }
        else{
            event_id = "-9999";
        }



        //ambil peserta yang sudah di daftar di event

        try{

            if (event_id.equals("-9999")){

                pesertaModels = new ArrayList<>();

            }
            else{
                pesertaModels = db.getAllPesertaByEventId(event_id);

            }
        }
        catch(Exception e)
        {
            pesertaModels = new ArrayList<>();
        }


        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPopUp();

            }
        });

        tv_harga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }
        });

        // ambil data detail dari event
        seeDetail(event_id);



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        pesertaRecyclerAdapter = new PesertaRecyclerAdapter(pesertaModels, this, db);

        if (pesertaModels.size() == 0){
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else{
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        pesertaRecyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(pesertaRecyclerAdapter);

        db.closeDB();


    }

    public void showPopUp(){


        final Dialog popUp = new Dialog(DetailActivity.this);

        try
        {
            popUp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        }
        catch (NullPointerException n){
            //nothing
        }

        popUp.setContentView(R.layout.add_peserta_popup);
        popUp.setCancelable(true);

        final Button btnCancel = popUp.findViewById(R.id.btn_cancel);
        final Button btnDaftar = popUp.findViewById(R.id.btn_daftar);
        final EditText et_nama = popUp.findViewById(R.id.et_nama);
        final EditText et_telp = popUp.findViewById(R.id.et_telepon);
        final EditText et_email = popUp.findViewById(R.id.et_email);
        final EditText et_keterangan = popUp.findViewById(R.id.et_keterangan);

        spinner = popUp.findViewById(R.id.spin_nama);
        formPesertaBaru = popUp.findViewById(R.id.et_form);

        et_nama.setFocusable(true);
        et_nama.setFocusableInTouchMode(true);
        et_nama.setCursorVisible(true);

        et_email.setFocusable(true);
        et_email.setFocusableInTouchMode(true);
        et_email.setCursorVisible(true);

        et_keterangan.setFocusable(true);
        et_keterangan.setFocusableInTouchMode(true);
        et_keterangan.setCursorVisible(true);



        ArrayList<PesertaModel> namaPeserta = db.getAllPeserta();


        PesertaModel p2 = new PesertaModel();
        p2.setNamaPeserta(" Daftar Peserta Baru ");
        p2.setEmail("");
        p2.setPhone("");

        namaPeserta.add(p2);

        final SpinnerAdapter spinnerAdapter = new SpinnerAdapter(DetailActivity.this,android.R.layout.simple_spinner_item, namaPeserta);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                PesertaModel selectedItem = spinnerAdapter.getItem(position);

                if(selectedItem.getNamaPeserta().equals(" Nama "))
                {
                    formPesertaBaru.setVisibility(View.GONE);

                }
                else if(selectedItem.getNamaPeserta().equals(" Daftar Peserta Baru ")){

                    formPesertaBaru.setVisibility(View.VISIBLE);

                }
                else {

                    formPesertaBaru.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.GONE);
                    et_nama.setText(selectedItem.getNamaPeserta());
                    et_email.setText(selectedItem.getEmail());
                    et_telp.setText(selectedItem.getPhone());
                    et_keterangan.setText(selectedItem.getKeterangan());
                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

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
                    popUp.dismiss();
                    Toast.makeText(getApplicationContext(), " Please fill all requirements ",Toast.LENGTH_LONG).show();
                }
                else if (newTelp.equals("")){
                    popUp.dismiss();
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

                        pesertaModels.add(c1);
                        popUp.dismiss();
                        pesertaRecyclerAdapter.notifyDataSetChanged();

                        if (pesertaModels.size() == 0){

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

    public void seeDetail(String event_id) {

        Call<Item> Itemmm;
        Itemmm = apiI.getDetail(event_id);


        Itemmm.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {

                // hapus html syntax
                String content;

                try{
                    content = response.body().getContent();

                }
                catch (Exception e){
                    content = " ";
                }
                content = content.replaceAll("<p>", "");
                content = content.replaceAll("</p>", "\n");
                content = content.replaceAll("<b>", "");
                content = content.replaceAll("</b>", "\n");

                content = content.replaceAll("                  ", "\n");

                if (response.body() != null) {

                    tv_event_Title.setText(response.body().getTitle());
                    tv_content.setText(content);
                    tv_author.setText(response.body().getAuthor());
                    tv_harga.setText(Utilities.FCurrency(Long.parseLong(response.body().getHarga().get(0).getHarga()))+" ,-");
                    Glide.with(getApplicationContext()).load(response.body().getImage()).into(event_image);


                } else {

                    Toast.makeText(getApplicationContext(), "Data Not Found", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(Call<Item> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "Failed to get data", Toast.LENGTH_LONG).show();

            }
        });

    }

    //refresh data
    public static void notifyAdapter(){

        pesertaRecyclerAdapter.notifyDataSetChanged();

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

    public Boolean cekNotif(String id){

        ArrayList<NotifItem> notifItems = db.getAllNotif();

        for (NotifItem n : notifItems){

            if (n.getValue().equals(id)){
                return true;
            }

        }

        return false;

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        notifyAdapter();

    }


}
