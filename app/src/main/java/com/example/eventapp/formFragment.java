package com.example.eventapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class formFragment extends Fragment {

    EditText et_nama;
    EditText et_phone;
    EditText et_email;
    Button btn_daftar;
    Button btn_cancel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.form_fragment, container, false);

        et_nama = (EditText) view.findViewById(R.id.et_name);
        et_email = (EditText) view.findViewById(R.id.et_email);
        et_phone = (EditText) view.findViewById(R.id.et_phone);
        btn_daftar = (Button) view.findViewById(R.id.btn_daftar);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String nama = et_nama.getText().toString();
                    String phone = et_phone.getText().toString();
                    String email = et_email.getText().toString();

                    DetailActivity detailActivity = (DetailActivity) getActivity();

                    if (nama != null && phone != null && email != null) {
//                        detailActivity.newName = nama;
//                        detailActivity.newTelp = phone;
//                        detailActivity.newEmail = email;
//                        detailActivity.finish();
                    } else {
                        Toast.makeText(detailActivity, "Please fill full requirement", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        return view;

    }
}
