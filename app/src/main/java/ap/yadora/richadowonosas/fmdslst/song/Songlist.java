package ap.yadora.richadowonosas.fmdslst.song;

import android.os.Environment;
import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import ap.yadora.richadowonosas.fmdslst.activity.MainActivity;

public final class Songlist {
    public static final int SORT_BY_SONG_ID = 0;
    public static final int SORT_BY_DATE = 1;
    public static final int SORT_BY_VERSION = 2;

    private static final SortCriterion[] sortBy = {
            new SortCriterion() {
                @Override
                public boolean shouldBeInFrontOf(SongInfo song1, SongInfo song2, boolean isAscend) {
                    return isAscend ?
                            (song1.getId().compareTo(song2.getId()) < 0) :
                            (song2.getId().compareTo(song1.getId()) < 0);
                }
            },
            new SortCriterion() {
                @Override
                public boolean shouldBeInFrontOf(SongInfo song1, SongInfo song2, boolean isAscend) {
                    if (song1.getDate() == song2.getDate()) {
                        return isAscend ?
                                (song1.getId().compareTo(song2.getId()) < 0) :
                                (song2.getId().compareTo(song1.getId()) < 0);
                    } else {
                        return isAscend ?
                                (song1.getDate() < song2.getDate()) :
                                (song2.getDate() < song1.getDate());
                    }
                }
            },
            new SortCriterion() {
                @Override
                public boolean shouldBeInFrontOf(SongInfo song1, SongInfo song2, boolean isAscend) {
                    if (song1.getVersion().equals(song2.getVersion())) {
                        return isAscend ?
                                (song1.getId().compareTo(song2.getId()) < 0) :
                                (song2.getId().compareTo(song1.getId()) < 0);
                    } else {
                        return isAscend ?
                                (song1.getVersion().compareTo(song2.getVersion()) < 0) :
                                (song2.getVersion().compareTo(song1.getVersion()) < 0);
                    }
                }
            }
    };

    private final ArrayMap<String, SongInfo> songs = new ArrayMap<>();
    private File songlistFile;

    public static final Songlist songlist = new Songlist();

    private Songlist() {
    }

    public int getSongAmount() {
        return songs.size();
    }

    public SongInfo getSongInfoById(String songId) {
        return new SongInfo(songs.get(songId));
    }

    public String[] getSongs(int sortFactor, boolean isAscend) {
        // Get song amount of this songlist
        int amount = songs.size();
        String[] result = new String[amount];
        // Get song ids
        for (int i = 0; i < amount; i++) {
            result[i] = songs.valueAt(i).getId();
        }
        // Return the result array
        return mergeSort(result, sortFactor, isAscend);
    }

    @NonNull
    private String[] mergeSort(@NonNull String[] original, int sortFactor, boolean isAscend) {
        int length = original.length;
        switch (length) {
            case 0:
            case 1:
                return original.clone();
            case 2:
                if (sortBy[sortFactor].shouldBeInFrontOf(songs.get(original[0]), songs.get(original[1]), isAscend)) {
                    return original.clone();
                } else {
                    return new String[]{original[1], original[0]};
                }
            default:
                int i, j;
                int lengthFormer = length / 2, lengthLatter = length - lengthFormer;
                String[] result = new String[length];
                String[] former = new String[lengthFormer];
                String[] latter = new String[lengthLatter];

                for (i = 0; i < lengthFormer; i++) {
                    former[i] = original[i];
                }
                for (; i < length; i++) {
                    latter[i - lengthFormer] = original[i];
                }

                former = mergeSort(former, sortFactor, isAscend);
                latter = mergeSort(latter, sortFactor, isAscend);

                for (i = j = 0; i < lengthFormer && j < lengthLatter; ) {
                    result[i + j] = (sortBy[sortFactor].shouldBeInFrontOf(songs.get(former[i]), songs.get(latter[j]), isAscend)) ?
                            former[i++] :
                            latter[j++];
                }
                for (; i < lengthFormer; i++) {
                    result[i + j] = former[i];
                }
                for (; j < lengthLatter; j++) {
                    result[i + j] = latter[j];
                }

                return result;
        }
    }

    public String addSong(String newId) {
        SongInfo single = new SongInfo();
        single.setId(newId);
        // Rename repeated
        if (songs.containsKey(single.getId())) {
            int renameFactor = 1;
            while (songs.containsKey(single.getId() + renameFactor)) {
                renameFactor++;
            }
            Log.w("Songlist", "Song id repeated! Renamed to (" + single.getId() + renameFactor + ")");
            single.setId(single.getId() + renameFactor);
        }

        songs.put(single.getId(), single);

        Log.i("Songlist", "Song (id = " + single.getId() + ") added successfully");
        return single.getId();
    }

    public String addSong(SongInfo newInfo) {
        // Rename repeated
        if (songs.containsKey(newInfo.getId())) {
            int renameFactor = 1;
            while (songs.containsKey(newInfo.getId() + renameFactor)) {
                renameFactor++;
            }
            Log.w("Songlist", "Song id repeated! Renamed to (" + newInfo.getId() + renameFactor + ")");
            newInfo.setId(newInfo.getId() + renameFactor);
        }

        songs.put(newInfo.getId(), newInfo);

        Log.i("Songlist", "Song (id = " + newInfo.getId() + ") added successfully");
        return newInfo.getId();
    }

