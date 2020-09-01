package com.llt.mybatis.pro.max.plug.file;

import com.intellij.openapi.vfs.LocalFileSystem;
import org.jetbrains.annotations.NotNull;
import java.io.*;
import java.util.Collections;

/**
 * @author llt
 * @date 2020-08-16 11:14
 */
public class MyFileOutputStream extends FileOutputStream {

    private final File file;

    public MyFileOutputStream(@NotNull File file) throws FileNotFoundException {
        super(file);
        this.file=file;
    }

    @Override
    public void close() throws IOException {
        super.close();
        LocalFileSystem.getInstance().refreshIoFiles(Collections.singletonList(file));

    }

    @Override
    public void flush() throws IOException {
        super.flush();
        LocalFileSystem.getInstance().refreshIoFiles(Collections.singletonList(file));

    }
}
