package de.projectdw.usermanager.data;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    int width;
    int height;
    Date uploaded;
    //https://www.bezkoder.com/spring-boot-upload-file-database/
    String contentType;
    @Lob
    byte[] file;
    @OneToOne
    Project project;

    public Image(int width, int height, Date uploaded, byte[] file, Project project) {
        this.width = width;
        this.height = height;
        this.uploaded = uploaded;
        this.file = file;
        this.project = project;
    }

    public Image() {

    }

    public Project getProject(){
        return project;
    }
}
