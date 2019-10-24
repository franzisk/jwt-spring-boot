package com.safeplace;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.safeplace.service.DeployService;

@SpringBootApplication
public class JwtSafePlaceApplication {

	@Autowired
	DeployService deployService;

	public static void main(String[] args) {
		SpringApplication.run(JwtSafePlaceApplication.class, args);
	}

	@PostConstruct
	public void postDeploy() {
		this.deployService.afterDeployAction();
	}

}
