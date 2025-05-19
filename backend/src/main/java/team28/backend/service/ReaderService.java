package team28.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

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
}
