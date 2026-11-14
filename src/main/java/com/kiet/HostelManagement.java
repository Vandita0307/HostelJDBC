
package com.kiet;

import java.sql.*;
import java.util.Scanner;

public class HostelManagement {

    public static void main(String[] args) {

        Connection con = DBConnection.getConnection();

        if (con == null) {
            return;
        }

        Scanner sc = new Scanner(System.in);

        // (a) Insert Students
        // insertStudents(con, sc);

        // (b) Retrieve and Sort Students
        // retrieveStudents(con, sc);

        // (c) Update Hostel Fee
        // updateFee(con, sc);

        // (d) Search and Modify Room
        // modifyRoom(con, sc);

        // (e) Aggregate and Conditional Delete
        aggregateAndDelete(con, sc);

        try {
            con.close();
            sc.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // (a) Insert Student Records
    public static void insertStudents(Connection con, Scanner sc) {

        String sql = "INSERT INTO HostelStudent VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            for (int i = 1; i <= 3; i++) {

                System.out.println("\nEnter details of Student " + i);

                System.out.print("Student ID: ");
                int id = sc.nextInt();
                sc.nextLine();

                System.out.print("Student Name: ");
                String name = sc.nextLine();

                System.out.print("Roll Number: ");
                String roll = sc.nextLine();

                System.out.print("Hostel Name: ");
                String hostel = sc.nextLine();

                System.out.print("Room Number: ");
                String room = sc.nextLine();

                System.out.print("Fee: ");
                double fee = sc.nextDouble();

                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setString(3, roll);
                ps.setString(4, hostel);
                ps.setString(5, room);
                ps.setDouble(6, fee);

                try {
                    int rows = ps.executeUpdate();

                    if (rows > 0) {
                        System.out.println("Student inserted successfully!");
                    }

                } catch (SQLIntegrityConstraintViolationException e) {
                    System.out.println("Duplicate Student ID!");
                }
            }

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // (b) Conditional Retrieval and Sorting
    public static void retrieveStudents(Connection con, Scanner sc) {

        System.out.print("Enter Hostel Name: ");
        String hostel = sc.nextLine();

        String sql = "SELECT * FROM HostelStudent WHERE hostel_name = ? AND fee > 50000 ORDER BY fee DESC";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, hostel);

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("\nStudent ID: " + rs.getInt("student_id"));
                System.out.println("Name: " + rs.getString("student_name"));
                System.out.println("Roll Number: " + rs.getString("roll_number"));
                System.out.println("Hostel: " + rs.getString("hostel_name"));
                System.out.println("Room: " + rs.getString("room_number"));
                System.out.println("Fee: " + rs.getDouble("fee"));
            }

            if (!found) {
                System.out.println("No matching students found!");
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // (c) Update Hostel Fee
    public static void updateFee(Connection con, Scanner sc) {

        System.out.print("Enter Student ID: ");
        int id = sc.nextInt();

        System.out.print("Enter New Hostel Fee: ");
        double fee = sc.nextDouble();

        String sql = "UPDATE HostelStudent SET fee = ? WHERE student_id = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setDouble(1, fee);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Hostel fee updated successfully!");
            } else {
                System.out.println("Student record does not exist!");
            }

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // (d) Search and Modify Hostel Details
    public static void modifyRoom(Connection con, Scanner sc) {

        System.out.print("Enter Student ID: ");
        int id = sc.nextInt();

        String searchSql = "SELECT * FROM HostelStudent WHERE student_id = ?";

        try {
            PreparedStatement ps = con.prepareStatement(searchSql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                System.out.println("\nStudent Found!");

                System.out.println("Name: " + rs.getString("student_name"));
                System.out.println("Roll Number: " + rs.getString("roll_number"));
                System.out.println("Hostel: " + rs.getString("hostel_name"));
                System.out.println("Current Room: " + rs.getString("room_number"));

                System.out.print("\nEnter New Room Number: ");
                sc.nextLine();
                String room = sc.nextLine();

                String updateSql = "UPDATE HostelStudent SET room_number = ? WHERE student_id = ?";

                PreparedStatement ps2 = con.prepareStatement(updateSql);

                ps2.setString(1, room);
                ps2.setInt(2, id);

                ps2.executeUpdate();

                System.out.println("\nRoom Number Updated Successfully!");

                String displaySql = "SELECT * FROM HostelStudent WHERE student_id = ?";

                PreparedStatement ps3 = con.prepareStatement(displaySql);

                ps3.setInt(1, id);

                ResultSet rs2 = ps3.executeQuery();

                if (rs2.next()) {

                    System.out.println("\nUpdated Student Details:");

                    System.out.println("Student ID: " + rs2.getInt("student_id"));
                    System.out.println("Name: " + rs2.getString("student_name"));
                    System.out.println("Roll Number: " + rs2.getString("roll_number"));
                    System.out.println("Hostel: " + rs2.getString("hostel_name"));
                    System.out.println("Room Number: " + rs2.getString("room_number"));
                    System.out.println("Fee: " + rs2.getDouble("fee"));
                }

                rs2.close();
                ps3.close();
                ps2.close();

            } else {
                System.out.println("Student record does not exist!");
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // (e) Aggregate Query and Conditional Deletion
    public static void aggregateAndDelete(Connection con, Scanner sc) {

        // Display total hostel fees
        String sumSql = "SELECT SUM(fee) FROM HostelStudent";

        try {
            PreparedStatement ps = con.prepareStatement(sumSql);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Total Hostel Fees Collected: " + rs.getDouble(1));
            }

            rs.close();
            ps.close();

            // Accept student ID
            System.out.print("\nEnter Student ID to Delete: ");
            int id = sc.nextInt();

            // Delete only if fee is greater than zero
            String deleteSql = "DELETE FROM HostelStudent WHERE student_id = ? AND fee > 0";

            PreparedStatement ps2 = con.prepareStatement(deleteSql);

            ps2.setInt(1, id);

            int rows = ps2.executeUpdate();

            if (rows > 0) {
                System.out.println("Student record deleted successfully!");
            } else {
                System.out.println("Record not found or fee is not fully paid!");
            }

            ps2.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
