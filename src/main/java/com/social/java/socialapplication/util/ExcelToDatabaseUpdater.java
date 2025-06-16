package com.social.java.socialapplication.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Iterator;

public class ExcelToDatabaseUpdater {

    // Database connection URL, user, and password
    private static final String URL = "jdbc:mysql://dcard.c8rqyqnpxnao.us-east-2.rds.amazonaws.com:3306/geonixApplication?useSSL=false";
    private static final String USER = "dcard";
    private static final String PASSWORD = "dcardapp";

    public static void main(String[] args) {
        String excelFilePath = "C:/report.xlsx";

        try {
            // Read Excel file
            FileInputStream file = new FileInputStream(new File(excelFilePath));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip the header row
            rowIterator.next();

            // Iterate through rows
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                String productId = row.getCell(2).getStringCellValue(); // Get Product ID from 1st index (A)
                String column3 = row.getCell(3).getStringCellValue(); // Get value from 3rd index (C)
                String column4 = row.getCell(4).getStringCellValue(); // Get value from 4th index (D)

                productId = productId.substring(productId.lastIndexOf('/')+1,productId.length());
                System.out.println(productId.substring(productId.lastIndexOf('/')+1,productId.length()));
                System.out.println(column3);
                System.out.println(column4);
                // Update the database
                updateDatabase(productId, column3, column4);
            }

            workbook.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updateDatabase(String productId, String column3Value, String column4Value) {
        // MySQL connection and update query
        String updateQuery = "UPDATE geonixApplication.product SET meta_title = ?, meta_description = ? WHERE product_url = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(updateQuery)) {

            // Set the parameters for the prepared statement
            ps.setString(1, column3Value);
            ps.setString(2, column4Value);
            ps.setString(3, productId);

            // Execute the update query
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Successfully updated product with ID: " + productId);
            } else {
                System.out.println("No matching product found with ID: " + productId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

