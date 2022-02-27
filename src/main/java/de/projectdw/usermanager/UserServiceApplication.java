package de.projectdw.usermanager;

import com.google.api.gax.rpc.UnauthenticatedException;
import com.netflix.graphql.dgs.*;
import com.netflix.graphql.dgs.DgsData.*;

import de.projectdw.sysedataclasses.data.*;
import de.projectdw.sysedataclasses.repos.ImageFileRepository;
import de.projectdw.sysedataclasses.utils.UtilFunction;
import de.projectdw.usermanager.repos.*;
import de.projectdw.usermanager.services.UtilService;
import de.projectdw.usermanager.util.LabelInput;
import graphql.schema.DataFetchingEnvironment;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

import java.awt.image.BufferedImage;
import java.util.stream.Collectors;

@SpringBootApplication(scanBasePackages = {"de.projectdw.sysedataclasses", "de.projectdw.usermanager"})
@DgsComponent
public class UserServiceApplication {
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

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @PreAuthorize("authentication.principal.uid != null")
    @DgsData(parentType = "Mutation", field = "uploadImage")
    @Transactional
    public Image uploadImage(String projectName, @InputArgument("inputImage") MultipartFile image, DataFetchingEnvironment dfe) throws IOException {
        Project project = utilService.getProject(projectName);

        ByteArrayInputStream bis = new ByteArrayInputStream(image.getBytes());
        BufferedImage bImage = ImageIO.read(bis);
        Image data = new Image(bImage.getWidth(), bImage.getHeight(), new Date(), image.getBytes(), project);
        imageFileRepository.save(data.getFile());
        data.setContentType(image.getContentType());
        data = imageRepository.save(data);
        return data;
    }

    @PreAuthorize("authentication.principal.uid != null")
    @Transactional
    @DgsData(parentType = "Image", field = "data")
    public String imageData(DataFetchingEnvironment dfe) {
        Image img = dfe.getSource();

        return img.getData();
    }

    @DgsMutation(field = "uploadLabel")
    @PreAuthorize("authentication.principal.uid != null")
    @SneakyThrows
    public boolean uploadLabel(@InputArgument(collectionType = LabelInput.class) List<LabelInput> labels, DataFetchingEnvironment dfe) {
        User user = utilService.getUserInformation();
        ArrayList<Project> projects = new ArrayList<>(user.getProjects());
        projects.addAll(user.getMemberProjects());
        for (LabelInput labelInput : labels) {
            Image image = imageRepository.getImageById(labelInput.getImageID());
            if (image == null)
                throw new AccessDeniedException("Image by id %s not found".formatted(labelInput.getImageID()));
            if (!projects.contains(image.getProject()))
                throw new AccessDeniedException("Cant upload labels to a project which is not owned by you");

            labelRepository.save(new Label(image, labelInput.getX1(), labelInput.getX2(), labelInput.getY1(), labelInput.getY2()));
            image.setFinished(true);
            imageRepository.save(image);
        }
        return true;

    }




    @DgsQuery(field = "labels")
    @PreAuthorize("authentication.principal.uid != null")
    @Transactional
    public List<Label> getLabels(@InputArgument("projectName") String projectName, DataFetchingEnvironment dfe) {
        User user = utilService.getUserInformation();
        Project project = utilService.getProject(projectName);
        System.out.println(project);
        if (user.getProjects().contains(project) || user.getMemberProjects().contains(project)){
            var images = imageRepository.getImagesByProject(project);
            List<Label> labels = new ArrayList<>();
            for(Image image : images){
                labels.addAll(labelRepository.getLabelsByImage(image));
            }
            return labels;
        }
        throw new AccessDeniedException("Can´t find images for a not owned project");
    }

    @DgsQuery(field = "imagesInProject")
    @PreAuthorize("authentication.principal.uid != null")
    @Transactional
    public List<Image> findAllImagesInProject(@InputArgument("projectName") String projectName, DataFetchingEnvironment dfe) {
        User user = utilService.getUserInformation();
        Project project = utilService.getProject(projectName);
        System.out.println(project);
        if (user.getProjects().contains(project) || user.getMemberProjects().contains(project))
            return imageRepository.getImagesByProject(project);
        throw new AccessDeniedException("Can´t find images for a not owned project");
    }

    @DgsQuery(field = "imageUnlabeledInProject")
    @Transactional
    @PreAuthorize("authentication.principal.uid != null")
    public Image findImageUnlabeledInProject(@InputArgument() String projectName, DataFetchingEnvironment dfe) {
        List<Image> imagesInProject = findAllImagesInProject(projectName, dfe);

        return imagesInProject.stream().filter(image -> !image.isFinished()).collect(Collectors.toList()).stream().findAny().orElse(null);
    }

    @DgsQuery(field = "image")
    @SneakyThrows
    @PreAuthorize("authentication.principal.uid != null")
    public Image getImageByID(@InputArgument("id") long ID) {
        User user = utilService.getUserInformation();
        Image image = imageRepository.findById(ID).orElse(null);
        if (image == null)
            return null;
        User owner = image.getProject().getOwner();
        if (!owner.equals(user) && !user.getMemberProjects().contains(image.getProject()))
            throw new Exception("You are not added to this project");
        return image;
    }


}
