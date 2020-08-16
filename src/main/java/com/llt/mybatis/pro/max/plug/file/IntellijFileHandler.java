package com.llt.mybatis.pro.max.plug.file;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;
import com.llt.mybatishelper.core.file.FileHandler;
import com.llt.mybatishelper.core.utils.FileUtils;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author llt
 * @date 2020-08-16 10:46
 */
public class IntellijFileHandler implements FileHandler {

    private final Project project;

    public IntellijFileHandler(Project project) {
        this.project = project;
    }

    private String getFileUrl(String path){
        return "file://"+path;
    }

    @Override
    public List<String> getAllFilePath(String folder) {
        VirtualFile fileByUrl = VirtualFileManager.getInstance().findFileByUrl(getFileUrl(folder));
        if (fileByUrl == null) {
            //noinspection unchecked
            return Collections.EMPTY_LIST;
        }
        VirtualFile[] children = fileByUrl.getChildren();
        return Arrays.stream(children).map(VirtualFile::getPath).collect(Collectors.toList());
    }

    @Override
    public String readJavaFileToString(String path, String charset) {
        VirtualFile fileByUrl = VirtualFileManager.getInstance().findFileByUrl(getFileUrl(path));
        if (fileByUrl == null) {
            return null;
        }
        PsiFile psiFile = PsiUtilBase.getPsiFile(project, fileByUrl);
        return psiFile.getText();
    }

    @Override
    public void mkdir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        LocalFileSystem.getInstance().refreshIoFiles(Collections.singletonList(file));
    }

    @Override
    public void writerString2File(String path, String str, String charset) throws IOException {
        FileUtils.writerString2File(path,str,charset);
        LocalFileSystem.getInstance().refreshIoFiles(Collections.singletonList(new File(path)));
    }

    @Override
    public OutputStream getOutputStream(String path) throws FileNotFoundException {
        return new MyFileOutputStream(new File(path));
    }

    @Override
    public boolean exists(String path) {
        File file = new File(path);
        boolean exists = file.exists();
        if (exists) {
            VirtualFileManager.getInstance().refreshAndFindFileByUrl(getFileUrl(path));
        }
        return exists;
    }
}
