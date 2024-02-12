package com.astocoding.devtools.listener;

import com.alibaba.fastjson.JSON;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileListener;
import io.github.sharelison.jsontojava.JsonToJava;
import io.github.sharelison.jsontojava.converter.JsonClassResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VirtualFileListenerImpl implements VirtualFileListener {

    @Override
    public void contentsChanged(@NotNull VirtualFileEvent event) {
        VirtualFileListener.super.contentsChanged(event);
        VirtualFile file = event.getFile();
        String text = "";
        try {
            text = VfsUtil.loadText(file);
            if ("".equals(text)){
                return;
            }
            JSON.parse(text);
        }catch (Exception e){
            return;
        }
        JsonToJava jsonToJava = new JsonToJava();

        List<JsonClassResult> jsonClassResults = jsonToJava.jsonToJava(text, getFileName(file), getFilePackage(file), false);
        try {
            for (JsonClassResult jsonClassResult : jsonClassResults) {
                VirtualFile parent = file.getParent();
                VirtualFile javaClassFile = parent.createChildData(null, jsonClassResult.getClassName() + ".java");
                javaClassFile.setBinaryContent(jsonClassResult.getClassDeclaration().getBytes());
            }
        }catch (Exception e){}
    }

    private String getFileName(VirtualFile file){
        return file.getName().replace("." + file.getFileType().getName().toLowerCase(),"");
    }

    private String getFilePackage(VirtualFile file){
        String path = file.getPath();
        System.out.println(path);
        return path.substring(path.indexOf("src/") + 4).replace("/",".").replace("."+getFileName(file),"");
    }

}

