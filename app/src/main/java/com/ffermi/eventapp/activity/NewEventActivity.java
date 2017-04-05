package com.ffermi.eventapp.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import com.ffermi.eventapp.R;
import com.ffermi.eventapp.fragment.DatePickerFragment;
import com.ffermi.eventapp.fragment.TimePickerFragment;
import com.ffermi.eventapp.model.EventMeta;
import com.ffermi.eventapp.model.EventShort;
import com.ffermi.eventapp.utils.Commons;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class NewEventActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerFragment.OnTimeSetListener{

    private static final String TAG = "_NewEventActivity";

    // UI widgets.
    private ImageButton mImageButton;
    private Button mStartDate;
    private Button mStartTime;
    private Button mEndDate;
    private Button mEndTime;
    private Switch mPrivateEvent;
    private EditText mTitile;
    private EditText mDescription;
    private EditText mHashtag;

    private Calendar startDate;
    private Calendar endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: se giro lo schermo mi si azzera tutto
        super.onCreate(savedInstanceState);
        setTitle(R.string.new_event);
        setContentView(R.layout.activity_new_event);

        // Locate the UI widgets.
        mImageButton = (ImageButton) findViewById(R.id.btn_add_image);
        mStartTime = (Button) findViewById(R.id.start_time);
        mStartDate = (Button) findViewById(R.id.start_date);
        mEndTime = (Button) findViewById(R.id.end_time);
        mEndDate = (Button) findViewById(R.id.end_date);
        mPrivateEvent = (Switch) findViewById(R.id.btn_private);
        mTitile = (EditText) findViewById(R.id.new_title);
        mDescription = (EditText) findViewById(R.id.new_description);
        mHashtag = (EditText) findViewById(R.id.new_hashtag);

        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

    }

    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        // TODO: restore values
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                if(onEventSet()) {
                    //TODO: save to database
                    onBackPressed();
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showTimePickerDialog(View v) {
        TimePickerFragment tpFragment = new TimePickerFragment();
        switch (v.getId()){
            case R.id.start_time:
                tpFragment.show(getSupportFragmentManager(), Commons.TAG_START_TIME );
                break;
            case R.id.end_time:
                tpFragment.show(getSupportFragmentManager(), Commons.TAG_END_TIME);
                break;
            default:
                break;
        }
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment dpFragment = new DatePickerFragment();
        switch (v.getId()){
            case R.id.start_date:
                dpFragment.show(getSupportFragmentManager(), Commons.TAG_START_DATE);
                break;
            case R.id.end_date:
                dpFragment.show(getSupportFragmentManager(), Commons.TAG_END_DATE);
                break;
            default:
                break;
        }
    }

    /**
     * Tries to create a new event
     *
     * @return true if it succeed
     */
    public boolean onEventSet() {
        boolean result = false;

        // check data && if ok save photo && upload event
        if (endDate.after(startDate)) {
            String userId = FirebaseAuth.getInstance().getCurrentUser()
                    .getProviderData().get(1).getProviderId();

            EventShort eventShort = new EventShort(
                    mTitile.getText().toString(), "photoUri",
                    startDate.getTime().getTime(), endDate.getTime().getTime(),
                    "locName", "locAddr", 45.4781, 9.2273, mPrivateEvent.isChecked(),
                    userId);
            EventMeta eventMeta = new EventMeta(
                    mDescription.getText().toString(),
                    mHashtag.getText().toString(),
                    null);

            result = true;
        }
        return result;
    }

    public void addPicture(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, Commons.REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (view.getTag().equals(Commons.TAG_START_DATE)) {
            mStartDate.setText(dayOfMonth + "/" + month + "/" + year);
            startDate.set(Calendar.YEAR, year);
            startDate.set(Calendar.MONTH, month);
            startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        } else {
            mEndDate.setText(dayOfMonth + "/" + month + "/" + year);
            endDate.set(Calendar.YEAR, year);
            endDate.set(Calendar.MONTH, month);
            endDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }
    }

    @Override
    public void onTimeSet(int hourOfDay, int minute, String tag) {
        String min = String.valueOf(minute);
        if (minute < 10) {
            min = "0" + min;
        }
        if (tag.equals(Commons.TAG_START_TIME)) {
            mStartTime.setText(hourOfDay + ":" + min);
            startDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            startDate.set(Calendar.MINUTE, minute);
        } else {
            mEndTime.setText(hourOfDay + ":" + min);
            endDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            endDate.set(Calendar.MINUTE, minute);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Commons.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Drawable imageDrawable = new BitmapDrawable(getResources(),imageBitmap);
            mImageButton.setBackground(imageDrawable);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        // TODO: save the state (on order to prevent flipscreen to reset the view)
    }
}
