package com.example.masterdueltimer

import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat


class DuelSettings: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, SettingsFragment())
            .commit()
    }
}
class SettingsFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val maxTimeField = findPreference<EditTextPreference>("maxTime")
        maxTimeField!!.setOnBindEditTextListener { editText ->
            editText.inputType =
                InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
            editText.transformationMethod = NumericKeyBoardTransformationMethod()
        }

        val turnEndGain = findPreference<EditTextPreference>("turnEndGain")
        turnEndGain!!.setOnBindEditTextListener { editText ->
            editText.inputType =
                InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
            editText.transformationMethod = NumericKeyBoardTransformationMethod()
        }

        val turnStartGain = findPreference<EditTextPreference>("turnStartGain")
        turnStartGain!!.setOnBindEditTextListener { editText ->
            editText.inputType =
                InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
            editText.transformationMethod = NumericKeyBoardTransformationMethod()
        }
    }
}

private class NumericKeyBoardTransformationMethod :
    PasswordTransformationMethod() {
    override fun getTransformation(source: CharSequence, view: View?): CharSequence {
        return source
    }
}