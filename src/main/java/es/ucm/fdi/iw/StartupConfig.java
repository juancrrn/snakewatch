package es.ucm.fdi.iw;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.text.SimpleDateFormat;

/**
 * Enables or disables debugging and sets the date format
 * 
 * When context is refreshed, i. e. when the app starts, this component enables
 * or disables debugging based in the "es.ucm.fdi.debug" enviroment variable.
 * It also sets the date format.
 * 
 * @author mfreire
 */
@Component
public class StartupConfig {
	
	private static final Logger log = LogManager.getLogger(StartupConfig.class);
	
	@Autowired
	private Environment env;

	@Autowired
	private ServletContext context;
	
	/**
	 * @see http://www.ecma-international.org/ecma-262/5.1/#sec-15.9.1.15
	 * @see https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html
	 */
	@EventListener(ContextRefreshedEvent.class)
	public void contextRefreshedEvent() {
		String debugProperty = env.getProperty("es.ucm.fdi.debug");
		context.setAttribute("debug", debugProperty != null 
				&& Boolean.parseBoolean(debugProperty.toLowerCase()));
		log.info("Setting global debug property to {}", 
				context.getAttribute("debug"));
		context.setAttribute("dateFormatter", 
				new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sssZ"));
	}
}
