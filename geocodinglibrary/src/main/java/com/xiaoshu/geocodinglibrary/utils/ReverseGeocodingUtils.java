package com.xiaoshu.geocodinglibrary.utils;

import android.content.Context;
import android.location.Location;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoshu.geocodinglibrary.model.Countries;
import com.xiaoshu.geocodinglibrary.model.Country;
import com.xiaoshu.geocodinglibrary.model.FeatureCollection;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by XiaoShu on 2018/4/9 0009.
 */
public class ReverseGeocodingUtils {
    private static FeatureCollection featureCollection;
    private static Gson gson = new Gson();

    /**
     * 根据坐标获取所在的国家
     * @param context
     * @param location
     * @return
     */
    public static Country getCountry(Context context, Location location) {
        if (null == featureCollection) {
            String json = LoadAssetsUtils.getString(context, "countries_geo.json");
            featureCollection = gson.fromJson(json, FeatureCollection.class);
        }

        for (Countries countries : featureCollection.getFeatures()) {
            if ("Polygon".equals(countries.getGeometry().getType())) {
                Type type = new TypeToken<List<List<List<Double>>>>() {
                }.getType();
                List<List<List<Double>>> data = gson.fromJson(countries.getGeometry().getCoordinates().toString(), type);
                if (containsPoint(data.get(0), location)) {
                    Country country = new Country();
                    country.setId(countries.getId());
                    country.setName(countries.getProperties().getName());
                    return country;
                }
            } else if ("MultiPolygon".equals(countries.getGeometry().getType())) {
                Type type = new TypeToken<List<List<List<List<Double>>>>>() {
                }.getType();
                List<List<List<List<Double>>>> data = gson.fromJson(countries.getGeometry().getCoordinates().toString(), type);
                for (List<List<List<Double>>> datum : data) {
                    if (containsPoint(datum.get(0), location)) {
                        Country country = new Country();
                        country.setId(countries.getId());
                        country.setName(countries.getProperties().getName());
                        return country;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 计算当前坐标是否在内
     *
     * @param coordinate
     * @param location
     * @return
     */
    private static boolean containsPoint(List<List<Double>> coordinate, Location location) {
        int size = coordinate.size();

        double lon = location.getLongitude();
        double lat = location.getLatitude();

        double[] longitudes = new double[size];
        double[] latitudes = new double[size];

        for (int i = 0; i < coordinate.size(); i++) {
            longitudes[i] = coordinate.get(i).get(0);
            latitudes[i] = coordinate.get(i).get(1);
        }

        boolean contains = false;
        for (int i = 0, j = size - 1; i < size; j = i++) {
            if (((latitudes[i] > lat) != (latitudes[j] > lat)) &&
                    (lon < (longitudes[j] - longitudes[i]) * (lat - latitudes[i]) / (latitudes[j] - latitudes[i]) + longitudes[i]))
                contains = !contains;
        }
        return contains;
    }
}
