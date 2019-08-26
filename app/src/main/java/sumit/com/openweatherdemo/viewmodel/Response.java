package sumit.com.openweatherdemo.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import static sumit.com.openweatherdemo.viewmodel.Status.ERROR;
import static sumit.com.openweatherdemo.viewmodel.Status.ERROR_CANCELLED;
import static sumit.com.openweatherdemo.viewmodel.Status.LOADING;
import static sumit.com.openweatherdemo.viewmodel.Status.SHOW_PERMISSION;
import static sumit.com.openweatherdemo.viewmodel.Status.SUCCESS;


public class Response {
    public final Status status;
    public final List<WeatherDetailModel> data;
    public final String error;


    private Response(Status status, @Nullable List<WeatherDetailModel> data, @Nullable String error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static Response loading() {
        return new Response(LOADING, null, null);
    }

    public static Response success(@NonNull List<WeatherDetailModel> data) {
        return new Response(SUCCESS, data, null);
    }

    public static Response error(@NonNull Throwable error) {
        return new Response(ERROR, null, error.getMessage());
    }

    public static Response error(@NonNull String error) {
        return new Response(ERROR, null, error);
    }

    public static Response showPermission() {
        return new Response(SHOW_PERMISSION, null, null);
    }

    public static Response clearError() {
        return new Response(ERROR_CANCELLED, null, null);
    }
}
