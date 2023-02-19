package com.example.demo.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

//@RestController
//public class DBController {
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @CrossOrigin
//    @GetMapping(path = "/getdeisignlist")
//    @ResponseBody
//    public List<Map<String, Object>> getDesignList() {
//        String sql = "select * from design;";
//        return jdbcTemplate.queryForList(sql);
//    }
//}
