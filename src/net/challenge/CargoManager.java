package net.challenge;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class CargoManager {

    /**
     * This method connects the program to the database
     * @return the connection conn
     * @throws SQLException
     */
    public static Connection connect() throws SQLException{
        String DB_URL = "jdbc:mysql://localhost/";
        String USER = "root";
        String PASS = "sql_PXY0320"; //Please replace the values with your own database URL, username, and password

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method creates a new database "Cargos" and two tables "table1" and "deleted"
     * @throws SQLException
     */
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

    /**
     * This method is used to add new rows to the table
     * @param product_id id of the product, not id of the row
     * @param product_name name of the product
     */
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

    /**
     * This method edit one term in a row
     * @param product_id id of the product, used to identify the row
     * @param column which column to edit
     * @param newContent new content update
     */
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

    /**
     * This method deletes a row (move it from "table1" to "deleted")
     * @param product_id used to identify which row to delete
     */
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

    /**
     * This method undeletes a row (move it from "deleted" back into "table1")
     * @param product_id
     */
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

    /**
     * This method read the table and sort it into a list of string
     * @param tableName determines which table to read
     * @return
     */
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

    /**
     * This method prints the list of a table to the console
     * @param tableName determines which table to print
     */
    public static void print(String tableName){
        ArrayList<String> result = getList(tableName);
        for (String row: result){
            System.out.println(row);
        }
    }

    /**
     * This method exports a csv file based on data from a table
     * @param tableName determines which table to export
     * @throws IOException
     */
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