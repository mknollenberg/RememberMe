package com.example.rememberme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// to do list made following tutorial at https://guides.codepath.com/android/Basic-Todo-App-Tutorial
public class MainActivity extends AppCompatActivity {

    private static final String ITEM_FILE_WRITE_ERROR = "MainActivity_itemsWrite";
    private static final String ITEM_FILE_READ_ERROR = "MainActivity_itemsRead";
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView todoList;
    private TextView emptyText;
    private String path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (path == null){
            path = MainActivity.this.getFilesDir().getAbsolutePath();
        }

        setContentView(R.layout.activity_main);
        todoList = (ListView) findViewById(R.id.todoList);
        emptyText = (TextView) findViewById(R.id.emptyText);
        todoList.setEmptyView(emptyText);
        items = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        todoList.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    // dialog help from https://developer.android.com/guide/topics/ui/dialogs
    public void todoInfo(View view) {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create(); //Read Update
            alertDialog.setTitle(MainActivity.this.getResources().getString(R.string.todoDiagTitle));
            alertDialog.setMessage(MainActivity.this.getResources().getString(R.string.todoDialog));

            alertDialog.setButton("Got it!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // add functions here if necessary
                }
            });
            alertDialog.show();
    }

    // Attaches a long click listener to the listview
    private void setupListViewListener() {
        todoList.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        // Remove the item within array at position
                        items.remove(pos);
                        // Refresh the adapter
                        itemsAdapter.notifyDataSetChanged();
                        // Return true consumes the long click event (marks it handled)
                        return true;
                    }

                });
    }

    // button adds item to list
    public void onAddItem(View v) {
        EditText newItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = newItem.getText().toString();
        itemsAdapter.add(itemText);
        itemsAdapter.notifyDataSetChanged();
        newItem.setText("");
    }

    // app specific storage following help from https://developer.android.com/training/data-storage/app-specific
    @Override
    protected void onStop() {
        super.onStop();
        String filename = "todo-items";

        // ArrayList<String> to byte[] from https://stackoverflow.com/questions/5618978/convert-arrayliststring-to-byte
        // write to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        for (String element : items) {
            try {
                out.writeUTF(element);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byte[] bytes = baos.toByteArray();

        try (FileOutputStream fos = MainActivity.this.openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(bytes);
        } catch (IOException e) {
            Log.e(ITEM_FILE_WRITE_ERROR,"error when writing item list to file");
        }
    }

    // app specific storage following help from https://developer.android.com/training/data-storage/app-specific
    @Override
    protected void onStart() {
        super.onStart();
        itemsAdapter.clear();

        // read from byte array
        try {
            byte[] fileContent = Files.readAllBytes(Paths.get(path + "/todo-items"));
            ByteArrayInputStream bais = new ByteArrayInputStream(fileContent);
            DataInputStream in = new DataInputStream(bais);
            while (in.available() > 0) {
                String element = in.readUTF();
                itemsAdapter.add(element);
            }
        } catch (IOException e) {
            Log.e(ITEM_FILE_READ_ERROR,"error when reading item list from file");
        }
    }

    public void toContacts(View view) {
        Intent i = new Intent(MainActivity.this,Contacts.class);
        startActivity(i);
    }
}