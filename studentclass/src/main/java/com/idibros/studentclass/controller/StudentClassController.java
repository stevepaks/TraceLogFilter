package com.idibros.studentclass.controller;

import com.idibros.studentclass.bo.StudentClassBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("studentclass")
public class StudentClassController {

    @Autowired
    private StudentClassBo studentClassBo;

    @GetMapping("{studentClassId}")
    public ResponseEntity<String> getStudentClass(@PathVariable("studentClassId") Long studentClassId) {

        return ResponseEntity.of(Optional.ofNullable(studentClassBo.getStudentCalss()));
    }

}
