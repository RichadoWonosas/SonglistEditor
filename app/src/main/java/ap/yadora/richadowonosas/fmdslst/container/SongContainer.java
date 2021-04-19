package ap.yadora.richadowonosas.fmdslst.container;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;

import java.math.BigDecimal;
import java.math.BigInteger;

import ap.yadora.richadowonosas.fmdslst.R;
import ap.yadora.richadowonosas.fmdslst.song.Difficulty;
import ap.yadora.richadowonosas.fmdslst.song.SongInfo;
import ap.yadora.richadowonosas.fmdslst.util.TimestampTransformer;

public final class SongContainer {

    private final View root;
    private final SingleContainer id, artist, bpm, bpmBase, set, purchase, audioPreview, audioPreviewEnd, bg, date, version, sourceCopyright;
    private final LocalizedContainer titleLocalized, artistLocalized, source;
    private final ChoiceContainer side;
    private final DaynightContainer bgDaynight;
    private final CheckContainer worldUnlock, remoteDl, bydLocalUnlock, songlistHidden, noPP;
    private final DifficultyContainer[] difficulties;
    private final ImageButton dateEditor;

    private boolean isVisible = false;

    private int partsHeight, textHeight;
    private final int HEIGHT_PARTS;

    public SongContainer(View root) {
        this.root = root;
        HEIGHT_PARTS = (int) root.getResources().getDimension(R.dimen.height_parts);

        id = new SingleContainer(root, R.id.editID, SingleContainer.TYPE_PLAIN_TEXT);
        titleLocalized = new LocalizedContainer(root, R.id.editTitle);
        artist = new SingleContainer(root, R.id.editArtist, SingleContainer.TYPE_MULTILINE_TEXT);
        artistLocalized = new LocalizedContainer(root, R.id.editArtistLocalized);
        bpm = new SingleContainer(root, R.id.editBPM, SingleContainer.TYPE_MULTILINE_TEXT);
        bpmBase = new SingleContainer(root, R.id.editBPMBase, SingleContainer.TYPE_DECIMAL);
        set = new SingleContainer(root, R.id.editSet, SingleContainer.TYPE_PLAIN_TEXT);
        purchase = new SingleContainer(root, R.id.editPurchase, SingleContainer.TYPE_PLAIN_TEXT);
        audioPreview = new SingleContainer(root, R.id.editAudioPreview, SingleContainer.TYPE_INTEGER);
        audioPreviewEnd = new SingleContainer(root, R.id.editAudioPreviewEnd, SingleContainer.TYPE_INTEGER);
        side = new ChoiceContainer(root, R.id.editSide);
        bg = new SingleContainer(root, R.id.editBG, SingleContainer.TYPE_PLAIN_TEXT);
        bgDaynight = new DaynightContainer(root, R.id.editBGDaynight);
        date = new SingleContainer(root, R.id.editDate, SingleContainer.TYPE_INTEGER);
        dateEditor = root.findViewById(R.id.editDateWindow);
        version = new SingleContainer(root, R.id.editVersion, SingleContainer.TYPE_PLAIN_TEXT);
        worldUnlock = new CheckContainer(root, R.id.editWorldUnlock);
        remoteDl = new CheckContainer(root, R.id.editRemoteDL);
        bydLocalUnlock = new CheckContainer(root, R.id.editBYDLocalUnlock);
        songlistHidden = new CheckContainer(root, R.id.editSonglistHidden);
        noPP = new CheckContainer(root, R.id.editNoPP);
        source = new LocalizedContainer(root, R.id.editSource);
        sourceCopyright = new SingleContainer(root, R.id.editSourceCopyright, SingleContainer.TYPE_PLAIN_TEXT);
        difficulties = new DifficultyContainer[]{
                new DifficultyContainer(root, R.id.editPast),
                new DifficultyContainer(root, R.id.editPresent),
                new DifficultyContainer(root, R.id.editFuture),
                new DifficultyContainer(root, R.id.editBeyond)
        };

        refresh();
    }

