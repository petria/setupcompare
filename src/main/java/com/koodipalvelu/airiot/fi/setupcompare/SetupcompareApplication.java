package com.koodipalvelu.airiot.fi.setupcompare;

import com.koodipalvelu.airiot.fi.setupcompare.service.FilesStorageService;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SetupcompareApplication implements CommandLineRunner {

//	@Resource
//	FilesStorageService storageService;

	@Resource
	private FilesStorageService filesStorageService;

	public static void main(String[] args) {
		SpringApplication.run(SetupcompareApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		filesStorageService.init();
	}
}
