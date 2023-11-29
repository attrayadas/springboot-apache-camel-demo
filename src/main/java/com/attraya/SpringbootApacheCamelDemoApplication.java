package com.attraya;

import java.util.Arrays;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootApacheCamelDemoApplication extends RouteBuilder {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApacheCamelDemoApplication.class, args);

	}

	@Override
	public void configure() throws Exception {

		/* move data from one file to another file */
		System.out.println("started...");
//		moveAllFile();
//		moveSpecificFile("myFile");
//		moveSpecificFileWithBody("How");
		fileProcess();
		System.out.println("end...");
	}

	/* Move all the files from source to destination folder */
	private void moveAllFile() {
		from("file:source?noop=true").to("file:destination");
	}

	/* Move all the files from source to destination folder with specific file name */
	private void moveSpecificFile(String type) {
		from("file:source?noop=true").filter(header(Exchange.FILE_NAME).startsWith(type)).to("file:destination");
	}

	/* Move all the files from source to destination folder with specific file content */
	private void moveSpecificFileWithBody(String content) {
		from("file:source?noop=true").filter(body().startsWith(content)).to("file:destination");
	}

	/* Creates a csv file with space separated content in source folder */
	private void fileProcess() {
		from("file:source?noop=true").process(p -> {
			String body = p.getIn().getBody(String.class);
			StringBuilder sb = new StringBuilder();
			Arrays.stream(body.split(" ")).forEach(s -> {
				sb.append(s + ",");
			});
			p.getIn().setBody(sb);
		}).to("file:destination?fileName=records.csv");
	}

}
