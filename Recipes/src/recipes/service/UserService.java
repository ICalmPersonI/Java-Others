package recipes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import recipes.entity.User;
import recipes.repository.UserRepository;
//import recipes.service.UserDetailsImpl;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    public UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> user = repository.findByEmail(userName);
        user.orElseThrow(() -> new UsernameNotFoundException(userName + " not found."));
        return user.map(UserDetailsImpl::new).get();
    }

    public boolean saveUser(User user) {
        Optional<User> record = repository.findByEmail(user.getEmail());

        if (record.isPresent()) {
            return false;
        } else {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            repository.save(user);
            return true;
        }

    }


}
