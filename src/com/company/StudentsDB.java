package com.company;

import java.sql.*;
import java.util.Scanner;

public class StudentsDB {
    /**
     * Консольная программа для работы с базой студентов
     */
    public static final String USER = "postgres";
    public static final String PASSWORD = "1234";
    public static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    // public static final String DRIVER = "org.postgresql.Driver";

    public static String[] commandToQuery(String[] command) {
        String[] annotatedQuery = new String[2];
        switch (command[0]) {
            case ("add_student") -> {
                if (command.length >= 6) {
                    annotatedQuery[0] = "INPUT";
                    annotatedQuery[1] = "INSERT INTO students " +
                            "(name, surname, patronymic, birthday, group_id) " +
                            "VALUES (" +
                            "'" + command[1] + "', " +
                            "'" + command[2] + "', " +
                            "'" + command[3] + "', " +
                            "'" + command[4] + "', " +
                            "'" + command[5] + "')";
                }
                else {
                    annotatedQuery[0] = "ERROR";
                    annotatedQuery[1] = "error: command add_student must have 5 operands";
                }
                return annotatedQuery;
            }
            case ("del_student") -> {
                if (command.length >= 2) {
                    annotatedQuery[0] = "INPUT";
                    annotatedQuery[1] = "DELETE FROM students WHERE id=" + command[1];
                }
                else {
                    annotatedQuery[0] = "ERROR";
                    annotatedQuery[1] = "error: command del_student must have 1 operand";
                }
                return annotatedQuery;
            }
            case ("show_student") -> {
                if (command.length >= 2) {
                    annotatedQuery[0] = "OUTPUT";
                    annotatedQuery[1] = "SELECT * FROM students WHERE id=" + command[1];
                }
                else {
                    annotatedQuery[0] = "ERROR";
                    annotatedQuery[1] = "error: command show_student must have 1 operand";
                }
                return annotatedQuery;
            }
            case ("show_students") -> {
                annotatedQuery[0] = "OUTPUT";
                annotatedQuery[1] = "SELECT * FROM students";
                return annotatedQuery;
            }
            case ("exit") -> {
                annotatedQuery[0] = "EXIT";
                annotatedQuery[1] = "";
                return annotatedQuery;
            }
            default -> {
                annotatedQuery[0] = "ERROR";
                annotatedQuery[1] = "error: command not found";
                return annotatedQuery;
            }
        }
    }

    public static void printStudents(ResultSet resultSet) throws SQLException {
        System.out.printf(" %-3s | %-15s | %-15s | %-15s | %-10s | %-10s \n",
                "id",
                "name",
                "surname",
                "patronymic",
                "birthday",
                "group_id");
        System.out.printf("-%s-+-%s-+-%s-+-%s-+-%s-+-%s-\n",
                "---",
                "---------------",
                "---------------",
                "---------------",
                "----------",
                "----------");
        while (resultSet.next()) {
            System.out.printf(" %3s | %-15s | %-15s | %-15s | %10s | %-10s \n",
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6));
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] command = scanner.nextLine().split(" ");
        String[] annotatedQuery = commandToQuery(command);
        ResultSet resultSet;

        try {
            // Class.forName(DRIVER);
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();

            while (!annotatedQuery[0].equals("EXIT")) {
                switch (annotatedQuery[0]) {
                    case ("INPUT") -> {
                        statement.executeUpdate(annotatedQuery[1]);
                    }
                    case ("OUTPUT") -> {
                        resultSet = statement.executeQuery(annotatedQuery[1]);
                        printStudents(resultSet);
                    }
                    case ("ERROR") -> {
                        System.out.println(annotatedQuery[1]);
                    }
                }
                command = scanner.nextLine().split(" ");
                annotatedQuery = commandToQuery(command);
            }
        }
        catch (/* ClassNotFoundException | */ SQLException ex) {
            ex.printStackTrace();
        }
    }
}
