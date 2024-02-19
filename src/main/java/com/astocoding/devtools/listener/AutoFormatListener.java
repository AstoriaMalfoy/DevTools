package com.astocoding.devtools.listener;

import com.astocoding.devtools.configurable.DevToolsGlobalSetting;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 自动格式化
 * 每当按下回车之后，在上一行的 逗号（,），运算符符号 （+，-，*，/）前后自动添加空格
 */
public class AutoFormatListener extends EditorActionHandler {
    private EditorActionHandler oldEditorActionHandler;
    public AutoFormatListener(EditorActionHandler e){
        this.oldEditorActionHandler = e;
    }
    @Override
    protected void doExecute(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {
        CaretModel caretModel = editor.getCaretModel();
        FileType fileType = editor.getVirtualFile( ).getFileType( );
        System.out.println( fileType.getName() );
        if ( fileType.getName().contains( "JAVA" ) && DevToolsGlobalSetting.getInstance().getAutoFormatEnable() ){
            LogicalPosition logicalPosition = caretModel.getLogicalPosition();
            // 获取当前行的数据
            Document document = editor.getDocument();
            int lineStartOffset = document.getLineStartOffset(logicalPosition.line);
            int lineEndOffset = document.getLineEndOffset(logicalPosition.line);
            WriteCommandAction.runWriteCommandAction(ProjectManager.getInstance().getDefaultProject(),()->{
                String currentLine = document.getText(new TextRange(lineStartOffset,lineEndOffset));
                // 对该行进行格式化
                String formattedLine = formatLine(currentLine);
                document.replaceString(lineStartOffset,lineEndOffset,formattedLine);
            });
        }
        oldEditorActionHandler.execute(editor,caret,dataContext);
    }

    private static String formatLine(String line) {
        StringBuilder sb = new StringBuilder(line);
        int length = sb.length();
        List<Integer> insertOffsetList = new ArrayList<>();
        for (int i = 0; i < length - 1; i++) { // 遍历字符串除了最后一个字符的所有字符
            char current = sb.charAt(i);
            char next = sb.charAt(i+1);
            // 插入空格的条件

            if (
                    (( current != next && current != '\'' && current != '\"' && next != '\'' && next != '\"' && (
                            (current != ' ' && current != '!' && next == '=' ) || (current == '=' && next != ' ')
                                    || ( current != ' ' && next == '+') || ( current == '+' && next != ' ')
                                    || ( current != ' ' && next == '-') || ( current == '-' && next != ' ')
                                    || ( current != ' ' && next == '*') || ( current == '*' && next != ' ')
                                    || ( current != ' ' && next == '/') || ( current == '/' && next != ' ')
                                    || ( current != ' ' && next == '&' ) || ( current == '&' && next != ' ' )
                                    || ( current != ' ' && next == '|' ) || ( current == '|' && next != ' ' )
                                    || ( current != ' ' && next == '!') || ( current == '!' && next != ' ' && next != '=')
                                    || ( current != ' ' && next == '>' ) || ( current == '>' && next != ' ' )
                                    || ( current != ' ' && next == '<' ) || ( current == '<' && next != ' ' )
                                    || ( current != ' ' && next == ',' ) || ( current == ',' && next != ' ' )
                        )
                    ) || (
                            ( current == '(' && next != ' ' && next != ')' && next != '\'' && next != '\"')
                                    || ( current == '{' && next != ' ' && next != '}' && next != '\'' && next != '\"')
                                    || ( current != ' ' && next == ')' && current != '(' && current != '\'' && current != '\"')
                                    || ( current != ' ' && next == '}' && current != '{' && current != '\'' && current != '\"')
                    ) )

            ){
                insertOffsetList.add(i+1);
            }
        }
        int correctOffset = 0;
        for (Integer offset : insertOffsetList) {
            sb.insert(offset + correctOffset , ' ');
            correctOffset++;
        }
        return sb.toString();
    }
}