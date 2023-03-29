package sg.edu.nus.server.controller;

import sg.edu.nus.server.service.designs.DesignService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
public class DesignController {
    private final DesignService designService;
    int cnt = 0;

    @Autowired
    public DesignController(DesignService designService) {
        this.designService = designService;
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // query list of files
    @CrossOrigin
    @GetMapping(path="/designlist")
    public List<Map<String, Object>> getDesigns() {
        String sql = "select * from design";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return maps;
    }

    // query specific file by filename
    @CrossOrigin
    @PostMapping(path="/design")
    public String getCertainDesign(@RequestParam("filename") String filename) {
        String jsonStr;
        try {
            jsonStr = designService.getDesignByName(filename);
        } catch(Exception e) {
            // return 404 if file doesn't exist
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found.");
        }
        return jsonStr;
    }

    // create new file
    @CrossOrigin
    @GetMapping(path="/create")
    public String newFile() {
//        String sql = "insert into design(filename,userId) value('invalid-test.json',1)";
        try {
            ObjectMapper mapper = new ObjectMapper();
            String filename = "Unnamed file" + (cnt++) + ".json";
            designService.newDesign(filename);
            String sql = String.format("insert into design(id, filename, userId) value('%d','%s', 1)", cnt, filename);
            jdbcTemplate.update(sql);
//            System.out.println(body.get("file").toString() + ".json");
        } catch (Exception e) {
            // TODO: make sure that file is removed and record in DB is dropped
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Create file failed.");
        }
        return "good";
    }

    // delete file by id
    @CrossOrigin
    @GetMapping (path = "/delete/{id}")
    public String deleteFile(@PathVariable("id") int id) {
        try {
            String sql = "delete from sys.design where id = ?";
            jdbcTemplate.update(sql, id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Delete file failed.");
        }
        return "good";
    }

    // save file
    @CrossOrigin
    @PostMapping(path="/save")
    public String saveDesign(@RequestBody Map body) {
//        System.out.println(body);
        try {
            ObjectMapper mapper = new ObjectMapper();
            designService.saveDesign(body.get("file").toString(), mapper.writeValueAsString(body.get("content")));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found.");
        }
        return "good";
    }

}
