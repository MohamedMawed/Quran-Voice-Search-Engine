package com.fcis.gp.aya.aya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    TextView AyaText;
    TextView AyaId;
    TextView SurahText;
    TextView SurahId;
    TextView GuzA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        AyaInfo info= (AyaInfo) intent.getSerializableExtra("AyaInfo");
        AyaId = findViewById(R.id.AyaId);
        AyaText = findViewById(R.id.AyaText);
        SurahId = findViewById(R.id.surahId);
        SurahText = findViewById(R.id.surahName);
        GuzA = findViewById(R.id.GuzA);
        if(info.Surah != null){
        AyaText.setText(info.Aya);
        GuzA.setText(String.valueOf(info.GuzA));
        SurahText.setText(info.Surah);
        SurahId.setText(String.valueOf(info.SurahId));
        AyaId.setText(String.valueOf(info.AyaId));
        }else{
            AyaText.setText("No Result");
        }

    }
}
