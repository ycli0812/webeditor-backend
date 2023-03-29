package sg.edu.nus.server.service.designs;

import sg.edu.nus.server.model.Design;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Date;
import java.util.List;

@Service
public class DesignService {
    public List<Design> getDesigns() {
        // only return three items for test
        return List.of(
            new Design("design1.json", new Date()),
            new Design("测试项目.json", new Date()),
            new Design("design3.json", new Date()) // not exist
        );
    }

    public String getDesignByName(String filename) throws Exception {
        // open file, throw exception if file not exist
        File fin = new File("src/main/resources/store/" + filename);
        if(!fin.exists()) throw new Exception("File " + filename + " not found.");
        // read file into a String
        InputStreamReader reader = new InputStreamReader(new FileInputStream(fin));
        char[] buf = new char[1000000];
        int len = reader.read(buf);
        // construct JSON object
        return new String(buf, 0, len);
    }

    public void saveDesign(String filename, String content) throws Exception {
        // open file, throw exception if file not exist
        File targetFile = new File("src/main/resources/" + filename);
        if(!targetFile.exists()) throw new Exception("File " + filename + " not found.");
        // write file
        FileWriter writer = new FileWriter(targetFile);
        writer.write(content);
        writer.close();
    }

    public void newDesign(String filename) throws Exception {
        File targetFile = new File("src/main/resources/" + filename);
        FileWriter writer = new FileWriter(targetFile);
        writer.write("{\"elementSet\": {}}");
        writer.close();
    }
}
