package de.projectdw.usermanager.repos;

import de.projectdw.sysedataclasses.data.Project;
import de.projectdw.sysedataclasses.data.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
    public Optional<Project> getProjectById(long id);
    public List<Project> getProjectsByOwner(User owner);
    public Project getProjectByProjectNameEquals(String projectname);

}
