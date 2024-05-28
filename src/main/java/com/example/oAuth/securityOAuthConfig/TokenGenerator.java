package com.example.oAuth.securityOAuthConfig; 

import java.text.MessageFormat; 
import java.time.Duration; 
import java.time.Instant; 
import java.time.temporal.ChronoUnit; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.beans.factory.annotation.Qualifier; 
import org.springframework.security.core.Authentication; 
import org.springframework.security.oauth2.jwt.JwtClaimsSet; 
import org.springframework.security.oauth2.jwt.JwtEncoder; 
import org.springframework.security.oauth2.jwt.JwtEncoderParameters; 
import com.example.oAuth.userDocument.User; 
import com.example.oAuth.userModel.Token; 
import org.springframework.security.authentication.BadCredentialsException; 
import org.springframework.security.oauth2.jwt.Jwt; 
import org.springframework.stereotype.Component; 

@Component
public class TokenGenerator { 
	
	@Autowired
	JwtEncoder accessTokenEncoder; 

	@Autowired
	@Qualifier("jwtRefreshTokenEncoder") 
	JwtEncoder refreshTokenEncoder;
	
	private String createAccessToken(Authentication authentication) { 
		User user = (User) authentication.getPrincipal(); 
		Instant now = Instant.now(); 

		JwtClaimsSet claimsSet = JwtClaimsSet.builder() 
				.issuer("myApp")
				.issuedAt(now)
				.expiresAt(now.plus(20, ChronoUnit.MINUTES))
				.subject(user.getId().toString())
				.build(); 

		return accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue(); 
	} 
	
	private String createRefreshToken(Authentication authentication) { 
		User user = (User) authentication.getPrincipal(); 
		Instant now = Instant.now(); 

		JwtClaimsSet claimsSet = JwtClaimsSet.builder()
				.issuer("myApp")
				.issuedAt(now)
				.expiresAt(now.plus(20, ChronoUnit.MINUTES))
				.subject(user.getId().toString()) 
				.build(); 

		return refreshTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue(); 
	} 
	
	public Token createToken(Authentication authentication) { 
		Object principal = authentication.getPrincipal();
		 if (!(principal instanceof User)) {
			 throw new BadCredentialsException(MessageFormat.format(
					 "Principal {0} is not of User type", principal.getClass()));
		 }
	    User user = (User) principal;
	    Token tokenDTO = new Token();
	    tokenDTO.setUserId(user.getId()); 
        tokenDTO.setAccessToken(createAccessToken(authentication));    
  
        String refreshToken; 
        Object credentials = authentication.getCredentials();
        if (!(credentials instanceof Jwt)) { 
        	 refreshToken = createRefreshToken(authentication); 
        } else { 
        	Jwt jwt = (Jwt) credentials;
            Instant now = Instant.now(); 
            Instant expiresAt = jwt.getExpiresAt(); 
            Duration duration = Duration.between(now, expiresAt); 
            long daysUntilExpired = duration.toDays(); 
            if (daysUntilExpired < 7) { 
                refreshToken = createRefreshToken(authentication); 
            } else { 
                refreshToken = jwt.getTokenValue(); 
            } 
        } 
        tokenDTO.setRefreshToken(refreshToken); 
  
        return tokenDTO; 
    }

} 
