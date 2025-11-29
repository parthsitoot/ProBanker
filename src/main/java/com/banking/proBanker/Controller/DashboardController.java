package com.banking.proBanker.Controller;

import com.banking.proBanker.Service.DashboardService;
import com.banking.proBanker.Utilities.JsonUtil;
import com.banking.proBanker.Utilities.LoggedinUser;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/user")
    public ResponseEntity<String> getUserDetails() {
        return ResponseEntity.ok(
                JsonUtil.toJSON(
                        dashboardService.getUserDetails(
                                LoggedinUser.getAccountNumber()
                        )
                )
        );
    }

    @GetMapping("/account")
    public ResponseEntity<String> getAccountDetails () {
        return ResponseEntity.ok(
                JsonUtil.toJSON(
                        dashboardService.getAccountDetails(
                                LoggedinUser.getAccountNumber()
                        )
                )
        );
    }
}
