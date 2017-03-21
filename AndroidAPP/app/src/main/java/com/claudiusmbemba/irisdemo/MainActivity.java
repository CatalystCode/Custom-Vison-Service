package com.claudiusmbemba.irisdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.claudiusmbemba.irisdemo.helpers.NetworkHelper;
import com.claudiusmbemba.irisdemo.helpers.RequestPackage;
import com.claudiusmbemba.irisdemo.model.Classification;
import com.claudiusmbemba.irisdemo.model.IrisData;
import com.claudiusmbemba.irisdemo.services.IrisService;
import com.claudiusmbemba.irisdemo.services.NutritionixService;
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
    Classification food_result;

    public final String URL = "url";
    public final String IMAGE = "image";
    public static final String TAG = "IRIS_LOGGER";
    private final String ENDPOINT = "https://customvisionppe.azure-api.net/v1.0/Prediction/b33db6e4-2356-4473-a861-c2e2e84ddb59/%s?iterationId=cfd42493-b244-43f3-b892-bdc2c7d6132c";
    private final String NUTRIENDPOINT = "https://api.nutritionix.com/v1_1/search/%s";

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
            }else if(intent.getExtras().containsKey(IrisService.IRIS_SERVICE_NAME)){
                IrisData irisData = (IrisData) intent
                        .getParcelableExtra(IrisService.IRIS_SERVICE_PAYLOAD);
                food_result = irisData.getClassifications().get(0);
                clearText();
                resultTV.append(String.format("I'm %.2f%% confident that this is a %s \n", food_result.getProbability() * 100, food_result.getClass_()));
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

    public void showNutritionInfo(View v){
        RequestPackage nutriRequest = new RequestPackage();
        Intent intent = new Intent(this, NutritionixService.class);
        nutriRequest.setEndPoint(String.format(NUTRIENDPOINT, food_result.getClass_()));
        nutriRequest.setParam("fields","item_name%2Citem_id%2Cbrand_name%2Cnf_calories%2Cnf_total_fat");
        nutriRequest.setMethod("GET");
        intent.putExtra(IrisService.REQUEST_PACKAGE, nutriRequest);
        startService(intent);

        Intent nutri = new Intent(this, NutritionActivity.class);
        startActivity(nutri);
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
