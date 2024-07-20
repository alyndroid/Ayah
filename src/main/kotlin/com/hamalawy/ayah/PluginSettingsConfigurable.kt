package com.hamalawy.ayah

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JRadioButton
import javax.swing.ButtonGroup
import java.util.prefs.Preferences

class PluginSettingsConfigurable : Configurable {

    private val sequentialRadioButton = JRadioButton("Sequential")
    private val randomRadioButton = JRadioButton("Random")
    private val buttonGroup = ButtonGroup()

    init {
        buttonGroup.add(sequentialRadioButton)
        buttonGroup.add(randomRadioButton)

        // Load the saved settings
        val isRandom = Preferences.userNodeForPackage(PluginSettingsConfigurable::class.java).getBoolean("isRandom", false)
        randomRadioButton.isSelected = isRandom
        sequentialRadioButton.isSelected = !isRandom
    }

    override fun createComponent(): JComponent? {
        val panel = JPanel()
        panel.add(sequentialRadioButton)
        panel.add(randomRadioButton)
        return panel
    }

    override fun isModified(): Boolean {
        val currentIsRandom = randomRadioButton.isSelected
        val savedIsRandom = Preferences.userNodeForPackage(PluginSettingsConfigurable::class.java).getBoolean("isRandom", false)
        return currentIsRandom != savedIsRandom
    }

    override fun apply() {
        val isRandom = randomRadioButton.isSelected
        Preferences.userNodeForPackage(PluginSettingsConfigurable::class.java).putBoolean("isRandom", isRandom)
    }

    override fun reset() {
        val isRandom = Preferences.userNodeForPackage(PluginSettingsConfigurable::class.java).getBoolean("isRandom", false)
        randomRadioButton.isSelected = isRandom
        sequentialRadioButton.isSelected = !isRandom
    }

    override fun disposeUIResources() {
        // Dispose of any resources if necessary
    }

    override fun getDisplayName(): String {
        return "Ayat Plugin Settings"
    }
}
