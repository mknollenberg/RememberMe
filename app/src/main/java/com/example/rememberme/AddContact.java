package com.example.rememberme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddContact extends Activity {

    private EditText name,bDay,addr1,addr2,city,state,zip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        this.setFinishOnTouchOutside(false);
        name = (EditText) findViewById(R.id.name);
        bDay = (EditText) findViewById(R.id.birthday);
        addr1 = (EditText) findViewById(R.id.addr1);
        addr2 = (EditText) findViewById(R.id.addr2);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        zip = (EditText) findViewById(R.id.zipCode);
    }

    public void onCancel(View view) {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    public void onAdd(View view) {
        String contactName = name.getText().toString();
        String contactBDay = bDay.getText().toString();
        String contactAddr1 = addr1.getText().toString();
        String contactAddr2 = addr2.getText().toString();
        String contactCity = city.getText().toString();
        String contactState = state.getText().toString();
        String contactZip = zip.getText().toString();
        Intent i = new Intent();
        i.putExtra("name",contactName);
        i.putExtra("bday",contactBDay);
        i.putExtra("addr1",contactAddr1);
        i.putExtra("addr2",contactAddr2);
        i.putExtra("city",contactCity);
        i.putExtra("state",contactState);
        i.putExtra("zip",contactZip);
        setResult(Activity.RESULT_OK,i);
        finish();
    }
}