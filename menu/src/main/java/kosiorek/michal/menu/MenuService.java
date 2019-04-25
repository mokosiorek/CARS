package kosiorek.michal.menu;

import kosiorek.michal.exceptions.MyException;
import kosiorek.michal.model.Car;
import kosiorek.michal.model.enums.SortType;
import kosiorek.michal.services.CarService;
import kosiorek.michal.services.UserDataService;

import java.math.BigDecimal;

public class MenuService {

    private CarService carService;
    private UserDataService userDataService = new UserDataService();

    public MenuService(String filename) {
        this.carService = new CarService(filename);
    }

    public void mainMenu() {

        String menu;
        while (true) {
            try {
                do {
                    displayMenu();
                    menu = userDataService.getString("Enter number:", "");

                    switch (menu) {
                        case "1":
                            option1();
                            break;
                        case "2":
                            option2();
                            break;
                        case "3":
                            option3();
                            break;
                        case "4":
                            option4();
                            break;
                        case "5":
                            option5();
                            break;
                        case "6":
                            option6();
                            break;
                        case "7":
                            option7();
                            break;
                        case "8":
                            option8();
                            break;
                        case "9":
                            option9();

                            break;
                        case "10":
                            option10();
                            break;
                        case "11":
                            option11();
                            break;
                        case "12":
                            userDataService.close();
                            System.out.println("The End");
                            return;
                        default:
                            System.out.println("Invalid option in menu. Enter number again.");
                            break;
                    }
                } while (true);

            } catch (MyException e) {
                System.out.println(e.getExceptionInfo());
            }
        }

    }

    private void displayMenu() {
        System.out.println("Menu - enter the number:");
        System.out.println("1 - Add new car.");
        System.out.println("2 - Print all cars.");
        System.out.println("3 - Print sorted cars.");
        System.out.println("4 - Print all cars with mileage higher than x.");
        System.out.println("5 - Print how many cars are in certain colors.");
        System.out.println("6 - Print the most expensive car in models.");
        System.out.println("7 - Print statistics.");
        System.out.println("8 - Print car/cars with the biggest price.");
        System.out.println("9 - Print cars with sorted components.");
        System.out.println("10 - Print map of components and cars with that component.");
        System.out.println("11 - Print cars with price between A and B.");
        System.out.println("12 - Exit");
    }

    private void option1() {

        Car car = Car.builder()
                .model(userDataService.getString("Enter car model name:", "[A-Z ]+"))
                .mileage(userDataService.getInt("Enter car mileage"))
                .color(userDataService.getColor())
                .components(userDataService.getComponents())
                .price(userDataService.getBigDecimal("Enter car price:"))
                .build();
        carService.addCar(car);
    }

    private void option2() {

        System.out.println(carService);
    }

    private void option3() {

        boolean descending = userDataService.getBoolean("Descending? ");
        SortType sortType = userDataService.getSortType();
        carService.sort(sortType, descending).forEach(System.out::println);
    }

    private void option4() {

        int x = userDataService.getInt("Enter X:");
        System.out.println(carService.deleteCarsWithMileageLowerThan(x).toString());
    }

    private void option5() {
        System.out.println(carService.groupByColorAndCount().toString());
    }

    private void option6() {
        System.out.println(carService.groupByModelAndMostExpensiveCar().toString());
    }

    private void option7() {
        carService.statistics();
    }

    private void option8() {
        System.out.println(carService.getCarWithTheBiggestPrice().toString());
    }
    private void option9() {
        System.out.println(carService.getCarsWithSortedComponents().toString());
    }
    private void option10() {
        System.out.println(carService.getMapOfComponentsAndCarsWithThatComponent().toString());
    }
    private void option11() {
        int a = userDataService.getInt("Enter A:");
        int b = userDataService.getInt("Enter B:");
        System.out.println(carService.getCarsWithPriceBetween(new BigDecimal(a), new BigDecimal(b)).toString());
    }

}
