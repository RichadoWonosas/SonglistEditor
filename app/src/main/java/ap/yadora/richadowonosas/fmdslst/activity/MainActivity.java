package ap.yadora.richadowonosas.fmdslst.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;

import ap.yadora.richadowonosas.fmdslst.R;
import ap.yadora.richadowonosas.fmdslst.song.Songlist;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 0x0100;
    private static final int CODE_FIND_SONGLIST = 0x0101;
    private final File defaultSonglistFile = new File(Environment.getExternalStorageDirectory().getPath(), "arcfmd/songs/songlist");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);

        // Disable all buttons
        findViewById(R.id.workDefault).setEnabled(false);
        findViewById(R.id.selectSonglist).setEnabled(false);

        // Check permission and request permission if needed
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Permission", "Permission forbidden");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
        } else {
            Log.d("Permission", "Permission admitted");
            // Enable the default songlist button if default songlist exists
            if (Songlist.isSonglistFileAvailable(defaultSonglistFile)) {
                findViewById(R.id.workDefault).setEnabled(true);
            }
            // Enable select button
            findViewById(R.id.selectSonglist).setEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.error_permission_denied, Toast.LENGTH_SHORT).show();
            } else {
                // Enable the default songlist button if default songlist exists
                if (Songlist.isSonglistFileAvailable(defaultSonglistFile)) {
                    findViewById(R.id.workDefault).setEnabled(true);
                }
                // Enable select button
                findViewById(R.id.selectSonglist).setEnabled(true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_scene, menu);
        return true;
    }

    public void onWorkDefault(View view) throws IOException, JSONException {
        Songlist.songlist.readSonglistFile(defaultSonglistFile);

        Intent intent = new Intent(MainActivity.this, SelectSongActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemInfo:
                final AlertDialog infoDialog = new AlertDialog.Builder(this).create();

                View infoDialogView = LayoutInflater.from(this.getBaseContext()).inflate(R.layout.content_about, null, false);

                infoDialog.setView(infoDialogView);
                infoDialog.show();

                ((ImageView) infoDialogView.findViewById(R.id.imageAuthor)).setImageResource(R.drawable.author);
                try {
                    ((TextView) infoDialogView.findViewById(R.id.descriptionVersion)).setText(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
                } catch (Exception e) {
                    ((TextView) infoDialogView.findViewById(R.id.descriptionVersion)).setText("");
                }

                infoDialogView.findViewById(R.id.infoFinishButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        infoDialog.dismiss();
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            Log.e("Activity", "Failed to get activity result");
            switch (requestCode) {
                case CODE_FIND_SONGLIST:
                    Toast.makeText(this, R.string.error_choose_songlist_failed, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            return;
        }

        switch (requestCode) {
            case CODE_FIND_SONGLIST:

                File songlistFile = new File(Environment.getExternalStorageDirectory().getPath(),
                        data.getData().getPath().replaceAll("/document/primary:", ""));

                Log.d("Songlist", "Songlist file path: " + data.getData().getPath());

                if (Songlist.isSonglistFileAvailable(songlistFile)) {
                    try {
                        Songlist.songlist.readSonglistFile(songlistFile);
                        Intent intent = new Intent(MainActivity.this, SelectSongActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, R.string.error_choose_songlist_failed, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, R.string.error_choose_songlist_failed, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    public void onSelectSonglist(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.title_choose_songlist)), CODE_FIND_SONGLIST);
    }

    public void onCheckLicense(View view) {
        startActivity(new Intent(this, LicenseActivity.class));
    }
}
