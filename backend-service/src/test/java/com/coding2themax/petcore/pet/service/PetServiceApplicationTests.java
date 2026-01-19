package com.coding2themax.petcore.pet.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest

class PetServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mainMethodStartsApplication() {
		// Test that main method doesn't throw an exception
		PetServiceApplication.main(new String[] {});
	}

}
