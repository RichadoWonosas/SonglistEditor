package ap.yadora.richadowonosas.fmdslst.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;

import ap.yadora.richadowonosas.fmdslst.R;
import ap.yadora.richadowonosas.fmdslst.util.SongInfoAdapter;
import ap.yadora.richadowonosas.fmdslst.song.Songlist;

public class SelectSongActivity extends AppCompatActivity {
    public static final String SONGS_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath() + "/arcfmd/songs";

    private FloatingActionButton menuButton, addSongButton, importSongButton, saveSonglistButton;
    private TextView addText, importText, saveText;
    private ImageView whiteBack;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private boolean isExpanded = false;
    private float colorRate = 0;

    private int sortCriterion = Songlist.SORT_BY_SONG_ID;
    private boolean isAscend = true;

    private final SongInfoAdapter songInfoAdapter = new SongInfoAdapter(sortCriterion, isAscend);
    private static final int CODE_FIND_SONG_INFO_FILE = 0xcafe;
    private static final int SELECTION_EDIT = 0;
    private static final int SELECTION_COPY = 1;
    private static final int SELECTION_EXPORT = 2;
    private static final int SELECTION_DELETE = 3;

    private static final int[] SORT_BY = {R.id.choiceById, R.id.choiceByDate, R.id.choiceByVersion};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_song);

        bindViews();

        setSupportActionBar(toolbar);

        setUpButtons();

        setUpRecyclerView();
    }

    @Override
    public void onBackPressed() {
        if (isExpanded) {
            isExpanded = false;
        } else {
            returnToMainPage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            Log.e("Activity", "Failed to get activity result");
            switch (requestCode) {
                case CODE_FIND_SONG_INFO_FILE:
                    Toast.makeText(this, R.string.error_choose_song_info_failed, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            return;
        }

        switch (requestCode) {
            case CODE_FIND_SONG_INFO_FILE:

                File songInfoFile = new File(Environment.getExternalStorageDirectory().getPath(),
                        data.getData().getPath().replaceAll("/document/primary:", ""));

                Log.d("Import", "Song info file path: " + data.getData().getPath());

                if (Songlist.isSonglistFileAvailable(songInfoFile)) {
                    try {
                        if (Songlist.songlist.importSongFromJSON(
                                new JSONObject(Songlist.readSonglistToString(songInfoFile)))) {
                            songInfoAdapter.refresh();
                            Toast.makeText(this, R.string.message_song_info_import_success, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, R.string.error_choose_song_info_failed, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, R.string.error_choose_song_info_failed, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, R.string.error_choose_song_info_failed, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void setUpButtons() {
        final Handler handler = new Handler();

        addSongButton.setEnabled(false);
        importSongButton.setEnabled(false);
        saveSonglistButton.setEnabled(false);
        addText.setScaleX(0);
        addText.setScaleY(0);
        importText.setScaleX(0);
        importText.setScaleY(0);
        saveText.setScaleX(0);
        saveText.setScaleY(0);
        menuButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                isExpanded = !isExpanded;
                addSongButton.setEnabled(isExpanded);
                importSongButton.setEnabled(isExpanded);
                saveSonglistButton.setEnabled(isExpanded);
            }
        });

        handler.post(new Runnable() {
            final float addSongButtonFinalPosition = getResources().getDimension(R.dimen.fab_margin_single) +
                    getResources().getDimension(R.dimen.fab_margin_single_small) * 2;
            final float importSongButtonFinalPosition = getResources().getDimension(R.dimen.fab_margin_single) +
                    getResources().getDimension(R.dimen.fab_margin_single_small);
            final float saveSonglistButtonFinalPosition = getResources().getDimension(R.dimen.fab_margin_single);
            final float textHorizontalFinalPosition = getResources().getDimension(R.dimen.text_expand_margin);
            final float deceleratingDistance = getResources().getDimension(R.dimen.fab_decelerating_distance);
            final int originalColor = ContextCompat.getColor(SelectSongActivity.this, R.color.tairitsuLight);
            final int expandedColor = ContextCompat.getColor(SelectSongActivity.this, R.color.hikariLight);
            float textHorizontalPosition = 0, textScale = 0;

            @Override
            synchronized public void run() {
                handler.postDelayed(this, 2);

                float addSongButtonCurrentPosition = addSongButton.getTranslationY(),
                        importSongButtonCurrentPosition = importSongButton.getTranslationY(),
                        saveSonglistButtonCurrentPosition = saveSonglistButton.getTranslationY(),
                        menuButtonCurrentAngle = menuButton.getRotation(),
                        whiteBackAlpha = whiteBack.getAlpha();

                if (isExpanded) {
                    whiteBack.setEnabled(true);
                    whiteBack.setClickable(true);
                    whiteBack.setFocusable(true);
                    addSongButton.setTranslationY(addSongButtonCurrentPosition -
                            getSpeedAtDistance(addSongButtonCurrentPosition - addSongButtonFinalPosition));
                    importSongButton.setTranslationY(importSongButtonCurrentPosition -
                            getSpeedAtDistance(importSongButtonCurrentPosition - importSongButtonFinalPosition));
                    saveSonglistButton.setTranslationY(saveSonglistButtonCurrentPosition -
                            getSpeedAtDistance(saveSonglistButtonCurrentPosition - saveSonglistButtonFinalPosition));
                    addText.setTranslationY(addSongButtonCurrentPosition -
                            getSpeedAtDistance(addSongButtonCurrentPosition - addSongButtonFinalPosition));
                    importText.setTranslationY(importSongButtonCurrentPosition -
                            getSpeedAtDistance(importSongButtonCurrentPosition - importSongButtonFinalPosition));
                    saveText.setTranslationY(saveSonglistButtonCurrentPosition -
                            getSpeedAtDistance(saveSonglistButtonCurrentPosition - saveSonglistButtonFinalPosition));
                    menuButton.setRotation((menuButtonCurrentAngle - 60) * 3 / 4);
                    textHorizontalPosition -= getSpeedAtDistance(textHorizontalPosition - textHorizontalFinalPosition);
                    addText.setTranslationX(textHorizontalPosition);
                    importText.setTranslationX(textHorizontalPosition);
                    saveText.setTranslationX(textHorizontalPosition);
                    textScale = (1 + textScale * 3) / 4;
                    addText.setScaleX(textScale);
                    addText.setScaleY(textScale);
                    importText.setScaleX(textScale);
                    importText.setScaleY(textScale);
                    saveText.setScaleX(textScale);
                    saveText.setScaleY(textScale);
                    if (colorRate < 1) {
                        colorRate = (colorRate + 1 / 8.0f);
                        if (colorRate > 1)
                            colorRate = 1;
                        menuButton.setBackgroundTintList(
                                new ColorStateList(
                                        new int[1][0],
                                        new int[]{getMixedColor(colorRate, originalColor, expandedColor)}
                                )
                        );
                    }
                    if (whiteBackAlpha < 0.6f) {
                        whiteBack.setAlpha(whiteBackAlpha + 0.06f);
                    }
                } else {
                    whiteBack.setEnabled(false);
                    whiteBack.setClickable(false);
                    whiteBack.setFocusable(false);
                    addSongButton.setTranslationY(addSongButtonCurrentPosition +
                            getSpeedAtDistance(-addSongButtonCurrentPosition));
                    importSongButton.setTranslationY(importSongButtonCurrentPosition +
                            getSpeedAtDistance(-importSongButtonCurrentPosition));
                    saveSonglistButton.setTranslationY(saveSonglistButtonCurrentPosition +
                            getSpeedAtDistance(-saveSonglistButtonCurrentPosition));
                    addText.setTranslationY(addSongButtonCurrentPosition +
                            getSpeedAtDistance(-addSongButtonCurrentPosition));
                    importText.setTranslationY(importSongButtonCurrentPosition +
                            getSpeedAtDistance(-importSongButtonCurrentPosition));
                    saveText.setTranslationY(saveSonglistButtonCurrentPosition +
                            getSpeedAtDistance(-saveSonglistButtonCurrentPosition));
                    menuButton.setRotation(menuButtonCurrentAngle * 3 / 4);
                    textHorizontalPosition = textHorizontalPosition + getSpeedAtDistance(-textHorizontalPosition);
                    addText.setTranslationX(textHorizontalPosition);
                    importText.setTranslationX(textHorizontalPosition);
                    saveText.setTranslationX(textHorizontalPosition);
                    textScale = textScale * 3 / 4;
                    addText.setScaleX(textScale);
                    addText.setScaleY(textScale);
                    importText.setScaleX(textScale);
                    importText.setScaleY(textScale);
                    saveText.setScaleX(textScale);
                    saveText.setScaleY(textScale);
                    colorRate = (colorRate > 0) ? (colorRate - 1 / 8.0f) : 0;
                    if (colorRate > 0) {
                        colorRate = (colorRate - 1 / 8.0f);
                        if (colorRate < 0)
                            colorRate = 0;
                        menuButton.setBackgroundTintList(
                                new ColorStateList(
                                        new int[1][0],
                                        new int[]{getMixedColor(colorRate, originalColor, expandedColor)}
                                )
                        );
                    }
                    if (whiteBackAlpha > 0) {
                        whiteBack.setAlpha(whiteBackAlpha - 0.06f);
                    }
                }
                menuButton.bringToFront();
            }

            private float getSpeedAtDistance(float distance) {
                if (distance == 0)
                    return 0;

                return ((distance > deceleratingDistance) ? deceleratingDistance : distance) / 3;
            }

            private int getMixedColor(float rate, int color1, int color2) {
                return ((int) (((color1 >> 24) & 0xff) * (1 - rate) + ((color2 >> 24) & 0xff) * rate) << 24) ^
                        ((int) (((color1 >> 16) & 0xff) * (1 - rate) + ((color2 >> 16) & 0xff) * rate) << 16) ^
                        ((int) (((color1 >> 8) & 0xff) * (1 - rate) + ((color2 >> 8) & 0xff) * rate) << 8) ^
                        ((int) ((color1 & 0xff) * (1 - rate) + (color2 & 0xff) * rate));
            }
        });
    }

    private void bindViews() {
        toolbar = findViewById(R.id.toolbarSelectSong);
        addSongButton = findViewById(R.id.addSongButton);
        addText = findViewById(R.id.textAdd);
        importSongButton = findViewById(R.id.importSongButton);
        importText = findViewById(R.id.textImport);
        saveSonglistButton = findViewById(R.id.saveSonglistButton);
        saveText = findViewById(R.id.textSave);
        menuButton = findViewById(R.id.menuButton);
        whiteBack = findViewById(R.id.backgroundWhite);
        recyclerView = findViewById(R.id.recyclerView);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        songInfoAdapter.refresh();
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        songInfoAdapter.bindColour(this);
        songInfoAdapter.setOnItemClickListener(new SongInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                editSongInfo(Songlist.songlist.getSongs(sortCriterion, isAscend)[position]);
            }

            @Override
            public void onItemLongClick(View view, final int position) {

                View operationView = LayoutInflater.from(SelectSongActivity.this.getBaseContext()).inflate(R.layout.content_selection, null, false);
                final String songId = Songlist.songlist.getSongs(sortCriterion, isAscend)[position];

                final AlertDialog operationDialog = new AlertDialog.Builder(SelectSongActivity.this)
                        .setView(operationView)
                        .create();

                operationDialog.show();

                operationView.findViewById(R.id.selection_edit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editSongInfo(songId);
                        operationDialog.dismiss();
                    }
                });

                operationView.findViewById(R.id.selection_duplicate).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        duplicateSongInfo(songId);
                        operationDialog.dismiss();
                    }
                });

                operationView.findViewById(R.id.selection_export).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exportSongInfoToJSONFile(songId);
                        operationDialog.dismiss();
                    }
                });

                operationView.findViewById(R.id.selection_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final AlertDialog confirmDialog = new AlertDialog.Builder(SelectSongActivity.this).create();

                        View confirmDialogView = LayoutInflater.from(SelectSongActivity.this.getBaseContext()).inflate(R.layout.content_confirm, null, false);

                        confirmDialog.setView(confirmDialogView);
                        confirmDialog.show();

                        ((TextView) confirmDialogView.findViewById(R.id.textConfirm)).setText(R.string.description_confirm_delete);

                        confirmDialogView.findViewById(R.id.yesButton).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteSongInfoFromSonglist(songId);
                                confirmDialog.dismiss();
                                operationDialog.dismiss();
                            }
                        });

                        confirmDialogView.findViewById(R.id.noButton).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                confirmDialog.dismiss();
                            }
                        });
                    }
                });
            }
        });
        recyclerView.setAdapter(songInfoAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    public void onClickBackgroundWhite(View view) {
        if (isExpanded) {
            isExpanded = false;
        }
    }

    public void onClickReturnButton(View view) {
        if (isExpanded) {
            isExpanded = false;
        } else {
            returnToMainPage();
        }
    }

    public void onClickSortButton(View view) {
        changeSortSetting();
    }

    public void onClickSaveSonglistButton(View view) {
        saveToSonglistFile();
    }

    public void onClickImportSongButton(View view) {
        importSongInfoFromJSONFile();
    }

    public void onClickAddSongButton(View view) {
        createNewSongInfo();
    }

    private void returnToMainPage() {
        final AlertDialog returnDialog = new AlertDialog.Builder(this).create();

        View returnDialogView = LayoutInflater.from(this.getBaseContext()).inflate(R.layout.content_return, null, false);

        returnDialog.setView(returnDialogView);
        returnDialog.show();

        returnDialogView.findViewById(R.id.confirmButton)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveToSonglistFile();
                        returnDialog.dismiss();
                        finish();
                    }
                });
        returnDialogView.findViewById(R.id.cancelButton)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        returnDialog.dismiss();
                    }
                });
        returnDialogView.findViewById(R.id.alternativeButton)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final AlertDialog confirmDialog = new AlertDialog.Builder(SelectSongActivity.this).create();

                        View confirmDialogView = LayoutInflater.from(SelectSongActivity.this.getBaseContext()).inflate(R.layout.content_confirm, null, false);

                        confirmDialog.setView(confirmDialogView);
                        confirmDialog.show();

                        confirmDialogView.findViewById(R.id.yesButton).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                confirmDialog.dismiss();
                                returnDialog.dismiss();
                                finish();
                            }
                        });

                        confirmDialogView.findViewById(R.id.noButton).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                confirmDialog.dismiss();
                            }
                        });
                    }
                });
    }

    private void changeSortSetting() {
        final AlertDialog sortDialog = new AlertDialog.Builder(this).create();

        final View sortDialogView = LayoutInflater.from(this.getBaseContext()).inflate(R.layout.content_sort_setting, null, false);

        sortDialog.setView(sortDialogView);
        sortDialog.show();

        ((RadioGroup) sortDialogView.findViewById(R.id.choiceCriterion)).check(SORT_BY[sortCriterion]);
        ((RadioGroup) sortDialogView.findViewById(R.id.choiceOrder)).check(isAscend ? R.id.choiceByAscending : R.id.choiceByDescending);

        sortDialogView.findViewById(R.id.finishButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((RadioGroup) sortDialogView.findViewById(R.id.choiceCriterion)).getCheckedRadioButtonId() == SORT_BY[1]) {
                    sortCriterion = 1;
                } else if (((RadioGroup) sortDialogView.findViewById(R.id.choiceCriterion)).getCheckedRadioButtonId() == SORT_BY[2]) {
                    sortCriterion = 2;
                } else {
                    sortCriterion = 0;
                }
                isAscend = (((RadioGroup) sortDialogView.findViewById(R.id.choiceOrder)).getCheckedRadioButtonId() == R.id.choiceByAscending);

                songInfoAdapter.setSorting(sortCriterion, isAscend);
                songInfoAdapter.refresh();
                sortDialog.dismiss();
            }
        });
    }

    private void createNewSongInfo() {
        final View newSongView = LayoutInflater.from(this.getBaseContext()).inflate(R.layout.content_add_new_song, null, false);
        final AlertDialog newSongDialog = new AlertDialog.Builder(this)
                .setView(newSongView)
                .create();

        newSongDialog.show();

        final EditText editNewSongID = newSongView.findViewById(R.id.editNewSongID);
        Button songFinishButton = newSongView.findViewById(R.id.songFinishButton);

        editNewSongID.setInputType(InputType.TYPE_CLASS_TEXT);
        songFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Songlist.songlist.addSong(editNewSongID.getText().toString());
                Toast.makeText(SelectSongActivity.this, R.string.message_song_info_create_success, Toast.LENGTH_SHORT).show();
                newSongDialog.dismiss();
                songInfoAdapter.refresh();
                isExpanded = false;
            }
        });
    }

    private void importSongInfoFromJSONFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.title_choose_song_info)), CODE_FIND_SONG_INFO_FILE);
        return;
    }

    private void saveToSonglistFile() {
        try {
            Songlist.songlist.saveSonglistToFile();
            Toast.makeText(SelectSongActivity.this, R.string.message_songlist_save_success, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("Save", e.getMessage());
            Toast.makeText(SelectSongActivity.this, R.string.error_save_failed, Toast.LENGTH_SHORT).show();
        }
    }

    private void editSongInfo(String songId) {
        Intent intent = new Intent(SelectSongActivity.this, SongInfoEditActivity.class);
        intent.putExtra("song_id", songId);
        startActivity(intent);
    }

    private void exportSongInfoToJSONFile(String songId) {
        try {
            File exportFile = new File(Songlist.songlist.getSonglistFileParent(), songId + ".inf");
            if (!exportFile.exists()) {
                if (!exportFile.createNewFile()) {
                    Toast.makeText(this, R.string.error_export_failed, Toast.LENGTH_SHORT).show();
                }
            }
            FileWriter fileWriter = new FileWriter(exportFile);

            fileWriter.write(Songlist.songlist.getSongJSON(songId).toString(2));

            fileWriter.close();
            Toast.makeText(this, R.string.message_song_info_export_success, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("Export", "Failed to export song info");
        }
    }

    private void duplicateSongInfo(String songId) {
        if (Songlist.songlist.duplicateSong(songId)) {
            songInfoAdapter.refresh();
            Toast.makeText(this, R.string.message_song_info_duplicate_success, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.error_song_info_not_found, Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteSongInfoFromSonglist(String songId) {
        if (Songlist.songlist.removeSong(songId)) {
            songInfoAdapter.refresh();
            Toast.makeText(this, R.string.message_song_info_delete_success, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.error_delete_failed, Toast.LENGTH_SHORT).show();
        }
    }
}