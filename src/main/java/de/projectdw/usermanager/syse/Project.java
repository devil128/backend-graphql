package de.projectdw.usermanager.syse;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "Projekte")

@Data
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    long userId;
    String projectName;
}
