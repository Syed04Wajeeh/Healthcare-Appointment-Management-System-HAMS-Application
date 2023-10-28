package com.example.hamsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Inbox extends AppCompatActivity {
    Button back;
    TableLayout layout;
    TableLayout layoutRej;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);



        layout  = (TableLayout) findViewById(R.id.buttonContainerReq);
        layoutRej = (TableLayout) findViewById(R.id.buttonContainerRej);

        back = (Button) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Inbox.this, welcomeScreenAdmin.class);
                startActivity(intent);
            }
        });

        TableRow newRow = new TableRow(this);
        TextView inboxText = new TextView(this);
        inboxText.setText("INBOX\n");
        newRow.addView(inboxText);
        layout.addView(newRow);

        TableRow newRowr = new TableRow(this);
        TextView inboxTextr = new TextView(this);
        inboxTextr.setText("REJECTED\n");
        newRowr.addView(inboxTextr);
        layoutRej.addView(newRowr);

        for (generalInformation info : generalInformation.collection) {
            if (info.getStatus() == 0){

                TableRow row = new TableRow(this);
                TextView text = new TextView(this);
                Button button = new Button(this);
                Button button1 = new Button(this);

                text.setText(info.username);
                //TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
                //text.setLayoutParams(params);
                button.setText("REJECT");
                button.setBackgroundColor(Color.RED);
                button1.setText("ACCEPT");
                button1.setBackgroundColor(Color.GREEN);

                row.addView(text);
                text.setId(View.generateViewId());
                button.setId(View.generateViewId());
                button1.setId(View.generateViewId());

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        info.setStatus(2);
                        button.setEnabled(false);
                        button1.setEnabled(false);
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        info.setStatus(1);
                        button.setEnabled(false);
                        button1.setEnabled(false);
                    }
                });

                // Add the button to the container

                row.addView(button);
                row.addView(button1);
                layout.addView(row);

            }else if (info.getStatus() == 2){
                TableRow row = new TableRow(this);
                TextView text = new TextView(this);
                Button button = new Button(this);
                text.setText(info.username);

                button.setText("ACCEPT");
                button.setBackgroundColor(Color.GREEN);

                row.addView(text);
                text.setId(View.generateViewId());
                button.setId(View.generateViewId());


                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        info.setStatus(1);
                        button.setEnabled(false);
                    }
                });

                // Add the button to the container

                row.addView(button);
                layoutRej.addView(row);
            }

        }



    }
}