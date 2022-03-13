package com.dopesatan.cemkian.ui_access;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.MITLicense;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;
import mehdi.sakout.aboutpage.Element;
import com.dopesatan.cemkian.BuildConfig;
import com.dopesatan.cemkian.R;

import java.util.Objects;

public class About extends androidx.appcompat.app.AppCompatActivity {

    ImageButton Linkedin, YouTube, GitHub, Facebook, Instagram, Donate, AboutBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abouts);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Linkedin = findViewById(R.id.linkedin);
        YouTube = findViewById(R.id.youtube);
        GitHub = findViewById(R.id.github);
        Facebook = findViewById(R.id.facebook);
        Instagram = findViewById(R.id.instagram);
        Donate = findViewById(R.id.donate);
        AboutBack = findViewById(R.id.about_back);

        Linkedin.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/utsanjan/"));
                startActivity(browserIntent);
            }
        });

        YouTube.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/DopeSatan/"));
                startActivity(browserIntent);
            }
        });

        GitHub.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/utsanjan/"));
                startActivity(browserIntent);
            }
        });

        Facebook.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/utsanjan/"));
                startActivity(browserIntent);
            }
        });

        Instagram.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/utsanjan/"));
                startActivity(browserIntent);
            }
        });

        Donate.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.buymeacoffee.com/utsanjan/"));
                startActivity(browserIntent);
            }
        });

        AboutBack.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                finish();
            }
        });
    }
}