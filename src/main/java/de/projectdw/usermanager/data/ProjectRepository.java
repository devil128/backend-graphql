package de.projectdw.usermanager.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
    public Optional<Project> getProjectById(long id);
    public List<Project> getProjectsByUserId(long id);

}
