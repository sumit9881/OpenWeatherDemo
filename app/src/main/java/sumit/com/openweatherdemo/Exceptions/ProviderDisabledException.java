package sumit.com.openweatherdemo.Exceptions;

public class ProviderDisabledException extends Exception {

    /**
     * This exception is thrown when all of the providers are disabled.
     * @param message
     */
    public ProviderDisabledException(String message) {
        super(message);
    }
}
