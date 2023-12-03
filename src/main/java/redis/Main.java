package redis;

import postgres.PsqlConnection;
import redis.client.Display;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {

        //Display dis = new Display();
        //dis.connexion();

        PsqlConnection psql = new PsqlConnection();
        Connection connexion = psql.connection();
        connexion.close();
    }
}