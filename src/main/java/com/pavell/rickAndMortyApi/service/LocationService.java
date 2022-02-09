package com.pavell.rickAndMortyApi.service;

import com.pavell.rickAndMortyApi.cache.LocationCache;
import com.pavell.rickAndMortyApi.dto.location.LocationDTO;
import com.pavell.rickAndMortyApi.dto.location.PageLocation;
import com.pavell.rickAndMortyApi.entity.Location;
import com.pavell.rickAndMortyApi.repo.LocationRepo;
import com.pavell.rickAndMortyApi.response.LocationResponse;
import com.pavell.rickAndMortyApi.response.common.InfoResponse;
import com.pavell.rickAndMortyApi.response.common.PageResponse;
import com.pavell.rickAndMortyApi.specification.SearchCriteriaLocation;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static com.pavell.rickAndMortyApi.specification.LocationSpecification.findByCriteria;
import static com.pavell.rickAndMortyApi.utils.Constants.*;
import static com.pavell.rickAndMortyApi.utils.InfoUtils.createInfoResponse;
import static com.pavell.rickAndMortyApi.utils.ParamsBuilder.isSinglePage;
import static com.pavell.rickAndMortyApi.utils.ParamsBuilder.setRequestParamsToPrevAndNext;

@Service
public class LocationService {
    final static Logger LOGGER = Logger.getLogger(LocationService.class);


    private ModelMapper modelMapper = new ModelMapper();

    private LocationRepo locationRepo;

    //TODO: Get all location from cache
    @Autowired
    private LocationCache locationCache;

    public LocationService(LocationRepo locationRepo) {
        this.locationRepo = locationRepo;
    }

    public Iterable<Location> list() {
        LOGGER.info(LocationService.class.getName() + " got all episodes ");
        return locationRepo.findAll();
    }

    public Location save(Location location) {
        LOGGER.info(LocationService.class.getName() + " saved episode with name " + location.getName());
        return locationRepo.save(location);
    }

    public void save(List<Location> locations) {
        LOGGER.info(LocationService.class.getName() + " saved all episodes");
        locationRepo.saveAll(locations);
    }


    public void loadData(RestTemplate restTemplate) {
        PageLocation pageLocation = restTemplate.getForObject(RESOURCE_LOCATION_URL, PageLocation.class);
        LOGGER.info(LocationService.class.getName() + " RestTemplate getForObject  with url " + RESOURCE_LOCATION_URL);

        List<PageLocation> pageLocationList = new ArrayList<>();
        while (true) {
            pageLocationList.add(pageLocation);
            pageLocation = restTemplate.getForObject(pageLocation.getInfo().getNext(), PageLocation.class);
            if (Objects.isNull(pageLocation) ||
                    Objects.isNull(pageLocation.getInfo()) ||
                    Objects.isNull(pageLocation.getInfo().getNext())) {
                LOGGER.info(LocationService.class.getName() + " RestTemplate getForObject  with url null");
            } else {
                LOGGER.info(LocationService.class.getName() + " RestTemplate getForObject  with url " + pageLocation.getInfo().getNext());

            }
            if (pageLocation.getInfo().getNext() == null) {
                pageLocationList.add(pageLocation);
                break;
            }
        }

        ArrayList<Location> locations = new ArrayList<Location>();
        pageLocationList.forEach(pageLocationElement -> {
            List<LocationDTO> results = pageLocationElement.getResults();
            results.forEach(result -> {
                Location location = modelMapper.map(result, Location.class);
                locations.add(location);
            });
        });
        save(locations);
    }

    public PageResponse getPage(Long page) {
        if (page == null) page = 1L;
        Page<Location> locationPage = locationRepo.findAll(PageRequest.of(page.intValue() - 1, SIZE));
        PageResponse pageResponse = parseToPageResponse(locationPage);

        InfoResponse info = createInfoResponse(locationPage);
        setPrevAndNextToInfo(info, locationPage, page);

        pageResponse.setInfo(info);

        return pageResponse;
    }

