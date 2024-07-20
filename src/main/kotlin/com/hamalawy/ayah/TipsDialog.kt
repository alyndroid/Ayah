package com.hamalawy.ayah

import com.intellij.openapi.ui.DialogWrapper
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.io.InputStreamReader
import javax.swing.*
import kotlin.random.Random
import java.util.prefs.Preferences

class TipsDialog : DialogWrapper(true) {
    private val numberLabel = JLabel()
    private var ayaIndex = 0
    private var isRandom = false
    private val kotlinArray: List<String>

    init {
        init()
        title = "Ayat"

        // Load the user preference for random or sequential mode
        isRandom = Preferences.userNodeForPackage(TipsDialog::class.java).getBoolean("isRandom", false)

        // Initialize the list of Aya strings
        kotlinArray = loadAyaList()
        ayaIndex = loadIndex()
        if (kotlinArray.isNotEmpty()) {
            numberLabel.text = kotlinArray[ayaIndex]
        } else {
            numberLabel.text = "No data available"
        }
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(GridBagLayout())
        val gridBagConstraints = GridBagConstraints().apply {
            anchor = GridBagConstraints.CENTER
            fill = GridBagConstraints.NONE
            insets = Insets(10, 10, 10, 10) // Optional padding around the components
            weightx = 1.0
            weighty = 1.0
        }

        // Title label
        val titleLabel = JLabel("Today's Ayah:")
        val titleConstraints = GridBagConstraints().apply {
            gridx = 0
            gridy = 0
            anchor = GridBagConstraints.CENTER
            insets = Insets(5, 5, 5, 5)
        }
        panel.add(titleLabel, titleConstraints)

        // Number label
        val numberConstraints = GridBagConstraints().apply {
            gridx = 0
            gridy = 1
            anchor = GridBagConstraints.CENTER
            insets = Insets(5, 5, 5, 5)
        }
        panel.add(numberLabel, numberConstraints)

        return panel
    }

    private fun loadAyaList(): List<String> {
        updateOKButtonText()
        // Path to your JSON file in the resources directory
        val classLoader = javaClass.classLoader
        val jsonFileStream = classLoader.getResourceAsStream("quran.json")

        if (jsonFileStream == null) {
            println("JSON file not found!")
            return emptyList()
        }

        val jsonString = InputStreamReader(jsonFileStream).readText()

        // Set up Moshi for JSON parsing
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val type = Types.newParameterizedType(List::class.java, Surah::class.java)
        val adapter = moshi.adapter<List<Surah>>(type).lenient()

        // Parse the JSON
        val surahList: List<Surah>? = adapter.fromJson(jsonString)

        if (surahList == null) {
            println("Failed to parse JSON")
            return emptyList()
        }

        // Convert to Kotlin array
        return surahList.flatMap { it.array.map { it.ar } }
    }

    override fun doOKAction() {
        if (kotlinArray.isNotEmpty()) {
            ayaIndex = if (isRandom) {
                Random.nextInt(kotlinArray.size)
            } else {
                (ayaIndex + 1) % kotlinArray.size
            }
            numberLabel.text = kotlinArray[ayaIndex]
            saveIndex()
        }
    }

    private fun updateOKButtonText() {
        val okButton = rootPane.defaultButton
        okButton.text = "Next Aya" // Update the OK button text
    }

    private fun loadIndex(): Int {
        // Use Preferences to load the index
        val prefs = Preferences.userNodeForPackage(TipsDialog::class.java)
        return prefs.getInt("ayaIndex", 0) // Default to 0 if not found
    }

    private fun saveIndex() {
        // Use Preferences to save the index
        val prefs = Preferences.userNodeForPackage(TipsDialog::class.java)
        prefs.putInt("ayaIndex", ayaIndex)
        println("Index saved: $ayaIndex")
    }
}
