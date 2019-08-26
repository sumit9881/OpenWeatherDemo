package sumit.com.openweatherdemo.Exceptions;

public class ProviderNotEnabledException extends Exception {

    /**
     * This exception is thrown when providers are not enabled.
     * @param message
     */
    public ProviderNotEnabledException(String message) {
        super(message);
    }
}
