package com.androidproject19.calendar_reminder_app;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class UserActivity extends AppCompatActivity {
    EditText mEmailEditText;
    EditText mPasswordEditText;
    EditText mNameEditText;
    TextView mSignUpTextView;
    Button mSignUpButton;
    Button mLoginButton;
    String APP_ID;
    String API_KEYS;
    BackendlessUser mBackendlessUser = new BackendlessUser();
    private final String TAG = this.getClass().getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEmailEditText = (EditText)findViewById(R.id.enter_email);
        mPasswordEditText = (EditText)findViewById(R.id.enter_password);
        mNameEditText = (EditText)findViewById(R.id.enter_name);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mSignUpTextView = (TextView) findViewById(R.id.sign_up_text);
        mSignUpButton = (Button)findViewById(R.id.sign_up_button);
        APP_ID = getString(R.string.APP_ID);
        API_KEYS = getString(R.string.API_KEYS);
        Backendless.initApp(this, APP_ID, API_KEYS);

        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSignUpTextView.getText() == getString(R.string.sign_up_button_caption)){
                    mNameEditText.setVisibility(View.GONE);
                    mSignUpButton.setVisibility(View.GONE);
                    mLoginButton.setVisibility(View.VISIBLE);
                    mSignUpTextView.setText("Sign up?");}
                else{
                    mNameEditText.setVisibility(View.VISIBLE);
                    mSignUpButton.setVisibility(View.VISIBLE);
                    mLoginButton.setVisibility(View.GONE);
                    mSignUpTextView.setText(getString(R.string.sign_up_button_caption));
                }
            }
        });
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = mEmailEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();
                String name = mNameEditText.getText().toString().trim();
                if (!userEmail.isEmpty() && !password.isEmpty() && !name.isEmpty()) {
                    mBackendlessUser = new BackendlessUser();
                    mBackendlessUser.setEmail(userEmail);
                    mBackendlessUser.setPassword(password);
                    mBackendlessUser.setProperty("name", name);
                    Backendless.UserService.register(mBackendlessUser, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            Log.i(TAG, "Successful registration for " + response.getEmail());
                            Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                            startActivity(intent);
                        }
                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Log.i(TAG, "The registration has failed " + fault.getMessage());
                        }
                    });
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
                    builder.setMessage(R.string.sign_up_field_error);
                    builder.setTitle(R.string.error_authentication);
                    builder.setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }

        });
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = mEmailEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();
                if (!userEmail.isEmpty() && !password.isEmpty()) {
                    Backendless.UserService.login(userEmail, password, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            Log.i(TAG, "Successful Login for " + response.getEmail());
                            Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                            startActivity(intent);
                        }
                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Log.i(TAG, "Login has failed " + fault.getMessage());
                        }
                    });
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
                    builder.setMessage(R.string.login_field_error);
                    builder.setTitle(R.string.error_login);
                    builder.setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }
}