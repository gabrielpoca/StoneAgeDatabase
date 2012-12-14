package database;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class DatabaseFile {

    private File file;
    private long size;
    private long last_modified;

    public DatabaseFile(File file) {
        this.file = file;
        this.size = 0;
        this.last_modified = 0;
    }

    public DatabaseFile(File file, long size) {
        this.file = file;
        this.size = size;
    }

    public DatabaseFile(File file, long size, long last_modified) {
        this.file = file;
        this.size = size;
        this.last_modified = last_modified;
    }

    public long size() {
        if(size == 0) {
            size = file.length();
        }
        return size;
    }

    public byte[] read() {
        byte[] file_content = new byte[100000];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(file_content);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file_content;
    }

    public void write(byte[] data) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(data);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        file.delete();
    }

}
