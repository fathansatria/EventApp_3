package com.example.eventapp;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eventapp.Adapter.EventRecyclerAdapter;
import com.example.eventapp.Adapter.PesertaListAdapter;
import com.example.eventapp.Database.DatabaseHelper;
import com.example.eventapp.Model.Item;
import com.example.eventapp.Model.PesertaModel;
import com.example.eventapp.Model.apiInterface;
import com.example.eventapp.Service.apiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private Button btnJoin;
    private Dialog dialog;
    private Button btnDaftar, btnTambah;
    private Button btnCancel;
    private String newName, newTelp, newEmail;
    private EditText et_nama, et_telp, et_email;
    private DatabaseHelper db;
    private List<PesertaModel> pesertaModels = new ArrayList<PesertaModel>();
    private ListView lv_peserta,pesertas;
    private PesertaListAdapter listAdapter;
    private String event_id;
    private BottomSheetBehavior bottomSheetBehavior;
    private apiService apiInit;
    private CoordinatorLayout mCoordinatorLayout;
    private LinearLayout bottom_model;
    private TextView tv_event_Title,tv_content,tv_author,tv_harga;
    apiInterface apiI;
    private PopupWindow mPopupWindow;
    private ImageView event_image;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        bottom_model = findViewById(R.id.bottom_model);
        View llBottomSheet = findViewById(R.id.bottom_sheet);
        pesertas = (ListView) llBottomSheet.findViewById(R.id.lv_pesertas);
        lv_peserta = (ListView) findViewById(R.id.list_perserta);
        btnTambah = (Button) llBottomSheet.findViewById(R.id.btn_tambah_peserta);
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        tv_content = findViewById(R.id.tv_content);
        tv_author = findViewById(R.id.author);
        tv_event_Title = findViewById(R.id.title);
        tv_harga = (TextView) bottom_model.findViewById(R.id.tv_harga);
        event_image = findViewById(R.id.iv_event_image);


        apiInit = new apiService();
        apiI = apiService.getClient().create(apiInterface.class);

        db = new DatabaseHelper(this);
        btnJoin = findViewById(R.id.btn_join);



        Bundle extras = getIntent().getExtras();
        if(extras != null){

            event_id = extras.getString("eventId");

        }
        else{
            event_id = "-9999";
        }


        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.detail_activity);
        listAdapter = new PesertaListAdapter(this, pesertaModels);
        pesertas.setAdapter(listAdapter);
        lv_peserta.setAdapter(listAdapter);



        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPopUp();

                //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });

        tv_harga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showPopUp();

                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }
        });


        db.closeDB();
        seeDetail(event_id);



    }

    public void showPopUp(){

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customView = inflater.inflate(R.layout.add_peserta_popup,null);

        mPopupWindow = new PopupWindow(
                customView,
                CoordinatorLayout.LayoutParams.WRAP_CONTENT,
                CoordinatorLayout.LayoutParams.WRAP_CONTENT
        );

        mPopupWindow.setFocusable(true);

        btnCancel = (Button)customView.findViewById(R.id.btn_cancel);
        btnDaftar = (Button)customView.findViewById(R.id.btn_daftar);
        et_nama = (EditText)customView.findViewById(R.id.et_nama);
        et_telp = (EditText)customView.findViewById(R.id.et_telepon);
        et_email = (EditText)customView.findViewById(R.id.et_email);



//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                new formFragment()).commit();


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
//
//
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                newName = et_nama.getText().toString();
                newTelp = et_telp.getText().toString();
                newEmail = et_email.getText().toString();

//                PesertaModel c1 = new PesertaModel();
//                c1.setNamaPeserta(newName);
//                c1.setEmail(newEmail);
//                c1.setPhone(newTelp);
//                c1.setId_event(event_id);

                PesertaModel c2 = new PesertaModel();
                c2.setNamaPeserta("fathan");
                c2.setEmail("lala@gmail.com");
                c2.setPhone("8723640");
                c2.setId_event(event_id);

                PesertaModel c3 = new PesertaModel();
                c3.setNamaPeserta("fathan13");
                c3.setEmail("lala@gmai2l.com");
                c3.setPhone("87236420");
                c3.setId_event(event_id);


                //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                //pesertaModels.add(c1);
                pesertaModels.add(c2);
                pesertaModels.add(c3);


                mPopupWindow.dismiss();
                listAdapter.notifyDataSetChanged();

                db.daftarPeserta(c2);
                db.daftarPeserta(c3);
                db.closeDB();


            }
        });
//
//
        mPopupWindow.showAtLocation(mCoordinatorLayout, Gravity.CENTER,0,0);


    }

    public void seeDetail(String event_id) {

        Call<Item> Itemmm;


        Itemmm = apiI.getDetail(event_id);


        Itemmm.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {


                String content = response.body().getContent();

                content = content.replaceAll("<p>", "");
                content = content.replaceAll("</p>", "\n");
                content = content.replaceAll("<b>", "");
                content = content.replaceAll("</b>", "\n");

                content = content.replaceAll("                  ", "\n");



                if(response.body() == null) {

                    Toast.makeText(getApplicationContext(), "Data Not Found", Toast.LENGTH_SHORT).show();

                }else {

                    tv_event_Title.setText(response.body().getTitle());
                    tv_content.setText(content);
                    tv_author.setText(response.body().getAuthor());
                    Glide.with(getApplicationContext()).load(response.body().getImage()).into(event_image);


                }
            }
            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                //empty_view.setVisibility(View.VISIBLE);
                //recyclerView.setVisibility(View.GONE);
            }
        });
    }



}
