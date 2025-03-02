package com.example.memorygame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    private int wiersze, kolumny;
    private List<Integer> zdjeciaKart;
    private Button pierwszaKarta = null; // Pierwsza kliknięta karta
    private boolean blokerKlikania = false; // Blokada klikania podczas odkrywania kart
    private int count = 0;
    public int liczbaKart;

    private TextView timerTextView;
    private Handler timerHandler = new Handler();
    private int elapsedTime = 0;
    private Runnable timerRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        timerTextView = findViewById(R.id.timerTextView);

        Button bPowrot = findViewById(R.id.buttonPowrot);

        bPowrot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences preferences = getSharedPreferences("MemoryGame", MODE_PRIVATE);
        String rozmiarPlanszy = preferences.getString("rozmiarPlanszy", "2x3");
        String[] size = rozmiarPlanszy.split("x");
        wiersze = Integer.parseInt(size[0]);
        kolumny = Integer.parseInt(size[1]);
        liczbaKart = (wiersze * kolumny);

        System.out.println("rows: " + wiersze + " cols: " + kolumny);
        ustawieniePlanszy(wiersze, kolumny);
        uruchomLicznikCzasu();
    }

    private void uruchomLicznikCzasu() {
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                elapsedTime++;
                int minuty = elapsedTime / 60;
                int sekundy = elapsedTime % 60;
                String time = String.format("%02d:%02d", minuty, sekundy);
                timerTextView.setText(time);

                // Ponowne uruchomienie po 1 sekundzie
                timerHandler.postDelayed(this, 1000);
            }
        };
        timerHandler.postDelayed(timerRunnable, 1000);
    }

    private void ustawieniePlanszy(int wiersze, int kolumny) {
        GridLayout gridLayout = findViewById(R.id.layoutGry);
        gridLayout.setRowCount(wiersze);
        gridLayout.setColumnCount(kolumny);

        zdjeciaKart = wybierzKarty(wiersze * kolumny);

        // Tworzenie kart
        for (int i = 0; i < wiersze * kolumny; i++) {
            Button karta = new Button(this);
            if(wiersze == 5) {
                karta.setBackgroundResource(R.drawable.card_back_64); // Tył karty
                karta.setTag(zdjeciaKart.get(i)); // Zapisz obrazek w tagu
            } else {
                karta.setBackgroundResource(R.drawable.card_back); // Tył karty
                karta.setTag(zdjeciaKart.get(i)); // Zapisz obrazek w tagu
            }

            karta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (blokerKlikania) {
                        System.out.println("Poczekaj!");
                        return;
                    }
                    klikniecieKarty(karta);
                }
            });

            gridLayout.addView(karta, new GridLayout.LayoutParams());
        }
    }

    private List<Integer> wybierzKarty(int potrzebneKarty) {

        List<Integer> zdjeciaKart = new ArrayList<>();
        if(wiersze == 5) {
            zdjeciaKart.add(R.drawable.card1_64);
            zdjeciaKart.add(R.drawable.card2_64);
            zdjeciaKart.add(R.drawable.card3_64);
            zdjeciaKart.add(R.drawable.card4_64);
            zdjeciaKart.add(R.drawable.card5_64);
            zdjeciaKart.add(R.drawable.card6_64);
            zdjeciaKart.add(R.drawable.card7_64);
            zdjeciaKart.add(R.drawable.card8_64);
            zdjeciaKart.add(R.drawable.card9_64);
            zdjeciaKart.add(R.drawable.card10_64);
        } else {
            zdjeciaKart.add(R.drawable.card1);
            zdjeciaKart.add(R.drawable.card2);
            zdjeciaKart.add(R.drawable.card3);
            zdjeciaKart.add(R.drawable.card4);
            zdjeciaKart.add(R.drawable.card5);
            zdjeciaKart.add(R.drawable.card6);
            zdjeciaKart.add(R.drawable.card7);
            zdjeciaKart.add(R.drawable.card8);
            zdjeciaKart.add(R.drawable.card9);
            zdjeciaKart.add(R.drawable.card10);
        }

        List<Integer> wybraneKarty = zdjeciaKart.subList(0, potrzebneKarty / 2);

        List<Integer> zdjeciaWybranychKart = new ArrayList<>(wybraneKarty);
        zdjeciaWybranychKart.addAll(wybraneKarty);
        Collections.shuffle(zdjeciaWybranychKart);

        return zdjeciaWybranychKart;
    }

    private void klikniecieKarty(Button karta) {
        blokerKlikania = true;
        int idObrazka = (int) karta.getTag();
        karta.setBackgroundResource(idObrazka);

        if (pierwszaKarta == null) {
            pierwszaKarta = karta;
            blokerKlikania = false;
        } else {
            if (pierwszaKarta.getTag().equals(karta.getTag())) {
                // Karty pasują
                pierwszaKarta.setEnabled(false);
                karta.setEnabled(false);
                pierwszaKarta = null;
                count++;
                blokerKlikania = false;
                if (count == liczbaKart / 2) {
                    timerHandler.removeCallbacks(timerRunnable); // Zatrzymaj licznik
                    Toast toast = Toast.makeText(getApplicationContext(), "KONIEC", Toast.LENGTH_LONG);
                    toast.show();
                }

            } else {
                // Karty nie pasują
                blokerKlikania = true;

                // Żeby karty od razu nie znikały
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pierwszaKarta.setBackgroundResource(R.drawable.card_back);
                        karta.setBackgroundResource(R.drawable.card_back);
                        pierwszaKarta = null;
                        blokerKlikania = false;
                    }
                }, 1500);

            }
        }
    }
}