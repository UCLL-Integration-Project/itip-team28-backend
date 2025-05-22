package team28.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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

    public ReaderService(ReaderRepository ReaderRepository, CoordinateRepository CoordinateRepository) {
        this.ReaderRepository = ReaderRepository;
        this.CoordinateRepository = CoordinateRepository;
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

    public Reader UpdateReader(ReaderUpdateInput ReaderInput) {
        boolean exists = ReaderRepository.existsById(ReaderInput.id());
        if (!exists) {
            throw new ServiceException("Reader doesn't exist");
        }
        boolean NameExists = ReaderRepository.existsByName(ReaderInput.name());
        if (NameExists) {
            throw new ServiceException("Name is already in use");
        }

        Optional<Reader> reader = ReaderRepository.findById(ReaderInput.id());
        Reader UpdatedReader = reader.get();

        Coordinate coordinates = new Coordinate(ReaderInput.coordinates().getLongitude(),
                ReaderInput.coordinates().getLatitude());

        var NewCoordinates = CoordinateRepository.save(coordinates);

        UpdatedReader.setMacAddress(ReaderInput.macAddress());
        UpdatedReader.setName(ReaderInput.name());
        UpdatedReader.setCoordinate(NewCoordinates);

        return ReaderRepository.save(UpdatedReader);
    }

    public Reader UpdateReaderName(Long id, ReaderInput ReaderInput) {
        boolean exists = ReaderRepository.existsById(id);
        if (!exists) {
            throw new ServiceException("Reader doesn't exist");
        }

        Optional<Reader> readerOpt = ReaderRepository.findById(id);
        Reader UpdatedReader = readerOpt.get();

        if (!UpdatedReader.getName().equals(ReaderInput.name()) && ReaderRepository.existsByName(ReaderInput.name())) {
            throw new ServiceException("Name is already in use");
        }

        UpdatedReader.setName(ReaderInput.name());

        return ReaderRepository.save(UpdatedReader);
    }
}
