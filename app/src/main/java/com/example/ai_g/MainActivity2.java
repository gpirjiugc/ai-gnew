package com.example.ai_g;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;
import com.google.mlkit.vision.objects.defaults.PredefinedCategory;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.TextRecognizerOptions;

import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    String type;
    ImageView imageView;
    Rect boundingBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        imageView = findViewById(R.id.image_box);

        Intent i = getIntent();
        type  =i.getExtras().getString("key");


        if(type.equals("text")){
            gettext();
        }
        if(type.equals("object")){
            getobject();
        }
        if(type.equals("lable")){
            getlable();
        }




    }


    private void getlable() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i,3);
    }

    private void getobject() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i,2);

    }

    private void gettext() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            Bundle ex = data.getExtras();
            Bitmap bitmap = (Bitmap) ex.get("data");
            InputImage image = InputImage.fromBitmap(bitmap,0);
            TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
            Task<Text> result =
                    recognizer.process(image)
                            .addOnSuccessListener(new OnSuccessListener<Text>() {
                                @Override
                                public void onSuccess(Text visionText) {
                                    if(visionText.getText().isEmpty()){
                                        Toast.makeText(MainActivity2.this, "Pls Capture Image Clear", Toast.LENGTH_SHORT).show();
                                        gettext();
                                    }
                                    else {
                                        Toast.makeText(MainActivity2.this, visionText.getText().toString(), Toast.LENGTH_SHORT).show();
                                        AlertDialog.Builder a = new AlertDialog.Builder(MainActivity2.this);
                                        View v = getLayoutInflater().inflate(R.layout.dbscanner, null);
                                        TextView textView = v.findViewById(R.id.tx_bar);
                                        textView.setText(visionText.getText().toString());
                                        a.setView(v);
                                        a.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = new Intent(MainActivity2.this, MainActivity.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        });
                                        a.show();
                                    }
                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MainActivity2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });


        }
        if(requestCode == 2){

            ObjectDetectorOptions options =
                    new ObjectDetectorOptions.Builder()
                            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
                            .enableMultipleObjects()
                            .enableClassification()  // Optional
                            .build();
            Bundle ex = data.getExtras();
            if(data == null){
                finish();
            }
            Bitmap bitmap = (Bitmap) ex.get("data");

            ObjectDetector objectDetector = ObjectDetection.getClient(options);
            InputImage inputImage = InputImage.fromBitmap(bitmap,0);
            objectDetector.process(inputImage)
                    .addOnSuccessListener(
                            new OnSuccessListener<List<DetectedObject>>() {
                                @Override
                                public void onSuccess(List<DetectedObject> detectedObjects) {
                                          String text = "";
                                        for (DetectedObject detectedObject : detectedObjects) {
                                            boundingBox = detectedObject.getBoundingBox();
                                            Integer trackingId = detectedObject.getTrackingId();
                                            for (DetectedObject.Label label : detectedObject.getLabels()) {
                                                text = label.getText();
                                                if (PredefinedCategory.FOOD.equals(text)) {

                                                }
                                                int index = label.getIndex();
                                                if (PredefinedCategory.FOOD_INDEX == index) {

                                                }
                                                float confidence = label.getConfidence();
                                            }
                                        }

                                   if(text.equals("")){
                                       Toast.makeText(MainActivity2.this, "Please Capture Image Clear", Toast.LENGTH_SHORT).show();
                                       getobject();
                                   }
                                   else {
                                       Paint paint = new Paint();
                                       paint.setStyle(Paint.Style.STROKE);

                                       paint.setColor(Color.RED);
                                       Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                                       Canvas canvas = new Canvas(mutableBitmap);

                                       canvas.drawRect(boundingBox,paint);

                                       imageView.setImageBitmap(mutableBitmap);

                                       BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity2.this);
                                       View v = getLayoutInflater().inflate(R.layout.dbscanner, null);
                                       TextView textView = v.findViewById(R.id.tx_bar);
                                       textView.setText(text.toString());
                                       bottomSheetDialog.setContentView(v);
                                       bottomSheetDialog.show();


                                   }
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity2.this, "getToast", Toast.LENGTH_SHORT).show();
                                }
                            });
        }
        if(requestCode == 3){
           Bundle bundle = data.getExtras();
           Bitmap bitmap = (Bitmap) bundle.get("data");
           InputImage inputImage = InputImage.fromBitmap(bitmap,0);
           ImageLabeler labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
           labeler.process(inputImage)
                   .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                       @Override
                       public void onSuccess(List<ImageLabel> labels){
                           String text="";
                           for (ImageLabel label : labels) {
                                text = label.getText();
                               float confidence = label.getConfidence();
                               int index = label.getIndex();

                           }
                           if(text.equals("")){
                               Toast.makeText(MainActivity2.this, "PLs Capture clear image", Toast.LENGTH_SHORT).show();
                           }
                           else {
                           AlertDialog.Builder a = new AlertDialog.Builder(MainActivity2.this);
                           View v = getLayoutInflater().inflate(R.layout.dbscanner, null);
                           TextView textView = v.findViewById(R.id.tx_bar);
                           textView.setText(text);
                           a.setView(v);
                           a.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   Intent i = new Intent(MainActivity2.this, MainActivity.class);
                                   startActivity(i);
                                   finish();
                               }
                           });
                           a.show(); }
                       }
                   })
                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           // Task failed with an exception
                           // ...
                       }
                   });

       }

    }
}