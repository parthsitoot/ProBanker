package com.banking.proBanker.Utilities;

import com.banking.proBanker.Exceptions.NotFoundException;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class LoggedinUser {
    /**
     * Returns the account number of currently logged-in user.
     * If there is no user logged-in, it will throw an exception.
     * @return account number of currently logged-in user.
     * @throws NotFoundException If there is no user logged-in.
     * @author Parth Sitoot
     */

    public static String getAccountNumber () {
        val authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new NotFoundException("No user is currently logged-in");
        }

        val principal = (User) authentication.getPrincipal();
        return principal.getUsername();
    }
}
