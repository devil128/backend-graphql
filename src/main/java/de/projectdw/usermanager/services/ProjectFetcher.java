package de.projectdw.usermanager.services;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsEntityFetcher;
import de.projectdw.sysedataclasses.data.Image;
import de.projectdw.sysedataclasses.data.Project;
import de.projectdw.sysedataclasses.repos.ImageFileRepository;
import de.projectdw.usermanager.repos.ImageRepository;
import de.projectdw.usermanager.repos.LabelRepository;
import de.projectdw.usermanager.repos.ProjectRepository;
import de.projectdw.usermanager.repos.UserRepository;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.transaction.Transactional;
import java.util.Map;

@DgsComponent
public class ProjectFetcher {
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
    @Autowired
    UtilService utilService;

    @DgsEntityFetcher(name = "Project")
    public Project project(Map<String, Object> values) {
        return projectRepository.getProjectById(Long.parseLong((String) values.get("id"))).orElse(null);
    }

    @PreAuthorize("authentication.principal.uid != null")
    @DgsData(parentType = "Project", field = "imagesTotal")
    @Transactional
    public int imagesPerProject(DataFetchingEnvironment dfe) {
        Project project = dfe.getSource();
        var images = imageRepository.getImagesByProject(project);
        return images.size();
    }

    @PreAuthorize("authentication.principal.uid != null")
    @DgsData(parentType = "Project", field = "imagesClassified")
    public int classifiedPerProject(DataFetchingEnvironment dfe) {
        Project project = dfe.getSource();
        var images = imageRepository.getImagesByProject(project);
        return (int) images.stream().filter(Image::isFinished).count();
    }
}
