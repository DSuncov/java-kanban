package exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Объект отсутствует.";
    }
}
