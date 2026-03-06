package ru.itis.soup2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itis.soup2.repositories.project.ProjectRepository;

@Controller
public class ProjectsController {

    private final ProjectRepository projectRepository;

    public ProjectsController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping("/projects")
    public String getProjectsPage(Model model) {
        model.addAttribute("projects", projectRepository.findAll());
        return "projects";
    }
}
