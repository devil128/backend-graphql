package de.projectdw.usermanager.syse;

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
    String filename;
    String generatedFileName;
    //https://www.bezkoder.com/spring-boot-upload-file-database/
    String contentType;
    @Lob
    byte[] file;
    @OneToOne
    Project project;
}
