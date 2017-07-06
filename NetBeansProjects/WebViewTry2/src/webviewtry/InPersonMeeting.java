/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webviewtry;

/**
 *
 * @author Administrator
 */
public class InPersonMeeting {
    private String Client, Name, Surname, Title, Region, Country;
    private int TerritoryID; 
    
    public InPersonMeeting(String Client, String Name, String Surname, String Title, String Region, String Country, int TerritoryID) {
        this.Client = Client;
        this.Name = Name;
        this.Surname = Surname;
        this.Title = Title;
        this.Region = Region;
        this.Country = Country;
        this.TerritoryID = TerritoryID;
    }

    public String getClient() {
        return Client;
    }

    public void setClient(String Client) {
        this.Client = Client;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String Surname) {
        this.Surname = Surname;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String Region) {
        this.Region = Region;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }

    public int getTerritoryID() {
        return TerritoryID;
    }

    public void setTerritoryID(int TerritoryID) {
        this.TerritoryID = TerritoryID;
    }
    
    
}
