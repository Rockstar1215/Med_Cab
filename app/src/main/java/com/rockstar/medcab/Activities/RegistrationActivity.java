package com.rockstar.medcab.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rockstar.medcab.R;
import com.rockstar.medcab.Utils.Endpoints;
import com.rockstar.medcab.Utils.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    String[] item={"O+","A+","A-","B+","B-","AB+","AB-","O-"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    private EditText nameEt, cityEt, bloodGroupEt, passwordEt, mobileEt;
    private Button userButton, donorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        autoCompleteTextView=findViewById(R.id.auto_complete_txt);
        adapterItems=new ArrayAdapter<String>(this,R.layout.list_item,item);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(RegistrationActivity.this, "Item:"+item, Toast.LENGTH_SHORT).show();
            }
        });
        nameEt = findViewById(R.id.name);
        cityEt = findViewById(R.id.city);
        passwordEt = findViewById(R.id.password);
        mobileEt = findViewById(R.id.number);
        userButton = findViewById(R.id.register_user_button);
        donorButton = findViewById(R.id.become_donor_button);

        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, city, bloodGroup, password, mobile;
                name = nameEt.getText().toString().trim();
                city = cityEt.getText().toString().trim();
                bloodGroup= autoCompleteTextView.getText().toString().trim();
                password = passwordEt.getText().toString().trim();
                mobile = mobileEt.getText().toString().trim();
                if (isValid(name, city, bloodGroup, password, mobile)) {
                    registerUser(name, city, bloodGroup, password, mobile);
                }
            }
        });

        donorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, city, bloodGroup, password, mobile;
                name = nameEt.getText().toString().trim();
                city = cityEt.getText().toString().trim();
                bloodGroup= autoCompleteTextView.getText().toString().trim();

                password = passwordEt.getText().toString().trim();
                mobile = mobileEt.getText().toString().trim();
                if (isValid(name, city, bloodGroup, password, mobile)) {
                    registerDonor(name, city, bloodGroup, password, mobile);
                }
            }
        });
    }


    private void registerUser(final String name, final String city, final String bloodGroup,
                              final String password, final String mobile) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.register_user_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.equals("Success")) {
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                                    .putString("city", city).apply();
                            Toast.makeText(RegistrationActivity.this, response, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                            RegistrationActivity.this.finish();
                        } else {
                            Toast.makeText(RegistrationActivity.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegistrationActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                        Log.d("VOLLEY", error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("city", city);
                params.put("blood_group", bloodGroup);
                params.put("password", password);
                params.put("number", mobile);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void registerDonor(final String name, final String city, final String bloodGroup,
                               final String password, final String mobile) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.register_donor_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.equals("Success")) {
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                                    .putString("city", city).apply();
                            Toast.makeText(RegistrationActivity.this, response, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                            RegistrationActivity.this.finish();
                        } else {
                            Toast.makeText(RegistrationActivity.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegistrationActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                        Log.d("VOLLEY", error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("city", city);
                params.put("blood_group", bloodGroup);
                params.put("password", password);
                params.put("number", mobile);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private boolean isValid(String name, String city, String bloodGroup, String password, String mobile) {
        List<String> validBloodGroups = new ArrayList<>();
        validBloodGroups.add("A+");
        validBloodGroups.add("A-");
        validBloodGroups.add("B+");
        validBloodGroups.add("B-");
        validBloodGroups.add("AB+");
        validBloodGroups.add("AB-");
        validBloodGroups.add("O+");
        validBloodGroups.add("O-");

        if (name.isEmpty()) {
            showMessage("Name is empty");
            return false;
        } else if (city.isEmpty()) {
            showMessage("City name is required");
            return false;
        } else if (!validBloodGroups.contains(bloodGroup)) {
            showMessage("Invalid blood group, choose from " + validBloodGroups);
            return false;
        } else if (mobile.length() != 10) {
            showMessage("Invalid mobile number, number should be 10 digits");
            return false;
        }

        return true;
    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
