package io.overpoet.netatmo.weather;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import io.overpoet.Key;
import io.overpoet.core.apparatus.Apparatus;
import io.overpoet.core.apparatus.ApparatusType;
import io.overpoet.core.apparatus.SimpleApparatus;
import io.overpoet.core.measurement.Speed;
import io.overpoet.core.platform.Platform;
import io.overpoet.core.platform.PlatformContext;
import io.overpoet.json.AbstractJSONSensorLogic;
import io.overpoet.core.sensor.Sensor;
import io.overpoet.netatmo.weather.temperature.OutsideTemperatureSensorLogic;
import io.overpoet.netatmo.weather.wind.GustAngleSensorLogic;
import io.overpoet.netatmo.weather.wind.GustStrengthSensorLogic;
import io.overpoet.netatmo.weather.wind.WindAngleSensorLogic;
import io.overpoet.netatmo.weather.wind.WindStrengthSensorLogic;
import net.minidev.json.JSONArray;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static io.overpoet.core.apparatus.ApparatusType.ANEMOMETER;
import static io.overpoet.core.apparatus.ApparatusType.THERMOMETER;
import static java.net.URI.create;

public class NetatmoWeatherPlatform implements Platform, LogicRegistry {

    private static final Key KEY = Key.of("netatmo", "weather");

    private static final URI baseURI = create("https://api.netatmo.com/");

    private static final URI tokenURI = baseURI.resolve(create("/oauth2/token"));

    private static final URI stationsDataURI = baseURI.resolve(create("/api/getstationsdata"));

    public NetatmoWeatherPlatform() {
    }

    @Override
    public void initialize(PlatformContext context) {
        this.context = context;
        try {
            String data = getData();
            if ( ! initialize(data) ) {
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        context.executor().scheduleAtFixedRate(this::poll, 0, 10, TimeUnit.MINUTES);
    }

    private boolean initialize(String data) {
        System.err.println( "--> " + data);
        if ( data.contains("error")) {
            return false;
        }
        ReadContext ctx = JsonPath.parse(data);
        initializeOutside(ctx);
        //initializeWind(ctx);
        return true;
    }

    private void poll() {
        try {
            String data = getData();
            ReadContext ctx = JsonPath.parse(data);

            for (AbstractJSONSensorLogic<?, ?> logic : this.logics) {
                logic.process(ctx);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeOutside(ReadContext ctx) {
        Key key = KEY.append("outside");

        Set<Sensor<?>> sensors = new HashSet<>();
        if (OutsideTemperatureSensorLogic.isApplicable(ctx)) {
            sensors.add(OutsideTemperatureSensorLogic.of(key, this));
        }
        if (sensors.isEmpty()) {
            return;
        }
        Apparatus apparatus = new SimpleApparatus(THERMOMETER, key, sensors, Collections.emptySet());

        this.context.connect(apparatus);
    }

    private void initializeWind(ReadContext ctx) {

        Key key = KEY.append("wind");

        int windUnit = (int) ((JSONArray)ctx.read(".user.administrative.windunit")).get(0);

        AbstractJSONSensorLogic.Converter<Speed,Integer> converter = null;

        if ( windUnit == 0 ) {
            converter = Speed::kilometersPerHour;
        } else if ( windUnit == 1 ) {
            converter = Speed::milesPerHour;
        } else if ( windUnit == 4 ) {
            converter = Speed::knots;
        }

        Set<Sensor<?>> sensors = new HashSet<>();
        if (WindStrengthSensorLogic.isApplicable(ctx)) {
            sensors.add(WindStrengthSensorLogic.of(key, converter, this));
        }
        if (WindAngleSensorLogic.isApplicable(ctx)) {
            sensors.add(WindAngleSensorLogic.of(key, this));
        }
        if (GustStrengthSensorLogic.isApplicable(ctx)) {
            sensors.add(GustStrengthSensorLogic.of(key, converter, this));
        }
        if (GustAngleSensorLogic.isApplicable(ctx)) {
            sensors.add(GustAngleSensorLogic.of(key, this));
        }
        if (sensors.isEmpty()) {
            return;
        }

        Apparatus apparatus = new SimpleApparatus(ANEMOMETER, key, sensors, Collections.emptySet());

        this.context.connect(apparatus);
    }

    private String getToken() throws IOException {
        HttpUrl url = HttpUrl.get(tokenURI).newBuilder()
                .build();

        FormBody body = new FormBody.Builder()
                .add("grant_type", "password")
                .add("username", this.context.configuration().get("username"))
                .add("password", this.context.configuration().get("password"))
                .add("client_id", this.context.configuration().get("client_id"))
                .add("client_secret", this.context.configuration().get("client_secret"))
                .add("scope", "read_station")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Response response = client.newCall(request).execute();
        return JsonPath.read(response.body().string(), "access_token");
    }

    private String getData() throws IOException {
        String accessToken = getToken();
        HttpUrl url = HttpUrl.get(stationsDataURI).newBuilder()
                .build();

        FormBody body = new FormBody.Builder()
                .add("access_token", accessToken)
                .add("device_id", this.context.configuration().get("device_id"))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    @Override
    public <T extends AbstractJSONSensorLogic<?,?>> T register(T logic) {
        this.logics.add(logic);
        return logic;
    }

    private PlatformContext context;

    private Set<AbstractJSONSensorLogic<?,?>> logics = new HashSet<>();
}
