package cat.desp.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "cat.desp.service" })
public class DespServiceConfig {
	public DespServiceConfig() {
		super();
	}
}
