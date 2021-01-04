package com.example.demo.planewars;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class HelloActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        Button startBtn = (Button) findViewById(R.id.btn_start);
        startBtn.setOnClickListener(this);

        Button quitBtn = (Button) findViewById(R.id.btn_quit);
        quitBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.btn_quit:
                finish();
                break;
            default:
                break;
        }
    }
}
