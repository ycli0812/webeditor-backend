package sg.edu.nus.server.model;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

/**
 * Modeling a circuit file instead a circuit object in verification.
 *
 * @author Lyc
 * @version 2023.02.19
 */
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
