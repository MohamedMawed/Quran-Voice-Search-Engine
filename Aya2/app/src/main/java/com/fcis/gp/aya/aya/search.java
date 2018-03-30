package com.fcis.gp.aya.aya;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

/**
 * Created by mohse on 2/14/2018.
 */

public class search extends Fragment implements RecognitionListener{

    private SpeechRecognizer quranRecognizer;
    private static final String MODEL = "HolyQuranSpeech";
    private FloatingActionButton RecordBtn;
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private TextView Result;
    private TextView FinalResult;
    Mapper m;
    public View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.search_fragment,container,false);
        RecordBtn = view.findViewById(R.id.recordBtn);
        Result = view.findViewById(R.id.tv_search);
        FinalResult = view.findViewById(R.id.tv_res);
        RecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pulse();
                runRecognizerSetup();
            }
        });
        int permissionCheck = ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);

        }


        return view;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                runRecognizerSetup();
            } else {

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (quranRecognizer != null) {
            quranRecognizer.cancel();
            quranRecognizer.shutdown();
        }
    }

    private void runRecognizerSetup(){
        new AsyncTask<Void,Void,Exception>(){

            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets rAssets = new Assets(view.getContext());

                    File assetsDir = rAssets.syncAssets();

                    setupRecognizer(assetsDir);


                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception e) {
                if(e != null) {
                    Result.setText("هناك مشكلة الرجأء المحاولة لاحقا " + e);
                }else{
                    switchSearch(MODEL);
                }

            }
        }.execute();
    }
    private void setupRecognizer(File assetsDir){
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them
        m = new Mapper(view.getContext());
        SpeechRecognizerSetup recognizerSetup = SpeechRecognizerSetup.defaultSetup();
        recognizerSetup.setAcousticModel(new File(assetsDir, "HolyQuranSpeech.ci_cont"));
        recognizerSetup.setDictionary(new File(assetsDir, "HolyQuranSpeech.dic"));
        recognizerSetup.setRawLogDir(assetsDir); // To disable logging of raw audio comment out this call (takes a lot of space on the device)
        //Didn't seem to has any significant impact
        recognizerSetup.setBoolean("-remove_noise", true);
        recognizerSetup.setKeywordThreshold(1e-1f);

        try {
            quranRecognizer = recognizerSetup.getRecognizer();
            quranRecognizer.addListener(this);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        File languageModel = new File(assetsDir, "HolyQuranSpeech.lm");
        quranRecognizer.addNgramSearch(MODEL, languageModel);

    }
    private void pulse(){
        TextView textView = view.findViewById(R.id.tv_search);
        textView.setText("جاري تجهيز ");
        PulsatorLayout pulsator =(PulsatorLayout)view.findViewById(R.id.pulsator);
        pulsator.start();
    }
    @Override
    public void onBeginningOfSpeech() {
        Result.setText("من فضلك اقرأ اية كاملة");

    }
    @Override
    public void onEndOfSpeech() {

        switchSearch(MODEL);
    }
    private void switchSearch(String SearchName){
        quranRecognizer.stop();

        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
        quranRecognizer.startListening(SearchName, 10000);

     //   Result.setText("You can recite now!");
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;
        Log.i("ResultForNow",Result.getText().toString());

        if(hypothesis.getHypstr().split(" ").length > 1)
        {

            Intent intent = new Intent(view.getContext(),ResultActivity.class);
            intent.putExtra("AyaInfo",m.arabicResult(hypothesis.getHypstr()));
            startActivity(intent);
        }
    }
    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        return;
    }
    @Override
    public void onError(Exception e) {
        Log.i("info", "onError: here");
                // ((TextView)view.findViewById(R.id.tv_search)).setText("error Happens");
        Result.setText(e.getMessage());
    }
    @Override
    public void onTimeout() {
        switchSearch(MODEL);
    }
}

