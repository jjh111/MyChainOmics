package com.worldblockchain.myapplication;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadActivity extends AppCompatActivity {

    @BindView(R.id.fragform_uniquecontentid)
    EditText mContentId;
    @BindView(R.id.fragform_uniquepatientid)
    EditText mPatientId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_activity);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.uploadactivity_refresh)
    public void initRefresh(){
        String one = generateUUID();
        String two = generateUUID();
        mContentId.setText(one);
        mPatientId.setText(two);
    }

    public static String generateUUID(){
        final String uuid = UUID.randomUUID().toString();
        //.replace("-", "")
        return uuid;
    }
}
