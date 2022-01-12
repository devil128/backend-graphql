package de.projectdw.usermanager.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.List;

@Repository
public interface ImageRepository extends CrudRepository<Image, Long> {
    public List<Image> getImagesByProject(Project project);
    public Image getImageById(Long id);

}
