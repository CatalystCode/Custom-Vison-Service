package com.claudiusmbemba.irisdemo;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.claudiusmbemba.irisdemo.helpers.NetworkHelper;
import com.claudiusmbemba.irisdemo.helpers.RequestPackage;
import com.claudiusmbemba.irisdemo.models.Classification;
import com.claudiusmbemba.irisdemo.models.Hit;
import com.claudiusmbemba.irisdemo.models.IrisData;
import com.claudiusmbemba.irisdemo.models.NutritionixData;
import com.claudiusmbemba.irisdemo.services.IrisService;
import com.claudiusmbemba.irisdemo.services.NutritionixService;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_CLICK_REQUEST_CODE = 3;
    private static final int GALLERY_CLICK_REQUEST_CODE = 4;
    private boolean networkOn;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int SELECT_PICTURE = 2;

    TextView resultTV;
    ImageButton photoButton, urlButton, nutritionButton, galleryButton;
    EditText urlText;
    CropImageView image;
    Bitmap bitmap;
    Drawable d;
    Classification food_result;
    LocalBroadcastManager broadcastManager;
    Hit nutritionixHit;

    public final String URL = "url";
    public final String IMAGE = "image";
    public static final String TAG = "IRIS_LOGGER";
    private final String ENDPOINT = "https://customvisionppe.azure-api.net/v1.0/Prediction/b33db6e4-2356-4473-a861-c2e2e84ddb59/%s?iterationId=cfd42493-b244-43f3-b892-bdc2c7d6132c";
    private final String NUTRI_ENDPOINT = "https://api.nutritionix.com/v1_1/search/%s";
    public static final String FOOD_RESULT = "FOOD_RESULT";
    public static final String NUTRITION_RESULT = "NUTRITION_RESULT";
    public static final String IRIS_REQUEST = "IRIS_REQUEST";
    private Activity thisActivity;


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private BroadcastReceiver irisReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras().containsKey(IrisService.IRIS_SERVICE_ERROR)) {
                String msg = intent.getStringExtra(IrisService.IRIS_SERVICE_ERROR);
                resultTV.setText(msg);
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            } else if (intent.getExtras().containsKey(IrisService.IRIS_SERVICE_PAYLOAD)) {
                IrisData irisData = (IrisData) intent
                        .getParcelableExtra(IrisService.IRIS_SERVICE_PAYLOAD);
                food_result = irisData.getClassifications().get(0);
                clearText();
                String msg = String.format("I'm %.0f%% confident that this is a %s \n", food_result.getProbability() * 100, food_result.getClass_());
                resultTV.append(msg);
                Log.i(TAG, irisData.getClassifications().toString());
                requestNutritionInfo();
            }
        }
    };

    private BroadcastReceiver nutritionixReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras().containsKey(NutritionixService.NUTRITION_SERVICE_ERROR)) {
                String msg = intent.getStringExtra(NutritionixService.NUTRITION_SERVICE_ERROR);
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            } else if (intent.getExtras().containsKey(NutritionixService.NUTRITION_SERVICE_PAYLOAD)) {
                NutritionixData results = (NutritionixData) intent.getParcelableExtra(NutritionixService.NUTRITION_SERVICE_PAYLOAD);
                nutritionixHit = results.getHits().get(0);
                nutritionButton.setEnabled(true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        thisActivity = this;

        broadcastManager = LocalBroadcastManager.getInstance(this);
        networkOn = NetworkHelper.hasNetworkAccess(this);

        image = (CropImageView) findViewById(R.id.imageView);
        try {
            d = Drawable.createFromStream(getAssets().open("apple.jpg"), null);
            image.setImageBitmap(((BitmapDrawable) d).getBitmap());
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultTV = (TextView) findViewById(R.id.resultText);
        nutritionButton = (ImageButton) findViewById(R.id.nutriButton);
        nutritionButton.setEnabled(false);
        photoButton = (ImageButton) findViewById(R.id.photoButon);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions(thisActivity,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_CLICK_REQUEST_CODE);
                } else {
                    takePhoto();
                }
            }
        });
        urlButton = (ImageButton) findViewById(R.id.urlButton);
        galleryButton = (ImageButton) findViewById(R.id.galleryButton);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions(thisActivity,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_CLICK_REQUEST_CODE);
                } else {
                    openGallery();
                }
            }
        });
        urlText = (EditText) findViewById(R.id.urlText);
        urlText.setText("http://www.statesymbolsusa.org/sites/statesymbolsusa.org/files/redrome.jpg");

        broadcastManager.registerReceiver(irisReceiver, new IntentFilter(IrisService.IRIS_SERVICE_NAME));
        broadcastManager.registerReceiver(nutritionixReceiver, new IntentFilter(NutritionixService.NUTRITION_SERVICE_NAME));
    }

    private void clearText() {
        resultTV.setText("");
    }

    private void progressLoader() {
        resultTV.setText("Thinking...");
    }

    public void openUrl(View v) {
        clearText();
        if (networkOn) {
            if (!urlText.getText().toString().equals("")) {
                progressLoader();
                requestIrisService(URL);
            } else {
                Toast.makeText(this, "Please enter url into text box above.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(irisReceiver);
        broadcastManager.unregisterReceiver(nutritionixReceiver);
    }

    private void requestIrisService(String type) {
        RequestPackage requestPackage = new RequestPackage();
        Intent intent = new Intent(this, IrisService.class);
        requestPackage.setParam(IRIS_REQUEST, "IRIS");

        if (type.equals(URL)) {
            requestPackage.setEndPoint(String.format(ENDPOINT, URL));
            requestPackage.setParam("Url", urlText.getText().toString());
        } else if (type.equals(IMAGE)) {
            requestPackage.setEndPoint(String.format(ENDPOINT, IMAGE));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            if (Build.MODEL.contains("x86")) {
                bitmap = ((BitmapDrawable) d).getBitmap();
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
            byte[] byteArray = stream.toByteArray();
            intent.putExtra(IrisService.REQUEST_IMAGE, byteArray);
        }

        requestPackage.setMethod("POST");
        intent.putExtra(IrisService.REQUEST_PACKAGE, requestPackage);
        startService(intent);
    }

    public void takePhoto() {
        clearText();
        if (networkOn) {
            if (Build.MODEL.contains("x86")) {
                requestIrisService(IMAGE);
                progressLoader();
            } else {
                dispatchTakePictureIntent();
            }
        } else {
            Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(bitmap);
        } else if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, "Error Selecting Image", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(new BufferedInputStream(inputStream));
                image.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "Error Selecting Image", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CAMERA_CLICK_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            }
        } else if (requestCode == GALLERY_CLICK_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
        }

    }

    public void getCrop(View v) {
        bitmap = image.getCroppedImage();
        requestIrisService(IMAGE);
        progressLoader();
    }

    public void showNutritionInfo(View v) {
        transitionToNutrition();
    }

    public void transitionToNutrition() {
        Intent nutri = new Intent(this, NutritionActivity.class);
        nutri.putExtra(FOOD_RESULT, food_result.getClass_());
        nutri.putExtra(NUTRITION_RESULT, nutritionixHit);
        startActivity(nutri);
    }

    public void requestNutritionInfo() {
        RequestPackage nutriRequest = new RequestPackage();
        Intent intent = new Intent(this, NutritionixService.class);
        nutriRequest.setEndPoint(String.format(NUTRI_ENDPOINT, food_result.getClass_()));
        nutriRequest.setParam("fields", "item_name,item_id,brand_name,nf_calories,nf_total_fat," +
                "nf_calories_from_fat,nf_saturated_fat,nf_monounsaturated_fat,nf_polyunsaturated_fat," +
                "nf_trans_fatty_acid,nf_cholesterol,nf_sodium,nf_total_carbohydrate,nf_dietary_fiber," +
                "nf_sugars,nf_protein,nf_vitamin_a_dv,nf_vitamin_c_dv,nf_calcium_dv,nf_iron_dv,nf_potassium");
        nutriRequest.setParam("appId", "f7292d95");
        nutriRequest.setParam("appKey", "9927906f82d77be0f0239d5a4dd71179");
        nutriRequest.setMethod("GET");
        intent.putExtra(IrisService.REQUEST_PACKAGE, nutriRequest);
        startService(intent);
    }

    public void openGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select An Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, SELECT_PICTURE);
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
