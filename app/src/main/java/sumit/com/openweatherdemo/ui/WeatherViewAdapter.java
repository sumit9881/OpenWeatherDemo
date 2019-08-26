package sumit.com.openweatherdemo.ui;

import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sumit.com.openweatherdemo.R;
import sumit.com.openweatherdemo.viewmodel.WeatherDetailModel;

public class WeatherViewAdapter extends RecyclerView.Adapter<WeatherViewAdapter.ViewHolder> {


    private List<WeatherDetailModel> mWeatherDetail = new ArrayList<>();

    public WeatherViewAdapter(List<WeatherDetailModel> items) {
        if (items != null) {
            mWeatherDetail.addAll(items);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_list_rowlayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        WeatherDetailModel detail = mWeatherDetail.get(position);
        Resources res = holder.itemView.getResources();

        holder.title.setText(String.format(res.getString(R.string.row_title), detail.getDayTemprature(), detail.getMain()));
        holder.descriptionView.setText(detail.getDescription());
        holder.mintemp.setText(String.format(res.getString(R.string.min_temp), detail.getMinTemprature()));
        holder.maxTemp.setText(String.format(res.getString(R.string.max_temp), detail.getMaxTemparature()));
        holder.date.setText(detail.getDate());
    }


    @Override
    public int getItemCount() {
        return mWeatherDetail.size();
    }


    /**
     * add Items to recycler view.
     * @param items  items to be added.
     */

    public void addItems(List<WeatherDetailModel> items) {
        mWeatherDetail.clear();
        mWeatherDetail.addAll(items);
        notifyDataSetChanged();
    }


    /**
     * remove all items.
     *
     */

    public void resetItems() {
        mWeatherDetail.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_temp_and_main)
        TextView title;

        @BindView(R.id.textview_detail)
        TextView descriptionView;

        @BindView(R.id.textView_max_temp)
        TextView maxTemp;

        @BindView(R.id.textView_min_temp)
        TextView mintemp;

        @BindView(R.id.textview_date)
        TextView date;


        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }

}
