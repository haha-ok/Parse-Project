/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity {

  Boolean loginmodeactive = false;

  public void redirectIfLoggedIn(){
    if(ParseUser.getCurrentUser()!=null){
      Intent intent = new Intent(getApplicationContext(),UserList.class);
      startActivity(intent);
    }
  }

  public void toggleLogInMode(View view){

    Button b1 = (Button) findViewById(R.id.signupLogIn);
    TextView tv = (TextView) findViewById(R.id.textview);
    if(loginmodeactive){
      loginmodeactive = false;
      b1.setText("Sign Up!");
      tv.setText("Or Log In!");
    }else{
      b1.setText("Log In!");
      tv.setText("Or Sign Up!");
      loginmodeactive = true;

    }
  }

  public void signupLogIn(View view){

    EditText username = (EditText) findViewById(R.id.username1);
    EditText password = (EditText) findViewById(R.id.password1);

    if(loginmodeactive){

      ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
        @Override
        public void done(ParseUser user, ParseException e) {
          if(e == null){
            Log.i("Info","User Logged In");
            redirectIfLoggedIn();
          }else {
            String message = e.getMessage();
            if(message.toLowerCase().contains("java")){
              message = e.getMessage().substring(e.getMessage().indexOf(" "));
            }
            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
          }
        }
      });
    }else{


    ParseUser user = new ParseUser();
    user.setUsername(username.getText().toString());
    user.setPassword(password.getText().toString());

    user.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(ParseException e) {
        if(e == null){
          Log.i("Info","user signed up");
          redirectIfLoggedIn();
        }else {
          String message = e.getMessage();
          if(message.toLowerCase().contains("java")){
            message = e.getMessage().substring(e.getMessage().indexOf(" "));
          }
          Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
        }
      }
    });

  }}


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setTitle("Log In");

    redirectIfLoggedIn();
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}