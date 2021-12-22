package de.projectdw.usermanager.syse;

import com.netflix.graphql.dgs.context.DgsCustomContextBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectContextBuilder implements DgsCustomContextBuilder<ProjectContext> {

    private List<Project> projectList;

    public ProjectContextBuilder withProjects(List<Project> projects) {
        this.projectList = projects;
        return this;
    }

    @Override
    public ProjectContext build() {
        return new ProjectContext(projectList);
    }
}
