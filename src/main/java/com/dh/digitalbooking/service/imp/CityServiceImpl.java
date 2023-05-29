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
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    private final CountryService countryService;
    private final ProductoServiceImp productoServiceImp;
    private final CityMapper cityMapper;

    public CityServiceImpl(CityRepository cityRepository, CountryService countryService, @Lazy ProductoServiceImp productoServiceImp, CityMapper cityMapper) {
        this.cityRepository = cityRepository;
        this.countryService = countryService;
        this.productoServiceImp = productoServiceImp;
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
    public CityFullDto saveCity(CityRequest cityRequest) {
        Country country = countryService.countryExistsById(cityRequest.countryId());
        City city = City.builder().name(cityRequest.name()).country(country).build();
        return cityMapper.cityToCityFullDto(cityRepository.save(city));
    }

    @Override
    public void deleteCity(Long id) {
        existByIdValidation(id);
        if(productoServiceImp.existByCityId(id))
            throw new BadRequestException("You cannot delete the city with id " + id + " because there are products registered in that city");
        cityRepository.deleteById(id);
    }

    @Override
    public CityFullDto updateCity(Long id, CityRequest cityRequest) {
        existByIdValidation(id);
        Country country = countryService.countryExistsById(cityRequest.countryId());
        City city = City.builder().id(id).name(cityRequest.name()).country(country).build();
        return cityMapper.cityToCityFullDto(cityRepository.save(city));
    }

    @Override
    public boolean existsCityByCountryId(Long id) {
        return cityRepository.existsByCountry_Id(id);
    }

    @Override
    public City existByIdValidation(Long id) {
        return cityRepository.findById(id).orElseThrow(() ->
                new NotFoundException("City with id  " + id + " not found"));
    }
}
