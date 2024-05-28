package com.example.oAuth.controller; 

import com.example.oAuth.repository.UserRepository;
import com.example.oAuth.securityOAuthConfig.TokenGenerator; 
import com.example.oAuth.userDocument.User; 
import com.example.oAuth.userModel.Login; 
import com.example.oAuth.userModel.SignUp; 
import com.example.oAuth.userModel.Token; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.beans.factory.annotation.Qualifier; 
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity; 
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; 
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; 
import org.springframework.security.core.Authentication; 
import org.springframework.security.core.userdetails.UserDetails; 
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
//import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken; 
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider; 
import org.springframework.security.provisioning.UserDetailsManager; 
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.RequestBody; 
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections; 

@RestController
@RequestMapping("/api/auth") 
public class AuthController { 
	@Autowired
	UserDetailsManager userDetailsManager; 
	@Autowired
	TokenGenerator tokenGenerator; 
	@Autowired
	DaoAuthenticationProvider daoAuthenticationProvider; 
	@Autowired
	@Qualifier("jwtRefreshTokenAuthProvider") 
	JwtAuthenticationProvider refreshTokenAuthProvider; 
	
	@Autowired
	UserRepository userRepository;

	@PostMapping("/register") 
	public ResponseEntity register(@RequestBody SignUp signupDTO) { 
		User user = new User(signupDTO.getUserName(), signupDTO.getPassword()); 
		userDetailsManager.createUser(user); 

		//Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(user, signupDTO.getPassword(), Collections.EMPTY_LIST); 
		Authentication authentication = new UsernamePasswordAuthenticationToken(
			    user, 
			    signupDTO.getPassword(), 
			    Collections.emptyList()
			);
		return ResponseEntity.ok(tokenGenerator.createToken(authentication)); 
	} 



	@PostMapping("/login") 
	public ResponseEntity login(@RequestBody Login loginDTO) { 
		//Authentication authentication = daoAuthenticationProvider.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(loginDTO.getUserName(), loginDTO.getPassword())); 
		Authentication authentication = new UsernamePasswordAuthenticationToken(
			    loginDTO.getUserName(),
			    loginDTO.getPassword());
		userRepository.findByUserName(authentication.getName());
		return ResponseEntity.ok(tokenGenerator.createToken(authentication)); 
	} 

	@PostMapping("/token") 
	public ResponseEntity token(@RequestBody Token tokenDTO) { 
		Authentication authentication = refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(tokenDTO.getRefreshToken())); 
		Jwt jwt = (Jwt) authentication.getCredentials(); 
		// check if present in db and not revoked, etc 

		return ResponseEntity.ok(tokenGenerator.createToken(authentication)); 
	} 
}
