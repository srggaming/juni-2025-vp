package mk.ukim.finki.wp.june2025g1.service.impl;

import mk.ukim.finki.wp.june2025g1.model.Founder;
import mk.ukim.finki.wp.june2025g1.model.exceptions.InvalidFounderIdException;
import mk.ukim.finki.wp.june2025g1.repository.FounderRepository;
import mk.ukim.finki.wp.june2025g1.service.FounderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FounderServiceImpl implements FounderService {

    private final FounderRepository founderRepository;

    public FounderServiceImpl(FounderRepository founderRepository) {
        this.founderRepository = founderRepository;
    }

    @Override
    public Founder findById(Long id) {
        return founderRepository.findById(id)
                .orElseThrow(InvalidFounderIdException::new);
    }

    @Override
    public List<Founder> listAll() {
        return founderRepository.findAll();
    }

    @Override
    public Founder create(String name, String email) {
        Founder founder = new Founder(name, email);
        return founderRepository.save(founder);
    }
}
