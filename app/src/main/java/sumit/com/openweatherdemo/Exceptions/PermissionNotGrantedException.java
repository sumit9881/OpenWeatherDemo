package sumit.com.openweatherdemo.Exceptions;


public class PermissionNotGrantedException extends  Exception {

    /**
     * This exception is thrown when permission is not granted.
     * @param message
     */
    public PermissionNotGrantedException(String message) {
        super(message);
    }
}
