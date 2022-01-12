package de.projectdw.usermanager;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;

import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.context.DgsContext;
import de.projectdw.usermanager.data.*;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@DgsComponent
public class UserServiceApplication {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private LabelRepository labelRepository;



    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @DgsData(parentType = "Mutation", field = "newProject")
    public Project createProject(DataFetchingEnvironment dfe) throws IOException {
            Project pro = new Project(dfe.getArgument("projectname"));
            projectRepository.save(pro);
            long projectID = pro.getId();
            return pro;
    }

    @DgsData(parentType = "Mutation", field = "uploadImage")
    public void uploadImage(DataFetchingEnvironment dfe) throws IOException {
        Project pro = new Project(dfe.getArgument("projectname"));
        FileUpload image = dfe.getArgument("inputImage");
        ByteArrayInputStream bis = new ByteArrayInputStream(image.getContent());
        BufferedImage bImage = ImageIO.read(bis);
        Image data = new Image(bImage.getWidth(), bImage.getHeight(), new Date(), image.getContent(), pro);
        imageRepository.save(data);

    }

    @DgsData(parentType = "Mutation", field = "uploadLabel")
    public void uploadLabel(DataFetchingEnvironment dfe) {
        Long imageID =  dfe.getArgument("imageID");
        int x1  =  dfe.getArgument("x1");
        int y1  =  dfe.getArgument("y1");
        int x2  =  dfe.getArgument("x2");
        int y2  =  dfe.getArgument("y2");
        Image image = imageRepository.getImageById(imageID);
        labelRepository.save(new Label(image,x1,x2,y1,y2));



    }

    @DgsData(parentType = "QueryResolver", field = "projects")
    public List<Project> findAllProjects() {
        List<Project> projects = (List<Project>) projectRepository.findAll();

        return projects;
    }

    @DgsData(parentType = "QueryResolver", field = "projectsByUserID")
    public List<Project> findAllProjectsForUser(@InputArgument("id") Long userID, DataFetchingEnvironment dfe) {
        return projectRepository.getProjectsByUserId(userID);
    }

    @DgsData(parentType = "QueryResolver", field = "imagesInProject")
    public List<Image> findAllImagesInProject(@InputArgument("projectname") String projectname, DataFetchingEnvironment dfe) {
        return imageRepository.getImagesByProject(projectRepository.getProjectByProjectNameEquals(projectname));
    }

    @DgsData(parentType = "QueryResolver", field = "imageUnlabeledInProject")
    public Image findImageUnlabeledInProject(@InputArgument("projectname") String projectname, DataFetchingEnvironment dfe) {
        List<Image> imagesInProject = findAllImagesInProject(projectname,dfe);
        for(Image i: imagesInProject){
            if(i.getProject().getProjectName().equals(projectname)){
                //TODO Logic to find unlabeled Image
                return i;
            }
        }
        return null;
    }






    @DgsData(parentType = "QueryResolver", field = "image")
    public Optional<Image> getImageByID(@InputArgument("id") long ID){
        return imageRepository.findById(ID);
    }








}
