package com.example.eksempeleksamen;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


// Oppgave 1
@RestController
public class SultenController {

    @Autowired
    SultenRepository rep;



    //Oppgave 7 ------------------------------------------------
    @Autowired
    private HttpSession session;

    //Oppgave 1 ------------------------------------------------
    @GetMapping("/sjekk")
    public String sjekk(){
        return "OK";
    }


    //Oppgave 4 ------------------------------------------------
    @PostMapping("/bestilling")
    public void sendBestilling(Bestilling bestilling, HttpServletResponse response) throws IOException{
        if(!rep.lagreBestilling(bestilling)){
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Det skjedde en feil, prøv igjen om litt.");
        }
    }

    //Oppgave 8 ------------------------------------------------
    @PostMapping("bestilling")
    public void bestilling(Bestilling bestilling, HttpServletResponse response) throws IOException {
        if (sjekkInnlogging()) {
            if (!rep.leggInnBestilling(bestilling, (Kunde) session.getAttribute("innlogget"))) {
                response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Det skjedde en feil ved innlegging av bestilling. Prøv igjen om litt.");
            }
        } else {
            response.sendError(HttpStatus.FORBIDDEN.value(), "Du må først logge inn for å legge inn en ny bestilling.");
        }
    }


    //Oppgave 6 ------------------------------------------------
    @PostMapping("/kunde"){
        public void kunde(Kunde kunde) {
            rep.leggInnKunde(kunde);
        }
    }

    //Oppgave 7 ------------------------------------------------
    @PostMapping("/login")
    public void login(Kunde kunde){
        if(rep.sjekkMobilOgPassord(kunde)){
            session.setAttribute("loggetInn", kunde);
        }
    }

    //Oppgave 7 ------------------------------------------------
    public boolean sjekkInnlogging(){
        if(session.getAttribute("loggetInn") != null){
            return true;
        } else {
            return false;
        }
    }

    //Oppgave 7 ------------------------------------------------
    @GetMapping("/loggut")
    public void loggut() {
        session.removeAttribute("loggetInn");
    }

}
