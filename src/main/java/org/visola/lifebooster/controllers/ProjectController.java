package org.visola.lifebooster.controllers;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.visola.lifebooster.dao.ProjectDao;
import org.visola.lifebooster.model.JournalEntry;
import org.visola.lifebooster.model.Project;
import org.visola.lifebooster.model.User;

@RequestMapping("${api.base.path}/projects")
@RestController
public class ProjectController {

  private final ProjectDao projectDao;

  public ProjectController(ProjectDao projectDao) {
    this.projectDao = projectDao;
  }

  @PostMapping
  public Project createProject(@RequestBody Project project,
      @AuthenticationPrincipal User user) {
    project.setUserId(user.getId());
    project.setCreated(System.currentTimeMillis());
    project.setId(projectDao.create(project));
    return project;
  }

  @GetMapping
  public List<Project> getProjects(@AuthenticationPrincipal User user) {
    return projectDao.list(user.getId());
  }

}
