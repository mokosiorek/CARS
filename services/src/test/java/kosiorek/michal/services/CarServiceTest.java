package kosiorek.michal.services;

import kosiorek.michal.model.Car;
import kosiorek.michal.model.enums.Color;
import kosiorek.michal.model.enums.SortType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CarServiceTest {

    private CarService carService;

    @BeforeEach
    public void init() {
        carService = new CarService("testcars.json");
        //System.out.println(carService.toString());
    }

    @Test
    @DisplayName("Check if sort by model desc works")
    public void test1() {

        // WHEN
        List<Car> cars = carService.sort(SortType.MODEL, true);
        List<String> models = cars.stream().map(Car::getModel).collect(Collectors.toList());

        // THEN
        Assertions.assertLinesMatch(models, List.of("MAZDA", "MAZDA", "BMW"));
    }

    @Test
    @DisplayName("Check if sort by color asc works")
    public void test2() {

        //GIVEN
        CarService carService2 = new CarService("testcars2.json");

        // WHEN
        List<Car> cars = carService2.sort(SortType.COLOR, false);
        List<String> models = cars.stream().map(Car::getModel).collect(Collectors.toList());

        // THEN
        Assertions.assertLinesMatch(models, List.of("BMW", "BMW", "MAZDA", "MAZDA"));
    }


    @Test
    @DisplayName("Check if sort by price asc works")
    public void test3() {

        //GIVEN
        CarService carService3 = new CarService("testcars3.json");

        // WHEN
        List<Car> cars = carService3.sort(SortType.PRICE, false);
        List<String> models = cars.stream().map(Car::getModel).collect(Collectors.toList());

        // THEN
        Assertions.assertLinesMatch(models, List.of("MAZDA", "BMW", "BMW", "MAZDA"));
    }

    @Test
    @DisplayName("Check if deleting cars with mileage lower than 2000 works")
    public void test4() {

        //GIVEN
        CarService carService3 = new CarService("testcars3.json");

        // WHEN
        List<Car> cars = carService3.deleteCarsWithMileageLowerThan(2000);
        List<String> models = cars.stream().map(Car::getModel).collect(Collectors.toList());

        // THEN
        Assertions.assertLinesMatch(models, List.of("MAZDA"));
    }

    @Test
    @DisplayName("Check if deleting cars with mileage lower than 2500 works")
    public void test5() {

        //GIVEN
        CarService carService3 = new CarService("testcars3.json");

        // WHEN
        List<Car> cars = carService3.deleteCarsWithMileageLowerThan(2500);
        List<String> models = cars.stream().map(Car::getModel).collect(Collectors.toList());

        // THEN
        Assertions.assertLinesMatch(models, List.of("MAZDA"));
    }

    @Test
    @DisplayName("Check if getting map of colors and numbers works")
    public void test6() {

        //GIVEN
        CarService carService3 = new CarService("testcars3.json");

        // WHEN
        Map<Color, Long> cars = carService3.groupByColorAndCount();
        //Map.of(Color.BLACK,2,Color.RED,1,Color.WHITE,1);

        // THEN
        Assertions.assertTrue(cars.containsKey(Color.BLACK) && cars.get(Color.BLACK) == 2);
        Assertions.assertTrue(cars.containsKey(Color.RED) && cars.get(Color.RED) == 1);
        Assertions.assertTrue(cars.containsKey(Color.WHITE) && cars.get(Color.WHITE) == 1);
        //Assertions.assertTrue(cars.containsKey(Color.GREEN)&&cars.get(Color.GREEN)==1);

    }

    @Test
    @DisplayName("Check if getting map of model and the most expensive car")
    public void test7() {

        //GIVEN
        CarService carService3 = new CarService("testcars3.json");

        // WHEN
        Map<String, Car> cars = carService3.groupByModelAndMostExpensiveCar();
        Car car1 = Car.builder().model("MAZDA").color(Color.WHITE).price(BigDecimal.valueOf(160)).mileage(2500).components(List.of("AIR CONDITIONING", "BLUETOOTH", "ALLOY WHEELS")).build();
        Car car2 = Car.builder().model("BMW").color(Color.RED).price(BigDecimal.valueOf(125)).mileage(1500).components(List.of("ABS")).build();
        Map<String, Car> expectedMap = Map.of("MAZDA",car1,"BMW",car2);

        // THEN
        //Assertions.assertTrue(cars.containsKey("MAZDA") && cars.get("MAZDA").equals(car1));
        //Assertions.assertTrue(cars.containsKey("BMW") && cars.get("BMW").equals(car2));
        Assertions.assertEquals(cars,expectedMap);
    }

    @Test
    @DisplayName("Check if getting list of most expensive cars")
    public void test8() {

        //GIVEN
        CarService carService3 = new CarService("testcars3.json");

        // WHEN
        List<Car> cars = carService3.getCarWithTheBiggestPrice();
        List<String> models = cars.stream().map(Car::getModel).collect(Collectors.toList());
        // THEN
        Assertions.assertLinesMatch(models, List.of("MAZDA"));

    }

    @Test
    @DisplayName("Check if getting list of most expensive cars")
    public void test9() {

        //GIVEN
        CarService carService3 = new CarService("testcars4.json");

        // WHEN
        List<Car> cars = carService3.getCarWithTheBiggestPrice();
        List<String> models = cars.stream().map(Car::getModel).collect(Collectors.toList());
        // THEN
        Assertions.assertLinesMatch(models, List.of("BMW", "BMW"));

    }

    @Test
    @DisplayName("Check if sorting components lists works")
    public void test10() {

        //GIVEN
        CarService carService3 = new CarService("testcars4.json");

        // WHEN
        List<Car> cars = carService3.getCarsWithSortedComponents();
        List<List<String>> models = cars.stream().map(Car::getComponents).collect(Collectors.toList());
        List<List<String>> actualmodels = List.of(List.of("ABS"), List.of("AIR CONDITIONING", "ALLOY WHEELS", "BLUETOOTH"), List.of("ABLUETOOTH", "AIR CON"), List.of("ABS"));

        // THEN
        Assertions.assertEquals(models, actualmodels);
    }

    @Test
    @DisplayName("Check if getting components map with car lists works")
    public void test11() {

        //GIVEN
        CarService carService3 = new CarService("testcars4.json");

        // WHEN
        Map<String, List<Car>> cars = carService3.getMapOfComponentsAndCarsWithThatComponent();
       // List<List<String>> models = cars.stream().map(Car::getComponents).collect(Collectors.toList());
        Car car1 = Car.builder().model("MAZDA").color(Color.WHITE).price(BigDecimal.valueOf(160)).mileage(2500).components(List.of("AIR CONDITIONING", "BLUETOOTH", "ALLOY WHEELS")).build();
        Car car2 = Car.builder().model("BMW").color(Color.RED).price(BigDecimal.valueOf(170)).mileage(1500).components(List.of("ABS")).build();
        Car car3 = Car.builder().model("BMW").color(Color.BLACK).price(BigDecimal.valueOf(170)).mileage(1500).components(List.of("ABS")).build();
        Car car4 = Car.builder().model("MAZDA").color(Color.BLACK).price(BigDecimal.valueOf(100)).mileage(500).components(List.of("AIR CON", "ABLUETOOTH")).build();

        Map<String, List<Car>> expectedMap = Map.of("ABS", List.of(car3,car2),"AIR CONDITIONING",List.of(car1),"BLUETOOTH",List.of(car1),"ALLOY WHEELS",List.of(car1),"AIR CON",List.of(car4),"ABLUETOOTH",List.of(car4));
        // THEN
        Assertions.assertEquals(cars, expectedMap);
    }

    @Test
    @DisplayName("Check if getting cars with prices between A and B works")
    public void test12() {

        //GIVEN
        CarService carService3 = new CarService("testcars4.json");

        // WHEN
        List<Car> cars = carService3.getCarsWithPriceBetween(BigDecimal.valueOf(110),BigDecimal.valueOf(200));

        Car car1 = Car.builder().model("MAZDA").color(Color.WHITE).price(BigDecimal.valueOf(160)).mileage(2500).components(List.of("AIR CONDITIONING", "BLUETOOTH", "ALLOY WHEELS")).build();
        Car car2 = Car.builder().model("BMW").color(Color.RED).price(BigDecimal.valueOf(170)).mileage(1500).components(List.of("ABS")).build();
        Car car3 = Car.builder().model("BMW").color(Color.BLACK).price(BigDecimal.valueOf(170)).mileage(1500).components(List.of("ABS")).build();
        Car car4 = Car.builder().model("MAZDA").color(Color.BLACK).price(BigDecimal.valueOf(100)).mileage(500).components(List.of("AIR CON", "ABLUETOOTH")).build();

        List<Car> expectedList = List.of(car3,car1,car2);
                // THEN
        //Assertions.assertTrue(cars.containsAll(expectedList));
        Assertions.assertEquals(cars, expectedList);
    }

}
