package kosiorek.michal.validators;

import java.util.Map;

public interface Validator<T> {

    Map<String, String> validate(T t);
    boolean hasErrors();
}
