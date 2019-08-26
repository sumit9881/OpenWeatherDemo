package sumit.com.openweatherdemo.viewmodel;

public class WeatherDetailModel {

    double dayTemprature;
    double minTemprature;
    double maxTemparature;
    String main;
    String description;
    String date;

    public double getDayTemprature() {
        return dayTemprature;
    }

    public double getMaxTemparature() {
        return maxTemparature;
    }

    public double getMinTemprature() {
        return minTemprature;
    }

    public String getDescription() {
        return description;
    }

    public String getMain() {
        return main;
    }

    public String getDate() {
        return date;
    }

    public void setDayTemprature(double temp) {
        dayTemprature = temp;
    }

    public void  setMaxTemparature(double temp) {
        maxTemparature = temp;
    }

    public void setMinTemprature(double temp) {
        minTemprature = temp;
    }

    public void setDescription(String desc) {
        description = desc;
    }

    public void setMain(String title) {
         main = title;
    }

    public void setDate(String dt) {
        this.date = dt;
    }

}
