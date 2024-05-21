package com.example.eksempeleksamen;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Repository
public class SultenRepository {

    @Autowired
    private JdbcTemplate db;

    Logger logger = LoggerFactory.getLogger(SultenRepository.class);

    //Oppgave 4 ------------------------------------------------
    @Transactional
    public boolean lagreBestilling(Bestilling bestilling) {
        String sjekkKunde = "SELECT COUNT(*) FROM kunder WHERE mobil = ?";                  //Sjekke om kunde finnes (A)
        String hentKunde = "SELECT KID FROM kunder WHERE mobil= ?";                         //Hente ut KID for fra kunde (B)
        String settInnKunde = "INSERT INTO kunder (navn, mobil, epost) VALUES (?,?,?)";     //Setter inn ny kunde (C)
        String sqlBestilling = "INSERT INTO bestilling (KID, bord, varer) VALUES (?,?,?)";  //Legger inn ny bestilling med KID fra kunde (D)

        int KID = 0; //Placeholder-verdi for nøkkel KID

        try{
            int kunder = db.queryForObject(sjekkKunde, Integer.class, bestilling.getMobil()); // (A)
            if(kunder == 0){
                db.update(settInnKunde,bestilling.getNavn(), bestilling.getMobil(), bestilling.getEpost()); // (C)
            }

            KID = db.queryForObject(hentKunde, Integer.class,bestilling.getMobil()); // (B) Placeholder-verdi oppdateres
            db.update(sqlBestilling, KID, bestilling.getBord(), bestilling.getVarer()); // (D)
        } catch (Exception e) {
            logger.error("Feil innlegging av bestilling" +e);
            return false;
        }
        return true;
    }


    //Oppgave 6 ------------------------------------------------
    public void leggInnKunde(Kunde kunde){
        String hashetPassord = BCrypt.hashpw(kunde.getPassord(), BCrypt.gensalt(12));
        String settInnKunde = "INSERT INTO kunder (navn, mobil, epost, passord) VALUES (?,?,?,?)";
        db.update(settInnKunde, kunde.getNavn(), kunde.getMobil(), kunde.getEpost(), hashetPassord);
    }

    //Oppgave 7 ------------------------------------------------
    public boolean sjekkMobilOgPassord(Kunde kunde){
        String sql = "SELECT * FROM users WHERE mobil=?";
        Kunde dbkunde = db.queryForObject(sql, BeanPropertyRowMapper.newInstance(Kunde.class), kunde.getMobil());
        String hashetPassord = dbkunde.getPassord();
        return BCrypt.checkpw(kunde.getPassord(), hashetPassord);
    }


    //Oppgave 8 ------------------------------------------------
    public boolean leggInnBestilling(Bestilling bestilling, Kunde kunde) {
        String hentKunde = "SELECT KID FROM Kunde WHERE mobil=?";
        String sqlBestilling = "INSERT INTO Bestilling (KID,bord,varer) VALUES(?,?,?)";
        try {
            int KID = db.queryForObject(hentKunde, Integer.class, kunde.getMobil());
            db.update(sqlBestilling, KID, bestilling.getBord(), bestilling.getVarer());
        } catch (Exception e) {
            logger.error("Feil ved innlegging av bestilling: " + e);
            return false;
        }
        return true;
    }
}
