package net.challenge;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class CargoManager {

    public static Connection connect() throws SQLException{
        String DB_URL = "jdbc:mysql://localhost/";
        String USER = "root";
        String PASS = "sql_PXY0320"; //Please replace the

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createTable() throws SQLException {
        try{
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS CARGOS");
            System.out.println("Database created");
            stmt.executeUpdate("USE CARGOS");
            PreparedStatement create = conn.prepareStatement("CREATE TABLE IF NOT EXISTS table1(id int NOT NULL AUTO_INCREMENT, product_id varchar(255), product_name varchar(255), PRIMARY KEY(id))");
            create.executeUpdate();

            stmt.executeUpdate("USE CARGOS");
            PreparedStatement deleted = conn.prepareStatement("CREATE TABLE IF NOT EXISTS deleted(id int NOT NULL AUTO_INCREMENT, product_id varchar(255), product_name varchar(255), PRIMARY KEY(id))");
            deleted.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            System.out.println("Tables created");
        }
    }

    public static void insert(String product_id, String product_name){
        try{
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("USE CARGOS");
            PreparedStatement insert = conn.prepareStatement("INSERT INTO table1 (product_id, product_name) VALUES ('"+product_id +"', '" + product_name +"')");
            insert.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            System.out.println("Item inserted");
        }
    }

    public static void edit(String product_id, String column, String newContent){
        try{
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("USE CARGOS");
            PreparedStatement insert = conn.prepareStatement("UPDATE table1 SET " + column + " = '" + newContent + "' WHERE product_id = '" + product_id +"'");
            insert.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            System.out.println("Item edited");
        }
    }

    public static void delete(String product_id) {
        try{
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("USE CARGOS");
            PreparedStatement move = conn.prepareStatement("INSERT INTO deleted SELECT * FROM table1 WHERE product_id = '" + product_id + "'");
            move.executeUpdate();

            stmt.executeUpdate("USE CARGOS");
            PreparedStatement delete = conn.prepareStatement("DELETE FROM table1 WHERE product_id = '" + product_id +"'");
            delete.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            System.out.println("Item deleted");
        }
    }
    public static void undelete(String product_id) {
        try{
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("USE CARGOS");
            PreparedStatement move = conn.prepareStatement("INSERT INTO table1 SELECT * FROM deleted WHERE product_id = '" + product_id + "'");
            move.executeUpdate();

            stmt.executeUpdate("USE CARGOS");
            PreparedStatement delete = conn.prepareStatement("DELETE FROM deleted WHERE product_id = '" + product_id +"'");
            delete.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            System.out.println("Item undeleted");
        }
    }

    public static ArrayList<String> getList(String tableName){
        try{
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("USE CARGOS");
            ResultSet row = stmt.executeQuery("SELECT * FROM " + tableName);
            ArrayList<String> result = new ArrayList<>();
            result.add("Items in table '" + tableName + "':");
            while(row.next()){
                result.add(row.getString("id") + " --- " + row.getString("product_id") + " --- " + row.getString("product_name"));
            }
            return result;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void print(String tableName){
        ArrayList<String> result = getList(tableName);
        for (String row: result){
            System.out.println(row);
        }
    }

    public static void exportCSV(String tableName) throws IOException {
        FileWriter writer = new FileWriter("./" + tableName + ".csv");
        writer.write("id, product_id, product_name\n");
        ArrayList<String> result = getList(tableName);
        for (int i=1; i<result.size(); i++){
            String row = result.get(i);
            writer.write(row.substring(0,1) + ", " + row.substring(6, 11) + ", " + row.substring(16) + "\n");
        }
        writer.close();
    }

    public static void main (String[]args) throws Exception {
        exportCSV("table1");
        /*createTable();
        insert("A1023", "Apple");
        insert("A1054", "Baaaaa");
        edit("A1054", "product_name", "Banana");
        delete("A1054");
        print("table1");
        print("deleted");
        undelete("A1054");
        print("table1");
        print("deleted");*/
    }
}