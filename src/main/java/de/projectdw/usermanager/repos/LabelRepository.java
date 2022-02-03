package de.projectdw.usermanager.repos;

import de.projectdw.sysedataclasses.data.Image;
import de.projectdw.sysedataclasses.data.Label;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelRepository extends CrudRepository<Label, Long> {
    public List<Label> getLabelsByImage(Image image);
}
