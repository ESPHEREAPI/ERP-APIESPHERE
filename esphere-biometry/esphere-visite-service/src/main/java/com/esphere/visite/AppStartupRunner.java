/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esphere.visite;

import com.esphere.visite.service.VisiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 *
 * @author USER01
 */
@Component
@RequiredArgsConstructor
public class AppStartupRunner implements ApplicationRunner {

    private final VisiteService visiteService;

    @Async
    @Override
    public void run(ApplicationArguments args) {
        visiteService.marquerPrestationsSansLignesCommeSupprimees();
    }
}