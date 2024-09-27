package com.route.todoapplication.ui.home.settings

import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.route.todoapplication.R
import com.route.todoapplication.applyModeChane
import com.route.todoapplication.base.BaseFragment
import com.route.todoapplication.databinding.FragmentSettingsBinding
import com.route.todoapplication.getCurrentDeviceLanguageCode
import com.route.todoapplication.ui.home.Constants

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_settings

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().setTitle(getString(R.string.settings))
        setInitialLanguageState()
        setInitialModeState()
        initLanguageTVAdapter()
        initModeTVAdapter()
        onLanguageTVClick()
        onModeTVClick()


    }

    override fun onStart() {
        super.onStart()
        initLanguageTVAdapter()
        initModeTVAdapter()
    }

    private fun onModeTVClick() {
        binding.autoCompleteTVModes.setOnItemClickListener { adapterView, view, position, id ->
            val selectedMode = adapterView.getItemAtPosition(position).toString()
            binding.autoCompleteTVModes.setText(selectedMode)
            val isDark = (selectedMode == getString(R.string.dark))
            applyModeChane(isDark)

        }
    }


    private fun onLanguageTVClick() {
        binding.autoCompleteTVLanguages.setOnItemClickListener { adapterView, view, position, id ->
            val selectedLanguage = adapterView.getItemAtPosition(position).toString()
            binding.autoCompleteTVLanguages.setText(selectedLanguage)
            val languageCode =
                if (selectedLanguage == getString(R.string.english)) Constants.ENGLISH_CODE else Constants.ARABIC_CODE
            applyLanguageChange(languageCode)
        }
    }

    private fun applyLanguageChange(languageCode: String) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
        activity?.let {
            it.finish()
            it.startActivity(it.intent)
        }

    }

    private fun initModeTVAdapter() {
        val modes = resources.getStringArray(R.array.modes).toList()
        val modeAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, modes)
        binding.autoCompleteTVModes.setAdapter(modeAdapter)

    }

    private fun initLanguageTVAdapter() {
        val languages = resources.getStringArray(R.array.languages).toList()
        val languageAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, languages)
        binding.autoCompleteTVLanguages.setAdapter(languageAdapter)
    }

    private fun setInitialModeState() {
        val currentMode = resources.configuration.uiMode and UI_MODE_NIGHT_MASK
        if (currentMode == UI_MODE_NIGHT_NO) {
            binding.autoCompleteTVModes.setText(getString(R.string.light))
            changeModeIcon(Constants.LIGHT)

        } else {
            binding.autoCompleteTVModes.setText(getString(R.string.dark))
            changeModeIcon(Constants.DARK)

        }
        binding.modeTil.refreshStartIconDrawableState()

    }

    private fun changeModeIcon(mode: String) {
        if (mode == Constants.LIGHT)
            binding.modeTil.setStartIconDrawable(R.drawable.ic_light_mode)
        else
            binding.modeTil.setStartIconDrawable(R.drawable.ic_dark)

    }


    private fun setInitialLanguageState() {
        val currentLocaleCode =
            AppCompatDelegate.getApplicationLocales()[0]?.language ?: getCurrentDeviceLanguageCode(
                requireContext()
            )
        val language =
            if (currentLocaleCode == Constants.ENGLISH_CODE) getString(R.string.english) else getString(
                R.string.arabic
            )
        binding.autoCompleteTVLanguages.setText(language)


    }


}
