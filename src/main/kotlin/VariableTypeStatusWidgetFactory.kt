package com.example

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import javax.swing.JOptionPane

class VariableTypeStatusWidgetFactory : StatusBarWidgetFactory {

    private val logger = Logger.getInstance(VariableTypeStatusWidgetFactory::class.java)

    override fun getId(): String {
        logger.info("getId() called, returning 'VariableTypeStatusWidget'")
        return "VariableTypeStatusWidget"
    }

    override fun getDisplayName(): String {
        logger.info("getDisplayName() called")
        return "Python Variable Type Widget"
    }

    override fun isAvailable(project: Project): Boolean {
        logger.info("isAvailable() called for project: ${project.name}")
        return true
    }

    override fun createWidget(project: Project): StatusBarWidget {
        val message = "createWidget() called for project: ${project.name}"
        println(message)
        logger.info(message)
        return VariableTypeStatusWidget()
    }

    override fun disposeWidget(widget: StatusBarWidget) {
        logger.info("disposeWidget() called for widget with id: ${widget.ID()}")
    }

    override fun canBeEnabledOn(statusBar: StatusBar): Boolean {
        logger.info("canBeEnabledOn() called for statusBar: $statusBar")
        return true
    }
}
