package ap.yadora.richadowonosas.fmdslst.song;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

final class Localized {
    private String en, ja = null, zhHans = null, zhHant = null, ko = null;

    public Localized() {
    }

    public Localized(Localized localized) {
        this.en = localized.getEn();
        this.ja = localized.getJa();
        this.zhHans = localized.getZhHans();
        this.zhHant = localized.getZhHant();
        this.ko = localized.getKo();
    }

    public static Localized getLocalizedFromJSON(@NonNull JSONObject localizedJson) throws JSONException {
        Localized result = new Localized();
        result.setEn(localizedJson.getString("en"));
        if (localizedJson.has("ja")) {
            result.setJa(localizedJson.getString("ja"));
        }
        if (localizedJson.has("zh-Hans")) {
            result.setZhHans(localizedJson.getString("zh-Hans"));
        }
        if (localizedJson.has("zh-Hant")) {
            result.setZhHant(localizedJson.getString("zh-Hant"));
        }
        if (localizedJson.has("ko")) {
            result.setKo(localizedJson.getString("ko"));
        }
        return result;
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject localizedJson = new JSONObject();

        localizedJson.put("en", en);
        if (ja != null) {
            localizedJson.put("ja", ja);
        }
        if (zhHans != null) {
            localizedJson.put("zh-Hans", zhHans);
        }
        if (zhHant != null) {
            localizedJson.put("zh-Hant", zhHant);
        }
        if (ko != null) {
            localizedJson.put("ko", ko);
        }

        return localizedJson;
    }

    public String getEn() {
        return en;
    }

    public void setEn(@NonNull String en) {
        this.en = en;
    }

    public String getJa() {
        return ja;
    }

    public void setJa(@Nullable String ja) {
        this.ja = ja;
    }

    public String getZhHans() {
        return zhHans;
    }

    public void setZhHans(@Nullable String zhHans) {
        this.zhHans = zhHans;
    }

    public String getZhHant() {
        return zhHant;
    }

    public void setZhHant(@Nullable String zhHant) {
        this.zhHant = zhHant;
    }

    public String getKo() {
        return ko;
    }

    public void setKo(@Nullable String ko) {
        this.ko = ko;
    }
}
