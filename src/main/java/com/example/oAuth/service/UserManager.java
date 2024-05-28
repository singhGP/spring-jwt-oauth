package com.example.oAuth.service; 

import java.text.MessageFormat; 
import java.util.Optional; 

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.security.core.userdetails.UserDetails; 
import org.springframework.security.core.userdetails.UsernameNotFoundException; 
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.security.provisioning.UserDetailsManager; 
import org.springframework.stereotype.Service; 

import com.example.oAuth.repository.UserRepository; 
import com.example.oAuth.userDocument.User; 

@Service
public class UserManager implements UserDetailsManager { 

	@Autowired
	UserRepository userRepository; 

	@Autowired
	PasswordEncoder passwordEncoder; 

	public void createUser(UserDetails user) { 
		
		((User) user).setPassword(passwordEncoder.encode(user.getPassword())); 
		userRepository.save((User) user); 
	} 

	public void updateUser(UserDetails user) { 
	} 

	public void deleteUser(String username) {  
	} 

	public void changePassword(String oldPassword, String newPassword) { 
	} 

	public boolean userExists(String username) { 
		
		return false; 
	} 

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { 
		Optional<User> userOptional = userRepository.findByUserName(username); 

		// Check if the user exists 
		if (!userOptional.isPresent()) { 
			throw new UsernameNotFoundException(MessageFormat.format("User with username {0} not found", username)); 
		} 
		return userOptional.get(); 
	} 

} 
