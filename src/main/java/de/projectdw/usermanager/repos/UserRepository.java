package de.projectdw.usermanager.repos;

import de.projectdw.sysedataclasses.data.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByUid(String uid);
}
