package com.example.opcv.repository.localForms;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "RE")
public class RE {

    @ColumnInfo(name = "idForm")
    public int idForm;

    @ColumnInfo(name = "nameForm")
    public String nameForm;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "eventName")
    public String eventName;

    @ColumnInfo(name = "totalPerson")
    public String totalPerson;

    @ColumnInfo(name = "womenNumber")
    public String womenNumber;

    @ColumnInfo(name = "menNumber")
    public String menNumber;

    @ColumnInfo(name = "noSpcNumber")
    public String noSpcNumber;

    @ColumnInfo(name = "infantNumber")
    public String infantNumber;

    @ColumnInfo(name = "childhoodNumber")
    public String childhoodNumber;

    @ColumnInfo(name = "teenNumber")
    public String teenNumber;

    @ColumnInfo(name = "youthNumber")
    public String youthNumber;

    @ColumnInfo(name = "adultNumber")
    public String adultNumber;

    @ColumnInfo(name = "elderlyNumber")
    public String elderlyNumber;

    @ColumnInfo(name = "afroNumber")
    public String afroNumber;

    @ColumnInfo(name = "nativeNumber")
    public String nativeNumber;

    @ColumnInfo(name = "lgtbiNumber")
    public String lgtbiNumber;

    @ColumnInfo(name = "romNumber")
    public String romNumber;

    @ColumnInfo(name = "victimNumber")
    public String victimNumber;

    @ColumnInfo(name = "disabilityNumber")
    public String disabilityNumber;

    @ColumnInfo(name = "demobilizedNumber")
    public String demobilizedNumber;

    @ColumnInfo(name = "mongrelNumber")
    public String mongrelNumber;

    @ColumnInfo(name = "foreignNumber")
    public String foreignNumber;

    @ColumnInfo(name = "peasantNumber")
    public String peasantNumber;

    @ColumnInfo(name = "otherNumber")
    public String otherNumber;

    @ColumnInfo(name = "Date")
    public String Date;

    public RE(int idForm, String nameForm, String date, String eventName, String totalPerson, String womenNumber, String menNumber, String noSpcNumber, String infantNumber, String childhoodNumber, String teenNumber, String youthNumber, String adultNumber, String elderlyNumber, String afroNumber, String nativeNumber, String lgtbiNumber, String romNumber, String victimNumber, String disabilityNumber, String demobilizedNumber, String mongrelNumber, String foreignNumber, String peasantNumber, String otherNumber, String date1) {
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
        Date = date1;
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

    public String getTotalPerson() {
        return totalPerson;
    }

    public void setTotalPerson(String totalPerson) {
        this.totalPerson = totalPerson;
    }

    public String getWomenNumber() {
        return womenNumber;
    }

    public void setWomenNumber(String womenNumber) {
        this.womenNumber = womenNumber;
    }

    public String getMenNumber() {
        return menNumber;
    }

    public void setMenNumber(String menNumber) {
        this.menNumber = menNumber;
    }

    public String getNoSpcNumber() {
        return noSpcNumber;
    }

    public void setNoSpcNumber(String noSpcNumber) {
        this.noSpcNumber = noSpcNumber;
    }

    public String getInfantNumber() {
        return infantNumber;
    }

    public void setInfantNumber(String infantNumber) {
        this.infantNumber = infantNumber;
    }

    public String getChildhoodNumber() {
        return childhoodNumber;
    }

    public void setChildhoodNumber(String childhoodNumber) {
        this.childhoodNumber = childhoodNumber;
    }

    public String getTeenNumber() {
        return teenNumber;
    }

    public void setTeenNumber(String teenNumber) {
        this.teenNumber = teenNumber;
    }

    public String getYouthNumber() {
        return youthNumber;
    }

    public void setYouthNumber(String youthNumber) {
        this.youthNumber = youthNumber;
    }

    public String getAdultNumber() {
        return adultNumber;
    }

    public void setAdultNumber(String adultNumber) {
        this.adultNumber = adultNumber;
    }

    public String getElderlyNumber() {
        return elderlyNumber;
    }

    public void setElderlyNumber(String elderlyNumber) {
        this.elderlyNumber = elderlyNumber;
    }

    public String getAfroNumber() {
        return afroNumber;
    }

    public void setAfroNumber(String afroNumber) {
        this.afroNumber = afroNumber;
    }

    public String getNativeNumber() {
        return nativeNumber;
    }

    public void setNativeNumber(String nativeNumber) {
        this.nativeNumber = nativeNumber;
    }

    public String getLgtbiNumber() {
        return lgtbiNumber;
    }

    public void setLgtbiNumber(String lgtbiNumber) {
        this.lgtbiNumber = lgtbiNumber;
    }

    public String getRomNumber() {
        return romNumber;
    }

    public void setRomNumber(String romNumber) {
        this.romNumber = romNumber;
    }

    public String getVictimNumber() {
        return victimNumber;
    }

    public void setVictimNumber(String victimNumber) {
        this.victimNumber = victimNumber;
    }

    public String getDisabilityNumber() {
        return disabilityNumber;
    }

    public void setDisabilityNumber(String disabilityNumber) {
        this.disabilityNumber = disabilityNumber;
    }

    public String getDemobilizedNumber() {
        return demobilizedNumber;
    }

    public void setDemobilizedNumber(String demobilizedNumber) {
        this.demobilizedNumber = demobilizedNumber;
    }

    public String getMongrelNumber() {
        return mongrelNumber;
    }

    public void setMongrelNumber(String mongrelNumber) {
        this.mongrelNumber = mongrelNumber;
    }

    public String getForeignNumber() {
        return foreignNumber;
    }

    public void setForeignNumber(String foreignNumber) {
        this.foreignNumber = foreignNumber;
    }

    public String getPeasantNumber() {
        return peasantNumber;
    }

    public void setPeasantNumber(String peasantNumber) {
        this.peasantNumber = peasantNumber;
    }

    public String getOtherNumber() {
        return otherNumber;
    }

    public void setOtherNumber(String otherNumber) {
        this.otherNumber = otherNumber;
    }
}
