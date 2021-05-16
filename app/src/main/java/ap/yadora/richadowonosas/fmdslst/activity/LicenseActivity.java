package ap.yadora.richadowonosas.fmdslst.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ap.yadora.richadowonosas.fmdslst.R;

public class LicenseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView licenseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        bindViews();

        setSupportActionBar(toolbar);
        setLicenseText();
    }

    private void bindViews() {
        toolbar = findViewById(R.id.toolbarLicense);
        licenseText = findViewById(R.id.licenseContent);
    }

    private void setLicenseText() {
        licenseText.setText(getText(R.string.content_license));
    }

    public void onClickBackButton(View view) {
        finish();
    }
}