    public boolean removeSong(String songId) {
        if (!songs.containsKey(songId)) {
            Log.w("Songlist", "Song (id = " + songId + ") not found");
            return false;
        } else {
            songs.remove(songId);
            Log.i("Songlist", "Song (id = " + songId + ") removed successfully");
            return true;
        }
    }

    public boolean duplicateSong(String songId) {
        if (!songs.containsKey(songId)) {
            Log.w("Songlist", "Song (id = " + songId + ") not found");
            return false;
        } else {
            SongInfo single = new SongInfo(songs.get(songId));
            int renameFactor = 1;
            while (songs.containsKey(single.getId() + renameFactor)) {
                renameFactor++;
            }
            single.setId(single.getId() + renameFactor);
            songs.put(single.getId(), single);
            Log.i("Songlist", "Song duplicated to (" + single.getId() + renameFactor + ")");
            return true;
        }
    }

    public JSONObject getSongJSON(String songId) throws JSONException {
        if (!songs.containsKey(songId)) {
            return null;
        } else {
            return songs.get(songId).toJSONObject();
        }
    }

    public boolean importSongFromJSON(JSONObject songJson) {
        try {
            SongInfo single = SongInfo.getSongInfoFromJson(songJson);
            // Rename repeated
            if (songs.containsKey(single.getId())) {
                int renameFactor = 1;
                while (songs.containsKey(single.getId() + renameFactor)) {
                    renameFactor++;
                }
                Log.w("Songlist", "Song id repeated! Renamed to (" + single.getId() + renameFactor + ")");
                single.setId(single.getId() + renameFactor);
            }

            songs.put(single.getId(), single);
            Log.i("Songlist", "Song (id = " + single.getId() + ") imported successfully");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isSonglistFileAvailable(File file) {
        Log.d("Songlist", file.getPath());
        Log.d("Songlist", String.format(
                "Info::\n" +
                        "Songlist path: \t%s, \n" +
                        "Songlist exists: \t%b, \n" +
                        "Songlist readable: \t%b, \n" +
                        "Songlist writable: \t%b",
                file.getPath(), file.exists(), file.canRead(), file.canWrite()));

        return file.exists() && file.canRead() && file.canWrite();
    }

    public static boolean createDefaultSonglistFile() {
        File songlistFile = new File(Environment.getExternalStorageDirectory().getPath(), "arcfmd/songs/songlist");
        try {
            // Create parent path
            File parent = songlistFile.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            // Create new songlist file and return result
            return songlistFile.createNewFile();
        } catch (Exception e) {
            // If failed
            e.printStackTrace();
            return false;
        }
    }

    public void readSonglistFile(File songlistFile) throws IOException, JSONException {
        this.songlistFile = songlistFile;
        parseSonglistFromString(readSonglistToString(this.songlistFile));
    }

    public static String readSonglistToString(File songlistFile) throws IOException {
        String result;

        FileReader fileReader = new FileReader(songlistFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        bufferedReader.close();
        fileReader.close();

        result = stringBuilder.toString();

        return result;
    }

    private JSONObject toJSONObject() throws JSONException {
        JSONObject result = new JSONObject();
        JSONArray songsArray = new JSONArray();
        String[] sortedSongIds = getSongs(SORT_BY_DATE, true);

        for (String songId : sortedSongIds) {
            songsArray.put(songs.get(songId).toJSONObject());
        }

        result.put("songs", songsArray);
        return result;
    }

    public static String packSonglistToString(JSONObject songlistFile) throws JSONException {
        return songlistFile.toString(2);
    }

    private void parseSonglistFromString(String jsonString) throws JSONException {
        songs.clear();

        JSONObject songlistObject;
        songlistObject = new JSONObject(jsonString);
        JSONArray songArray = songlistObject.getJSONArray("songs");

        for (int index = 0; index < songArray.length(); index++) {
            SongInfo single = SongInfo.getSongInfoFromJson(songArray.getJSONObject(index));
            // Rename repeated
            if (songs.containsKey(single.getId())) {
                int renameFactor = 1;
                while (songs.containsKey(single.getId() + renameFactor)) {
                    renameFactor++;
                }
                Log.w("Songlist", "Song id repeated! Renamed to (" + single.getId() + renameFactor + ")");
                single.setId(single.getId() + renameFactor);
            }

            songs.put(single.getId(), single);

            Log.i("Songlist", "Song (id = " + single.getId() + ") parsed successfully");
        }

        Log.i("Songlist", "Songlist parsed successfully.");
    }

    public void saveSonglistToFile() throws IOException, JSONException {
        FileWriter fileWriter = new FileWriter(songlistFile);

        fileWriter.write(toJSONObject().toString(2));

        fileWriter.close();
    }

    public File getSonglistFileParent() {
        return songlistFile.getParentFile();
    }

    private interface SortCriterion {
        boolean shouldBeInFrontOf(SongInfo song1, SongInfo song2, boolean isAscend);
    }
}

