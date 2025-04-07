package com.example

import com.intellij.openapi.editor.event.CaretEvent
import com.intellij.openapi.editor.event.CaretListener
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidget.Multiframe
import com.intellij.openapi.wm.StatusBarWidget.TextPresentation
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.python.psi.PyAssignmentStatement
import com.jetbrains.python.psi.PyExpression
import com.jetbrains.python.psi.types.TypeEvalContext
import java.util.function.Consumer
import java.awt.event.MouseEvent
import javax.swing.JComponent
import java.awt.Component

class VariableTypeStatusWidget : StatusBarWidget, TextPresentation, Multiframe {
    private var statusBar: StatusBar? = null
    private var currentType: String = "N/A"
    private var caretListener: CaretListener? = null
    private var project: Project? = null

    override fun ID(): String = "VariableTypeStatusWidget"

    override fun install(statusBar: StatusBar) {
        this.statusBar = statusBar
        project = statusBar.project

        // Attach a caret listener to all editors.
        val editorFactory = EditorFactory.getInstance()
        caretListener = object : CaretListener {
            override fun caretPositionChanged(event: CaretEvent) {
                val editor = event.editor
                val proj = editor.project ?: return
                val psiFile = PsiDocumentManager.getInstance(proj).getPsiFile(editor.document) ?: return
                val offset = event.caret?.offset ?: return
                val element = psiFile.findElementAt(offset) ?: return

                // Try to locate an assignment statement in which this element is part of the target.
                val assignment = PsiTreeUtil.getParentOfType(element, PyAssignmentStatement::class.java)
                if (assignment != null) {
                    // Check if the caret is on one of the assignment targets.
                    val targetMatches = assignment.targets.any { it.text == element.text }
                    if (targetMatches) {
                        val value: PyExpression? = assignment.assignedValue
                        if (value != null) {
                            // Use the PyCharm type evaluation API to infer the type.
                            val context = TypeEvalContext.userInitiated(proj, psiFile)
                            val inferredType = context.getType(value)
                            currentType = inferredType?.getName() ?: "Unknown"
                        } else {
                            currentType = "N/A"
                        }
                    } else {
                        currentType = "N/A"
                    }
                } else {
                    currentType = "N/A"
                }
                statusBar.updateWidget(ID())
            }
        }
        editorFactory.eventMulticaster.addCaretListener(caretListener!!, statusBar)
    }

    override fun dispose() {
        // Clean up the listener if needed.
        caretListener = null
    }

    override fun getPresentation(): StatusBarWidget.WidgetPresentation = this

    override fun getText(): String = "Type: $currentType"

    override fun getTooltipText(): String = "Displays the type of the variable under the caret"

    override fun getAlignment(): Float = Component.CENTER_ALIGNMENT

    override fun getClickConsumer(): com.intellij.util.Consumer<MouseEvent>? = null

    override fun copy(): StatusBarWidget = VariableTypeStatusWidget()
}
