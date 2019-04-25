package kosiorek.michal.validators;

import kosiorek.michal.model.Car;

import java.util.HashMap;
import java.util.Map;

public class CarValidator implements Validator<Car> {
    private Map<String, String> errors = new HashMap<>();

    @Override
    public Map<String, String> validate(Car car) {

        errors.clear();

        if (car == null) {
            errors.put("car", "null");
        }

        if (!isModelValid(car)) {
            errors.put("model", "not valid: " + car.getModel());
        }

        if (!areComponentsValid(car)) {
            errors.put("components", "not valid: " + car.getComponents());
        }

        return errors;
    }

    @Override
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    private boolean isModelValid(Car car) {
        return car.getModel() != null && car.getModel().matches("[A-Z ]+");
    }

    private boolean areComponentsValid(Car car) {
        return car.getComponents() != null && car.getComponents().stream().allMatch(c -> c.matches("[A-Z ]+"));
    }


}
