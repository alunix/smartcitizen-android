package cat.lafosca.smartcitizen.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cat.lafosca.smartcitizen.R;
import cat.lafosca.smartcitizen.commons.PrettyTimeHelper;
import cat.lafosca.smartcitizen.model.rest.Device;
import cat.lafosca.smartcitizen.model.rest.Sensor;
import cat.lafosca.smartcitizen.ui.widgets.SensorView;

public class KitDetailActivity extends AppCompatActivity {

    private Device mDevice;

    @InjectView(R.id.kit_detail_title)
    TextView mKitTitle;

    @InjectView(R.id.kit_detail_kit_type)
    TextView mKitType;

    @InjectView(R.id.kit_detail_timestamp)
    TextView mKitTimestamp;

    @InjectView(R.id.kit_detail_user)
    TextView mKitUser;

    @InjectView(R.id.kit_detail_location)
    TextView mKitLocation;

    @InjectView(R.id.sensors_layout)
    LinearLayout mSensorsLayout;

    public static Intent getCallingIntent(Context context, Device device) {

        Intent intent = new Intent(context, KitDetailActivity.class);
        intent.putExtra("Device", device);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kit_detail);

        ButterKnife.inject(this);

        mDevice = getIntent().getParcelableExtra("Device");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        setTextLabels();
        setSensorsView();
    }

    private void setSensorsView() {
        if (mDevice.getData()!= null && mDevice.getData().getSensors().size() > 0) {
            List<Sensor> sensors = mDevice.getData().getSensors();
            int numSensors = sensors.size();
            for (int i = 0; i<numSensors; i++) {
                Sensor sensor = sensors.get(i);

                SensorView sensorView = new SensorView(this);
                sensorView.setSensorName(sensor.getPrettyName(), 0);
                float sensorValue = sensor.getValue();
                sensorView.setSensorValue(sensorValue + " " + sensor.getUnit());

                if (i == numSensors - 1) {
                    sensorView.findViewById(R.id.sensor_separator).setVisibility(View.INVISIBLE);
                }

                mSensorsLayout.addView(sensorView);
            }
        }
    }

    private void setTextLabels() {
        mKitTitle.setText(mDevice.getName());
        mKitType.setText(mDevice.getKit().getName().toUpperCase());

        if (mDevice.getUpdatedAt() != null) {
            String updatedAt = "";
            try {
                updatedAt = PrettyTimeHelper.getInstance().getPrettyTime(mDevice.getUpdatedAt());
                mKitTimestamp.setText(updatedAt);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else {
            mKitTimestamp.setVisibility(View.GONE);
        }

        if (mDevice.getOwner() != null && mDevice.getOwner().getUsername() != null) {
            mKitUser.setText(mDevice.getOwner().getUsername());
        } else {
            mKitUser.setVisibility(View.GONE);
        }

        if (mDevice.getData() != null && mDevice.getData().getLocation() != null) {
            String location = mDevice.getData().getLocation().getCity();
            String country = mDevice.getData().getLocation().getCountry();
            if (country != null) {
                location += ", " + country;
            }
            mKitLocation.setText(location);
        } else {
            mKitLocation.setVisibility(View.GONE);
        }
    }
}
