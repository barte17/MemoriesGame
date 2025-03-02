package com.example.memorygame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Spinner spinner = findViewById(R.id.spinner_rozmiarPlanszy);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.rozmiarPlanszy, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button zapisz = findViewById(R.id.bZapisz);

        zapisz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String wybranyRozmiar = spinner.getSelectedItem().toString();
                SharedPreferences preferences = getSharedPreferences("MemoryGame", MODE_PRIVATE);
                preferences.edit().putString("rozmiarPlanszy", wybranyRozmiar).apply();
                finish();
            }
        });

    }
}
