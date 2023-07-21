package pt.famility.backoffice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.famility.backoffice.domain.User;
import pt.famility.backoffice.domain.UserMessagingToken;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the UserMessagingToken entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserMessagingTokenRepository extends JpaRepository<UserMessagingToken, Long> {
    List<UserMessagingToken> findAllByUser_OrganizationId(Long organizationId);

    List<UserMessagingToken> findAllByUser(User user);

    Optional<UserMessagingToken> findByToken(String token);

    List<UserMessagingToken> deleteByToken(String token);
}
