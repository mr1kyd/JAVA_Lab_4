import java.sql.*;

/**
 * Created by andrey on 30.03.17.
 */
public class DBHelper {
    private static Connection connection;
    private static Statement statement;
    private static final String url = "jdbc:mysql://localhost:3306/lab4";
    private static final String name = "root";
    private static final String password = "2";

    public DBHelper() throws SQLException {
        connection = DriverManager.getConnection(url, name, password);
        statement = connection.createStatement();
    }

    private ResultSet executeQuery(String query) throws SQLException {
        return statement.executeQuery(query);
    }

    public String selectAll(){
        String query = "SELECT * FROM car LEFT JOIN fuel ON car.FUEL_ID = fuel.FUEL_ID";
        StringBuffer result = new StringBuffer("|      car_name      |  tank_volume  |  fuel_type  |  fuel_cost |\n" +
                        " ---------------------------------------------------------------\n");
        try {
            ResultSet rs = executeQuery(query);
            while(rs.next()){
                String carName = rs.getString("car_name");
                double tankVolume = rs.getDouble("tank_volume");
                String fuelType = rs.getString("fuel_type");
                double fuelCost = rs.getDouble("fuel_cost");
                result.append("|");
                result.append(format(20, carName));
                result.append(format(15, String.valueOf(tankVolume)));
                result.append(format(13, fuelType));
                result.append(format(12, String.valueOf(fuelCost)));
                result.append("\n ---------------------------------------------------------------\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(result.toString());
        return "   ";
    }

    public String selectFuel(){
        StringBuffer result = new StringBuffer("|  fuel_id  |  fuel_type  |  fuel_cost |\n" +
                                                    " -------------------------------------- \n");
        String query = "SELECT * FROM fuel";
        try {
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
                int fuelId = rs.getInt("fuel_id");
                String fuelType = rs.getString("fuel_type");
                double fuelCost = rs.getDouble("fuel_cost");
                result.append("|");
                result.append(format(11, String.valueOf(fuelId)));
                result.append(format(13, fuelType));
                result.append(format(12, String.valueOf(fuelCost)));
                result.append("\n -------------------------------------- \n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(result.toString());
        return result.toString();
    }

    public String selectCar(){
        StringBuffer result = new StringBuffer("|  car_id  |      car_name      |  tank_volume  |  fuel_type  |\n" +
                                                    " ----------------------------------------------------------- \n");
        String query = "SELECT * FROM car INNER JOIN fuel ON car.fuel_id = fuel.fuel_id";
        try {
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
                int carId = rs.getInt("car_id");
                String carName = rs.getString("car_name");
                double tankVolume = rs.getDouble("tank_volume");
                String fuelType = rs.getString("fuel_type");
                result.append("|");
                result.append(format(10, String.valueOf(carId)));
                result.append(format(20, carName));
                result.append(format(15, String.valueOf(tankVolume)));
                result.append(format(11, fuelType));
                result.append("\n -----------------------------------------------------------\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(result.toString());
        return result.toString();
    }

    public boolean insertFuel(String fuelType, double fuelCost){
        String query = "INSERT INTO fuel (fuel_type, fuel_cost) VALUES (?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, fuelType);
            ps.setDouble(2, fuelCost);
            return ps.execute();
        } catch (SQLException e) {
            System.out.println("Дублироввание уникальной записи");
            return false;
        }
    }

    public boolean insertCar(String carName, double tankVolume, int fuelID){
        String query = "INSERT INTO car (car_name, tank_volume, fuel_id) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, carName);
            ps.setDouble(2, tankVolume);
            ps.setInt(3, fuelID);
            return ps.execute();
        } catch (SQLException e) {
            System.out.println("Дублироввание уникальной записи");
            return false;
        }
    }

    public boolean deleteCar(int carID){
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM car WHERE car_id = ?;");
            ps.setInt(1, carID);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Невозможно удалить запись");
            return false;
        }
    }

    public boolean deleteFuel(int fuelID){
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM fuel WHERE fuel_id = ?;");
            ps.setInt(1, fuelID);
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Невозможно удалить запись, с ней связаны другие записи");
            return false;
        }
    }

    private String format(int length, String str){
        int delta = length - str.length();
        StringBuffer result = new StringBuffer();
        for(int i = 0; i < delta / 2; i++){
            result.append(" ");
        }
        result.append(str);
        for(int i = delta / 2 + str.length(); i < length; i++){
            result.append(" ");
        }
        result.append("|");
        return result.toString();
    }
}
