package com.example.rememberme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView todoList;
    private TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        todoList = (ListView) findViewById(R.id.todoList);
        emptyText = (TextView) findViewById(R.id.emptyText);
        emptyText.setVisibility(View.VISIBLE);
        todoList.setEmptyView(emptyText);
        items = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        todoList.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    public void todoInfo(View view) {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create(); //Read Update
            alertDialog.setTitle(R.string.todoDiagTitle);
            alertDialog.setMessage(getResources().getString(R.string.todoDialog));

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
        newItem.setText("");
    }

}