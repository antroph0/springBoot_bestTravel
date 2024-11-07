package com.debbugeando_ideas.best_travel.infraestructure.services;

import com.debbugeando_ideas.best_travel.api.models.responses.FlyResponse;
import com.debbugeando_ideas.best_travel.configs.WebClientConfig;
import com.debbugeando_ideas.best_travel.domain.entities.FlyEntity;
import com.debbugeando_ideas.best_travel.domain.repositories.FlyRepository;
import com.debbugeando_ideas.best_travel.infraestructure.abstract_services.IFlyService;
import com.debbugeando_ideas.best_travel.util.enums.SortType;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class FlyService implements IFlyService {

    private final FlyRepository flyRepository;
    private final WebClient webClient;

    public FlyService(FlyRepository flyRepository, @Qualifier(value = "currency") WebClient webClient) {
        this.flyRepository = flyRepository;
        this.webClient = webClient;
    }

    @Override
    public Page<FlyResponse> readAll(Integer page, Integer size, SortType sortType) {
        PageRequest pageRequest = null;
        switch (sortType){
            case NONE -> pageRequest = PageRequest.of(page, size);
            case LOWER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_VY_SORT).ascending());
            case UPPER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_VY_SORT).descending());

        }
        return this.flyRepository.findAll(pageRequest).map(this::entityToResponse);
    }

    @Override
    public Set<FlyResponse> readByOriginDestiny(String origin, String destiny) {
        return this.flyRepository.selectOriginDestiny(origin, destiny)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }



    @Override
    public Set<FlyResponse> readLessPrice(BigDecimal price) {

        return this.flyRepository.selectLessPrice(price)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<FlyResponse> readBetweenPrice(BigDecimal min, BigDecimal max) {

        return this.flyRepository.selectBetweenPrice(min, max)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    //Utility to response.

    private FlyResponse entityToResponse(FlyEntity entity){
        FlyResponse response = new FlyResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }
}
