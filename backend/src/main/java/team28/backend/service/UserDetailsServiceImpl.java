package team28.backend.service;

import team28.backend.exceptions.ServiceException;
import team28.backend.model.User;
import team28.backend.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, UserDetailsPasswordService {
    private final UserRepository UserRepository;

    public UserDetailsServiceImpl(UserRepository UserRepository) {
        this.UserRepository = UserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws ServiceException {
        User user = UserRepository.findByUsername(username);
        if (user == null) {
            throw new ServiceException("Username not found");
        }
        return new UserDetailsImpl(user);

    }

    @Override
    public UserDetails updatePassword(UserDetails userDetails, String newPassword) {
        final var user = ((UserDetailsImpl) userDetails).user();
        user.setPassword(newPassword);
        return userDetails;
    }
}