package com.example.oops.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.oops.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LanguageOption extends AppCompatActivity {
    @BindView(R.id.txtHeading)
    AppCompatTextView txtHeading;
    @BindView(R.id.radioButtonHindi)
    AppCompatRadioButton radioButtonHindi;
    @BindView(R.id.radioButtonMarathi)
    RadioButton radioButtonMarathi;
    @BindView(R.id.radioButtonEnglish)
    RadioButton radioButtonEnglish;
    @BindView(R.id.imgBackPressed)
    AppCompatImageView imgBackPressed;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.languageoption);
        ButterKnife.bind(this);
        initView();
        imgBackPressed.setVisibility(View.VISIBLE);
    }

    private void initView() {
        txtHeading.setText(getString(R.string.language));
    }
    @OnClick(R.id.imgBackPressed)
    public  void setImgBackPressed(){
        onBackPressed();
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((AppCompatRadioButton) view).isChecked();
        String str = "";
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radioButtonEnglish:
                if (checked)

                    str = "English";
                radioButtonHindi.setChecked(false);
                radioButtonMarathi.setChecked(false);
                break;
            case R.id.radioButtonHindi:
                if (checked)
                    str = "Hindi";
                radioButtonEnglish.setChecked(false);
                radioButtonMarathi.setChecked(false);
                break;
            case R.id.radioButtonMarathi:
                if (checked)
                    str = "Marathi";
                radioButtonHindi.setChecked(false);
                radioButtonEnglish.setChecked(false);
                break;

        }
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }
}
