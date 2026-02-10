package exception;

public class FoodItemNotValidException extends ValidationException {
    public FoodItemNotValidException(String message) {
        super(message);
    }
}

