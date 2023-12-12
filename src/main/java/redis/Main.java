package redis;

import postgres.PsqlConnection;
import redis.client.Display;
import java.sql.*;

public class Main {
    public static void main(String[] args)  {

        Display dis = new Display();
        dis.connexion();
    }
}