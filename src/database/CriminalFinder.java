package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CriminalFinder {

    public static String[] findCriminal() {

        try {

            Connection conn = DBConnection.connect();

            String sql = "SELECT name, crime FROM criminals LIMIT 1";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String name = rs.getString("name");
                String crime = rs.getString("crime");

                return new String[]{name, crime};

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;
    }
}