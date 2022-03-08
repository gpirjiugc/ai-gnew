package com.example.ai_g;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarCode extends Activity implements ZXingScannerView.ResultHandler {
ZXingScannerView zXingScannerView;

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        zXingScannerView = new ZXingScannerView(this);
        getWindow().setStatusBarColor(Color.parseColor("#2B2B32"));
        setContentView(zXingScannerView);

    }

    @Override
    public void handleResult(Result rawResult) {


       if( Patterns.WEB_URL.matcher(rawResult.toString().toLowerCase()).matches()){
           Toast.makeText(this, "Website", Toast.LENGTH_SHORT).show();

           AlertDialog.Builder a = new AlertDialog.Builder(BarCode.this);
           View v = getLayoutInflater().inflate(R.layout.dbscanner,null);
           TextView textView = v.findViewById(R.id.tx_bar);

           textView.setText("WEBSITE :  "+rawResult.toString());

           a.setView(v);
           a.setPositiveButton("Browse", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {

                  WebView webView = new WebView(BarCode.this);
                   WebSettings webSettings = webView.getSettings();
                   webSettings.setJavaScriptEnabled(true);
                   webView.loadUrl(rawResult.toString());

               }
           });
           a.setNegativeButton("Back", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   Intent i = new Intent(BarCode.this,MainActivity.class);
                   startActivity(i);
                   finish();
               }
           });
           a.show();

       }
       else {
           Toast.makeText(this, rawResult.toString(), Toast.LENGTH_SHORT).show();
           AlertDialog.Builder a = new AlertDialog.Builder(BarCode.this);
           View v = getLayoutInflater().inflate(R.layout.dbscanner,null);
           TextView textView = v.findViewById(R.id.tx_bar);
           textView.setText(rawResult.toString());
           a.setView(v);
           a.show();
       }

    }

    @Override
    protected void onResume() {
        super.onResume();
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }
}