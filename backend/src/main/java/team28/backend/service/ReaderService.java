package team28.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import team28.backend.controller.dto.ReaderInput;
import team28.backend.exceptions.ServiceException;
import team28.backend.model.Coordinate;
import team28.backend.model.Item;
import team28.backend.model.Reader;
import team28.backend.model.Stock;
import team28.backend.repository.CoordinateRepository;
import team28.backend.repository.ItemRepository;
import team28.backend.repository.ReaderRepository;
import team28.backend.repository.StockRepository;

@Service
public class ReaderService {
    private final ReaderRepository readerRepository;
    private final CoordinateRepository coordinateRepository;
    private final StockService stockService;
    private final ItemRepository itemRepository;

    public ReaderService(ReaderRepository readerRepository, CoordinateRepository coordinateRepository, StockService stockService, ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
        this.readerRepository = readerRepository;
        this.coordinateRepository = coordinateRepository;
        this.stockService = stockService;
    }

    public List<Reader> GetAllReaders() {
        return readerRepository.findAll();
    }

    public void DeleteReader(Long id) {
        boolean ExistingReader = readerRepository.existsById(id);

        if (!ExistingReader) {
            throw new ServiceException("Reader not found");
        }

        readerRepository.deleteById(id);
    }

    public Reader CreateReader(ReaderInput ReaderInput) {
        boolean exists = readerRepository.existsByName(ReaderInput.name());
        if (exists) {
            throw new ServiceException("Name is already in use");
        }

        Coordinate coordinates = new Coordinate(ReaderInput.coordinates().getLongitude(),
                ReaderInput.coordinates().getLatitude());

        var NewCoordinates = coordinateRepository.save(coordinates);

        final var reader = new Reader(
                ReaderInput.MacAddress(), ReaderInput.name(), NewCoordinates);

        return readerRepository.save(reader);
    }

    public Reader UpdateReader(Long id, ReaderInput ReaderInput) {
        boolean exists = readerRepository.existsById(id);
        if (!exists) {
            throw new ServiceException("Reader doesn't exist");
        }

        Optional<Reader> reader = readerRepository.findById(id);
        Reader UpdatedReader = reader.get();

        Coordinate coordinates = new Coordinate(ReaderInput.coordinates().getLongitude(),
                ReaderInput.coordinates().getLatitude());

        var NewCoordinates = coordinateRepository.save(coordinates);

        UpdatedReader.setMacAddress(ReaderInput.MacAddress());
        UpdatedReader.setName(ReaderInput.name());
        UpdatedReader.setCoordinate(NewCoordinates);

        return readerRepository.save(UpdatedReader);
    }

    public List<Stock> getStockForReader(Long readerId){
        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(() -> new ServiceException("Reader with id " + readerId + " not found"));

        return stockService.getStocksForHolder(reader);
    }

    public Stock addStockToReader(Long readerId, Long itemId, int quantity){
        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(() -> new ServiceException("Reader with id " + readerId + " not found"));
        Optional<Item> item = itemRepository.findById(itemId);

        return stockService.addStockToHolder(reader, item.get(), quantity);
    }
}
