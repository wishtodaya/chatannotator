package com.nicegpt.chatannotator;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

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
        code = "为这段代码添加中文注释,不对代码进行改变,只允许添加注释,并且注释格式以chatGPT开头:" + code;
        // Call the ChatGPT API with progress bar
        annotateWithProgress(project, editor, code);
    }




    private void annotateWithProgress(Project project, Editor editor, String code) {
        ProgressManager.getInstance().run(new Task.Backgroundable(project, "Annotating Code", false) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.setIndeterminate(true);
                indicator.setText("Annotating code...");

                try {
                    String annotatedCode = ChatGPTApi.requestChatGpt(code);
                    ApplicationManager.getApplication().invokeLater(() -> {
                        Document document = editor.getDocument();
                        document.setText(annotatedCode);
                    });
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}