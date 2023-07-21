package pt.famility.backoffice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.famility.backoffice.domain.User;
import pt.famility.backoffice.domain.UserMessagingToken;
import pt.famility.backoffice.repository.UserMessagingTokenRepository;
import pt.famility.backoffice.service.dto.UserMessagingTokenDTO;
import pt.famility.backoffice.web.rest.errors.InvalidUIDException;

import java.util.List;

@Slf4j
@Service
@Transactional
public class UserMessagingTokenService {

    private UserMessagingTokenRepository userMessagingTokenRepository;

    private UserService userService;

    public UserMessagingTokenService(UserMessagingTokenRepository userMessagingTokenRepository, UserService userService) {
        this.userMessagingTokenRepository = userMessagingTokenRepository;
        this.userService = userService;
    }

    public UserMessagingToken save(UserMessagingTokenDTO userMessagingTokenDTO) {

        return userMessagingTokenRepository.findByToken(userMessagingTokenDTO.getToken()).orElseGet(() -> {
            UserMessagingToken userMessagingToken = new UserMessagingToken();
            userMessagingToken.setToken(userMessagingTokenDTO.getToken());
            userMessagingToken.setOrigin(userMessagingTokenDTO.getOrigin());
            userMessagingToken.setUser(userService.loadUserById(userMessagingTokenDTO.getUserId()).orElseThrow(() -> new InvalidUIDException("userMessagingTokenService")));
            return userMessagingTokenRepository.save(userMessagingToken);
        });

    }

    public List<UserMessagingToken> findAllByUserOrganizationId(Long organizationId) {
        return userMessagingTokenRepository.findAllByUser_OrganizationId(organizationId);
    }

    public List<UserMessagingToken> findAllByUser(User user) {
        return userMessagingTokenRepository.findAllByUser(user);
    }

    public void delete(String token) {
        log.debug("Deleting token {}", token);
        List<UserMessagingToken> userMessagingTokens = userMessagingTokenRepository.deleteByToken(token);
        log.info("Deleted tokens {}", userMessagingTokens);
    }
}
