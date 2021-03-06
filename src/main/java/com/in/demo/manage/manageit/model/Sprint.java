package com.in.demo.manage.manageit.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sprints")
@AllArgsConstructor
@Data
@ToString(exclude = {"project", "tasks"})
public class Sprint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Sprint must have a name")
    @Size(min = 3, max = 55, message = "Sprint name has to be between 3 and 55 characters long")
    private String name;
    private LocalDateTime startDate;
    @Future
    private LocalDateTime endDate;
    private Integer storyPointsToSpend;
    @JsonIgnoreProperties(value = {"sprint"})
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "sprint")
    private List<Task> tasks;
    private boolean isActive;
    @ManyToMany
    private List<User> users;
    @ManyToOne
    private Project project;

    public Sprint() {
        tasks = new ArrayList<>();
        isActive = false;
        users = new ArrayList<>();
    }

    // todo ---------- sprawdzic z service czy spoko
//    public void setProject(Project project) {
//        this.project = project;
//        this.project.getSprints().add(this);
//        this.project.getOwner().getSprints().add(this);
//        this.getUsers().add(this.project.getOwner());
//    }
}
