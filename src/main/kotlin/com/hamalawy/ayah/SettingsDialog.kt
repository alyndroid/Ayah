package com.hamalawy.ayah

import com.intellij.openapi.ui.DialogWrapper
import javax.swing.*
import java.util.prefs.Preferences

class SettingsDialog : DialogWrapper(true) {
    private val sequentialRadioButton = JRadioButton("Sequential")
    private val randomRadioButton = JRadioButton("Random")
    private val buttonGroup = ButtonGroup()

    init {
        init()
        title = "Settings"
    }

    override fun createCenterPanel(): JComponent {
        // Create and set up the panel
        val panel = JPanel()
        buttonGroup.add(sequentialRadioButton)
        buttonGroup.add(randomRadioButton)

        // Set default selection
        val isRandom = Preferences.userNodeForPackage(TipsDialog::class.java).getBoolean("isRandom", false)
        randomRadioButton.isSelected = isRandom
        sequentialRadioButton.isSelected = !isRandom

        // Add components to the panel
        panel.add(sequentialRadioButton)
        panel.add(randomRadioButton)
        return panel
    }

    fun getSelectedMode(): Boolean {
        return randomRadioButton.isSelected
    }

    override fun doOKAction() {
        val isRandom = getSelectedMode()
        Preferences.userNodeForPackage(TipsDialog::class.java).putBoolean("isRandom", isRandom)
        super.doOKAction()
    }
}
