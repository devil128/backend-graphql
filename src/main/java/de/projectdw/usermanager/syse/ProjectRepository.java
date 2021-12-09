package de.projectdw.usermanager.syse;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
    public Optional<Project> getProjectById(long id);
    public Optional<Project> getProjectByUserId(long id);

}
