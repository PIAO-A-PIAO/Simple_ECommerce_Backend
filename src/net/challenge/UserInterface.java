package net.challenge;

import java.sql.SQLOutput;
import java.util.Scanner;

public class UserInterface {
    public static void main(String[] args) throws Exception{
        Scanner scan = new Scanner(System.in);
            System.out.println("Your DB Address:");
            String URL = scan.next();
            System.out.println("Your Username:");
            String USER = scan.next();
            System.out.println("Your Password:");
            String PASS = scan.next();
            CargoManager.set(URL, USER, PASS);
            CargoManager.connect();
            System.out.println("Connected to the database!");

        CargoManager.createTable();
        Boolean flag = true;
        while (flag){
            System.out.println("Command here:");
            String command = scan.next();
            if (command.equals("q")){
                System.out.println("Exit!");
                break;
            }else if(command.equals("add")){
                System.out.println("Product ID:");
                String id = scan.next();
                System.out.println("Product Name:");
                String name = scan.next();
                CargoManager.insert(id, name);
            }else if(command.equals("edit")){
                System.out.println("Product ID:");
                String id = scan.next();
                System.out.println("Column to Edit:");
                String col = scan.next();
                System.out.println("New Content:");
                String content = scan.next();
                CargoManager.edit(id, col, content);
            }else if(command.equals("delete")){
                System.out.println("Product ID:");
                String id = scan.next();
                CargoManager.delete(id);
            }else if(command.equals("undelete")){
                System.out.println("Product ID:");
                String id = scan.next();
                CargoManager.undelete(id);
            }else if(command.equals("print")){
                System.out.println("Name of Table:");
                String table = scan.next();
                CargoManager.print(table);
            }else if(command.equals("csv")){
                System.out.println("Name of Table:");
                String table = scan.next();
                CargoManager.exportCSV(table);
            }else{
                System.out.println("Command unknown\n");
                System.out.println("Try: q, add, edit, delete, undelete, print, csv ...");
            }
        }
    }
}
