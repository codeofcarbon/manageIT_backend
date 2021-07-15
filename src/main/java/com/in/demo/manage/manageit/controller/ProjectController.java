package com.in.demo.manage.manageit.controller;

import com.in.demo.manage.manageit.model.Project;
import com.in.demo.manage.manageit.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService service;

    @PostMapping("/projects")
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project createdProject = service.saveProject(project);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdProject);
    }
}
