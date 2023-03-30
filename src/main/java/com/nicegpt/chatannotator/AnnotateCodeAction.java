package com.nicegpt.chatannotator;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

public class AnnotateCodeAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(PlatformDataKeys.EDITOR);

        if (editor == null) {
            Messages.showMessageDialog(project, "Please open a file before running the plugin.", "Error", Messages.getErrorIcon());
            return;
        }

        FileType fileType = e.getData(PlatformDataKeys.VIRTUAL_FILE).getFileType();
        String code = editor.getDocument().getText();

        // Call the ChatGPT API here, and add the annotations to the code.
        // Here's a simple example of how to add a comment at the beginning of the file:
        String annotatedCode = "// ChatGPT: This is an annotated comment\n" + code;

        Document document = editor.getDocument();
        document.setText(annotatedCode);
    }
}
