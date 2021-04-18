package ap.yadora.richadowonosas.fmdslst.song;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

final class DayNight {
    private String day;
    private String night;

    DayNight() {
        this.day = null;
        this.night = null;
    }

    DayNight(DayNight dayNight) {
        this.day = dayNight.getDay();
        this.night = dayNight.getNight();
    }

    public static DayNight getDaynightFromJson(JSONObject dnJson) throws JSONException {
        DayNight result = new DayNight();
        result.setDay(dnJson.getString("day"));
        result.setNight(dnJson.getString("night"));
        return result;
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject daynightJson = new JSONObject();

        daynightJson.put("day", day);
        daynightJson.put("night", night);

        return daynightJson;
    }

    public String getDay() {
        return day;
    }

    public void setDay(@Nullable String day) {
        this.day = day;
    }

    public String getNight() {
        return night;
    }

    public void setNight(@Nullable String night) {
        this.night = night;
    }
}
