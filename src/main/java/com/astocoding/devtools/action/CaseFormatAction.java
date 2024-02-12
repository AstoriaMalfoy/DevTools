package com.astocoding.devtools.action;


import com.google.common.base.CaseFormat;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.NotNull;

public class CaseFormatAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        String selectText = editor.getSelectionModel().getSelectedText();
        String result;
        if (selectText.contains("_")){
            result = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL,selectText);
        }else if(Character.isUpperCase(selectText.charAt(0))){
            result = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL,selectText);
        }else {
            result = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE,selectText);
        }


        Caret currentCaret = editor.getCaretModel().getCurrentCaret();

        int start = currentCaret.getSelectionStart();
        int end = currentCaret.getSelectionEnd();

        Document document = editor.getDocument();

        WriteCommandAction.runWriteCommandAction(editor.getProject(),()->{
            document.replaceString(start,end,result);
        });
    }
}
