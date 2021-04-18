package ap.yadora.richadowonosas.fmdslst.song;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class Difficulty {
    public static final int DIFFICULTY_PAST = 0;
    public static final int DIFFICULTY_PRESENT = 1;
    public static final int DIFFICULTY_FUTURE = 2;
    public static final int DIFFICULTY_BEYOND = 3;

    private int ratingClass;
    private String chartDesigner;
    private String jacketDesigner;
    private int rating;
    private boolean ratingPlus = false;
    private String jacket_night = null;
    private boolean jacketOverride = false;
    private boolean hidden_until_unlocked = false;
    private String bg = null;
    private boolean world_unlock = false;

    public Difficulty() {
    }

    public Difficulty(Difficulty difficulty) {
        this.ratingClass = difficulty.getRatingClass();
        this.chartDesigner = difficulty.getChartDesigner();
        this.jacketDesigner = difficulty.getJacketDesigner();
        this.rating = difficulty.getRating();
        this.ratingPlus = difficulty.isRatingPlus();
        this.jacket_night = difficulty.getJacket_night();
        this.jacketOverride = difficulty.isJacketOverride();
        this.hidden_until_unlocked = difficulty.isHidden_until_unlocked();
        this.bg = difficulty.getBg();
        this.world_unlock = difficulty.isWorld_unlock();
    }

    public static Difficulty getDifficultyFromJson(JSONObject diffJson) throws JSONException {
        Difficulty result = new Difficulty();

        result.setRatingClass(diffJson.getInt("ratingClass"));
        result.setChartDesigner(diffJson.getString("chartDesigner"));
        result.setJacketDesigner(diffJson.getString("jacketDesigner"));
        result.setRating(diffJson.getInt("rating"));
        if (diffJson.has("ratingPlus")) {
            result.setRatingPlus(diffJson.getBoolean("ratingPlus"));
        }
        if (diffJson.has("jacket_night")) {
            result.setJacket_night(diffJson.getString("jacket_night"));
        }
        if (diffJson.has("jacketOverride")) {
            result.setJacketOverride(diffJson.getBoolean("jacketOverride"));
        }
        if (diffJson.has("hidden_until_unlocked")) {
            result.setHidden_until_unlocked(diffJson.getBoolean("hidden_until_unlocked"));
        }
        if (diffJson.has("bg")) {
            result.setBg(diffJson.getString("bg"));
        }
        if (diffJson.has("world_unlock")) {
            result.setWorld_unlock(diffJson.getBoolean("world_unlock"));
        }

        return result;
    }

    public static Difficulty[] getDifficultiesFromJSON(JSONArray diffsJSON) throws JSONException {
        Difficulty[] result = new Difficulty[]{null, null, null, null};

        int amount = 4;
        int[] diffCount = new int[]{0, 0, 0, 0};
        int[] diffPos = new int[]{-1, -1, -1, -1};
        // Set amount of difficulties
        if (diffsJSON.length() < 4) {
            amount = diffsJSON.length();
        }
        // Initialize difficulties
        JSONObject[] diffJson = new JSONObject[4];
        int[] diffs = new int[]{-1, -1, -1, -1};
        for (int i = 0; i < amount; i++) {
            // Fetch difficulty JSONObject
            diffJson[i] = diffsJSON.getJSONObject(i);
            // Save difficulty class
            diffs[i] = diffJson[i].getInt("ratingClass") & 3;
            // Make count of the difficulty
            if (++diffCount[diffs[i]] == 1) {
                // Bind the first difficulty matches
                diffPos[diffs[i]] = i;
            }
        }

        Log.d("Difficulty", "Original Difficulties: " + diffs[0] + ", " + diffs[1] + ", " + diffs[2] + ", " + diffs[3]);

        // Adjust difficulty position
        int posPointer = 0, changePointer = 0;
        while (maximum(diffCount) > 1) {
            // Find the first unbound difficulty
            while (posPointer < 4 && diffPos[posPointer] >= 0) {
                posPointer++;
            }
            // Find the first difficulty which is illegally bound
            while (changePointer < 4 && changePointer == diffPos[diffs[changePointer]]) {
                changePointer++;
            }
            // Rebind this difficulty
            diffCount[diffs[changePointer]]--;
            diffs[changePointer] = posPointer;
            diffPos[posPointer] = changePointer;
            diffCount[posPointer]++;
            // Save changes
            diffJson[changePointer].put("ratingClass", diffs[changePointer]);
        }

        // Import difficulties from JSONObject
        for (int i = 0, j = 0; i < amount; i++, j++) {
            while (diffPos[j] == -1) {
                j++;
            }
            result[i] = getDifficultyFromJson(diffJson[diffPos[j]]);
        }

        Log.d("Difficulty", "Changed Difficulties: " + +diffs[0] + ", " + diffs[1] + ", " + diffs[2] + ", " + diffs[3]);

        // Return the result
        return result;
    }

    private static int maximum(int[] array) {
        int result = 0;

        for (int i = 0; i < array.length; i++) {
            if (array[i] > result) {
                result = array[i];
            }
        }

        return result;
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject difficultyJson = new JSONObject();

        difficultyJson.put("ratingClass", ratingClass);
        difficultyJson.put("chartDesigner", chartDesigner);
        difficultyJson.put("jacketDesigner", jacketDesigner);
        difficultyJson.put("rating", rating);
        if (ratingPlus) {
            difficultyJson.put("ratingPlus", true);
        }
        if (jacket_night != null) {
            difficultyJson.put("jacket_night", jacket_night);
        }
        if (jacketOverride) {
            difficultyJson.put("jacketOverride", true);
        }
        if (hidden_until_unlocked) {
            difficultyJson.put("hidden_until_unlocked", true);
        }
        if (bg != null) {
            difficultyJson.put("bg", bg);
        }
        if (world_unlock) {
            difficultyJson.put("world_unlock", true);
        }

        return difficultyJson;
    }

    public static JSONArray packDifficultiesToJSON(Difficulty[] difficulties) throws JSONException {
        JSONArray difficultiesJson = new JSONArray();

        for (int i = 0; i < difficulties.length; i++) {
            if (difficulties[i] != null) {
                difficultiesJson.put(difficulties[i].toJSONObject());
            }
        }

        return difficultiesJson;
    }

    public int getRatingClass() {
        return ratingClass;
    }

    public void setRatingClass(int ratingClass) {
        this.ratingClass = ratingClass & 3;
    }

    public String getChartDesigner() {
        return chartDesigner;
    }

    public void setChartDesigner(@NonNull String chartDesigner) {
        this.chartDesigner = chartDesigner;
    }

    public String getJacketDesigner() {
        return jacketDesigner;
    }

    public void setJacketDesigner(@NonNull String jacketDesigner) {
        this.jacketDesigner = jacketDesigner;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean isRatingPlus() {
        return ratingPlus;
    }

    public void setRatingPlus(boolean ratingPlus) {
        this.ratingPlus = ratingPlus;
    }

    public String getJacket_night() {
        return jacket_night;
    }

    public void setJacket_night(@Nullable String jacket_night) {
        this.jacket_night = jacket_night;
    }

    public boolean isJacketOverride() {
        return jacketOverride;
    }

    public void setJacketOverride(boolean jacketOverride) {
        this.jacketOverride = jacketOverride;
    }

    public boolean isHidden_until_unlocked() {
        return hidden_until_unlocked;
    }

    public void setHidden_until_unlocked(boolean hidden_until_unlocked) {
        this.hidden_until_unlocked = hidden_until_unlocked;
    }

    public String getBg() {
        return bg;
    }

    public void setBg(@Nullable String bg) {
        this.bg = bg;
    }

    public boolean isWorld_unlock() {
        return world_unlock;
    }

    public void setWorld_unlock(boolean world_unlock) {
        this.world_unlock = world_unlock;
    }
}
