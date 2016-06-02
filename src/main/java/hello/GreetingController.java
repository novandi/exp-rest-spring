package hello;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
	private static final String template = "Hello, %s";
	private final AtomicLong counter = new AtomicLong();
	
	public static class PrintTask implements Runnable{

		String name;
		
		public PrintTask(String name){
			this.name = name;
		}
		
		@Override
		public void run() {
			
			System.out.println(name + " is running");
		}

	}
	
	@RequestMapping("/greeting")
	public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name ) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("app-config.xml");
		
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) context.getBean("taskExecutor");
	    
	    for(int i=0 ; i<10 ; i++) {
	    	taskExecutor.execute(new PrintTask(name +" " + i));
	    	try {
	    		System.out.println("Sleeping");
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	System.out.println("Started");
	    }
		
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
		
	}
	
}
