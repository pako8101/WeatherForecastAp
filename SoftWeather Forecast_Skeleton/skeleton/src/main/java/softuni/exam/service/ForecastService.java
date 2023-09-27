package softuni.exam.service;

import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;

// TODO: Implement all methods

public interface ForecastService {

    boolean areImported();

    String readForecastsFromFile() throws IOException;
	
	String importForecasts() throws IOException, JAXBException;

    String exportForecasts();
}
