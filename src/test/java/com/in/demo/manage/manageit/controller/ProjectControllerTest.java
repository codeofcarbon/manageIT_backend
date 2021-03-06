package com.in.demo.manage.manageit.controller;

import com.in.demo.manage.manageit.error.DataNotFoundException;
import com.in.demo.manage.manageit.mapper.ProjectMapper;
import com.in.demo.manage.manageit.model.Project;
import com.in.demo.manage.manageit.model.dto.ProjectDTO;
import com.in.demo.manage.manageit.service.ProjectService;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static com.in.demo.manage.manageit.data.TestsData.generateSampleProject;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@WebMvcTest(ProjectController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProjectControllerTest {

    private static final String PROJECTS_URI = "/api/v1/projects";

    @MockBean
    private ProjectService service;
    @MockBean
    private DataSource dataSource;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateMockMvc() {
        assertNotNull(mockMvc);
    }

    @Test
    void shouldReturnListOfAllProjects() {
        var p1 = generateSampleProject();
        var p2 = generateSampleProject();
        List<Project> projectList = new ArrayList<>();
        projectList.add(p1);
        projectList.add(p2);
        Mockito.when(service.getAllProjects()).thenReturn(projectList);
        ProjectDTO pDTO1 = ProjectMapper.mapToProjectDTO(projectList.get(0));
        ProjectDTO pDTO2 = ProjectMapper.mapToProjectDTO(projectList.get(1));

        given()
                .mockMvc(mockMvc)
                .contentType(ContentType.JSON)
                .get(PROJECTS_URI)
                .then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .body(".", notNullValue())
                .body("size()", is(2))
                .body("[0].id", equalTo(pDTO1.getId().intValue()))
                .body("[1].id", equalTo(pDTO2.getId().intValue()))
                .body("[0].name", equalTo(pDTO1.getName()))
                .body("[1].name", equalTo(pDTO2.getName()))
                .body("[0].description", equalTo(pDTO1.getDescription()))
                .body("[1].description", equalTo(pDTO2.getDescription()))
                .body("[0].owner.username", equalTo(pDTO1.getOwner().getUsername()))
                .body("[1].owner.username", equalTo(pDTO2.getOwner().getUsername()))
                .body("[0].sprints", equalTo(pDTO1.getSprints()))
                .body("[1].sprints", equalTo(pDTO2.getSprints()));
    }

    @Test
    void testGetProjectById_WhenSuccess() throws DataNotFoundException {
        Project project = generateSampleProject();

        Mockito.when(service.getProjectById(1L)).thenReturn(project);
        ProjectDTO projectDTO = ProjectMapper.mapToProjectDTO(project);

        given()
                .mockMvc(mockMvc)
                .contentType(ContentType.JSON)
                .when().get(PROJECTS_URI + "/1")
                .then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(projectDTO.getId().intValue()))
                .body("name", equalTo(projectDTO.getName()))
                .body("description", equalTo(projectDTO.getDescription()))
                .body("owner.username", equalTo(projectDTO.getOwner().getUsername()))
                .body("sprints", equalTo(projectDTO.getSprints()));
    }

    @Test
    void testCreateProject_WhenSuccess() throws DataNotFoundException {
        var p1 = generateSampleProject();
        p1.setId(null);
        Project project = generateSampleProject();

        Mockito.when(service.addNewProject(p1)).thenReturn(project);
        ProjectDTO projectDTO = ProjectMapper.mapToProjectDTO(project);

        given()
                .mockMvc(mockMvc)
                .contentType(ContentType.JSON)
                .body(p1)
                .when().post(PROJECTS_URI)
                .then().assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("id", equalTo(projectDTO.getId().intValue()))
                .body("name", equalTo(projectDTO.getName()))
                .body("description", equalTo(projectDTO.getDescription()))
                .body("owner.username", equalTo(projectDTO.getOwner().getUsername()))
                .body("sprints", equalTo(projectDTO.getSprints()));
    }


    @Test
    void testRemoveProject_WhenSuccess() throws DataNotFoundException {
        Mockito.doNothing().when(service).deleteProject(1L);
        given()
                .mockMvc(mockMvc)
                .contentType(ContentType.JSON)
                .when().delete(PROJECTS_URI + "/1")
                .then().assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
        Mockito.verify(service, Mockito.times(1)).deleteProject(1L);
    }

    @Test
    void testUpdateProject_WhenSuccess() throws DataNotFoundException {
        var p1 = generateSampleProject();
        var p2 = generateSampleProject();
        Mockito.when(service.updateProject(p1)).thenReturn(p2);
        ProjectDTO projectDTO = ProjectMapper.mapToProjectDTO(p2);

        given()
                .mockMvc(mockMvc)
                .contentType(ContentType.JSON)
                .body(p1)
                .when().put(PROJECTS_URI)
                .then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("id", equalTo(projectDTO.getId().intValue()))
                .body("name", equalTo(projectDTO.getName()))
                .body("description", equalTo(projectDTO.getDescription()))
                .body("owner.username", equalTo(projectDTO.getOwner().getUsername()))
                .body("sprints", equalTo(projectDTO.getSprints()));
    }
}
