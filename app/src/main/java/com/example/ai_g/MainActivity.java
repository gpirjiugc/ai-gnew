package com.example.ai_g;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {
CardView cardView1,cardView2,cardView4,cardView5;
int len = 0;
    @Override
    public void onBackPressed() {

        if(len==0){
            len = 1;
            Toast.makeText(this, "One More Time Click To Exit", Toast.LENGTH_SHORT).show();
        }
        else {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        cardView1 = findViewById(R.id.card_bar_detect);
        cardView2 = findViewById(R.id.card_lable_detect);

        cardView4 = findViewById(R.id.card_object_detect);
        cardView5 = findViewById(R.id.card_text_detect);
        getWindow().setStatusBarColor(Color.parseColor("#2B2B32"));


        Dexter.withContext(getApplicationContext()).withPermissions(Manifest.permission.CAMERA).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if(multiplePermissionsReport.isAnyPermissionPermanentlyDenied()){
                    Toast.makeText(MainActivity.this, "pls allow permission", Toast.LENGTH_SHORT).show();
                }
                if(multiplePermissionsReport.areAllPermissionsGranted()){
                    Toast.makeText(getApplicationContext(), "Starting", Toast.LENGTH_SHORT).show();
                      start();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();





    }

    public  void start(){
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(MainActivity.this,BarCode.class);
                startActivity(ii);

            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,MainActivity2.class);
                i.putExtra("key","lable");
                startActivity(i);
            }
        });

        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,MainActivity2.class);
                i.putExtra("key","object");
                startActivity(i);
            }
        });
        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,MainActivity2.class);
                i.putExtra("key","text");
                startActivity(i);
            }
        });
    }
}