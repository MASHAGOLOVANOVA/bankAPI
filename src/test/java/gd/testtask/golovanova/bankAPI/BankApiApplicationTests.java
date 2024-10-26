package gd.testtask.golovanova.bankAPI;

import gd.testtask.golovanova.bankAPI.services.BankService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BankApiApplicationTests {

	@Autowired
	BankService bankService;

	@Test
	void contextLoads() {
	}

}
