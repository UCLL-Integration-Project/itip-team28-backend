package team28.backend.service;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import team28.backend.controller.dto.ReaderInput;
import team28.backend.controller.dto.ReaderUpdateInput;
import team28.backend.exceptions.ServiceException;
import team28.backend.model.Coordinate;
import team28.backend.model.Reader;
import team28.backend.repository.CoordinateRepository;
import team28.backend.repository.ReaderRepository;

@Service
public class ReaderService {
    private final ReaderRepository ReaderRepository;
    private final CoordinateRepository CoordinateRepository;
    private final RestTemplate restTemplate;

    public ReaderService(ReaderRepository ReaderRepository, CoordinateRepository CoordinateRepository) {
        this.ReaderRepository = ReaderRepository;
        this.CoordinateRepository = CoordinateRepository;
        this.restTemplate = new RestTemplate();

    }

    public List<Reader> GetAllReaders() {
        return ReaderRepository.findAll();
    }

    public void DeleteReader(Long id) {
        boolean ExistingReader = ReaderRepository.existsById(id);

        if (!ExistingReader) {
            throw new ServiceException("Reader not found");
        }

        ReaderRepository.deleteById(id);
    }

    public Reader CreateReader(ReaderInput ReaderInput) {
        boolean exists = ReaderRepository.existsByName(ReaderInput.name());
        if (exists) {
            throw new ServiceException("Name is already in use");
        }

        Coordinate coordinates = new Coordinate(ReaderInput.coordinates().getLongitude(),
                ReaderInput.coordinates().getLatitude());

        var NewCoordinates = CoordinateRepository.save(coordinates);

        final var reader = new Reader(
                ReaderInput.macAddress(), ReaderInput.name(), NewCoordinates);

        return ReaderRepository.save(reader);
    }

    public Reader UpdateReader(ReaderUpdateInput readerInput) {
        boolean exists = ReaderRepository.existsById(readerInput.id());
        if (!exists) {
            throw new ServiceException("Reader doesn't exist");
        }
        boolean nameExists = ReaderRepository.existsByName(readerInput.name());
        if (nameExists) {
            throw new ServiceException("Name is already in use");
        }

        Reader updatedReader = ReaderRepository.findById(readerInput.id())
                .orElseThrow(() -> new ServiceException("Reader not found"));

        Coordinate coordinates = new Coordinate(readerInput.coordinates().getLongitude(),
                readerInput.coordinates().getLatitude());
        Coordinate newCoordinates = CoordinateRepository.save(coordinates);

        updatedReader.setMacAddress(readerInput.macAddress());
        updatedReader.setName(readerInput.name());
        updatedReader.setCoordinate(newCoordinates);

        Reader savedReader = ReaderRepository.save(updatedReader);

        // Stuur naam naar ESP32
        if (savedReader.getIpAddress() != null && !savedReader.getIpAddress().isEmpty()) {
            try {
                String url = "http://" + savedReader.getIpAddress() + "/set-name";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                String body = "{\"name\":\"" + readerInput.name() + "\"}";
                HttpEntity<String> request = new HttpEntity<>(body, headers);
                restTemplate.postForObject(url, request, String.class);
            } catch (Exception e) {
                System.err.println("Failed to send name to ESP32: " + e.getMessage());
            }
        } else {
            System.err.println("No IP address for reader ID " + readerInput.id());
        }

        return savedReader;
    }

    public Reader RegisterIpAddress(String MacAddress, String ipAddress) {
        Reader reader = ReaderRepository.findByMacAddress(MacAddress);
        if (reader == null) {
            throw new ServiceException("Reader not found");
        }

        reader.setIpAddress(ipAddress);
        return ReaderRepository.save(reader);
    }
}
