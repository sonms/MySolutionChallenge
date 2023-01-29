package com.example.mysolutionchallenge.Navigation

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.mysolutionchallenge.R

class AccountFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)
    }
}