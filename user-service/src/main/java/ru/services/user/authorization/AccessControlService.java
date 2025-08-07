package ru.services.user.authorization;

import org.springframework.stereotype.Service;

@Service
public class AccessControlService {
    
    public boolean checkUsernameMatches(String clientId, String usernameFromRequest) {
        return clientId.equals(usernameFromRequest);
    }
}