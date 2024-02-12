package com.astocoding.devtools.listener;

import com.alibaba.fastjson.JSON;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.mac.MacOSApplicationProvider;
import io.github.sharelison.jsontojava.JsonToJava;
import io.github.sharelison.jsontojava.converter.JsonClassResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class EditorPasteListener extends EditorActionHandler {
    @Override
    protected void doExecute(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {
        super.doExecute(editor, caret, dataContext);
        String contents = CopyPasteManager.getInstance().getContents(DataFlavor.stringFlavor);
        try {
            if (Objects.equals("",contents)){
                return;
            }
            // parse json string
            JSON.parse(contents);
        }catch (Exception e){
            // insert data
            WriteCommandAction.runWriteCommandAction(ProjectManager.getInstance().getDefaultProject(),()->{
                editor.getDocument().insertString(editor.getCaretModel().getOffset(),contents);
            });
        }

        VirtualFile virtualFile = dataContext.getData( CommonDataKeys.VIRTUAL_FILE );
        JsonToJava jsonToJava = new JsonToJava();

        List<JsonClassResult> jsonClassResults = jsonToJava.jsonToJava(contents, getFileName(virtualFile), getFilePackage(virtualFile), false);
        for (JsonClassResult jsonClassResult : jsonClassResults) {
            VirtualFile parent = virtualFile.getParent();
            WriteCommandAction.runWriteCommandAction(ProjectManager.getInstance().getDefaultProject(),()->{
                VirtualFile currentFile = null;
                try {
                    currentFile = parent.createChildData(null, jsonClassResult.getClassName() + ".java");
                    currentFile.setBinaryContent(jsonClassResult.getClassDeclaration().getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

    }

    private String getFilePackage(VirtualFile virtualFile){
        VirtualFile parent = virtualFile.getParent();
        String path = parent.getPath();
        System.out.println("the file path is " + path);
        String packageString = path.substring(path.indexOf("/src")+4).replaceAll("/", ".");
        System.out.println("the package string is " + packageString);
        return packageString ;
    }

    private String getFileName(VirtualFile virtualFile){
        System.out.println("the file name is " + virtualFile.getName());
        System.out.println("the file type is " + virtualFile.getFileType().getName());
        return virtualFile.getName().replace("."+virtualFile.getFileType().getName().toLowerCase(),"");
    }
}
