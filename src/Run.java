import java.sql.SQLException;
import java.util.Scanner;

/**
 * Created by andrey on 30.03.17.
 */
public class Run {
    public static void main(String[] args) {
        try {
            DBHelper helper = new DBHelper();
            int choice = -1;
            while (choice != 0){
                System.out.print("1. Select all records\n2. Insert fuel\n3. Insert car\n4. Delete fuel\n5. Delete car\n0. Exit\n>");
                Scanner sc = new Scanner(System.in);
                choice = sc.nextInt();
                sc.nextLine();
                switch (choice){
                    case 1:
                        System.out.println(helper.selectAll());
                        break;
                    case 2:
                        System.out.print("Fuel name: ");
                        String fuelName = sc.next();
                        System.out.print("Fuel cost: ");
                        double cost = sc.nextDouble();
                        helper.insertFuel(fuelName, cost);
                        break;
                    case 3:
                        System.out.print("Car name: ");
                        String carName = sc.nextLine();
                        System.out.print("Tank volume: ");
                        double tankVolume = sc.nextDouble();
                        int fuelID = selectID(helper, "inserting", "fuel", sc);
                        helper.insertCar(carName, tankVolume, fuelID);
                        break;
                    case 4:
                        helper.deleteFuel(selectID(helper, "deleting", "fuel", sc));
                        break;
                    case 5:
                        helper.deleteCar(selectID(helper, "deleting", "car", sc));
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int selectID(DBHelper helper, String action, String table, Scanner sc){
        if(table.equals("fuel")) {
            helper.selectFuel();
        }
        else if(table.equals("car")){
            helper.selectCar();
        }
        System.out.print("Select " + table + " id for " + action +": ");
        return sc.nextInt();
    }
}


