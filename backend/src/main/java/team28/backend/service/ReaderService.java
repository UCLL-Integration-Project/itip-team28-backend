package team28.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

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
    private final StockService StockService;
    private final ItemRepository ItemRepository;
    private final GridRepository GridRepository;
    private final RouteService RouteService;

    public ReaderService(ReaderRepository ReaderRepository, CoordinateRepository CoordinateRepository,
            StockService StockService, ItemRepository ItemRepository, GridRepository GridRepository,
            RouteService RouteService) {
        this.ReaderRepository = ReaderRepository;
        this.CoordinateRepository = CoordinateRepository;

        this.StockService = StockService;
        this.ItemRepository = ItemRepository;
        this.GridRepository = GridRepository;
        this.RouteService = RouteService;
    }

    public List<Reader> GetAllReaders() {
        return ReaderRepository.findAll();
    }

    @Transactional
    public void DeleteReader(Long id) {
        try {
            Reader reader = ReaderRepository.findById(id)
                    .orElseThrow(() -> new ServiceException("Reader not found"));
            Coordinate coordinate = reader.getCoordinates();
            if (coordinate != null) {
                coordinate.setReader(null); 
                CoordinateRepository.save(coordinate);
            }
            ReaderRepository.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException("Failed to delete reader: " + e.getMessage());
        }
        try {
            RouteService.generateRoutes();
        } catch (Exception e) {
            System.err.println("Failed to generate routes after deletion: " + e.getMessage());
        }
    }

    @Transactional
    public Reader CreateReader(ReaderInput readerInput) {
        if (ReaderRepository.existsByName(readerInput.name())) {
            throw new ServiceException("Name is already in use");
        }

        Coordinate coordinates = CoordinateRepository.findByLongitudeAndLatitude(
                readerInput.coordinates().longitude(),
                readerInput.coordinates().latitude())
                .orElseGet(() -> {
                    Coordinate newCoord = new Coordinate(
                            readerInput.coordinates().longitude(),
                            readerInput.coordinates().latitude());
                    return CoordinateRepository.save(newCoord);
                });

        // Grid grid = GridRepository.findFirstByOrderByIdAsc()
        //         .orElseThrow(() -> new ServiceException("No grid found"));

        // boolean coordinateBelongsToGrid = grid.getCoordinates().stream()
        //         .anyMatch(c -> c.getId() == coordinates.getId());

        // if (!coordinateBelongsToGrid) {
        //     throw new ServiceException("Grid must contain the reader coordinate");
        // }

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

        Coordinate coordinates = new Coordinate(
                readerInput.coordinates().longitude(),
                readerInput.coordinates().latitude());
        Coordinate newCoordinates = CoordinateRepository.save(coordinates);

        updatedReader.setMacAddress(readerInput.macAddress());
        updatedReader.setName(readerInput.name());
        updatedReader.setCoordinate(newCoordinates);

        Reader savedReader = ReaderRepository.save(updatedReader);

        return savedReader;
    }

    // retrieves all stock for this reader
    public List<Stock> getStockForReader(Long readerId) {
        Reader reader = ReaderRepository.findById(readerId)
                .orElseThrow(() -> new ServiceException("Reader with id " + readerId + " not found"));

        return StockService.getStocksForHolder(reader);
    }

    // adds stock to the reader
    public Stock addStockToReader(Long readerId, Long itemId, int quantity) {
        Reader reader = ReaderRepository.findById(readerId)
                .orElseThrow(() -> new ServiceException("Reader with id " + readerId + " not found"));
        Item item = ItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item with id " + itemId + " not found"));

        return StockService.addStockToHolder(reader, item, quantity);
    }

    public String getReaderName(String macAddress) {
        Reader reader = ReaderRepository.findByMacAddress(macAddress);
        if (reader == null) {
            throw new ServiceException("Reader not found");
        }
        return reader.getName();
    }

}
