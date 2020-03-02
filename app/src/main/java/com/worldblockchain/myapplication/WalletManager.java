package com.worldblockchain.myapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class WalletManager extends AppCompatActivity {

    private static final String TAG = "WalletManager";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        Log.d(TAG, "onCreate: lol: " + strTokenAmount);
    }
}
