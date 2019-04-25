package kosiorek.michal.services;

import kosiorek.michal.converters.CarsJsonConverter;
import kosiorek.michal.exceptions.ExceptionCode;
import kosiorek.michal.exceptions.MyException;
import kosiorek.michal.model.Car;
import kosiorek.michal.model.enums.Color;
import kosiorek.michal.model.enums.SortType;
import kosiorek.michal.validators.CarValidator;
import org.eclipse.collections.impl.collector.BigDecimalSummaryStatistics;
import org.eclipse.collections.impl.collector.Collectors2;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CarService {

    private final List<Car> cars;

    public CarService(String jsonFilename) {
        cars = getCarsFromJson(jsonFilename);
    }

    private List<Car> getCarsFromJson(String jsonFilename) {

        CarValidator carValidator = new CarValidator();
        AtomicInteger counter = new AtomicInteger(1);

        return new CarsJsonConverter(jsonFilename)
                .fromJson()
                .orElseThrow(() -> new MyException(ExceptionCode.OTHER, "Car service json file parse"))
                .stream()
                .filter(car -> {

                    Map<String, String> errors = carValidator.validate(car);

                    if (carValidator.hasErrors()) {
                        System.out.println("---------------------------------- VALIDATION ERROR -----------------------");
                        System.out.println("CAR NO. " + counter.get());
                        errors.forEach((k, v) -> System.out.println(k + " " + v));
                    }

                    counter.incrementAndGet();

                    return !carValidator.hasErrors();

                })
                .collect(Collectors.toList());

    }

    public void addCar(Car car) {

        if (car == null) {
            throw new MyException(ExceptionCode.OTHER, "Car is null");
        }

        this.cars.add(car);
    }

    public List<Car> sort(SortType sortType, boolean descending) {

        Stream<Car> carsStream = this.cars.stream();

        switch (sortType) {
            case MODEL:
                carsStream = this.cars.stream().sorted(Comparator.comparing(Car::getModel));
                break;
            case PRICE:
                carsStream = this.cars.stream().sorted(Comparator.comparing(Car::getPrice));
                break;
            case MILEAGE:
                carsStream = this.cars.stream().sorted(Comparator.comparing(Car::getMileage));
                break;
            case COLOR:
                carsStream = this.cars.stream().sorted(Comparator.comparing(Car::getColor));
                break;
        }

        List<Car> sortedCars = carsStream.collect(Collectors.toList());

        if (descending) {
            Collections.reverse(sortedCars);
        }

        return sortedCars;
    }


    public List<Car> deleteCarsWithMileageLowerThan(double x) {

        return this.cars.stream().
                filter(c -> c.getMileage() >= x)
                .collect(Collectors.toList());

    }

    public Map<Color, Long> groupByColorAndCount() {

        return this.cars
                .stream().collect(Collectors.groupingBy(Car::getColor, Collectors.counting()))
                .entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::max, LinkedHashMap::new));
    }

    public Map<String, Car> groupByModelAndMostExpensiveCar() {

        return cars
                .stream()
                .collect(Collectors.groupingBy(
                        Car::getModel,
                        Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparing(Car::getPrice)), maxPriceCar -> maxPriceCar.orElse(null))
                        )
                );

    }

    public void statistics() {

        System.out.println("MILEAGE");
        DoubleSummaryStatistics dss = cars.stream()
                .collect(Collectors.summarizingDouble(Car::getMileage));
        System.out.println("MIN: " + dss.getMin());
        System.out.println("MAX: " + dss.getMax());
        System.out.println("AVG: " + dss.getAverage());

        BigDecimalSummaryStatistics carPriceSummaryStatistics = cars.stream().collect(Collectors2.summarizingBigDecimal(Car::getPrice));

        System.out.println("PRICE");
        System.out.println("MIN: " + carPriceSummaryStatistics.getMin());
        System.out.println("MAX: " + carPriceSummaryStatistics.getMax());
        System.out.println("AVG: " + carPriceSummaryStatistics.getAverage());
    }

    public List<Car> getCarWithTheBiggestPrice() {

        return cars
                .stream()
                .collect(Collectors.groupingBy(Car::getPrice))
                .entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getKey))
                .orElseThrow(() -> new MyException(ExceptionCode.OTHER, "No most expensive car"))
                .getValue();

    }

    public List<Car> getCarsWithSortedComponents() {
        return cars.stream()
                .peek(car -> car.setComponents(car.getComponents().stream().sorted().collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    public Map<String, List<Car>> getMapOfComponentsAndCarsWithThatComponent() {

        return cars.stream()
                .flatMap(car -> car.getComponents().stream())
                .collect(Collectors.toSet())
                .stream().collect(Collectors.toMap(
                        component -> component,
                        component -> cars.stream().filter(car -> car.getComponents().contains(component)).collect(Collectors.toList())
                ));
    }


    public List<Car> getCarsWithPriceBetween(BigDecimal priceFrom, BigDecimal priceTo) {

        if (priceFrom.compareTo(priceTo) >= 0) {
            throw new MyException(ExceptionCode.OTHER, "Price range is not valid");
        }

        return cars
                .stream()
                .filter(car -> car.getPrice().compareTo(priceFrom) >= 0 && car.getPrice().compareTo(priceTo) <= 0)
                .collect(Collectors.toList());

    }

    @Override
    public String toString() {
        return this.cars.stream().map(Car::toString).collect(Collectors.joining("\n"));

    }


}