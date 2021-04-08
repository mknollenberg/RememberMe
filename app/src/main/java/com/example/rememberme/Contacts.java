package com.example.rememberme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Contacts extends AppCompatActivity {

    private static final int LAUNCH_SECOND_ACTIVITY = 1;
    private static final String CONTACTS_WRITE_ERROR = "Contacts_contactsWrite";
    private static final String CONTACTS_READ_ERROR = "Contacts_contactsRead";
    private ArrayList<Person> contacts;
    private ArrayList<String> contactNames;
    private ArrayAdapter<String> contactAdapter;
    private ListView contactList;
    private TextView emptyContacts;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        if (path == null){
            path = Contacts.this.getFilesDir().getAbsolutePath();
        }

        contactList = (ListView) findViewById(R.id.contactList);
        emptyContacts = (TextView) findViewById(R.id.emptyContacts);
        contactList.setEmptyView(emptyContacts);
        contacts = new ArrayList<Person>();
        contactNames = new ArrayList<String>();
        contactAdapter = new ArrayAdapter<String>(this,
                R.layout.mytextview, contactNames);
        contactList.setAdapter(contactAdapter);
        setupListViewListener();
    }

    // Attaches a click listener to the listview
    private void setupListViewListener() {
        contactList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        // View contact within array at position
                        Person viewedContact = contacts.get(pos);
                        String name = viewedContact.getName();
                        String addr1 = viewedContact.getAddressLineOne();
                        String addr2 = viewedContact.getAddressLineTwo();
                        String city = viewedContact.getCity();
                        String state = viewedContact.getState();
                        String zipCode = viewedContact.getZipCode();
                        String contactAddr = String.format("%s\n%s\n%s, %s, %s\n",addr1,addr2,city,state,zipCode);
                        AlertDialog.Builder builder = new AlertDialog.Builder(Contacts.this);
                        builder.setNegativeButton("Delete Contact", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // user has chosen to delete contact
                                contacts.remove(pos);
                                contactNames.remove(pos);
                                // Refresh the adapter
                                contactAdapter.notifyDataSetChanged();
                            }
                        });
                        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // user is done viewing contact
                            }
                        });
                        builder.setTitle(name);
                        builder.setMessage("Birthday: " + viewedContact.getBDay() + "\n\nAddress:\n" + contactAddr);
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                });
    }

    public void contactInfo(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(Contacts.this).create(); //Read Update
        alertDialog.setTitle(Contacts.this.getResources().getString(R.string.contactDiagTitle));
        alertDialog.setMessage(Contacts.this.getResources().getString(R.string.contactDialog));

        alertDialog.setButton("Got it!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // add functions here if necessary
            }
        });
        alertDialog.show();
    }

    public void toToDoList(View view) {
        Intent i = new Intent(Contacts.this,MainActivity.class);
        startActivity(i);
    }

    //  https://stackoverflow.com/questions/1979369/android-activity-as-a-dialog
    //  https://stackoverflow.com/questions/10407159/how-to-manage-startactivityforresult-on-android
    public void onAddContact(View view) {
        Intent i = new Intent(Contacts.this, AddContact.class);
        startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                String contactName = data.getStringExtra("name");
                String contactBDay = data.getStringExtra("bday");
                String contactAddr1 = data.getStringExtra("addr1");
                String contactAddr2 = data.getStringExtra("addr2");
                String contactCity = data.getStringExtra("city");
                String contactState = data.getStringExtra("state");
                String contactZip = data.getStringExtra("zip");
                Person contact = new Person(contactName,contactBDay,contactAddr1,contactAddr2,contactCity,contactState,contactZip);
                contacts.add(contact);
                contactAdapter.add(contactName);
                contactAdapter.notifyDataSetChanged();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    // app specific storage following help from https://developer.android.com/training/data-storage/app-specific
    @Override
    protected void onPause() {
        super.onPause();
        String filename = "contact-list";

        // ArrayList<Person> to byte[] based on https://stackoverflow.com/questions/5618978/convert-arrayliststring-to-byte
        // write to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        for (Person person : contacts) {
            try {
                out.writeUTF(person.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e)
            {
                e.printStackTrace();
            }
        }
        byte[] bytes = baos.toByteArray();

        try (FileOutputStream fos = Contacts.this.openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(bytes);
        } catch (IOException e) {
            Log.e(CONTACTS_WRITE_ERROR,"error when writing contact list to file");
        }
    }

    // app specific storage following help from https://developer.android.com/training/data-storage/app-specific
    @Override
    protected void onStart() {
        super.onStart();
        contactAdapter.clear();
        contacts.clear();

        // read from byte array
        try {
            byte[] fileContent = Files.readAllBytes(Paths.get(path + "/contact-list"));
            ByteArrayInputStream bais = new ByteArrayInputStream(fileContent);
            DataInputStream in = new DataInputStream(bais);
            while (in.available() > 0) {
                String[] temp = new String[7];
                String contactAsString = in.readUTF();
                int count = 0;
                for (String element : contactAsString.split(",",7))
                {
                    temp[count]=element;
                    Log.i(CONTACTS_READ_ERROR,"temp[0] = " + temp[0]);
                    count++;
                }
                Person contact = new Person(temp[0],temp[1],temp[2],temp[3],temp[4],temp[5],temp[6]);
                contactAdapter.add(contact.getName());
                contacts.add(contact);
            }
        } catch (IOException e) {
            Log.e(CONTACTS_READ_ERROR,"error when reading contact list from file");
        } catch (IndexOutOfBoundsException a)
        {
            Log.e(CONTACTS_READ_ERROR,"index out of bounds");
        }
    }
}