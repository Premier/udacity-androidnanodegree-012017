package com.alessiogrumiro.udacity.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SettingsActivity";
    public static final String EXTRA_APIKEY = "apikey";

    private EditText mApiKeyView;
    private Button mConfirmButton;
    private Toast mToast;

    public static Intent newIntent(Context context) {
        return newIntent(context, null);
    }

    public static Intent newIntent(Context context, String apikey) {
        Intent i = null;
        try {
            i = new Intent(context, SettingsActivity.class);
            i.putExtra(EXTRA_APIKEY, apikey);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mApiKeyView = (EditText) findViewById(android.R.id.edit);
        mConfirmButton = (Button) findViewById(android.R.id.button1);
        mConfirmButton.setOnClickListener(this);

        Intent data = getIntent();
        if (data != null && data.hasExtra(EXTRA_APIKEY)) {
            mApiKeyView.setText(data.getStringExtra(EXTRA_APIKEY));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mApiKeyView.requestFocus();
    }

    @Override
    public void onClick(View v) {
        String apikey = mApiKeyView.getText().toString();
        if (TextUtils.isEmpty(apikey)) {
            if (mToast != null) mToast.cancel();
            mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
            mToast.show();
        } else {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_APIKEY, apikey);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}
