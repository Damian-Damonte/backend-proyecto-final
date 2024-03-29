package com.dh.digitalbooking.service.imp;

import com.dh.digitalbooking.dto.city.CityFullDto;
import com.dh.digitalbooking.dto.city.CityRequest;
import com.dh.digitalbooking.entity.City;
import com.dh.digitalbooking.exception.BadRequestException;
import com.dh.digitalbooking.exception.NotFoundException;
import com.dh.digitalbooking.entity.Country;
import com.dh.digitalbooking.mapper.CityMapper;
import com.dh.digitalbooking.repository.CityRepository;
import com.dh.digitalbooking.service.CityService;
import com.dh.digitalbooking.service.CountryService;
import com.dh.digitalbooking.service.ProductService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    private final CountryService countryService;
    private final ProductService productoService;
    private final CityMapper cityMapper;

    public CityServiceImpl(CityRepository cityRepository, @Lazy CountryService countryService, @Lazy ProductService productoService, CityMapper cityMapper) {
        this.cityRepository = cityRepository;
        this.countryService = countryService;
        this.productoService = productoService;
        this.cityMapper = cityMapper;
    }

    @Override
    public List<CityFullDto> getAllCities() {
        return cityRepository.findAll().stream().map(cityMapper::cityToCityFullDto).toList();
    }

    @Override
    public CityFullDto getCityById(Long id) {
        return cityMapper.cityToCityFullDto(existByIdValidation(id));
    }

    @Override
    @Transactional
    public CityFullDto saveCity(CityRequest cityRequest) {
        Country country = countryService.countryExistsById(cityRequest.country().id());
        String cityName = cityRequest.name();
        validNameAndCountry(cityName, country);

        City city = City.builder().name(cityName).country(country).build();
        return cityMapper.cityToCityFullDto(cityRepository.save(city));
    }

    @Override
    @Transactional
    public void deleteCity(Long id) {
        existByIdValidation(id);
        if(productoService.existByCityId(id))
            throw new BadRequestException("You cannot delete the city with id " + id + " because there are products registered in that city");
        cityRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CityFullDto updateCity(Long id, CityRequest cityRequest) {
        City city = existByIdValidation(id);
        Country country = countryService.countryExistsById(cityRequest.country().id());
        String cityName = cityRequest.name();
        validNameAndCountry(cityName, country);

        city.setName(cityName);
        city.setCountry(country);
        return cityMapper.cityToCityFullDto(city);
    }

    @Override
    public boolean existsCityByCountryId(Long id) {
        return cityRepository.existsByCountry_Id(id);
    }

    public void validNameAndCountry(String cityName, Country country) {
        if(cityRepository.existsByNameAndCountry(cityName, country))
            throw new BadRequestException("There is already a city named " + cityName + " in " + country.getName());

    }

    @Override
    public City existByIdValidation(Long id) {
        return cityRepository.findById(id).orElseThrow(() ->
                new NotFoundException("City with id  " + id + " not found"));
    }
}
