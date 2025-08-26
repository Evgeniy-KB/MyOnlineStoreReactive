package task7.exception;

public class ErrorResponse {
    private final String message;

    public ErrorResponse(String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
