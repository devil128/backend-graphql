package de.projectdw.usermanager.services;

import de.projectdw.sysedataclasses.data.Project;
import de.projectdw.sysedataclasses.data.User;
import de.projectdw.sysedataclasses.repos.ImageFileRepository;
import de.projectdw.sysedataclasses.utils.UtilFunction;
import de.projectdw.usermanager.repos.ImageRepository;
import de.projectdw.usermanager.repos.LabelRepository;
import de.projectdw.usermanager.repos.ProjectRepository;
import de.projectdw.usermanager.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
public class UtilService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    ImageFileRepository imageFileRepository;

    @NotNull
    public Project getProject(String projectName) {
        User user = getUserInformation();
        List<Project> userProjects = new ArrayList<>(user.getProjects());
        userProjects.addAll(new ArrayList<>(user.getMemberProjects()));
        Project selectedProject = userProjects.stream().filter(project -> project.getProjectName().equals(projectName)).findFirst().orElse(null);
        if (selectedProject == null) {
            Project project = new Project(projectName);
            project.setOwner(user);
            project = projectRepository.save(project);
            user.getProjects().add(project);
            userRepository.save(user);
            return project;
        }
        return selectedProject;
    }

    public User getUserInformation() {
        User user = UtilFunction.getAuthUser();
        User dbUser = userRepository.findUserByUid(user.getUid());

        if (dbUser == null) {
            user.initFromFirebase();
            dbUser = userRepository.save(user);
        }
        return dbUser;
    }

    public boolean hasAccessToProject(User user, Project project) {
        List<Project> projects = user.getProjects();
        projects.addAll(user.getMemberProjects());

        if (projects.contains(project))
            return true;
        throw new AccessDeniedException("User has no access to project");
    }
    public boolean hasAccessToProject(Project project) {
        User user = getUserInformation();
        List<Project> projects = user.getProjects();
        projects.addAll(user.getMemberProjects());

        if (projects.contains(project))
            return true;
        throw new AccessDeniedException("User has no access to project");
    }
}
