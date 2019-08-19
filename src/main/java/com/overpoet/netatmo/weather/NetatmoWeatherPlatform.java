package com.overpoet.netatmo.weather;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.overpoet.Key;
import com.overpoet.core.apparatus.Apparatus;
import com.overpoet.core.apparatus.SimpleApparatus;
import com.overpoet.core.platform.Platform;
import com.overpoet.core.platform.PlatformContext;
import com.overpoet.core.sensor.Sensor;
import com.overpoet.core.sensor.SensorLogic;
import com.overpoet.netatmo.weather.wind.BaseNetatamoSensorLogic;
import com.overpoet.netatmo.weather.wind.GustAngleSensorLogic;
import com.overpoet.netatmo.weather.wind.GustStrengthSensorLogic;
import com.overpoet.netatmo.weather.wind.WindAngleSensorLogic;
import com.overpoet.netatmo.weather.wind.WindStrengthSensorLogic;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import static java.net.URI.create;

public class NetatmoWeatherPlatform implements Platform, LogicRegistry {

    private static final Key KEY = Key.of("netatmo", "weather");

    private static final URI baseURI = create("https://api.netatmo.com/");

    private static final URI tokenURI = baseURI.resolve(create("/oauth2/token"));

    private static final URI stationsDataURI = baseURI.resolve(create("/api/getstationsdata"));

    public NetatmoWeatherPlatform() {
        this.timer = new Timer();
    }

    @Override
    public void configure(PlatformContext context) {
        this.context = context;

        try {
            String data = getData();
            initialize(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.timer.scheduleAtFixedRate(new Task(), 1000, 2000);
    }

    private void initialize(String data) {
        ReadContext ctx = JsonPath.parse(data);
        initializeWind(ctx);
    }

    private void poll() throws IOException {
        String data = getData();
        ReadContext ctx = JsonPath.parse(data);

        for (BaseNetatamoSensorLogic<?> logic : this.logics) {
            logic.process(ctx);
        }
    }

    private void initializeWind(ReadContext ctx) {
        Key key = KEY.append("wind");

        Set<Sensor<?>> sensors = new HashSet<>();
        if (WindStrengthSensorLogic.isApplicable(ctx)) {
            sensors.add(WindStrengthSensorLogic.of(key, this));
        }
        if (WindAngleSensorLogic.isApplicable(ctx)) {
            sensors.add(WindAngleSensorLogic.of(key, this));
        }
        if (GustStrengthSensorLogic.isApplicable(ctx)) {
            sensors.add(GustStrengthSensorLogic.of(key, this));
        }
        if (GustAngleSensorLogic.isApplicable(ctx)) {
            sensors.add(GustStrengthSensorLogic.of(key, this));
        }
        if (sensors.isEmpty()) {
            return;
        }

        Apparatus apparatus = new SimpleApparatus(KEY.append("wind"), sensors, Collections.emptySet());

        this.context.connect(apparatus);
    }

    private String getToken() throws IOException {
        HttpUrl url = HttpUrl.get(tokenURI).newBuilder()
                .build();

        FormBody body = new FormBody.Builder()
                .add("grant_type", "password")
                .add("username", this.context.configuration().getProperty("username"))
                .add("password", this.context.configuration().getProperty("password"))
                .add("client_id", this.context.configuration().getProperty("client_id"))
                .add("client_secret", this.context.configuration().getProperty("client_secret"))
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
        JSONObject result = new JSONObject(response.body().string());
        return result.get("access_token").toString();
    }

    private String getData() throws IOException {
        String accessToken = getToken();
        HttpUrl url = HttpUrl.get(stationsDataURI).newBuilder()
                .build();

        FormBody body = new FormBody.Builder()
                .add("access_token", accessToken)
                .add("device_id", this.context.configuration().getProperty("device_id"))
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
    public <T extends BaseNetatamoSensorLogic<?>> T register(T logic) {
        this.logics.add(logic);
        return logic;
    }

    private final Timer timer;

    private PlatformContext context;

    private Set<BaseNetatamoSensorLogic<?>> logics = new HashSet<>();

    private class Task extends TimerTask {

        @Override
        public void run() {
            try {
                poll();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