    public LocationResponse getLocationById(Long id) {
        Optional<Location> optionalEpisode = locationRepo.findById(id);
        if (optionalEpisode.isPresent()) {
            return modelMapper.map(optionalEpisode.get(), LocationResponse.class);
        } else {
            //TODO:return exception
            return new LocationResponse();
        }
    }

    public List<LocationResponse> getLocationsByIds(String[] ids) {
        return Arrays.stream(ids)
                .map(id -> locationRepo.findById(Long.valueOf(id)).orElseGet(null))
                .filter(Objects::nonNull)
                .map(location -> modelMapper.map(location, LocationResponse.class))
                .collect(Collectors.toList());
    }

    public PageResponse getFilteredPage(String type, String name, String dimension, Long page) {
        if (page == null) page = 1L;
        Specification<Location> specification =
                findByCriteria(new SearchCriteriaLocation(type, name, dimension));
        Page<Location> pageEntity = locationRepo.findAll(specification, PageRequest.of(page == null ? 0 : (int) (page - 1), SIZE));

        PageResponse pageResponse = parseToPageResponse(pageEntity);

        InfoResponse info = createInfoResponse(pageEntity);
        Map<String, String> paramsMap = createParamsMap(page, name, type, dimension);

        setPrevAndNextToInfoFiltered(info, pageEntity, page);
        setRequestParamsToPrevAndNext(info, paramsMap);

        pageResponse.setInfo(info);

        return pageResponse;

    }

    private Map<String, String> createParamsMap(Long page, String name, String type, String dimension) {

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("page", page.toString());
        paramsMap.put("name", name);
        paramsMap.put("dimension", dimension);
        paramsMap.put("type", type);

        return paramsMap;
    }

    private void setPrevAndNextToInfoFiltered(InfoResponse info, Page<Location> pageEntity, Long page) {
        String next;
        String prev;
        if (page == null || pageEntity.getTotalPages() == page) {
            next = null;
        } else {
            next = LOCATION_URL;
        }
        if (page == null || page == 2) {
            prev = LOCATION_URL;
        } else if (page == 1) {
            prev = null;
        } else {
            prev = LOCATION_URL;
        }

        info.setNext(next);
        info.setPrev(prev);

        isSinglePage(pageEntity.getTotalPages(), info);
        isMoreThenAllPagesFiltered(pageEntity, info, page);
    }

    private void isMoreThenAllPagesFiltered(Page<Location> pageEntity, InfoResponse info, Long page) {
        if (pageEntity.getTotalPages() < page || pageEntity.getTotalPages() == page) {
            info.setNext(null);
        }
        if (pageEntity.getTotalPages() == page || (pageEntity.getTotalPages() > page && page != 1)) {
            info.setPrev(LOCATION_URL);
        } else if ((pageEntity.getTotalPages() + 1) == page) {
            info.setPrev(LOCATION_URL);
        } else if (page == 2) {
            info.setPrev(LOCATION_URL);
        } else {
            info.setPrev(null);
        }
    }

    private PageResponse parseToPageResponse(Page<Location> page) {
        List<LocationResponse> resultList = new ArrayList<>();
        page.get().forEach(episode -> resultList.add(modelMapper.map(episode, LocationResponse.class)));

        PageResponse pageEpisode = new PageResponse();
        pageEpisode.setResults(resultList);
        return pageEpisode;
    }

    private void setPrevAndNextToInfo(InfoResponse info, Page<Location> locationPage, Long page) {
        String next = null;
        String prev = null;
        if (page == null || locationPage.getTotalPages() == page) {
            next = null;
        } else {
            next = LOCATION_URL + REQUEST_PARAM_PAGE_DELIMITER + (page + 1);
        }
        if (page == null || page == 2) {
            prev = LOCATION_URL;
        } else if (page == 1) {
            prev = null;
        } else {
            prev = LOCATION_URL + REQUEST_PARAM_PAGE_DELIMITER + (page - 1);
        }

        info.setNext(next);
        info.setPrev(prev);
        isSinglePage(locationPage.getTotalPages(), info);
    }
}
