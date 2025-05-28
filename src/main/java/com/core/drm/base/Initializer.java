package com.core.drm.base;

import com.core.drm.admin.dto.UserDTO;
import com.core.drm.admin.service.SignService;
import com.core.drm.crypto.config.GenerateAsymmetricKey;
import com.core.drm.crypto.util.PropertiesUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Initializer implements ApplicationRunner {

    private final SignService signService;

    @Override
    public void run(ApplicationArguments args) {
        registerAdmin();
        generatePem();
    }

    private void registerAdmin() {
        String flag = PropertiesUtil.getApplicationProperty("app.init.admin");
        if (flag.equals("true")) {
            log.debug("create admin account!");
            String account = PropertiesUtil.getApplicationProperty("default.admin.account");
            UserDTO userDTO = UserDTO.builder()
                    .userId(account)
                    .name(account)
                    .password(account)
                    .build();
            signService.signUp(userDTO);
        }
    }

    private void generatePem() {
        String flag = PropertiesUtil.getApplicationProperty("app.init.pem");
        if (flag.equals("true")) {
            log.debug("create pem key!");
            GenerateAsymmetricKey.main(null);
        }
    }
}
