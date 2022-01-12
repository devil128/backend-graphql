package de.projectdw.usermanager.data;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "Projekte")

@Data
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;
    long userId;
    String projectName;

    public Project(String projectName) {
        this.projectName = projectName;
    }

    public Project() {

    }

    public boolean isUserInProject(long userID){
        return this.userId == userID;
    }




}
