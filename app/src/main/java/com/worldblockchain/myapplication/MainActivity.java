package com.worldblockchain.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.client.results.SignInResult;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kenai.jffi.Main;

import org.web3j.abi.datatypes.Utf8String;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.File;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.fragform_icon)
    ImageView app_icon;

    @BindView(R.id.fragform_edittext)
    EditText mEditText;

    @BindView(R.id.fragform_recieved)
    TextView tv_recieved;


    public static final int PICK_IMAGE = 1;
    private Uri mImageUri;

    private int mCurrentAmount, mRecievedAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_form);
        ButterKnife.bind(this);

        Glide.with(this).load(R.drawable.app7)
                .apply(RequestOptions.centerInsideTransform()).into(app_icon);

        initAWS();


        Web3j web3 = Web3j.build(new
                HttpService("https://ropsten.infura.io/v3/a9ca793c9570453b8b7d618b727b0cf0"));


        EthGetBalance ethGetBalance = null;
        try {
            ethGetBalance = web3
                    .ethGetBalance("0x9783988EdC5fD2AFbad7A6db11ddFe9e88790Cbf",
                            DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        assert ethGetBalance != null;
        BigInteger wei = ethGetBalance.getBalance();

        java.math.BigDecimal tokenValue = Convert.fromWei(String.valueOf(wei), Convert.Unit.ETHER);
        String strTokenAmount = String.valueOf(tokenValue);
        mEditText.setText(strTokenAmount);

        Log.d(TAG, "onCreate: lol: " + strTokenAmount);



    }

    @OnClick(R.id.fragform_refresh)
    public void initRefresh(){
        Web3j web3 = Web3j.build(new
                HttpService("https://ropsten.infura.io/v3/a9ca793c9570453b8b7d618b727b0cf0"));

        EthGetBalance ethGetBalance = null;
        try {
            ethGetBalance = web3
                    .ethGetBalance("0x9783988EdC5fD2AFbad7A6db11ddFe9e88790Cbf",
                            DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        assert ethGetBalance != null;
        BigInteger wei = ethGetBalance.getBalance();

        java.math.BigDecimal tokenValue = Convert.fromWei(String.valueOf(wei), Convert.Unit.ETHER);
        String strTokenAmount = String.valueOf(tokenValue);

        tv_recieved.setText(strTokenAmount);



    }

    @OnClick(R.id.fragform_entermedicalinfo)
    public void initEnterMedical(){
        Intent intent = new Intent(this, UploadMedicalInfo.class);
        startActivity(intent);
    }

    public void initAWS() {

        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
                    @Override
                    public void onResult(UserStateDetails userStateDetails) {
                        switch (userStateDetails.getUserState()){
                            case SIGNED_IN:
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d(TAG, "run: Signed in");
                                    }
                                });
                                break;
                            case SIGNED_OUT:
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d(TAG, "run: Signed out");

                                    }
                                });
                                break;
                            default:
                                AWSMobileClient.getInstance().signOut();
                                break;
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("INIT", "Initialization error.", e);
                    }
                }
        );

        String username = "pizzaguy44";
        String password = "4523039";

        AWSMobileClient.getInstance().signIn(username, password, null, new Callback<SignInResult>() {
            @Override
            public void onResult(final SignInResult signInResult) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Sign-in callback state: " + signInResult.getSignInState());
                        switch (signInResult.getSignInState()) {
                            case DONE:
                                Log.d(TAG, "run: Signed in");
                                break;
                            case SMS_MFA:
                                break;
                            case NEW_PASSWORD_REQUIRED:
                                break;
                            default:
                                break;
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Sign-in ERROR", e);

            }
        });


    }


    /**
     *  @OnClick(R.id.fragform_nextpage)
     *     public void initNextPage(){
     *         //Intent intent = new Intent(MainActivity.this, UploadActivity.class);
     *         //startActivity(intent);
     *         isStoragePermissionGranted();
     *     }
     */


    @OnClick(R.id.fragform_uploadmedicalrecord)
    public void initUploadMedical(){
        imageFileChooser();
    }

    private void imageFileChooser(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        this.startActivityForResult(intent, PICK_IMAGE);
    }

    public void CheckInLoadImage(Intent data){
        if( data != null){
            mImageUri = data.getData();
            Log.d(TAG, "onActivityResult: mImageUri: " + getRealPathFromURI(this, mImageUri));
            //Glide.with(this).load(mImageUri).apply(RequestOptions.centerCropTransform()).into(mCheckInImage);
        }
    }

    public void isStoragePermissionGranted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG,"Permission is granted");
            if(mImageUri == null){
                Toast.makeText(this, "Please upload an image", Toast.LENGTH_SHORT).show();
            }else{
                uploadWithTransferUtility();
            }

        } else {

            Log.v(TAG,"Permission is revoked");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e(TAG, "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    public void uploadWithTransferUtility() {

        TransferNetworkLossHandler.getInstance(this);

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(this)
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(new AmazonS3Client(AWSMobileClient.getInstance()))
                        .build();

        TransferObserver uploadObserver =
                transferUtility.upload(
                        "worldblockchain-bucket1",
                        "myface2.jpg",
                        new File(getRealPathFromURI(this, mImageUri)));

        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    Log.d(TAG, "onStateChanged: StateComplete: " + state);
                    Toast.makeText(MainActivity.this, "Image uploaded to S3", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            }

            @Override
            public void onError(int id, Exception ex) {
            }

        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: MAINACTIVITY");
        Log.d(TAG, "onActivityResult: RequestCode: " + requestCode);

        if (requestCode == 1) {
            if( data != null){
                mImageUri = data.getData();
                isStoragePermissionGranted();
            }
        }
    }




}
