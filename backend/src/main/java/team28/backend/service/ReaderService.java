package team28.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import team28.backend.controller.dto.ReaderInput;
import team28.backend.exceptions.ReaderException;
import team28.backend.model.Reader;
import team28.backend.repository.ReaderRepository;

@Service
public class ReaderService {
    private final ReaderRepository ReaderRepository;

    public ReaderService(ReaderRepository ReaderRepository) {
        this.ReaderRepository = ReaderRepository;
    }

    public List<Reader> GetAllReaders() {
        return ReaderRepository.findAll();
    }

    public void DeleteReader(Long id) {
        boolean ExistingReader = ReaderRepository.existsById(id);

        if (!ExistingReader) {
            throw new ReaderException("Reader not found");
        }

        ReaderRepository.deleteById(id);
    }

    public Reader CreateReader(ReaderInput ReaderInput) {
        boolean exists = ReaderRepository.existsByName(ReaderInput.name());
        if (exists) {
            throw new ReaderException("Name is already in use");
        }

        final var reader = new Reader(
                ReaderInput.MacAddress(), ReaderInput.name(), ReaderInput.coordinate());

        return ReaderRepository.save(reader);
    }

    public Reader UpdateReader(Long id, ReaderInput ReaderInput) {
        boolean exists = ReaderRepository.existsById(id);
        if (!exists) {
            throw new ReaderException("Reader doesn't exist");
        }

        Optional<Reader> reader = ReaderRepository.findById(id);
        Reader UpdatedReader = reader.get();

        UpdatedReader.setMacAddress(ReaderInput.MacAddress());
        UpdatedReader.setName(ReaderInput.name());
        UpdatedReader.setCoordinate(ReaderInput.coordinate());

        return ReaderRepository.save(UpdatedReader);
    }
}
