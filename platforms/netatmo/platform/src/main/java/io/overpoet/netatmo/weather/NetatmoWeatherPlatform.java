package io.overpoet.netatmo.weather;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import io.overpoet.spi.Key;
import io.overpoet.spi.apparatus.Apparatus;
import io.overpoet.spi.apparatus.SimpleApparatus;
import io.overpoet.spi.aspect.AspectBuilder;
import io.overpoet.spi.measurement.Temperature;
import io.overpoet.spi.metadata.ApparatusMetadata;
import io.overpoet.spi.metadata.SimpleApparatusMetadata;
import io.overpoet.spi.metadata.TemperatureMetadata;
import io.overpoet.spi.platform.Platform;
import io.overpoet.spi.platform.PlatformContext;
import io.overpoet.spi.json.JSONSensor;
import io.overpoet.spi.json.JSONSensorFactory;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.overpoet.spi.apparatus.ApparatusType.THERMOMETER;
import static java.net.URI.create;

public class NetatmoWeatherPlatform implements Platform {
    private static final Logger LOG = LoggerFactory.getLogger("overpoet.netatmo");

    private static final Key KEY = Key.of("netatmo", "weather");

    private static final URI baseURI = create("https://api.netatmo.com/");

    private static final URI tokenURI = baseURI.resolve(create("/oauth2/token"));

    private static final URI stationsDataURI = baseURI.resolve(create("/api/getstationsdata"));

    public NetatmoWeatherPlatform() {
    }

    //@Override
    //public String id() {
    //return "netatmo-weather";
    //}

    //@Override
    //public String name() {
    //return "Netatmo Weather Station";
    //}

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
            ReadContext ctx = JsonPath.parse(data);

            for (JSONSensor<?, ?> logic : this.logics) {
                logic.process(ctx);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //private final static JsonPath OUTSIDE_TEMP = JsonPath.compile("$.body.devices[0].modules[?(@.type == 'NAModule1')].dashboard_data.Temperature");
    //private final static JsonPath OUTSIDE_TEMP_MIN = JsonPath.compile("$.body.devices[0].modules[?(@.type == 'NAModule1')].dashboard_data.min_temp");
    //private final static JsonPath OUTSIDE_TEMP_MAX = JsonPath.compile("$.body.devices[0].modules[?(@.type == 'NAModule1')].dashboard_data.max_temp");
    private final static JSONSensorFactory<Temperature, Double> OUTSIDE_TEMP_CURRENT
            = new JSONSensorFactory<>(Double.class,
                                      Temperature::celsius,
                                      JsonPath.compile("$.body.devices[0].modules[?(@.type == 'NAModule1')].dashboard_data.Temperature")
    );

    private final static JSONSensorFactory<Temperature, Double> OUTSIDE_TEMP_MIN
            = new JSONSensorFactory<>(Double.class,
                                      Temperature::celsius,
                                      JsonPath.compile("$.body.devices[0].modules[?(@.type == 'NAModule1')].dashboard_data.min_temp")
    );

    private final static JSONSensorFactory<Temperature, Double> OUTSIDE_TEMP_MAX
            = new JSONSensorFactory<>(Double.class,
                                      Temperature::celsius,
                                      JsonPath.compile("$.body.devices[0].modules[?(@.type == 'NAModule1')].dashboard_data.max_temp")
    );

    private ApparatusMetadata thermometerMetadata(String name) {
        return new SimpleApparatusMetadata(
                THERMOMETER,
                name,
                "Netatmo",
                "Weather Station",
                "1.0",
                "10001"
        );
    }

    private void initializeOutside(ReadContext ctx) {
        Key key = KEY.append("outside");

        //Set<Sensor<?>> sensors = new HashSet<>();
        if (OUTSIDE_TEMP_CURRENT.isApplicable(ctx)) {
            LOG.info("Adding outside current temperature");
            Apparatus apparatus = new SimpleApparatus(thermometerMetadata("Current Temperature"),
                                                      key,
                                                      AspectBuilder.of(key.append("temperature", "current"),
                                                                       Temperature.class,
                                                                       TemperatureMetadata.DEFAULT)
                                                              .withSensor(register(OUTSIDE_TEMP_CURRENT.build()))
            );
            this.context.connect(apparatus);
        }
        if (OUTSIDE_TEMP_MIN.isApplicable(ctx)) {
            LOG.info("Adding outside min temperature");
            Apparatus apparatus = new SimpleApparatus(thermometerMetadata("Minimum Temperature"),
                                                      key,
                                                      AspectBuilder.of(key.append("temperature", "minimum"),
                                                                       Temperature.class,
                                                                       TemperatureMetadata.DEFAULT)
                                                              .withSensor(register(OUTSIDE_TEMP_MIN.build()))
            );
            this.context.connect(apparatus);
        }
        if (OUTSIDE_TEMP_MAX.isApplicable(ctx)) {
            LOG.info("Adding outside max temperature");
            Apparatus apparatus = new SimpleApparatus(thermometerMetadata("Maximum Temperature"),
                                                      key,
                                                      AspectBuilder.of(key.append("temperature", "maximum"),
                                                                       Temperature.class,
                                                                       TemperatureMetadata.DEFAULT)
                                                              .withSensor(register(OUTSIDE_TEMP_MAX.build()))
            );

            this.context.connect(apparatus);
        }
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

    public <T, JSONTYPE> JSONSensor<T, JSONTYPE> register(JSONSensor<T, JSONTYPE> logic) {
        this.logics.add(logic);
        return logic;
    }

    private PlatformContext context;

    private Set<JSONSensor<?, ?>> logics = new HashSet<>();
}
