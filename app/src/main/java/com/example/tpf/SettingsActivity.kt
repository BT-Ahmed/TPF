package com.example.tpf

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

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

            //val colorCodesRegexPattern = "^# ( [a-fA-F0-9] {6}| [a-fA-F0-9] {3})$".toRegex()

            // Get an instance of your preference widget
            val productLabelHexTextColor: EditTextPreference? =
                findPreference("product_label_hex_text_color") as EditTextPreference?

            // Set a listener to validate the input
            if (productLabelHexTextColor != null) {
                productLabelHexTextColor.onPreferenceChangeListener =
                    Preference.OnPreferenceChangeListener { _, newValue ->
                        // Get the new value as a string
                        val color = newValue as String

                        // Check if the color starts with #
                        if (!color.startsWith("#")) {
                            // Show a toast message to inform the user
                            Toast.makeText(
                                this.context,
                                "Invalid color format. Please start with #",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Reject the input
                            return@OnPreferenceChangeListener false
                        }

                        // Check if the color has 6 characters after #
                        if (color.length != 7) {
                            // Show a toast message to inform the user
                            Toast.makeText(
                                this.context,
                                "Invalid color format. Please enter 6 characters after #",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Reject the input
                            return@OnPreferenceChangeListener false
                        }

                        /*// Redundant
                        if (!color.matches(colorCodesRegexPattern)) {
                            Toast.makeText(
                                this.context,
                                "Invalid color code. Please enter another one",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@OnPreferenceChangeListener false
                        }*/

                        // Check if the color contains only hexadecimal digits
                        try {
                            // Parse the color as an integer
                            val colorValue = color.substring(1).toInt(16)
                            // If no exception is thrown, the color is valid
                            // Accept the input
                            return@OnPreferenceChangeListener true
                        } catch (e: NumberFormatException) {
                            // Show a toast message to inform the user
                            Toast.makeText(
                                this.context,
                                "Invalid color format. Please enter only hexadecimal digits",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Reject the input
                            return@OnPreferenceChangeListener false
                        }
                    }
            }

        }
    }
}
