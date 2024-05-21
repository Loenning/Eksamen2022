//------Oppgave 3--------
function validerNavn(navn){
    const regexp = /^[a-zæøåA-ZÆØÅ]+$/;
    if(!regexp.test(navn)) {
        $("#feilNavn").html("Legg inn gyldig navn");
        return false;
    } else {
        $("#feilNavn").html("");
        return true;
    }
}
//------Oppgave 3--------
function validerMobil(mobil){
    const regexp = /^[0-9]{8}$/;
    if(!regexp.test(mobil)) {
        $("#feilMobil").html("Legg inn gyldig mobilnummer - 8 siffer");
        return false;
    } else {
        $("#feilMobil").html("");
        return true;
    }
}
//------Oppgave 3--------
function validerEpost(epost){
    const regexp = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    if(!regexp.test(epost)) {
        $("#feilEpost").html("Legg inn gyldig epost");
        return false;
    } else {
        $("#feilEpost").html("");
        return true;
    }
}
//------Oppgave 3--------
function validerBord(bord){
    if(bord) {
        $("#feilBord").html("");
        return true;
    } else {
        $("#feilBord").html("Fyll ut felt");
        return true;
    }
}
//------Oppgave 3--------
function validerVarer(varer){
    if(varer) {
        $("#feilVarer").html("");
        return true;
    } else {
        $("#feilVarer").html("Fyll ut felt");
        return false;
    }
}
//------Oppgave 3--------
function validerBestilling(bestilling){
    navnOK = validerNavn(bestilling.navn);
    mobilOK = validerMobil(bestilling.mobil);
    epostOK = validerEpost(bestilling.epost);
    bordOK = validerBord(bestilling.bord);
    varerOK = validerVarer(bestilling.varer);

    if(navnOK && mobilOK && epostOK && bordOK && varerOK){
        return true;
    } else {
        return false;
    }
}
//------Oppgave 3--------
function sendBestilling(){
    let bestilling = {
        "navn": $("#navn").val(),
        "mobil": $("#mobil").val(),
        "epost": $("#epost").val(),
        "bord": $("#bord").val(),
        "varer": $("#varer").val()
    }; //------Oppgave 3--------
    if(validerBestilling(bestilling)){
        $.post("/bestilling", bestilling, function(){
            $("melding").html("Bestilling sendt inn")
            $("#navn").val("");
            $("#mobil").val("");
            $("#epost").val("");
            $("#bord").val("");
            $("#varer").val("");

        }) //------Oppgave 5--------
            .fail(function(jqXHR){
                const json = $.parseJSON(jqXHR.responseText);
                $("#melding").html(json.message);
            });
    } else { //------Oppgave 3--------
        $("#melding").html("Fyll ut alle felter og rett feil før du sender på nytt");
    }
}
