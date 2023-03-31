// 该类是AnAction的子类，用于处理用户在IDEA中点击插件按钮时的事件
package com.nicegpt.chatannotator;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

public class AnnotateCodeAction extends AnAction {
    // actionPerformed方法重载了AnAction中的方法，在用户点击插件按钮时会自动执行该方法
    @Override
    public void actionPerformed(AnActionEvent e) {
        // 获取当前的工程和编辑器对象
        Project project = e.getProject();
        Editor editor = e.getData(PlatformDataKeys.EDITOR);

        // 如果当前没有打开任何文件，弹出错误提示框并返回
        if (editor == null) {
            Messages.showMessageDialog(project, "Please open a file before running the plugin.", "Error", Messages.getErrorIcon());
            return;
        }

        // 获取文件类型和代码内容
        FileType fileType = e.getData(PlatformDataKeys.VIRTUAL_FILE).getFileType(); 
        String code = editor.getDocument().getText();

        // 调用ChatGPT API接口添加代码注释
        // 这里是一个简单的示例，在文件开头添加了一行注释
        code = "为这段代码添加中文注释,不对代码进行改变,只允许添加注释,并且注释格式以chatGPT开头:" + code;
        try {
            String annotatedCode = ChatGPTApi.requestChatGpt(code);
            // 将注释后的代码更新到编辑器中
            ApplicationManager.getApplication().runWriteAction(() -> {
                Document document = editor.getDocument();
                document.setText(annotatedCode);
            });
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }
}