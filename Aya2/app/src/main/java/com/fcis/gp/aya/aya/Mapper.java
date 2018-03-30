package com.fcis.gp.aya.aya;

import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;

import edu.cmu.pocketsphinx.Assets;

/**
 * Created by mohamed on 30/03/18.
 */

public class Mapper {
    private LinkedList<String> EnglishCorpus;
    private LinkedList<String> ArabicCorpus;
    private LinkedList<Pair<String , Integer> >SurahCount;
    private int begin = 112;
    public Mapper(Context con)
    {
        EnglishCorpus = new LinkedList<>();
        ArabicCorpus = new LinkedList<>();
        SurahCount = new LinkedList<>();
        try {
            BufferedReader EnglishQuranReader = new BufferedReader(new InputStreamReader(con.getAssets().open("HolyQuranSpeech.sent")));
            BufferedReader ArabicQuranReader = new BufferedReader(new InputStreamReader(con.getAssets().open("HolyQuranSpeechArabic.sent")));
            BufferedReader SurahQuranReader = new BufferedReader(new InputStreamReader(con.getAssets().open("surah.sent")));
            while (EnglishQuranReader.ready())
            {
                EnglishCorpus.add(EnglishQuranReader.readLine());
                ArabicCorpus.add(ArabicQuranReader.readLine());
            }
            while (SurahQuranReader.ready())
            {
                String[] input = SurahQuranReader.readLine().split(" ");

                SurahCount.add(Pair.create(input[0],
                        Integer.parseInt(input[1])));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public AyaInfo arabicResult(String input)
    {
        int idx = EnglishBestMatch(input);
        if (idx >= 0) {
           AyaInfo ayaInfo = new AyaInfo();
           ayaInfo.Aya = ArabicCorpus.get(idx);
           int c = 0;
            for (Pair SurahName:SurahCount) {
                    c+= Integer.parseInt(SurahName.second.toString());
                    if(idx <= c)
                    {
                        ayaInfo.SurahId = begin;
                        ayaInfo.Surah = SurahName.first.toString();
                        int Bef = c - Integer.parseInt(SurahName.second.toString());
                        ayaInfo.AyaId = idx - Bef + 1;
                        ayaInfo.GuzA = 30;
                        return ayaInfo;
                    }
            }
        }
        return new AyaInfo();
    }

    private int EnglishBestMatch(String Input)
    {
        int count = 0;
        int idx = -1;
        for (int i = 0 ;i < EnglishCorpus.size() ; i++ )
        {
            int c = 0;
            for (String Word:Input.split(" "))
            {
                if(EnglishCorpus.get(i).toLowerCase().contains(Word.toLowerCase()))
                {
                    c++;
                }
            }
            if(c > count)
            {
                count = c;
                idx = i;
            }
        }
        return idx;
    }
}
