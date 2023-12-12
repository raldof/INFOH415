package postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PsqlConnection {
    public Connection connection(){
        Connection conn = null;
        try{
            //Connection to postgresql
            Class.forName("org.postgresql.Driver");
            String username = "postgres"; //the username
            String password = "arek"; //the password
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/livechat",username ,password);
            if(conn != null){
                System.out.println("Connection established");
            }else{
                System.out.println("Connection not established");
            }

        }catch(Exception e){
            System.out.println(e);
        }

        return conn;
    }

    public void closeConnection(Connection conn) throws SQLException {
        conn.close();
    }

}
