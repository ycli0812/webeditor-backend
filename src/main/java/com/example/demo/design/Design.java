package com.example.demo.design;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class Design {
    public String getFilename() {
        return filename;
    }

    public String getEditTime() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        return fmt.format(editTime);
    }

    private String filename;
    private Date editTime;

    public Design(String filename, Date editTime) {
        this.filename = filename;
        this.editTime = editTime;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }
}
