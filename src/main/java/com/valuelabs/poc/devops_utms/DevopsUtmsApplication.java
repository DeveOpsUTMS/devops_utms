package com.valuelabs.poc.devops_utms;

import org.kohsuke.github.GitHub;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.valuelabs.poc.devops_utms.service.GitHubServiceImpl;
import com.valuelabs.poc.devops_utms.service.JiraServiceImpl;

@SpringBootApplication
@EnableScheduling
public class DevopsUtmsApplication extends SpringBootServletInitializer{
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DevopsUtmsApplication.class);
    }

	public static void main(String[] args) {
		SpringApplication.run(DevopsUtmsApplication.class, args);
		System.out.println("application start running..............");
	}

	@Bean(name = "gitJiraServiceImpl")
	JiraServiceImpl JiraServiceImpl() throws Exception {
		JiraServiceImpl JiraServiceImpl = new JiraServiceImpl();
		return JiraServiceImpl;
	}

	@Bean(name = "gitHubServiceImpl")
	GitHubServiceImpl gitHubServiceImpl() throws Exception {
		GitHubServiceImpl gitHubServiceImpl = new GitHubServiceImpl();
		return gitHubServiceImpl;
	}

	@SuppressWarnings("static-access")
	@Bean(name = "gitHub")
	GitHub gitHub() throws Exception {
		GitHub gitHub = null;
		String userName = "DeveOpsUTMS";
		//String token = "da3fe296dcde8fc9b506a8dbb397ffc3bdfdef11";
		String password = "DeveOpsUTMS2";
		try {
			gitHub = gitHub.connectUsingPassword(userName, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gitHub;
	}
	
}