package com.fcis.gp.aya.aya;

import java.io.Serializable;

/**
 * Created by mohamed on 30/03/18.
 */

public class AyaInfo implements Serializable{
    public String Aya;
    public String Surah;
    public int SurahId;
    public int AyaId;
    public int GuzA;

    public AyaInfo() {
    }

    public AyaInfo(String aya, String surah, int surahId, int ayaId, int guzA) {
        Aya = aya;
        Surah = surah;
        SurahId = surahId;
        AyaId = ayaId;
        GuzA = guzA;

    }
}
