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
import io.overpoet.core.apparatus.SimpleApparatus;
import io.overpoet.core.measurement.Temperature;
import io.overpoet.core.metadata.TemperatureMetadata;
import io.overpoet.core.platform.Platform;
import io.overpoet.core.platform.PlatformContext;
import io.overpoet.core.sensor.Sensor;
import io.overpoet.core.sensor.TemperatureSensor;
import io.overpoet.json.JSONSensorLogic;
import io.overpoet.json.JSONSensorLogicFactory;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.overpoet.core.apparatus.ApparatusType.THERMOMETER;
import static java.net.URI.create;

public class NetatmoWeatherPlatform implements Platform {
    private static final Logger LOG = LoggerFactory.getLogger(NetatmoWeatherPlatform.class);

    private static final Key KEY = Key.of("netatmo", "weather");

    private static final URI baseURI = create("https://api.netatmo.com/");

    private static final URI tokenURI = baseURI.resolve(create("/oauth2/token"));

    private static final URI stationsDataURI = baseURI.resolve(create("/api/getstationsdata"));

    public NetatmoWeatherPlatform() {
    }

    @Override
    public String id() {
        return "netatmo-weather";
    }

    @Override
    public String name() {
        return "Netatmo Weather Station";
    }

    @Override
    public void initialize(PlatformContext context) {
        this.context = context;
        try {
            String data = getData();
            if (!initialize(data)) {
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        context.executor().scheduleAtFixedRate(this::poll, 0, 10, TimeUnit.MINUTES);
    }

    private boolean initialize(String data) {
        if (data.contains("error")) {
            return false;
        }
        ReadContext ctx = JsonPath.parse(data);
        initializeOutside(ctx);
        //initializeWind(ctx);
        return true;
    }

    private void poll() {
        LOG.debug("polling");
        try {
            String data = getData();
            System.err.println(data);
            ReadContext ctx = JsonPath.parse(data);

            for (JSONSensorLogic<?, ?> logic : this.logics) {
                logic.process(ctx);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //private final static JsonPath OUTSIDE_TEMP = JsonPath.compile("$.body.devices[0].modules[?(@.type == 'NAModule1')].dashboard_data.Temperature");
    //private final static JsonPath OUTSIDE_TEMP_MIN = JsonPath.compile("$.body.devices[0].modules[?(@.type == 'NAModule1')].dashboard_data.min_temp");
    //private final static JsonPath OUTSIDE_TEMP_MAX = JsonPath.compile("$.body.devices[0].modules[?(@.type == 'NAModule1')].dashboard_data.max_temp");
    private final static JSONSensorLogicFactory<Temperature, Double> OUTSIDE_TEMP_CURRENT
            = new JSONSensorLogicFactory<>(Double.class,
                                           Temperature::celsius,
                                           JsonPath.compile("$.body.devices[0].modules[?(@.type == 'NAModule1')].dashboard_data.Temperature")
    );

    private final static JSONSensorLogicFactory<Temperature, Double> OUTSIDE_TEMP_MIN
            = new JSONSensorLogicFactory<>(Double.class,
                                           Temperature::celsius,
                                           JsonPath.compile("$.body.devices[0].modules[?(@.type == 'NAModule1')].dashboard_data.min_temp")
    );

    private final static JSONSensorLogicFactory<Temperature, Double> OUTSIDE_TEMP_MAX
            = new JSONSensorLogicFactory<>(Double.class,
                                           Temperature::celsius,
                                           JsonPath.compile("$.body.devices[0].modules[?(@.type == 'NAModule1')].dashboard_data.max_temp")
    );

    private void initializeOutside(ReadContext ctx) {
        Key key = KEY.append("outside");

        //Set<Sensor<?>> sensors = new HashSet<>();
        if (OUTSIDE_TEMP_CURRENT.isApplicable(ctx)) {
            LOG.info("Adding outside current temperature");
            Apparatus apparatus = new SimpleApparatus(THERMOMETER,
                                                      key,
                                                      Collections.singleton(
                                                              new TemperatureSensor(
                                                                      key.append("temperature", "current"),
                                                                      TemperatureMetadata.DEFAULT,
                                                                      register(OUTSIDE_TEMP_CURRENT.build()))),
                                                      Collections.emptySet());
            this.context.connect(apparatus);
        }
        if (OUTSIDE_TEMP_MIN.isApplicable(ctx)) {
            LOG.info("Adding outside min temperature");
            Apparatus apparatus = new SimpleApparatus(THERMOMETER,
                                                      key,
                                                      Collections.singleton(
                                                              new TemperatureSensor(
                                                                      key.append("temperature", "minimum"),
                                                                      TemperatureMetadata.DEFAULT,
                                                                      register(OUTSIDE_TEMP_MIN.build()))),
                                                      Collections.emptySet());
            this.context.connect(apparatus);
        }
        if (OUTSIDE_TEMP_MAX.isApplicable(ctx)) {
            LOG.info("Adding outside max temperature");
            Apparatus apparatus = new SimpleApparatus(THERMOMETER,
                                                      key,
                                                      Collections.singleton(
                                                              new TemperatureSensor(
                                                                      key.append("temperature", "maximum"),
                                                                      TemperatureMetadata.DEFAULT,
                                                                      register(OUTSIDE_TEMP_MAX.build()))),
                                                      Collections.emptySet());
            this.context.connect(apparatus);
            //sensors.add(new TemperatureSensor(key.append("temperature", "maximum"), TemperatureMetadata.DEFAULT, register(OUTSIDE_TEMP_MAX.build())));
        }
        //if (sensors.isEmpty()) {
        //return;
        //}
        //Apparatus apparatus = new SimpleApparatus(THERMOMETER, key, sensors, Collections.emptySet());
        //this.context.connect(apparatus);
    }

    /*
    private void initializeWind(ReadContext ctx) {

        Key key = KEY.append("wind");

        int windUnit = (int) ((JSONArray) ctx.read(".user.administrative.windunit")).get(0);

        JSONSensorLogic.Converter<Speed, Integer> converter = null;

        if (windUnit == 0) {
            converter = Speed::kilometersPerHour;
        } else if (windUnit == 1) {
            converter = Speed::milesPerHour;
        } else if (windUnit == 4) {
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

     */

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

    public <T, JSONTYPE> JSONSensorLogic<T, JSONTYPE> register(JSONSensorLogic<T, JSONTYPE> logic) {
        this.logics.add(logic);
        return logic;
    }

    private PlatformContext context;

    private Set<JSONSensorLogic<?, ?>> logics = new HashSet<>();
}
