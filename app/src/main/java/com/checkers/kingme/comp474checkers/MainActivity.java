package com.checkers.kingme.comp474checkers;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity
{
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

            /* Attach CheckedChangeListener to radio group */
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton rb = (RadioButton) group.findViewById(checkedId);
                    if(null!=rb && checkedId == 0){
                        Toast.makeText(MainActivity.this, "Let's get started", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

        // Call game activity
        public void sendMessage(View view) {
            Intent intent = new Intent(this, CheckersSystem.class);
            startActivity(intent);
        }
        // Set the visibility of the start button
        public void startVisibility(View view) {
            ImageButton btnStart= (ImageButton) findViewById(R.id.startButton);
            btnStart.setVisibility(view.VISIBLE);
        }
}