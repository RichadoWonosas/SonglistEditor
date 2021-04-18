package ap.yadora.richadowonosas.fmdslst.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;

import ap.yadora.richadowonosas.fmdslst.R;
import ap.yadora.richadowonosas.fmdslst.container.SongContainer;
import ap.yadora.richadowonosas.fmdslst.song.SongInfo;
import ap.yadora.richadowonosas.fmdslst.song.Songlist;

public class SongInfoEditActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SongInfo savedInfo, currentInfo;
    private SongContainer single;
    private boolean isVisible = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_info_edit);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                bindView();

                setSupportActionBar(toolbar);

                if (savedInstanceState == null) {
                    getSongInfo();
                } else {
                    recoverSongInfo(savedInstanceState);
                }
                setViewContent();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            cacheSongInfo();
            outState.putString("current", currentInfo.toJSONObject().toString());
            outState.putString("saved", savedInfo.toJSONObject().toString());
            Log.d("Save", "Info cached");
        } catch (Exception e) {
            // Log.e("Save", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        returnToSelectPage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_song_edit, menu);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    private void getSongInfo() {
        String songId = getIntent().getStringExtra("song_id");
        savedInfo = Songlist.songlist.getSongInfoById(songId);
        currentInfo = new SongInfo(savedInfo);
    }

    private void recoverSongInfo(Bundle savedInstanceState) {
        try {
            currentInfo = SongInfo.getSongInfoFromJson(new JSONObject(savedInstanceState.getString("current")));
            savedInfo = SongInfo.getSongInfoFromJson(new JSONObject(savedInstanceState.getString("saved")));
            Log.d("Recovery", "Info loaded, equals = " + currentInfo.equals(savedInfo));
        } catch (Exception e) {
            Log.e("Recovery", e.getMessage());
        }
    }

    private void bindView() {
        toolbar = findViewById(R.id.editToolbar);
        single = new SongContainer(findViewById(R.id.editSingle));
    }

    private void setViewContent() {
        single.getInitialContentFromSongInfo(currentInfo);
    }

    public void onClickBackButton(View view) {
        returnToSelectPage();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemSaveSongInfo:
                cacheSongInfo();
                saveSongInfo();
                Log.d("Save", "Saved, equals check = " + savedInfo.equals(currentInfo));
                break;
            case R.id.itemExportSongInfo:
                exportSongInfoToJSONFile();
                Log.d("Export", "Exported (id = " + currentInfo.getId() + "}");
                break;
            case R.id.itemSwitchSeldomUsed:
                isVisible = !isVisible;
                single.setVisible(isVisible);
                if (isVisible) {
                    item.setIcon(R.drawable.icon_menu_invisible);
                    item.setTitle(R.string.selection_hide_seldom_used);
                } else {
                    item.setIcon(R.drawable.icon_menu_visible);
                    item.setTitle(R.string.selection_show_seldom_used);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void returnToSelectPage() {
        cacheSongInfo();
        if (savedInfo.equals(currentInfo)) {
            finish();
        } else {
            final AlertDialog returnDialog = new AlertDialog.Builder(this).create();

            View returnDialogView = LayoutInflater.from(this.getBaseContext()).inflate(R.layout.content_return, null, false);

            returnDialog.setView(returnDialogView);
            returnDialog.show();

            Button confirmButton = returnDialogView.findViewById(R.id.confirmButton),
                    cancelButton = returnDialogView.findViewById(R.id.cancelButton),
                    alternativeButton = returnDialogView.findViewById(R.id.alternativeButton);

            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveSongInfo();
                    returnDialog.dismiss();
                    finish();
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnDialog.dismiss();
                }
            });
            alternativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog confirmDialog = new AlertDialog.Builder(SongInfoEditActivity.this).create();

                    View confirmDialogView = LayoutInflater.from(SongInfoEditActivity.this.getBaseContext()).inflate(R.layout.content_confirm, null, false);

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
    }

    private void saveSongInfo() {
        Songlist.songlist.removeSong(savedInfo.getId());
        savedInfo = Songlist.songlist.getSongInfoById(Songlist.songlist.addSong(currentInfo));
        currentInfo = new SongInfo(savedInfo);
        single.getInitialContentFromSongInfo(currentInfo);
        Toast.makeText(this, R.string.message_song_info_save_success, Toast.LENGTH_SHORT).show();
    }

    private void cacheSongInfo() {
        Log.d("Cache", String.valueOf(single.getSongInfo().equals(currentInfo)));
        currentInfo = new SongInfo(single.getSongInfo());
    }

    private void exportSongInfoToJSONFile() {
        try {
            cacheSongInfo();
            File exportFile = new File(Songlist.songlist.getSonglistFileParent(), currentInfo.getId() + ".inf");
            if (!exportFile.exists()) {
                if (!exportFile.createNewFile()) {
                    Toast.makeText(this, R.string.error_export_failed, Toast.LENGTH_SHORT).show();
                }
            }
            FileWriter fileWriter = new FileWriter(exportFile);

            fileWriter.write(currentInfo.toJSONObject().toString(2));

            fileWriter.close();
            Toast.makeText(this, R.string.message_song_info_export_success, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("Export", "Failed to export song info");
        }
    }
}
