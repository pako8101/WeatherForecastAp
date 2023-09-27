package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CountryImportDTO;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLIntegrityConstraintViolationException;

@Service
public class CountryServiceImpl implements CountryService {

    private CountryRepository countryRepository;
    @Autowired

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public boolean areImported() {
        return this.countryRepository.count() >0;
    }

    @Override
    public String readCountriesFromFile() throws IOException {

         InputStream stream = this.getClass().getResourceAsStream("/files/json/countries.json");
         byte[] bytes = stream.readAllBytes();

         return new String(bytes);


    }

    @Override
    public String importCountries() throws IOException {
        Gson gson = new GsonBuilder().create();
        ModelMapper mapper = new ModelMapper();
        String json = this.readCountriesFromFile();

        final CountryImportDTO[] countryImportDTOS = gson.fromJson(json, CountryImportDTO[].class);

        for (CountryImportDTO countryDTO : countryImportDTOS) {
            if (countryDTO.isValid()){

                final TypeMap<CountryImportDTO,Country> typeMap = mapper.getTypeMap(CountryImportDTO.class, Country.class);
                final Country country = typeMap.map(countryDTO);
                    this.countryRepository.save(country);


            }else {
                System.out.println("Invalid countryDTO");
            }
        }
        return "";
    }
}
