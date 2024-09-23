package com.anarchyadventure.music_dabang_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MusicDabangApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusicDabangApiApplication.class, args);
	}

}
