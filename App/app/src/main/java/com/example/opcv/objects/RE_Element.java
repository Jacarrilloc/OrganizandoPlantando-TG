package com.example.opcv.objects;

import java.util.HashMap;
import java.util.Map;

public class RE_Element {
    int idForm;
    String nameForm;
    String date;
    String eventName;
    int totalPerson,womenNumber,menNumber,noSpcNumber,infantNumber,childhoodNumber,teenNumber, youthNumber,adultNumber,elderlyNumber,afroNumber,nativeNumber,lgtbiNumber,romNumber,victimNumber, disabilityNumber, demobilizedNumber,mongrelNumber,foreignNumber,peasantNumber,otherNumber;


    public RE_Element(int idForm, String nameForm, String date, String eventName, int totalPerson, int womenNumber, int menNumber, int noSpcNumber, int infantNumber, int childhoodNumber, int teenNumber, int youthNumber, int adultNumber, int elderlyNumber, int afroNumber, int nativeNumber, int lgtbiNumber, int romNumber, int victimNumber, int disabilityNumber, int demobilizedNumber, int mongrelNumber, int foreignNumber, int peasantNumber, int otherNumber) {
        this.idForm = idForm;
        this.nameForm = nameForm;
        this.date = date;
        this.eventName = eventName;
        this.totalPerson = totalPerson;
        this.womenNumber = womenNumber;
        this.menNumber = menNumber;
        this.noSpcNumber = noSpcNumber;
        this.infantNumber = infantNumber;
        this.childhoodNumber = childhoodNumber;
        this.teenNumber = teenNumber;
        this.youthNumber = youthNumber;
        this.adultNumber = adultNumber;
        this.elderlyNumber = elderlyNumber;
        this.afroNumber = afroNumber;
        this.nativeNumber = nativeNumber;
        this.lgtbiNumber = lgtbiNumber;
        this.romNumber = romNumber;
        this.victimNumber = victimNumber;
        this.disabilityNumber = disabilityNumber;
        this.demobilizedNumber = demobilizedNumber;
        this.mongrelNumber = mongrelNumber;
        this.foreignNumber = foreignNumber;
        this.peasantNumber = peasantNumber;
        this.otherNumber = otherNumber;
    }

    public int getIdForm() {
        return idForm;
    }

    public void setIdForm(int idForm) {
        this.idForm = idForm;
    }

    public String getNameForm() {
        return nameForm;
    }

    public void setNameForm(String nameForm) {
        this.nameForm = nameForm;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getTotalPerson() {
        return totalPerson;
    }

    public void setTotalPerson(int totalPerson) {
        this.totalPerson = totalPerson;
    }

    public int getWomenNumber() {
        return womenNumber;
    }

    public void setWomenNumber(int womenNumber) {
        this.womenNumber = womenNumber;
    }

    public int getMenNumber() {
        return menNumber;
    }

    public void setMenNumber(int menNumber) {
        this.menNumber = menNumber;
    }

    public int getNoSpcNumber() {
        return noSpcNumber;
    }

    public void setNoSpcNumber(int noSpcNumber) {
        this.noSpcNumber = noSpcNumber;
    }

    public int getInfantNumber() {
        return infantNumber;
    }

    public void setInfantNumber(int infantNumber) {
        this.infantNumber = infantNumber;
    }

    public int getChildhoodNumber() {
        return childhoodNumber;
    }

    public void setChildhoodNumber(int childhoodNumber) {
        this.childhoodNumber = childhoodNumber;
    }

    public int getTeenNumber() {
        return teenNumber;
    }

    public void setTeenNumber(int teenNumber) {
        this.teenNumber = teenNumber;
    }

    public int getYouthNumber() {
        return youthNumber;
    }

    public void setYouthNumber(int youthNumber) {
        this.youthNumber = youthNumber;
    }

    public int getAdultNumber() {
        return adultNumber;
    }

    public void setAdultNumber(int adultNumber) {
        this.adultNumber = adultNumber;
    }

    public int getElderlyNumber() {
        return elderlyNumber;
    }

    public void setElderlyNumber(int elderlyNumber) {
        this.elderlyNumber = elderlyNumber;
    }

    public int getAfroNumber() {
        return afroNumber;
    }

    public void setAfroNumber(int afroNumber) {
        this.afroNumber = afroNumber;
    }

    public int getNativeNumber() {
        return nativeNumber;
    }

    public void setNativeNumber(int nativeNumber) {
        this.nativeNumber = nativeNumber;
    }

    public int getLgtbiNumber() {
        return lgtbiNumber;
    }

    public void setLgtbiNumber(int lgtbiNumber) {
        this.lgtbiNumber = lgtbiNumber;
    }

    public int getRomNumber() {
        return romNumber;
    }

    public void setRomNumber(int romNumber) {
        this.romNumber = romNumber;
    }

    public int getVictimNumber() {
        return victimNumber;
    }

    public void setVictimNumber(int victimNumber) {
        this.victimNumber = victimNumber;
    }

    public int getDisabilityNumber() {
        return disabilityNumber;
    }

    public void setDisabilityNumber(int disabilityNumber) {
        this.disabilityNumber = disabilityNumber;
    }

    public int getDemobilizedNumber() {
        return demobilizedNumber;
    }

    public void setDemobilizedNumber(int demobilizedNumber) {
        this.demobilizedNumber = demobilizedNumber;
    }

    public int getMongrelNumber() {
        return mongrelNumber;
    }

    public void setMongrelNumber(int mongrelNumber) {
        this.mongrelNumber = mongrelNumber;
    }

    public int getForeignNumber() {
        return foreignNumber;
    }

    public void setForeignNumber(int foreignNumber) {
        this.foreignNumber = foreignNumber;
    }

    public int getPeasantNumber() {
        return peasantNumber;
    }

    public void setPeasantNumber(int peasantNumber) {
        this.peasantNumber = peasantNumber;
    }

    public int getOtherNumber() {
        return otherNumber;
    }

    public void setOtherNumber(int otherNumber) {
        this.otherNumber = otherNumber;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("idForm", idForm);
        map.put("nameForm", nameForm);
        map.put("date", date);
        map.put("eventName", eventName);
        map.put("totalPerson", totalPerson);
        map.put("womenNumber", womenNumber);
        map.put("menNumber", menNumber);
        map.put("infantNumber", infantNumber);
        map.put("childhoodNumber", childhoodNumber);
        map.put("teenNumber", teenNumber);
        map.put("youthNumber", youthNumber);
        map.put("adultNumber", adultNumber);
        map.put("elderlyNumber", elderlyNumber);
        map.put("noSpcNumber", noSpcNumber);
        map.put("nativeNumber", nativeNumber);
        map.put("lgtbiNumber", lgtbiNumber);
        map.put("romNumber", romNumber);
        map.put("victimNumber", victimNumber);
        map.put("disabilityNumber", disabilityNumber);
        map.put("demobilizedNumber", demobilizedNumber);
        map.put("mongrelNumber", mongrelNumber);
        map.put("foreignNumber", foreignNumber);
        map.put("peasantNumber", peasantNumber);
        map.put("otherNumber", otherNumber);
        map.put("afroNumber", afroNumber);

        return map;
    }
}