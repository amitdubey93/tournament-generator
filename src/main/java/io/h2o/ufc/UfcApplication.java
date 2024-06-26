package io.h2o.ufc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class UfcApplication {

//	@Autowired
//	private static Environment environment;

	public static void main(String[] args) throws IOException {
		System.err.println("start");
		SpringApplication.run(UfcApplication.class, args);
		System.err.println("end");
		excCommand();
	}

	public static void excCommand() throws IOException {
		Runtime rt = Runtime.getRuntime();
		String url = "http://localhost:8080";
		try {
			rt.exec(new String[]{"cmd.exe", "/C", "start", "chrome", url});

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println("excCommand");
	}
}
