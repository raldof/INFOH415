package postgres;

import java.sql.Connection;
import java.sql.DriverManager;

public class PsqlConnection {
    public Connection connection(){
        Connection conn = null;
        try{
            Class.forName("org.postgresql.Driver");
            String username = null; //the username
            String password = null; //the password
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

}
