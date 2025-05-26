package team28.backend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.transaction.Transactional;
import team28.backend.controller.dto.ReaderInput;
import team28.backend.controller.dto.ReaderUpdateInput;
import team28.backend.exceptions.ServiceException;
import team28.backend.model.Coordinate;
import team28.backend.model.Grid;
import team28.backend.model.Item;
import team28.backend.model.Reader;
import team28.backend.model.Stock;
import team28.backend.repository.CoordinateRepository;
import team28.backend.repository.GridRepository;
import team28.backend.repository.ItemRepository;
import team28.backend.repository.ReaderRepository;

@Service
public class ReaderService {
    private final ReaderRepository ReaderRepository;
    private final CoordinateRepository CoordinateRepository;
    private final RestTemplate restTemplate;
    private final StockService StockService;
    private final ItemRepository ItemRepository;
    private final GridRepository GridRepository;
    private final RouteService RouteService;

    public ReaderService(ReaderRepository ReaderRepository, CoordinateRepository CoordinateRepository,
            StockService StockService, ItemRepository ItemRepository, GridRepository GridRepository,
            RouteService RouteService) {
        this.ReaderRepository = ReaderRepository;
        this.CoordinateRepository = CoordinateRepository;
        this.restTemplate = new RestTemplate();

        this.StockService = StockService;
        this.ItemRepository = ItemRepository;
        this.GridRepository = GridRepository;
        this.RouteService = RouteService;
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

    @Transactional
    public Reader CreateReader(ReaderInput readerInput) {
        if (ReaderRepository.existsByName(readerInput.name())) {
            throw new ServiceException("Name is already in use");
        }

        Coordinate coordinates = CoordinateRepository.findByLongitudeAndLatitude(
                readerInput.coordinates().getLongitude(),
                readerInput.coordinates().getLatitude())
                .orElseThrow(() -> new ServiceException("Given coordinates do not exist."));

        Grid grid = GridRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new ServiceException("No grid found"));

        boolean coordinateBelongsToGrid = grid.getCoordinates().stream()
                .anyMatch(c -> c.getId() == coordinates.getId());

        if (!coordinateBelongsToGrid) {
            throw new ServiceException("Grid must contain the reader coordinate");
        }

        Reader reader = new Reader(readerInput.macAddress(), readerInput.name(), coordinates);
        coordinates.setReader(reader);

        CoordinateRepository.save(coordinates);
        Reader savedReader = ReaderRepository.save(reader);
        RouteService.generateRoutes();
        return savedReader;
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
        RouteService.generateRoutes();

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

    public List<Stock> getStockForReader(Long readerId) {
        Reader reader = ReaderRepository.findById(readerId)
                .orElseThrow(() -> new ServiceException("Reader with id " + readerId + " not found"));

        return StockService.getStocksForHolder(reader);
    }

    public Stock addStockToReader(Long readerId, Long itemId, int quantity) {
        Reader reader = ReaderRepository.findById(readerId)
                .orElseThrow(() -> new ServiceException("Reader with id " + readerId + " not found"));
        Item item = ItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item with id " + itemId + " not found"));

        return StockService.addStockToHolder(reader, item, quantity);
    }

    public Map<String, String> getReaderConfig(String macAddress) {
        Reader reader = ReaderRepository.findByMacAddress(macAddress);
        if (reader == null) {
            throw new ServiceException("Reader not found");
        }

        Map<String, String> config = new HashMap<>();
        config.put("name", reader.getName());
        return config;
    }

}
