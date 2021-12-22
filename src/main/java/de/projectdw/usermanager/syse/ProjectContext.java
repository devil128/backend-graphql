package de.projectdw.usermanager.syse;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ProjectContext {
    private List<Project> projectList = new ArrayList<>();
}
