package de.projectdw.usermanager.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelRepository extends CrudRepository<Label, Long> {
    public List<Label> getLabelsByImage(Image image);
}