    public void getInitialContentFromSongInfo(SongInfo single) {
        id.getContent().setText(single.getId());
        id.getTitle().setText(R.string.info_songid);
        {
            String[] titles = SongInfo.getLocalizedStrings(single.getTitle_localized());
            titleLocalized.getEn().getContent().setText(titles[SongInfo.LOCALIZED_EN]);
            titleLocalized.getJa().getContent().setText(titles[SongInfo.LOCALIZED_JA]);
            titleLocalized.getZhHans().getContent().setText(titles[SongInfo.LOCALIZED_ZH_HANS]);
            titleLocalized.getZhHant().getContent().setText(titles[SongInfo.LOCALIZED_ZH_HANT]);
            titleLocalized.getKo().getContent().setText(titles[SongInfo.LOCALIZED_KO]);
            titleLocalized.getTitle().setText(R.string.info_title);
        }
        artist.getContent().setText(single.getArtist());
        artist.getTitle().setText(R.string.info_artist);
        {
            String[] artists = SongInfo.getLocalizedStrings(single.getArtist_localized());
            artistLocalized.getEn().getContent().setText(artists[SongInfo.LOCALIZED_EN]);
            artistLocalized.getJa().getContent().setText(artists[SongInfo.LOCALIZED_JA]);
            artistLocalized.getZhHans().getContent().setText(artists[SongInfo.LOCALIZED_ZH_HANS]);
            artistLocalized.getZhHant().getContent().setText(artists[SongInfo.LOCALIZED_ZH_HANT]);
            artistLocalized.getKo().getContent().setText(artists[SongInfo.LOCALIZED_KO]);
            artistLocalized.getTitle().setText(R.string.info_artist_localized);
        }
        bpm.getContent().setText(single.getBpm());
        bpm.getTitle().setText(R.string.info_bpm);
        bpmBase.getContent().setText(String.valueOf(single.getBpm_base()));
        bpmBase.getTitle().setText(R.string.info_bpm_base);
        set.getContent().setText(single.getSet());
        set.getTitle().setText(R.string.info_set);
        purchase.getContent().setText(single.getPurchase());
        purchase.getTitle().setText(R.string.info_purchase);
        audioPreview.getContent().setText(String.valueOf(single.getAudioPreview()));
        audioPreview.getTitle().setText(R.string.info_preview_start);
        audioPreviewEnd.getContent().setText(String.valueOf(single.getAudioPreviewEnd()));
        audioPreviewEnd.getTitle().setText(R.string.info_preview_end);
        side.getChoice().check(ChoiceContainer.CHOICE_ID[single.getSide() % 2]);
        side.getTitle().setText(R.string.info_side);
        side.getHikari().setText(R.string.info_side_hikari);
        side.getTairitsu().setText(R.string.info_side_tairitsu);
        bg.getContent().setText(single.getBg());
        bg.getTitle().setText(R.string.info_bg);
        {
            String[] daynight = single.getDaynight();
            bgDaynight.getDay().getContent().setText(daynight[SongInfo.DAYNIGHT_DAY]);
            bgDaynight.getNight().getContent().setText(daynight[SongInfo.DAYNIGHT_NIGHT]);
            bgDaynight.getTitle().setText(R.string.info_daynight);
        }
        date.getContent().setText(String.valueOf(single.getDate()));
        date.getTitle().setText(R.string.info_date);
        dateEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDate();
            }
        });
        version.getContent().setText(single.getVersion());
        version.getTitle().setText(R.string.info_version);
        worldUnlock.getContent().setChecked(single.isWorld_unlock());
        worldUnlock.getTitle().setText(R.string.info_world_unlock);
        remoteDl.getContent().setChecked(single.isRemote_dl());
        remoteDl.getTitle().setText(R.string.info_remote_dl);
        bydLocalUnlock.getContent().setChecked(single.isByd_local_unlock());
        bydLocalUnlock.getTitle().setText(R.string.info_byd_local_unlock);
        songlistHidden.getContent().setChecked(single.isSonglist_hidden());
        songlistHidden.getTitle().setText(R.string.info_songlist_hidden);
        noPP.getContent().setChecked(single.isNo_pp());
        noPP.getTitle().setText(R.string.info_no_pp);
        {
            String[] sources = SongInfo.getLocalizedStrings(single.getSource());
            source.getEn().getContent().setText(sources[SongInfo.LOCALIZED_EN]);
            source.getJa().getContent().setText(sources[SongInfo.LOCALIZED_JA]);
            source.getZhHans().getContent().setText(sources[SongInfo.LOCALIZED_ZH_HANS]);
            source.getZhHant().getContent().setText(sources[SongInfo.LOCALIZED_ZH_HANT]);
            source.getKo().getContent().setText(sources[SongInfo.LOCALIZED_KO]);
            source.getTitle().setText(R.string.info_source);
        }
        sourceCopyright.getContent().setText(single.getSource_copyright());
        sourceCopyright.getTitle().setText(R.string.info_source_copyright);
        difficulties[Difficulty.DIFFICULTY_PAST].getTitle().setText(R.string.info_past);
        difficulties[Difficulty.DIFFICULTY_PRESENT].getTitle().setText(R.string.info_present);
        difficulties[Difficulty.DIFFICULTY_FUTURE].getTitle().setText(R.string.info_future);
        difficulties[Difficulty.DIFFICULTY_BEYOND].getTitle().setText(R.string.info_beyond);
        for (int i = 0; i < 4; i++) {
            if (single.getDifficulty(i) != null) {
                Difficulty difficulty = new Difficulty(single.getDifficulty(i));
                int ratingClass = difficulty.getRatingClass() % 4;
                difficulties[ratingClass].getEnabled().getContent().setChecked(true);
                difficulties[ratingClass].getChartDesigner().getContent().setText(difficulty.getChartDesigner());
                difficulties[ratingClass].getJacketDesigner().getContent().setText(difficulty.getJacketDesigner());
                difficulties[ratingClass].getRating().getContent().setText(String.valueOf(difficulty.getRating()));
                difficulties[ratingClass].getRatingPlus().getContent().setChecked(difficulty.isRatingPlus());
                difficulties[ratingClass].getJacketNight().getContent().setText(difficulty.getJacket_night());
                difficulties[ratingClass].getJacketOverride().getContent().setChecked(difficulty.isJacketOverride());
                difficulties[ratingClass].getHiddenUntilUnlocked().getContent().setChecked(difficulty.isHidden_until_unlocked());
                difficulties[ratingClass].getBg().getContent().setText(difficulty.getBg());
                difficulties[ratingClass].getWorldUnlock().getContent().setChecked(difficulty.isWorld_unlock());
                difficulties[ratingClass].refresh();
            }
        }
        Log.d("SongInfoParse", "Song info parsed");
    }

    public void editDate() {
        final AlertDialog dateDialog = new AlertDialog.Builder(root.getContext()).create();
        final View dateWindowView = LayoutInflater.from(root.getContext()).inflate(R.layout.content_edit_date, (ViewGroup) root, false);
        final TimestampTransformer timestampTransformer = new TimestampTransformer(0);

        dateDialog.setView(dateWindowView);
        dateDialog.show();

        final SingleContainer
                editDateYear = new SingleContainer(dateWindowView, R.id.editDateYear, SingleContainer.TYPE_INTEGER),
                editDateMonth = new SingleContainer(dateWindowView, R.id.editDateMonth, SingleContainer.TYPE_INTEGER),
                editDateDate = new SingleContainer(dateWindowView, R.id.editDateDate, SingleContainer.TYPE_INTEGER),
                editDateHour = new SingleContainer(dateWindowView, R.id.editDateHour, SingleContainer.TYPE_INTEGER),
                editDateMinute = new SingleContainer(dateWindowView, R.id.editDateMinute, SingleContainer.TYPE_INTEGER),
                editDateSecond = new SingleContainer(dateWindowView, R.id.editDateSecond, SingleContainer.TYPE_INTEGER),
                editDateOriginal = new SingleContainer(dateWindowView, R.id.editDateOriginal, SingleContainer.TYPE_INTEGER);
        final Button
                finishButton = dateWindowView.findViewById(R.id.dateFinishButton),
                useCurrentTimeButton = dateWindowView.findViewById(R.id.dateUseCurrentTimeButton);

        editDateYear.getTitle().setText(R.string.info_date_year);
        editDateMonth.getTitle().setText(R.string.info_date_month);
        editDateDate.getTitle().setText(R.string.info_date_date);
        editDateHour.getTitle().setText(R.string.info_date_hour);
        editDateMinute.getTitle().setText(R.string.info_date_minute);
        editDateSecond.getTitle().setText(R.string.info_date_second);
        editDateOriginal.getTitle().setText(R.string.info_date);

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog.dismiss();
            }
        });

        useCurrentTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timestampTransformer.setTimestamp(System.currentTimeMillis() / 1000);

                int[] dates = timestampTransformer.getTimes();
                editDateYear.getContent().setText(String.valueOf(dates[TimestampTransformer.TIME_YEAR]));
                editDateMonth.getContent().setText(String.valueOf(dates[TimestampTransformer.TIME_MONTH]));
                editDateDate.getContent().setText(String.valueOf(dates[TimestampTransformer.TIME_DATE]));
                editDateHour.getContent().setText(String.valueOf(dates[TimestampTransformer.TIME_HOUR]));
                editDateMinute.getContent().setText(String.valueOf(dates[TimestampTransformer.TIME_MINUTE]));
                editDateSecond.getContent().setText(String.valueOf(dates[TimestampTransformer.TIME_SECOND]));
                editDateOriginal.getContent().setText(String.valueOf(timestampTransformer.getTimestamp()));
                date.getContent().setText(String.valueOf(timestampTransformer.getTimestamp()));
            }
        });

        timestampTransformer.setTimestamp(new BigInteger(date.getContent().getText().toString(), 10).longValue());

        {
            int[] times = timestampTransformer.getTimes();
            editDateYear.getContent().setText(String.valueOf(times[TimestampTransformer.TIME_YEAR]));
            editDateMonth.getContent().setText(String.valueOf(times[TimestampTransformer.TIME_MONTH]));
            editDateDate.getContent().setText(String.valueOf(times[TimestampTransformer.TIME_DATE]));
            editDateHour.getContent().setText(String.valueOf(times[TimestampTransformer.TIME_HOUR]));
            editDateMinute.getContent().setText(String.valueOf(times[TimestampTransformer.TIME_MINUTE]));
            editDateSecond.getContent().setText(String.valueOf(times[TimestampTransformer.TIME_SECOND]));
            editDateOriginal.getContent().setText(String.valueOf(timestampTransformer.getTimestamp()));
        }

        View.OnFocusChangeListener dateChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (editDateYear.getContent().getText().length() == 0) {
                    editDateYear.getContent().setText("0");
                }
                if (editDateMonth.getContent().getText().length() == 0) {
                    editDateMonth.getContent().setText("0");
                }
                if (editDateDate.getContent().getText().length() == 0) {
                    editDateDate.getContent().setText("0");
                }
                if (editDateHour.getContent().getText().length() == 0) {
                    editDateHour.getContent().setText("0");
                }
                if (editDateMinute.getContent().getText().length() == 0) {
                    editDateMinute.getContent().setText("0");
                }
                if (editDateSecond.getContent().getText().length() == 0) {
                    editDateSecond.getContent().setText("0");
                }
                if (!hasFocus) {
                    timestampTransformer.setDateTime(new int[]{
                            new BigInteger(editDateYear.getContent().getText().toString(), 10).intValue(),
                            new BigInteger(editDateMonth.getContent().getText().toString(), 10).intValue(),
                            new BigInteger(editDateDate.getContent().getText().toString(), 10).intValue(),
                            new BigInteger(editDateHour.getContent().getText().toString(), 10).intValue(),
                            new BigInteger(editDateMinute.getContent().getText().toString(), 10).intValue(),
                            new BigInteger(editDateSecond.getContent().getText().toString(), 10).intValue()
                    });
                    int[] dates = timestampTransformer.getTimes();
                    editDateYear.getContent().setText(String.valueOf(dates[TimestampTransformer.TIME_YEAR]));
                    editDateMonth.getContent().setText(String.valueOf(dates[TimestampTransformer.TIME_MONTH]));
                    editDateDate.getContent().setText(String.valueOf(dates[TimestampTransformer.TIME_DATE]));
                    editDateHour.getContent().setText(String.valueOf(dates[TimestampTransformer.TIME_HOUR]));
                    editDateMinute.getContent().setText(String.valueOf(dates[TimestampTransformer.TIME_MINUTE]));
                    editDateSecond.getContent().setText(String.valueOf(dates[TimestampTransformer.TIME_SECOND]));
                    editDateOriginal.getContent().setText(String.valueOf(timestampTransformer.getTimestamp()));
                    date.getContent().setText(String.valueOf(timestampTransformer.getTimestamp()));
                }
            }
        };

        View.OnFocusChangeListener timeChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (editDateOriginal.getContent().getText().length() == 0) {
                    editDateOriginal.getContent().setText("0");
                }
                if (!hasFocus) {
                    timestampTransformer.setTimestamp(new BigInteger(editDateOriginal.getContent().getText().toString(), 10).longValue());
                    int[] dates = timestampTransformer.getTimes();
                    editDateYear.getContent().setText(String.valueOf(dates[TimestampTransformer.TIME_YEAR]));
                    editDateMonth.getContent().setText(String.valueOf(dates[TimestampTransformer.TIME_MONTH]));
                    editDateDate.getContent().setText(String.valueOf(dates[TimestampTransformer.TIME_DATE]));
                    editDateHour.getContent().setText(String.valueOf(dates[TimestampTransformer.TIME_HOUR]));
                    editDateMinute.getContent().setText(String.valueOf(dates[TimestampTransformer.TIME_MINUTE]));
                    editDateSecond.getContent().setText(String.valueOf(dates[TimestampTransformer.TIME_SECOND]));
                    editDateOriginal.getContent().setText(String.valueOf(timestampTransformer.getTimestamp()));
                    date.getContent().setText(String.valueOf(timestampTransformer.getTimestamp()));
                }
            }
        };

        editDateYear.getContent().setOnFocusChangeListener(dateChangeListener);
        editDateMonth.getContent().setOnFocusChangeListener(dateChangeListener);
        editDateDate.getContent().setOnFocusChangeListener(dateChangeListener);
        editDateHour.getContent().setOnFocusChangeListener(dateChangeListener);
        editDateMinute.getContent().setOnFocusChangeListener(dateChangeListener);
        editDateSecond.getContent().setOnFocusChangeListener(dateChangeListener);
        editDateOriginal.getContent().setOnFocusChangeListener(timeChangeListener);
    }

    public SongInfo getSongInfo() {
        SongInfo single = new SongInfo();
        single.setId(id.getContent().getText().toString());
        single.setTitle_localized(SongInfo.getLocalizedFromStrings(
                new String[]{
                        titleLocalized.getEn().getContent().getText().toString(),
                        (titleLocalized.getJa().getContent().getText().length() == 0) ? null : titleLocalized.getJa().getContent().getText().toString(),
                        (titleLocalized.getZhHans().getContent().getText().length() == 0) ? null : titleLocalized.getZhHans().getContent().getText().toString(),
                        (titleLocalized.getZhHant().getContent().getText().length() == 0) ? null : titleLocalized.getZhHant().getContent().getText().toString(),
                        (titleLocalized.getKo().getContent().getText().length() == 0) ? null : titleLocalized.getKo().getContent().getText().toString()
                }
        ));
        single.setArtist(artist.getContent().getText().toString());
        single.setArtist_localized(
                (
                        (artistLocalized.getEn().getContent().getText().length() == 0) &&
                                (artistLocalized.getJa().getContent().getText().length() == 0) &&
                                (artistLocalized.getZhHans().getContent().getText().length() == 0) &&
                                (artistLocalized.getZhHant().getContent().getText().length() == 0) &&
                                (artistLocalized.getKo().getContent().getText().length() == 0)
                ) ?
                        null :
                        SongInfo.getLocalizedFromStrings(
                                new String[]{
                                        artistLocalized.getEn().getContent().getText().toString(),
                                        (artistLocalized.getJa().getContent().getText().length() == 0) ? null : titleLocalized.getJa().getContent().getText().toString(),
                                        (artistLocalized.getZhHans().getContent().getText().length() == 0) ? null : titleLocalized.getZhHans().getContent().getText().toString(),
                                        (artistLocalized.getZhHant().getContent().getText().length() == 0) ? null : titleLocalized.getZhHant().getContent().getText().toString(),
                                        (artistLocalized.getKo().getContent().getText().length() == 0) ? null : titleLocalized.getKo().getContent().getText().toString()
                                })
        );
        single.setBpm(bpm.getContent().getText().toString());
        single.setBpm_base(new BigDecimal(bpmBase.getContent().getText().toString()).doubleValue());
        single.setSet(set.getContent().getText().toString());
        single.setPurchase(purchase.getContent().getText().toString());
        single.setAudioPreview(new BigInteger(audioPreview.getContent().getText().toString()).intValue());
        single.setAudioPreviewEnd(new BigInteger(audioPreviewEnd.getContent().getText().toString()).intValue());
        single.setSide(side.getSelected());
        single.setBg(bg.getContent().getText().toString());
        single.setBg_daynight(
                (
                        (bgDaynight.getDay().getContent().getText().length() == 0) &&
                                (bgDaynight.getNight().getContent().getText().length() == 0)
                ) ?
                        null :
                        SongInfo.getDaynightFromStrings(
                                new String[]{
                                        bgDaynight.getDay().getContent().getText().toString(),
                                        bgDaynight.getNight().getContent().getText().toString()
                                })
        );
        single.setDate(new BigInteger(date.getContent().getText().toString()).longValue());
        single.setVersion(version.getContent().getText().toString());
        single.setWorld_unlock(worldUnlock.getContent().isChecked());
        single.setRemote_dl(remoteDl.getContent().isChecked());
        single.setByd_local_unlock(bydLocalUnlock.getContent().isChecked());
        single.setNo_pp(noPP.getContent().isChecked());
        single.setSource(
                (
                        (source.getEn().getContent().getText().length() == 0) &&
                                (source.getJa().getContent().getText().length() == 0) &&
                                (source.getZhHans().getContent().getText().length() == 0) &&
                                (source.getZhHant().getContent().getText().length() == 0) &&
                                (source.getKo().getContent().getText().length() == 0)
                ) ?
                        null :
                        SongInfo.getLocalizedFromStrings(
                                new String[]{
                                        source.getEn().getContent().getText().toString(),
                                        (source.getJa().getContent().getText().length() == 0) ? null : titleLocalized.getJa().getContent().getText().toString(),
                                        (source.getZhHans().getContent().getText().length() == 0) ? null : titleLocalized.getZhHans().getContent().getText().toString(),
                                        (source.getZhHant().getContent().getText().length() == 0) ? null : titleLocalized.getZhHant().getContent().getText().toString(),
                                        (source.getKo().getContent().getText().length() == 0) ? null : titleLocalized.getKo().getContent().getText().toString()
                                })
        );
        single.setSource_copyright((sourceCopyright.getContent().getText().length() == 0) ? null : sourceCopyright.getContent().getText().toString());
        int cursor = -1;
        for (int i = 0; i < 4; i++) {
            if (difficulties[i].getEnabled().getContent().isChecked()) {
                cursor++;
                Difficulty difficulty = new Difficulty();
                difficulty.setRatingClass(i);
                difficulty.setChartDesigner(difficulties[i].getChartDesigner().getContent().getText().toString());
                difficulty.setJacketDesigner(difficulties[i].getJacketDesigner().getContent().getText().toString());
                difficulty.setRating((difficulties[i].getRating().getContent().getText().length() == 0) ? 0 : (new BigInteger(difficulties[i].getRating().getContent().getText().toString()).intValue()));
                difficulty.setRatingPlus(difficulties[i].getRatingPlus().getContent().isChecked());
                difficulty.setJacket_night((difficulties[i].getJacketNight().getContent().getText().length() == 0) ? null : difficulties[i].getJacketNight().getContent().getText().toString());
                difficulty.setJacketOverride(difficulties[i].getJacketOverride().getContent().isChecked());
                difficulty.setHidden_until_unlocked(difficulties[i].getHiddenUntilUnlocked().getContent().isChecked());
                difficulty.setBg((difficulties[i].getBg().getContent().getText().length() == 0) ? null : difficulties[i].getBg().getContent().getText().toString());
                difficulty.setWorld_unlock(difficulties[i].getWorldUnlock().getContent().isChecked());
                single.setDifficulty(difficulty, cursor);
            }
        }
        return single;
    }

    private void refresh() {
        for (DifficultyContainer difficulty : difficulties) {
            difficulty.setVisible(isVisible);
        }
        if (isVisible) {
            partsHeight = HEIGHT_PARTS;
            textHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            partsHeight = textHeight = 0;
        }

        setHeight(artistLocalized.getRoot(), textHeight);
        setHeight(purchase.getRoot(), textHeight);
        setHeight(bgDaynight.getRoot(), textHeight);
        setHeight(worldUnlock.getRoot(), partsHeight);
        setHeight(remoteDl.getRoot(), partsHeight);
        setHeight(bydLocalUnlock.getRoot(), partsHeight);
        setHeight(songlistHidden.getRoot(), partsHeight);
        setHeight(noPP.getRoot(), partsHeight);
        setHeight(source.getRoot(), textHeight);
        setHeight(sourceCopyright.getRoot(), textHeight);
    }

    private void setHeight(View view, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
        refresh();
    }
}
