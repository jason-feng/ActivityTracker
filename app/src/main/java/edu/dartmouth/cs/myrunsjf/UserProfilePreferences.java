package edu.dartmouth.cs.myrunsjf;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class UserProfilePreferences extends Activity {

    public static final String TAG = "UserProfilePreferences.java";

    public static final int REQUEST_CODE_TAKE_FROM_CAMERA = 0;
    public static final int REQUEST_CODE_CROP_PHOTO = 2;
    public static final int REQUEST_CODE_TAKE_FROM_GALLERY = 3;

    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final String URI_INSTANCE_STATE_KEY = "saved_uri";
    private static final String IMAGE_BITMAP = "image_bit_map";

    private Uri ImageCaptureUri;
    private ImageView profile_image;
    private boolean isTakenFromCamera;
    public String selectedImagePath;
    private Bitmap profileBitMap;

    // temp buffer for storing the image
    private byte[] mProfilePictureArray;

    private Button save_button;
    private Button cancel_button;
    private Button change_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_preferences);

        save_button = (Button)findViewById(R.id.save_button);
        cancel_button = (Button)findViewById(R.id.cancel_button);
        change_button = (Button)findViewById(R.id.change_button);
        profile_image = (ImageView)findViewById(R.id.profile_image);

        if (savedInstanceState != null) {
            ImageCaptureUri = savedInstanceState.getParcelable(URI_INSTANCE_STATE_KEY);
            profileBitMap = savedInstanceState.getParcelable(IMAGE_BITMAP);
        }

        loadUserData();
        loadImage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        // Save the image capture uri before the activity goes into background
        outState.putParcelable(URI_INSTANCE_STATE_KEY, ImageCaptureUri);
        outState.putParcelable(IMAGE_BITMAP, profileBitMap);

        super.onSaveInstanceState(outState);
    }

    @Override
    /**
     * There are two cases here - One for croping the image after taking the image. The other for
     * updating the image view after finishing the image crop.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {

            case REQUEST_CODE_TAKE_FROM_GALLERY:
                Log.d(TAG, "onActivityResult entering gallery");
                Uri selectedImageUri = data.getData();
                selectedImagePath = getRealPathFromURI(selectedImageUri);
                Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                profileBitMap = bitmap;
                profile_image.setImageBitmap(bitmap);
                break;

            case REQUEST_CODE_TAKE_FROM_CAMERA:
                Log.d(TAG, "onActivityResult entering take camera");
                // Send image taken from camera for cropping
                cropImage();
                break;

            case REQUEST_CODE_CROP_PHOTO:
                // Update image view after image crop
                Bundle extras = data.getExtras();
                if (extras != null) {
                    profileBitMap = extras.getParcelable("data");
                    profile_image.setImageBitmap((Bitmap) extras.getParcelable("data"));
                    Log.d(TAG, "REQUEST CODE CROP PHOTO");
                }

                // Delete temporary image taken by camera after crop.
                if (isTakenFromCamera) {
                    File file = new File(ImageCaptureUri.getPath());
                    if (file.exists())
                        file.delete();
                }

                break;
        }
    }

    // ****************** button click callbacks ***************************//

    public void onUserProfileSaveClicked(View v) {
        saveUserData();
        saveImage();
        // Making a "toast" informing the user the profile is saved.
        Toast.makeText(getApplicationContext(),
                getString(R.string.ui_profile_toast_save_text),
                Toast.LENGTH_SHORT).show();
        // Close the activity
        finish();
    }

    public void onUserProfileCancelClicked(View v) {
        // Making a "toast" informing the user changes are canceled.
        Toast.makeText(getApplicationContext(),
                getString(R.string.ui_profile_toast_cancel_text),
                Toast.LENGTH_SHORT).show();
        finish();
    }

    public void onChangeClicked(View v) {
        displayDialog(PhotoPickerFragment.DIALOG_ID_PHOTO_PICKER);
    }

    public void displayDialog(int id) {
        DialogFragment fragment = PhotoPickerFragment.newInstance(id);
        fragment.show(getFragmentManager(), getString(R.string.dialog_fragment_tag_photo_picker));
    }

    // ****************** private helper functions ***************************//

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = new String[] { android.provider.MediaStore.Images.ImageColumns.DATA };

        Cursor cursor = getContentResolver().query(contentUri, proj, null,
                null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        String filename = cursor.getString(column_index);
        cursor.close();

        return filename;
    }
    /**
     * Takes photo from carea. Uses a temporary image path to save the photo.
     * Uses a REQUEST_CODE to idetnify the activity in `onActivityResult()
     */
    public void changeImage(int item) {
        Intent intent;

        switch (item) {

            case PhotoPickerFragment.ID_PHOTO_PICKER_FROM_CAMERA:
                // Take photo from camera，
                // Construct an intent with action
                // MediaStore.ACTION_IMAGE_CAPTURE
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Construct temporary image path and name to save the taken
                // photo
                ImageCaptureUri = Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "tmp_"
                        + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageCaptureUri);
                intent.putExtra("return-data", true);
                try {
                    // Start a camera capturing activity
                    // REQUEST_CODE_TAKE_FROM_CAMERA is an integer tag you
                    // defined to identify the activity in onActivityResult()
                    // when it returns
                    startActivityForResult(intent, REQUEST_CODE_TAKE_FROM_CAMERA);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                isTakenFromCamera = true;
                break;

            case PhotoPickerFragment.ID_PHOTO_PICKER_FROM_LIBRARY:
                Log.d(TAG, "entering photo picker from library");
                intent = new Intent(Intent.ACTION_PICK);
                intent.setType(IMAGE_UNSPECIFIED);
                startActivityForResult(intent, REQUEST_CODE_TAKE_FROM_GALLERY);
                break;

            default:
                return;
        }
    }

    /**
     * Loads a profile photo from storage. If there is no photo stored, load the default image.
     */
    private void loadImage() {
        Log.d(TAG, "loadImage()");
        try {
            if (profileBitMap != null) {
                Log.d(TAG, "Save photo in ByteArray to profile_photo.png");

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                profileBitMap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                mProfilePictureArray = bos.toByteArray();
                profile_image.setImageBitmap(profileBitMap);
                bos.close();
            }
            else {
                Log.d(TAG, "load profile image");
                FileInputStream fis = openFileInput(getString(R.string.profile_image_file_name));
                Bitmap bmap = BitmapFactory.decodeStream(fis);
                profile_image.setImageBitmap(bmap);
                fis.close();
            }
        } catch (IOException e) {
            profile_image.setImageResource(R.drawable.andrew_campbell);
        }
    }

    /**
     * Save the profile image
     */
    private void saveImage() {

        profile_image.buildDrawingCache();
        Bitmap bmap = profile_image.getDrawingCache();
        try {
            FileOutputStream fos = openFileOutput(
                    getString(R.string.profile_image_file_name), MODE_PRIVATE);
            bmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Crop and resize the image.
     * Includes a REQUEST_CODE_CROP_PHOTO tag to identify the activity in onActivityResult()
     */
    private void cropImage() {
        // Use existing crop activity.
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(ImageCaptureUri, IMAGE_UNSPECIFIED);

        // Specify image size
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);

        // Specify aspect ratio, 1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, REQUEST_CODE_CROP_PHOTO);
    }

    /**
     * Load the user data from preferences
     */
    private void loadUserData() {

        // Get the shared preferences - create or retrieve the activity preference object

        String mKey = getString(R.string.preference_name);
        SharedPreferences mPrefs = getSharedPreferences(mKey, MODE_PRIVATE);

        // Load the name information

        mKey = getString(R.string.preference_key_profile_name);
        String name = mPrefs.getString(mKey, " ");
        ((EditText) findViewById(R.id.name_field)).setText(name);

        // Load the user email

        mKey = getString(R.string.preference_key_profile_email);
        String email = mPrefs.getString(mKey, " ");
        ((EditText) findViewById(R.id.email_field)).setText(email);

        // Load the user phone

        mKey = getString(R.string.preference_key_profile_phone);
        String phone = mPrefs.getString(mKey, " ");
        ((EditText) findViewById(R.id.phone_field)).setText(phone);

        // Please Load gender info and set radio box
        mKey = getString(R.string.preference_key_profile_gender);

        int mIntValue = mPrefs.getInt(mKey, -1);
        // In case there isn't one saved before:
        if (mIntValue >= 0) {
            // Find the radio button that should be checked.
            RadioButton radioBtn = (RadioButton) ((RadioGroup) findViewById(R.id.gender_group))
                    .getChildAt(mIntValue);
            // Check the button.
            radioBtn.setChecked(true);
        }

        // Load the user class year

        mKey = getString(R.string.preference_key_profile_class_year);
        String class_year = mPrefs.getString(mKey, " ");
        ((EditText) findViewById(R.id.class_field)).setText(class_year);

        // Load the user major

        mKey = getString(R.string.preference_key_profile_major);
        String major = mPrefs.getString(mKey, " ");
        ((EditText) findViewById(R.id.major_field)).setText(major);
    }

    /**
     * Save the user data
     */
    private void saveUserData() {

        // Getting the shared preferences editor

        String mKey = getString(R.string.preference_name);
        SharedPreferences mPrefs = getSharedPreferences(mKey, MODE_PRIVATE);

        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.clear();

        // Save name information

        mKey = getString(R.string.preference_key_profile_name);
        String name = (String) ((EditText) findViewById(R.id.name_field))
                .getText().toString();
        mEditor.putString(mKey, name);

        // Save email information

        mKey = getString(R.string.preference_key_profile_email);
        String email = (String) ((EditText) findViewById(R.id.email_field))
                .getText().toString();
        mEditor.putString(mKey, email);

        // Save phone information

        mKey = getString(R.string.preference_key_profile_phone);
        String phone = (String) ((EditText) findViewById(R.id.phone_field))
                .getText().toString();
        mEditor.putString(mKey, phone);

        // Save gender information

        mKey = getString(R.string.preference_key_profile_gender);

        RadioGroup mRadioGroup = (RadioGroup) findViewById(R.id.gender_group);
        int mIntValue = mRadioGroup.indexOfChild(findViewById(mRadioGroup
                .getCheckedRadioButtonId()));
        mEditor.putInt(mKey, mIntValue);

        // Save class year information

        mKey = getString(R.string.preference_key_profile_class_year);
        String class_year = (String) ((EditText) findViewById(R.id.class_field))
                .getText().toString();
        mEditor.putString(mKey, class_year);

        // Save major information

        mKey = getString(R.string.preference_key_profile_major);
        String major = (String) ((EditText) findViewById(R.id.major_field))
                .getText().toString();
        mEditor.putString(mKey, major);

        // Commit all the changes into the shared preference
        mEditor.commit();
    }
}
