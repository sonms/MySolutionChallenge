package com.example.mysolutionchallenge.Navigation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.example.mysolutionchallenge.MainActivity
import com.example.mysolutionchallenge.R
import com.example.mysolutionchallenge.databinding.FragmentAccountBinding
import com.example.mytodolist.SharedPref
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountFragment : PreferenceFragmentCompat() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var accountBinding: FragmentAccountBinding
    //상태유지
    var sharedPref : SharedPref? = null
    var darkthemePreference : SwitchPreferenceCompat? = null
    var smallthemePreference : SwitchPreferenceCompat? = null
    var middlethemePreference : SwitchPreferenceCompat? = null
    var largethemePreference : SwitchPreferenceCompat? = null
    var logoutPreference : Preference? = null
    var isChecked = false
    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        accountBinding = FragmentAccountBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }*/

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        sharedPref = this.context?.let { SharedPref(it) }
        if (sharedPref!!.loadNightModeState()) {
            context?.setTheme(R.style.darktheme)
        } else {
            context?.setTheme(R.style.AppTheme)
        }

        setPreferencesFromResource(R.xml.preference, rootKey)

        darkthemePreference = findPreference("themeKey0")
        smallthemePreference = findPreference("themeKey1")
        middlethemePreference = findPreference("themeKey2")
        largethemePreference = findPreference("themeKey3")
        /*if (sharedPref!!.loadNightModeState()) {
            themePreference!!.isChecked = true
        }*/
        darkthemePreference!!.setOnPreferenceChangeListener { preference, newValue ->
            //var isChecked = false
            if (newValue as Boolean) {
                isChecked = newValue
            }
            if (isChecked) {
                preferenceManager.sharedPreferences!!.edit().putBoolean("themeKey0", true).apply()
                sharedPref!!.setNightModeState(true)
                restartApp()
            } else {
                preferenceManager.sharedPreferences!!.edit().putBoolean("themeKey0", false).apply()
                sharedPref!!.setNightModeState(false)
                restartApp()
            }
            return@setOnPreferenceChangeListener true
        }

        smallthemePreference!!.setOnPreferenceChangeListener { preference, newValue ->
            //var isChecked = false
            if (newValue as Boolean) {
                isChecked = newValue
            }
            if (isChecked) {
                preferenceManager.sharedPreferences!!.edit().putBoolean("themeKey1", true).apply()
                sharedPref!!.setSmallModeState(true)
                restartApp()
            } else {
                preferenceManager.sharedPreferences!!.edit().putBoolean("themeKey1", false).apply()
                sharedPref!!.setSmallModeState(false)
                restartApp()
            }
            return@setOnPreferenceChangeListener true
        }

        middlethemePreference!!.setOnPreferenceChangeListener { preference, newValue ->
            //var isChecked = false
            if (newValue as Boolean) {
                isChecked = newValue
            }
            if (isChecked) {
                preferenceManager.sharedPreferences!!.edit().putBoolean("themeKey2", true).apply()
                sharedPref!!.setMiddleModeState(true)
                restartApp()
            } else {
                preferenceManager.sharedPreferences!!.edit().putBoolean("themeKey2", false).apply()
                sharedPref!!.setMiddleModeState(false)
                restartApp()
            }
            return@setOnPreferenceChangeListener true
        }

        largethemePreference!!.setOnPreferenceChangeListener { preference, newValue ->
            //var isChecked = false
            if (newValue as Boolean) {
                isChecked = newValue
            }
            if (isChecked) {
                preferenceManager.sharedPreferences!!.edit().putBoolean("themeKey3", true).apply()
                sharedPref!!.setLargeModeState(true)
                restartApp()
            } else {
                preferenceManager.sharedPreferences!!.edit().putBoolean("themeKey3", false).apply()
                sharedPref!!.setLargeModeState(false)
                restartApp()
            }
            return@setOnPreferenceChangeListener true
        }

        //themePreference!!.setOnPreferenceChangeListener(prefListener)
    }

    //테마 변경 시 적용을 위한 재시작
    fun restartApp() {
        val intent = Intent(context?.applicationContext, MainActivity::class.java)
        activity?.startActivity(intent)
        activity?.finish()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AccountTestFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}