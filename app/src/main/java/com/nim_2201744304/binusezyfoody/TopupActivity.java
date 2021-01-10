package com.nim_2201744304.binusezyfoody;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class TopupActivity extends AppCompatActivity {
    Integer balance = 0;
    Integer amount = 0;
    EditText num;
    TextView currentBalance;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);
        Intent intent = getIntent();
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        balance = sp.getInt("balance", 0);
        num = findViewById(R.id.topupAmount);
        currentBalance = findViewById(R.id.currentBalance);
        currentBalance.setText("Current balance: IDR " + balance);
    }

    public void topup(View view) {
//        Intent intent = new Intent(this, TopupActivity.class);
//        Bundle bundle = getIntent().getExtras();
//        intent.putExtra("balance", balance);
//        intent.putExtras(bundle);
        try {
            amount = Integer.parseInt(num.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "Must be filled!", Toast.LENGTH_SHORT).show();
        }

        if (amount > 2000000) {
            Toast.makeText(this, "Can't top up more than IDR 2,000,000", Toast.LENGTH_SHORT).show();
        } else if(amount == 0) {
            Toast.makeText(this, "Must be larger than 0.", Toast.LENGTH_SHORT).show();
        } else {
            balance += amount;
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("balance", balance);
            editor.apply();
            Toast.makeText(this, "Topup successful! New balance is " + balance, Toast.LENGTH_SHORT).show();
            Bundle bundle = getIntent().getExtras();
            bundle.remove("type");
            Intent intent = new Intent(TopupActivity.this, MenuActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    }
}