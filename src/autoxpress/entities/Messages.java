/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoxpress.entities;
import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author BKHmidi
 */
public class Messages {

    private int id_message;
    private int id_client ;
    private int id_conducteur;
    private Timestamp  date_message;
    private String contenu;

    public Messages() {
    }

    public Messages(int id_message, int id_client, int id_conducteur, Timestamp date_message, String contenu) {
        this.id_message = id_message;
        this.id_client = id_client;
        this.id_conducteur = id_conducteur;
        this.date_message = date_message;
        this.contenu = contenu;
    }



    public Messages(int id_client, int id_conducteur, Timestamp date_message, String contenu) {
        this.id_client = id_client;
        this.id_conducteur = id_conducteur;
        this.date_message = date_message;
        this.contenu = contenu;
    }

    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public int getId_conducteur() {
        return id_conducteur;
    }

    public void setId_conducteur(int id_conducteur) {
        this.id_conducteur = id_conducteur;
    }
    

    
    public int getId_message() {
        return id_message;
    }

    public void setId_message(int id_message) {
        this.id_message = id_message;
    }

 
    public Timestamp  getDate_message() {
        return date_message;
    }

    public void setDate_message(Timestamp  date_message) {
        this.date_message = date_message;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    @Override
    public String toString() {
        return "Message{" + "id_message=" + id_message + ", id_client=" + id_client + ", id_conducteur=" + id_conducteur + ", date_message=" + date_message + ", contenu=" + contenu + '}';
    }

  

   

}
