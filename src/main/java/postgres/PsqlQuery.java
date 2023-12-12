package postgres;

import redis.object.Message;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PsqlQuery {


    /**
     *
     *
     * Insert message to postgreSQL database, the message is generated randomly
     * @Param :
     * Connection conn : Contains the connection to the database
     * String user : Cotnains the name of the user(can be random)
     * LocalDateTime : Contains the date at which the message was sent
     *
     * */
    public void insertMessage(Connection conn, String user, LocalDateTime date)  {
        String sqlQuery = "INSERT INTO message (messagecontent, username, date) VALUES (?,?,?)";
        try(PreparedStatement query = conn.prepareStatement(sqlQuery)){
            conn.setAutoCommit(false);
            String message =createMessage();
            query.setString(1,message);
            query.setString(2,user);
            String dt = date.toString();
            query.setString(3,dt);
            query.execute();
            conn.commit();
        }catch(SQLException e){

            try{
                conn.rollback();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

    }

    /**
     *
     *
     * Create a random message in UTF 16 format
     *
     *
     * */
    public String createMessage(){
        byte[] array = new byte[150]; // length is bounded by 150
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_16);
    }

    /**
     *
     *
     * Delete all messsages from postgreSQL message table
     * @Param :
     * Connection conn : Contains the connection to the database
     *
     * */
    public void deleteMessages(Connection connexionSQL) {
        String sqlQuery = "DELETE FROM message";
        try(PreparedStatement query = connexionSQL.prepareStatement(sqlQuery)){
            connexionSQL.setAutoCommit(false);
            query.executeUpdate();
            connexionSQL.commit();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     *
     *
     * Select X messages where X was chosen by user
     * @Param :
     * Connection conn : Contains the connection to the database
     * int i : Contains the number of messages to be selected
     *
     * */
    public void selectXMessages(Connection connexionSQL, int i) {
        String sqlQuery = "SELECT messagecontent FROM message LIMIT ?";
        ResultSet resultSet = null;
        List<String> resultList = new ArrayList<>();
        try(PreparedStatement query = connexionSQL.prepareStatement(sqlQuery)){
            connexionSQL.setAutoCommit(false);
            query.setString(1,String.valueOf(i));
            resultSet = query.executeQuery();
            /*int d = 1;
            while (resultSet.next()){
                String m = resultSet.getObject(d,String.class);
                resultList.add(m);
                d++;
            }
            System.out.println(resultList.size());*/
            connexionSQL.commit();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
