package pt.famility.backoffice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.famility.backoffice.domain.Configuration;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, String> {

}
