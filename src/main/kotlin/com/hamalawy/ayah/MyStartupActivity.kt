package com.hamalawy.ayah

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.project.Project

class MyStartupActivity : StartupActivity {
    override fun runActivity(project: Project) {
        ApplicationManager.getApplication().invokeLater {
            showTipsDialog()
        }
    }

    private fun showTipsDialog() {
        val dialog = TipsDialog()
        dialog.showAndGet()
    }
}
