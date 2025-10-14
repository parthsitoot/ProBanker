package com.banking.proBanker.DTO;

public record ResetPasswordRequest (String identifier, String resetToken, String newPassword){
}
