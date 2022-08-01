package victor.testing.mutation;

public class CustomerValidationException extends RuntimeException {
    private final String fieldInError;

    CustomerValidationException(String fieldInError) {
        this.fieldInError = fieldInError;
    }

    public String getFieldInError() {
        return fieldInError;
    }
}
