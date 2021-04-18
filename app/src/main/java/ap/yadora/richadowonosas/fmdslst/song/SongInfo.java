package ap.yadora.richadowonosas.fmdslst.song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class SongInfo {
    public static final int LOCALIZED_EN = 0;
    public static final int LOCALIZED_JA = 1;
    public static final int LOCALIZED_ZH_HANS = 2;
    public static final int LOCALIZED_ZH_HANT = 3;
    public static final int LOCALIZED_KO = 4;

    public static final int DAYNIGHT_DAY = 0;
    public static final int DAYNIGHT_NIGHT = 1;

    private String id = "";
    private Localized title_localized = new Localized();
    private String artist = "";
    private Localized artist_localized = null;
    private String bpm = "";
    private double bpm_base = 180;
    private String set = "";
    private String purchase = "";
    private int audioPreview = 0;
    private int audioPreviewEnd = 0;
    private int side = 0;
    private String bg = "";
    private DayNight bg_daynight = null;
    private long date = 0;
    private String version = "";
    private boolean world_unlock = false;
    private boolean remote_dl = false;
    private boolean byd_local_unlock = true;
    private boolean songlist_hidden = false;
    private boolean no_pp = false;
    private Localized source = null;
    private String source_copyright = null;
    private Difficulty[] difficulties = {null, null, null, null};

    public SongInfo() {
    }

    public SongInfo(SongInfo songInfo) {
        this.id = songInfo.getId();
        this.title_localized = new Localized(songInfo.getTitle_localized());
        this.artist = songInfo.getArtist();
        if (songInfo.getArtist_localized() == null) {
            this.artist_localized = null;
        } else {
            this.artist_localized = new Localized(songInfo.getArtist_localized());
        }
        this.bpm = songInfo.getBpm();
        this.bpm_base = songInfo.getBpm_base();
        this.set = songInfo.getSet();
        this.purchase = songInfo.getPurchase();
        this.audioPreview = songInfo.getAudioPreview();
        this.audioPreviewEnd = songInfo.getAudioPreviewEnd();
        this.side = songInfo.getSide();
        this.bg = songInfo.getBg();
        if (songInfo.getBg_daynight() == null) {
            this.bg_daynight = null;
        } else {
            this.bg_daynight = new DayNight(songInfo.getBg_daynight());
        }
        this.date = songInfo.getDate();
        this.version = songInfo.getVersion();
        this.world_unlock = songInfo.isWorld_unlock();
        this.remote_dl = songInfo.isRemote_dl();
        this.byd_local_unlock = songInfo.isByd_local_unlock();
        this.songlist_hidden = songInfo.isSonglist_hidden();
        this.no_pp = songInfo.isNo_pp();
        if (songInfo.getSource() == null) {
            this.source = null;
        } else {
            this.source = new Localized(songInfo.getSource());
        }
        this.source_copyright = songInfo.getSource_copyright();
        this.difficulties = new Difficulty[4];
        for (int i = 0; i < 4; i++) {
            this.setDifficulty(songInfo.getDifficulty(i), i);
        }
    }

    public static SongInfo getSongInfoFromJson(JSONObject song) throws JSONException {
        SongInfo single = new SongInfo();
        JSONArray difficulties;

        single.setId(song.getString("id"));
        single.setTitle_localized(Localized.getLocalizedFromJSON(song.getJSONObject("title_localized")));
        single.setArtist(song.getString("artist"));
        single.setBpm(song.getString("bpm"));
        single.setBpm_base(song.getDouble("bpm_base"));
        single.setSet(song.getString("set"));
        single.setPurchase(song.getString("purchase"));
        single.setAudioPreview(song.getInt("audioPreview"));
        single.setAudioPreviewEnd(song.getInt("audioPreviewEnd"));
        single.setSide(song.getInt("side"));
        single.setBg(song.getString("bg"));
        single.setDate(song.getLong("date"));
        single.setVersion(song.getString("version"));
        difficulties = song.getJSONArray("difficulties");
//        for (int j = 0; j < difficulties.length(); j++) {
//            single.setDifficulty(Difficulty.getDifficultyFromJson(difficulties.getJSONObject(j)), j);
//        }
        single.setDifficulties(Difficulty.getDifficultiesFromJSON(difficulties));

        if (song.has("artist_localized")) {
            single.setArtist_localized(Localized.getLocalizedFromJSON(song.getJSONObject("artist_localized")));
        }
        if (song.has("bg_daynight")) {
            single.setBg_daynight(DayNight.getDaynightFromJson(song.getJSONObject("bg_daynight")));
        }
        if (song.has("world_unlock")) {
            single.setWorld_unlock(song.getBoolean("world_unlock"));
        }
        if (song.has("remote_dl")) {
            single.setRemote_dl(song.getBoolean("remote_dl"));
        }
        if (song.has("byd_local_unlock")) {
            single.setByd_local_unlock(song.getBoolean("byd_local_unlock"));
        }
        if (song.has("songlist_hidden")) {
            single.setSonglist_hidden(song.getBoolean("songlist_hidden"));
        }
        if (song.has("no_pp")) {
            single.setNo_pp(song.getBoolean("no_pp"));
        }
        if (song.has("source")) {
            single.setSource(Localized.getLocalizedFromJSON(song.getJSONObject("source")));
        }
        if (song.has("source_copyright")) {
            single.setSource_copyright(song.getString("source_copyright"));
        }

        return single;
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject songJson = new JSONObject();

        songJson.put("id", id);
        songJson.put("title_localized", title_localized.toJSONObject());
        songJson.put("artist", artist);
        if (artist_localized != null) {
            songJson.put("artist_localized", artist_localized.toJSONObject());
        }
        songJson.put("bpm", bpm);
        songJson.put("bpm_base", bpm_base);
        songJson.put("set", set);
        songJson.put("purchase", purchase);
        songJson.put("audioPreview", audioPreview);
        songJson.put("audioPreviewEnd", audioPreviewEnd);
        songJson.put("side", side);
        songJson.put("bg", bg);
        if (bg_daynight != null) {
            songJson.put("bg_daynight", bg_daynight.toJSONObject());
        }
        songJson.put("date", date);
        songJson.put("version", version);
        if (world_unlock) {
            songJson.put("world_unlock", true);
        }
        if (remote_dl) {
            songJson.put("remote_dl", true);
        }
        if (byd_local_unlock) {
            songJson.put("byd_local_unlock", true);
        }
        if (songlist_hidden) {
            songJson.put("songlist_hidden", true);
        }
        if (no_pp) {
            songJson.put("no_pp", true);
        }
        if (source != null) {
            songJson.put("source", source.toJSONObject());
        }
        if (source_copyright != null) {
            songJson.put("source_copyright", source_copyright);
        }
        songJson.put("difficulties", Difficulty.packDifficultiesToJSON(difficulties));

        return songJson;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Localized getTitle_localized() {
        return title_localized;
    }

    public void setTitle_localized(Localized title_localized) {
        this.title_localized = title_localized;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Localized getArtist_localized() {
        return artist_localized;
    }

    public void setArtist_localized(Localized artist_localized) {
        this.artist_localized = (artist_localized == null) ? null : (new Localized(artist_localized));
    }

    public String getBpm() {
        return bpm;
    }

    public void setBpm(String bpm) {
        this.bpm = bpm;
    }

    public double getBpm_base() {
        return bpm_base;
    }

    public void setBpm_base(double bpm_base) {
        this.bpm_base = bpm_base;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public String getPurchase() {
        return purchase;
    }

    public void setPurchase(String purchase) {
        this.purchase = purchase;
    }

    public int getAudioPreview() {
        return audioPreview;
    }

    public void setAudioPreview(int audioPreview) {
        this.audioPreview = audioPreview;
    }

    public int getAudioPreviewEnd() {
        return audioPreviewEnd;
    }

    public void setAudioPreviewEnd(int audioPreviewEnd) {
        this.audioPreviewEnd = audioPreviewEnd;
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side & 1;
    }

    public String getBg() {
        return bg;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }

    public DayNight getBg_daynight() {
        return bg_daynight;
    }

    public void setBg_daynight(DayNight bg_daynight) {
        this.bg_daynight = (bg_daynight == null) ? null : (new DayNight(bg_daynight));
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isWorld_unlock() {
        return world_unlock;
    }

    public void setWorld_unlock(boolean world_unlock) {
        this.world_unlock = world_unlock;
    }

    public boolean isRemote_dl() {
        return remote_dl;
    }

    public void setRemote_dl(boolean remote_dl) {
        this.remote_dl = remote_dl;
    }

    public boolean isByd_local_unlock() {
        return byd_local_unlock;
    }

    public void setByd_local_unlock(boolean byd_local_unlock) {
        this.byd_local_unlock = byd_local_unlock;
    }

    public boolean isSonglist_hidden() {
        return songlist_hidden;
    }

    public void setSonglist_hidden(boolean songlist_hidden) {
        this.songlist_hidden = songlist_hidden;
    }

    public boolean isNo_pp() {
        return no_pp;
    }

    public void setNo_pp(boolean no_pp) {
        this.no_pp = no_pp;
    }

    public Localized getSource() {
        return source;
    }

    public void setSource(Localized source) {
        this.source = source;
    }

    public String getSource_copyright() {
        return source_copyright;
    }

    public void setSource_copyright(String source_copyright) {
        this.source_copyright = source_copyright;
    }

    public Difficulty getDifficulty(int index) {
        return difficulties[index];
    }

    public void setDifficulty(Difficulty difficulty, int index) {
        index = (index >= 0) ? (index & 3) : ((-index) & 3);
        if (difficulty == null) {
            this.difficulties[index] = null;
        } else {
            this.difficulties[index] = new Difficulty(difficulty);
        }
    }

    public void setDifficulties(Difficulty[] difficulties) {
        int loop = 4;
        this.difficulties = new Difficulty[]{null, null, null, null};
        if (difficulties.length < loop) {
            loop = difficulties.length;
        }

        for (int i = 0; i < loop; i++) {
            if (difficulties[i] == null) {
                this.difficulties[i] = null;
            } else {
                this.difficulties[i] = new Difficulty(difficulties[i]);
            }
        }
    }

    public boolean equals(SongInfo anotherSongInfo) {
        try {
            String songJSON = toJSONObject().toString(),
                    anotherSongJSON = anotherSongInfo.toJSONObject().toString();
            return songJSON.equals(anotherSongJSON);
        } catch (Exception e) {
            return false;
        }
    }

    public static String[] getLocalizedStrings(Localized localized) {
        if (localized == null) {
            return new String[]{null, null, null, null, null};
        } else {
            return new String[]{
                    localized.getEn(),
                    localized.getJa(),
                    localized.getZhHans(),
                    localized.getZhHant(),
                    localized.getKo()
            };
        }
    }

    public static Localized getLocalizedFromStrings(String[] localizedStrings) {
        Localized result = new Localized();

        result.setEn((localizedStrings[LOCALIZED_EN] == null) ? "" : localizedStrings[LOCALIZED_EN]);
        result.setJa(localizedStrings[LOCALIZED_JA]);
        result.setZhHans(localizedStrings[LOCALIZED_ZH_HANS]);
        result.setZhHant(localizedStrings[LOCALIZED_ZH_HANT]);
        result.setKo(localizedStrings[LOCALIZED_KO]);

        return result;
    }

    public String[] getDaynight() {
        if (bg_daynight == null) {
            return new String[]{null, null};
        } else {
            return new String[]{
                    bg_daynight.getDay(),
                    bg_daynight.getNight()
            };
        }
    }

    public static DayNight getDaynightFromStrings(String[] daynightString) {
        DayNight result = new DayNight();
        result.setDay((daynightString[DAYNIGHT_DAY] == null) ? "" : daynightString[DAYNIGHT_DAY]);
        result.setNight((daynightString[DAYNIGHT_NIGHT] == null) ? "" : daynightString[DAYNIGHT_NIGHT]);
        return result;
    }
}
