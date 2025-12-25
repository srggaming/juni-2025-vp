package mk.ukim.finki.wp.june2025g1.service.impl;

import mk.ukim.finki.wp.june2025g1.model.Founder;
import mk.ukim.finki.wp.june2025g1.model.Industry;
import mk.ukim.finki.wp.june2025g1.model.Startup;
import mk.ukim.finki.wp.june2025g1.model.exceptions.InvalidStartupIdException;
import mk.ukim.finki.wp.june2025g1.repository.StartupRepository;
import mk.ukim.finki.wp.june2025g1.service.FounderService;
import mk.ukim.finki.wp.june2025g1.service.StartupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StartupServiceImpl implements StartupService {

    private final StartupRepository startupRepository;
    private final FounderService founderService;

    public StartupServiceImpl(StartupRepository startupRepository, FounderService founderService) {
        this.startupRepository = startupRepository;
        this.founderService = founderService;
    }

    @Override
    public List<Startup> listAll() {
        return startupRepository.findAll();
    }

    @Override
    public Startup findById(Long id) {
        return startupRepository.findById(id)
                .orElseThrow(InvalidStartupIdException::new);
    }

    @Override
    public Startup create(String name, Double valuation, Integer yearFounded, Industry industry, Long founderId) {
        Founder founder = founderService.findById(founderId);
        Startup startup = new Startup(name, valuation, yearFounded, industry, founder);
        return startupRepository.save(startup);
    }

    @Override
    public Startup update(Long id, String name, Double valuation, Integer yearFounded, Industry industry, Long founderId) {
        Startup startup = findById(id);
        Founder founder = founderService.findById(founderId);
        startup.setName(name);
        startup.setValuation(valuation);
        startup.setYearFounded(yearFounded);
        startup.setIndustry(industry);
        startup.setFounder(founder);
        return startupRepository.save(startup);
    }

    @Override
    public Startup delete(Long id) {
        Startup startup = findById(id);
        startupRepository.delete(startup);
        return startup;
    }

    @Override
    public Startup deactivate(Long id) {
        Startup startup = findById(id);
        startup.setActive(false);
        return startupRepository.save(startup);
    }

    @Override
    public Page<Startup> findPage(String name, Double valuation, Integer yearFounded, Industry industry, Long founderId, int pageNum, int pageSize) {
        Specification<Startup> spec = Specification.where(null);

        if (name != null && !name.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("name"), "%" + name + "%"));
        }
        if (valuation != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThan(root.get("valuation"), valuation));
        }
        if (yearFounded != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThan(root.get("yearFounded"), yearFounded));
        }
        if (industry != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("industry"), industry));
        }
        if (founderId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("founder").get("id"), founderId));
        }

        return startupRepository.findAll(spec, PageRequest.of(pageNum, pageSize));
    }
}
