package de.projectdw.usermanager.repos;

import de.projectdw.sysedataclasses.data.Image;
import de.projectdw.sysedataclasses.data.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ImageRepository extends CrudRepository<Image, Long> {
    public List<Image> getImagesByProject(Project project);
    public Image getImageById(Long id);

}
