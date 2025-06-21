package com.tama.basicjfxmultistage.Model;

import java.time.Instant;
import java.util.Date;

public class Pesan {
    private Date tanggal;
    private String pesan;

    public Pesan(String pesan) {
        this.tanggal = Date.from(Instant.now());
        this.pesan = pesan;
    }

    @Override
    public String toString() {
        return "Pesan{" +
                "tanggal= " + tanggal +
                ", pesan= '" + pesan + '\'' +
                '}';
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public String getPesan() {
        return pesan;
    }
}
