Spring Boot JWT OAuth Implementation


This project demonstrates the implementation of JWT (JSON Web Tokens) with OAuth in a Spring Boot application. It includes APIs for user registration, authentication, and token refreshing.

Features:

1.JWT Authentication: Secure user authentication using JSON Web Tokens.
2.OAuth Integration: OAuth client ID and secret for secure API access.
3.Register API: Register users with client ID and secret.
4.User Login: Authenticate users using client ID, secret, username, and password.
5.Token Refreshing:  refresh tokens before expiration (default: 20 minutes).


Steps to create a private key file for JWT access token generation using CMD (Command Prompt) on Windows:
1. Open Command Prompt
2. Navigate to the Directory Where You Want to Create the Key
  cd path\to\directory
3. Generate the Private Key:
 openssl genrsa -out access-token-private.key 2048

For refresh Key:
Command:
openssl genrsa -out refresh-token-private.key 2048