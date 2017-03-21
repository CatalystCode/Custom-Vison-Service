package com.claudiusmbemba.irisdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.claudiusmbemba.irisdemo.helpers.NetworkHelper;
import com.claudiusmbemba.irisdemo.helpers.RequestPackage;
import com.claudiusmbemba.irisdemo.model.Classification;
import com.claudiusmbemba.irisdemo.model.IrisData;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private boolean networkOn;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    TextView resultTV;
    Button photoButton;
    Button urlButton;
    EditText urlText;
    CropImageView image;
    Bitmap bitmap;
    Drawable d;

    public final String URL = "url";
    public final String IMAGE = "image";
    public static final String TAG = "IRIS_LOGGER";
    private final String ENDPOINT = "https://customvisionppe.azure-api.net/v1.0/Prediction/e7e9da08-dc38-4d1a-8274-7339cb0474c0/%s?iterationId=5606be21-ef44-4b51-b278-0f3a6565c5aa";

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getExtras().containsKey(IrisService.IRIS_SERVICE_ERROR)){
                String msg = intent.getStringExtra(IrisService.IRIS_SERVICE_ERROR);
                resultTV.setText(msg);
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }else {
                IrisData irisData = (IrisData) intent
                        .getParcelableExtra(IrisService.IRIS_SERVICE_PAYLOAD);
                Classification result = irisData.getClassifications().get(0);
                clearText();
                resultTV.append(String.format("I'm %.2f%% confident that this is a %s \n", result.getProbability() * 100, result.getClass_()));
                Log.i(TAG, irisData.getClassifications().toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        networkOn = NetworkHelper.hasNetworkAccess(this);

        image = (CropImageView) findViewById(R.id.imageView);
        try {
             d = Drawable.createFromStream(getAssets().open("apple.jpg"), null);
            image.setImageBitmap(((BitmapDrawable)d).getBitmap());
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultTV = (TextView) findViewById(R.id.resultText);
        photoButton = (Button) findViewById(R.id.photoButon);
        urlButton = (Button) findViewById(R.id.urlButton);
        urlText = (EditText) findViewById(R.id.urlText);
        urlText.setText("http://www.statesymbolsusa.org/sites/statesymbolsusa.org/files/redrome.jpg");

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(receiver, new IntentFilter(IrisService.IRIS_SERVICE_NAME));
    }

    private void clearText(){
        resultTV.setText("");
    }

    private void progressLoader(){
        resultTV.setText("Thinking...");
    }

    public void openUrl(View v){
        clearText();
        if(networkOn){
            if(!urlText.getText().toString().equals("")){
                progressLoader();
                requestIrisService(URL);
            }else{
                Toast.makeText(this, "Please enter url into text box above.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(receiver);
    }

    private void requestIrisService(String type) {
        RequestPackage requestPackage = new RequestPackage();
        Intent intent = new Intent(this, IrisService.class);

        if(type.equals(URL)){
            requestPackage.setEndPoint(String.format(ENDPOINT, URL));
            requestPackage.setParam("Url", urlText.getText().toString());
        }else if (type.equals(IMAGE)){
            requestPackage.setEndPoint(String.format(ENDPOINT, IMAGE));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            if(Build.MODEL.contains("x86")) {
                bitmap = ((BitmapDrawable)d).getBitmap();
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            intent.putExtra(IrisService.REQUEST_IMAGE, byteArray);
        }

        requestPackage.setMethod("POST");
        intent.putExtra(IrisService.REQUEST_PACKAGE, requestPackage);
        startService(intent);
    }

    public void takePhoto(View v){
        clearText();
        if(networkOn){
            if(Build.MODEL.contains("x86")){
                requestIrisService(IMAGE);
                progressLoader();
            }else {
                dispatchTakePictureIntent();
            }
        }else{
            Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(bitmap);
        }
    }

    public void getCrop(View v){
        bitmap =  image.getCroppedImage();
        requestIrisService(IMAGE);
        progressLoader();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
