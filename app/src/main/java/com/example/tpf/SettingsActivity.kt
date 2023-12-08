package com.example.tpf

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.skydoves.colorpickerview.ColorPickerView

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        setSupportActionBar(findViewById(R.id.settings_activity_toolbar))

        // Enable the home button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Display the SettingsFragment as the main content
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, SettingsFragment())
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            // Load the preferences from the preferences.xml file
            setPreferencesFromResource(R.xml.preferences, rootKey)

            // Get the color preference and set the onPreferenceClickListener
            val colorPreference = findPreference<Preference>("text_color")

            /*  colorPreference?.onPreferenceClickListener =
                  Preference.OnPreferenceClickListener {
                      // Show the color picker dialog with the current color
                      val currentColor = colorPreference.getColor()
                      ColorPickerView colorPickerView = new ColorPickerView.Builder(context)
                          .setColorListener(colorPreference)
                          .setPreferenceName("MyColorPicker");
                      .setActionMode(ActionMode.LAST)
                      .setAlphaSlideBar(alphaSlideBar)
                      .setBrightnessSlideBar(brightnessSlideBar)
                      .setFlagView(new CustomFlag(context, R.layout.layout_flag))
                      .setPaletteDrawable(ContextCompat.getDrawable(context, R.drawable.palette))
                      .setSelectorDrawable(ContextCompat.getDrawable(context, R.drawable.selector))
                      .build();
                      true
                  }*/
        }
    }
}
