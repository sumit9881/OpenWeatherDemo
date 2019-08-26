package sumit.com.openweatherdemo.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import sumit.com.openweatherdemo.R;
import sumit.com.openweatherdemo.viewmodel.OpenWeatherViewModel;
import sumit.com.openweatherdemo.viewmodel.OpenWeatherViewModelFactory;
import sumit.com.openweatherdemo.viewmodel.Response;
import sumit.com.openweatherdemo.viewmodel.WeatherDetailModel;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1001;

    @Inject
    OpenWeatherViewModelFactory mOpenWeatherViewModelFactory;

    @BindView(R.id.edit_text_search_string)
    EditText mSearchView;

    @BindView(R.id.recyvlerview_serach_result)
    RecyclerView mResultRecyclerView;

    @BindView(R.id.progressbar)
    ProgressBar mProgressbar;

    @BindView(R.id.text_view_current_location)
    TextView mCurrentLocationTextView;


    private OpenWeatherViewModel mOpenWeatherViewModel;

    private WeatherViewAdapter mAdapter;
    private AlertDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAdapter = new WeatherViewAdapter(null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mResultRecyclerView.setLayoutManager(layoutManager);
        mResultRecyclerView.setAdapter(mAdapter);

        mOpenWeatherViewModel = ViewModelProviders.of(this, mOpenWeatherViewModelFactory).get(OpenWeatherViewModel.class);
        mOpenWeatherViewModel.response().observe(this, response -> processResponse(response));

        initListeners();
        initAlertDialog();
    }

    private void initListeners() {
        mSearchView.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
                mOpenWeatherViewModel.loadExtendedWeather(v.getText().toString());
                return true;
            }
            return false;
        });

        mSearchView.setOnFocusChangeListener((v, focus) -> {
            if (focus) {
                mCurrentLocationTextView.setVisibility(View.VISIBLE);
                mCurrentLocationTextView.bringToFront();
            } else {
                mCurrentLocationTextView.setVisibility(View.GONE);
            }
        });

        mCurrentLocationTextView.setOnClickListener(v -> {
            mOpenWeatherViewModel.loadCurrentCityWeather();
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);

        });

    }

    private void initAlertDialog() {
        mDialog =  new AlertDialog.Builder(this)
                .setTitle(R.string.error)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    dialog.dismiss();
                    mOpenWeatherViewModel.clearErrorUI();
                }).create();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    private void processResponse(Response response) {
        switch (response.status) {
            case LOADING:
                renderLoadingState();
                break;

            case SUCCESS:
                renderDataState(response.data);
                break;

            case ERROR:
                renderErrorState(response.error);
                break;
            case SHOW_PERMISSION:
                requestLocationPermission();
                break;
            case ERROR_CANCELLED:
                handleErrorResets();

        }
    }

    private void renderLoadingState() {
        mSearchView.setText("");
        mSearchView.clearFocus();
        mProgressbar.setVisibility(View.VISIBLE);
    }

    private void renderDataState(List<WeatherDetailModel> data) {
        mProgressbar.setVisibility(View.GONE);
        mAdapter.addItems(data);
    }

    private void renderErrorState(String error) {
        mProgressbar.setVisibility(View.GONE);
        mAdapter.resetItems();
        mDialog.setMessage(error);
        mDialog.show();
    }


    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_CODE);

    }

    private void handleErrorResets() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                mOpenWeatherViewModel.loadCurrentCityWeather();

            }
        }
    }

}
