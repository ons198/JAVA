/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoxpress.services;

import autoxpress.entities.Client;
import autoxpress.entities.Conducteur;
import autoxpress.entities.Contrat;
import autoxpress.entities.Messages;
import autoxpress.interfaces.MessageInterface;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author BKHmidi
 */
public class MessageCRUD implements MessageInterface<Messages> {

    private final String url = "jdbc:mysql://localhost:3306/autoxpress";
    private final String user = "root";
    private final String password = "";

    @Override
    public void AddMessage(Messages m) {
       String sql = "INSERT INTO message(id_conducteur,id_client,  contenu, date_message) VALUES(?,?,?,?)";

    try (Connection conn = DriverManager.getConnection(url, user, password);
         PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        
        pstmt.setInt(1, m.getId_conducteur());
        pstmt.setInt(2, m.getId_client());
        pstmt.setString(3, m.getContenu());
        pstmt.setTimestamp(4, m.getDate_message());

        int affectedRows = pstmt.executeUpdate();
        System.out.println("message ajouté");
        if (affectedRows == 0) {
            throw new SQLException("Creating message failed, no rows affected.");
        }

        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                m.setId_message(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Creating message failed, no ID obtained.");
            }
        }
    } catch (SQLException e) {
        System.out.println("Error adding message: " + e.getMessage());
    }
    }

    @Override
    public void UpdateMessage(Messages m) {
        String sql = "Update INTO Message (contenu) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, m.getContenu());

            pstmt.executeUpdate();
            System.out.println("message modifié");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void DeleteMessage(int id_message) {
        String sql = "DELETE FROM Message WHERE id_message = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_message);
            pstmt.executeUpdate();
            System.out.println("message supprimé");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Messages> MessageList() {
        List<Messages> messages = new ArrayList<>();
        String sql = "SELECT * FROM Message";
        try (Connection conn = DriverManager.getConnection(url, user, password);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Messages m = new Messages();
                m.setId_message(rs.getInt("id_message"));
                m.setId_client(rs.getInt("id_client"));
                m.setId_conducteur(rs.getInt("id_conducteur"));
                m.setDate_message(rs.getTimestamp("date_message"));

                m.setContenu(rs.getString("contenu"));
                LocalDateTime date = rs.getTimestamp("date_message").toLocalDateTime();

                // Format the LocalDateTime object using a DateTimeFormatter
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formattedDate = date.format(formatter);
                m.setDate_message(Timestamp.valueOf(date));
                messages.add(m);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;

    }

    public ObservableList<Messages> getDataMsg() {
        ObservableList<Messages> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Message";
        try (Connection conn = DriverManager.getConnection(url, user, password);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Messages c = new Messages();
                c.setId_message(rs.getInt("id_message"));
                c.setContenu(rs.getString("contenu"));
                c.setId_client(rs.getInt("id_client"));
                c.setId_conducteur(rs.getInt("id_conducteur"));
                // Convert the Timestamp object to LocalDateTime object
                LocalDateTime date = rs.getTimestamp("date_message").toLocalDateTime();

                // Format the LocalDateTime object using a DateTimeFormatter
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formattedDate = date.format(formatter);
                c.setDate_message(Timestamp.valueOf(date));
                list.add(c);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public int getIdClient(String prenom_client) {
        int id_client = 0;
        String sql = "SELECT id_client FROM client WHERE prenom_client = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, prenom_client);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                id_client = rs.getInt("id_client");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id_client;
    }

    public int getIdConducteur(String prenom_conducteur) {
        int id_conducteur = 0;
        try (Connection conn = DriverManager.getConnection(url, user, password);
                PreparedStatement pstmt = conn.prepareStatement("SELECT id_conducteur FROM conducteur WHERE prenom_conducteur = ?")) {
            pstmt.setString(1, prenom_conducteur);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                id_conducteur = rs.getInt("id_conducteur");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id_conducteur;
    }

    @Override
    public Messages ReadMessage(int id_message) {
  String sql = "SELECT m.id_message, c.nom_client, d.nom_conducteur, m.contenu, m.date_message "
            + "FROM message m "
            + "JOIN client c ON m.id_client = c.id_client "
            + "JOIN conducteur d ON m.id_conducteur = d.id_conducteur "
            + "WHERE m.id_message = ?";
    try (Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, id_message);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            Messages m = new Messages();
            m.setId_message(rs.getInt("id_message"));
            m.setId_client(rs.getInt("id_client"));
            m.setId_conducteur(rs.getInt("id_conducteur"));
            m.setDate_message(rs.getTimestamp("date_message"));
            m.setContenu(rs.getString("contenu"));

            // Set the client and conducteur information in the message object
            Client client = new Client();
            client.setNom_client(rs.getString("nom_client"));
         

            m.setId_client(client.getId_client()); // assuming client is a Client object
 
            
            
            
            
            Conducteur conducteur = new Conducteur();
            conducteur.setNom_conducteur(rs.getString("nom_conducteur"));
            m.setId_conducteur(conducteur.getId_conducteur());

            return m;
        } else {
            System.out.println("No message found with id " + id_message);
        }
    } catch (SQLException e) {
        System.out.println("Error reading message: " + e.getMessage());
    }
    return null;    }

  
   
}
