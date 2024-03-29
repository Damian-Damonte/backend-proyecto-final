package com.dh.digitalbooking.service.imp;

import com.dh.digitalbooking.dto.product.*;
import com.dh.digitalbooking.dto.user.UserDetailsSlim;
import com.dh.digitalbooking.exception.BadRequestException;
import com.dh.digitalbooking.exception.ForbiddenException;
import com.dh.digitalbooking.exception.NotFoundException;
import com.dh.digitalbooking.entity.*;
import com.dh.digitalbooking.mapper.ProductMapper;
import com.dh.digitalbooking.repository.ProductRepository;
import com.dh.digitalbooking.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final CityService cityService;
    private final AmenityService amenityService;
    private final PolicyTypeService policyTypeService;
    private final ImageService imageService;
    private final PolicyService policyService;
    private final ProductMapper productMapper;
    private final UserService userService;
    private final AuthenticationUserService authenticationUserService;

    public ProductServiceImp(ProductRepository productRepository, CategoryService categoryService, CityService cityService, AmenityService amenityService, PolicyTypeService policyTypeService, ImageService imageService, PolicyService policyService, ProductMapper productMapper, UserService userService, AuthenticationUserService authenticationUserService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.cityService = cityService;
        this.amenityService = amenityService;
        this.policyTypeService = policyTypeService;
        this.imageService = imageService;
        this.policyService = policyService;
        this.productMapper = productMapper;
        this.userService = userService;
        this.authenticationUserService = authenticationUserService;
    }

    @Override
    public List<ProductResponse> getAllProductNoPage() {
        return productRepository.findAll().stream().map(productMapper::productToProductResponse).toList();
    }

    @Override
    public ProductPage getWithFilters(int page, Long cityId, Long categoryId, LocalDate checkIn, LocalDate checkout) {
        filtersValidations(cityId, categoryId, checkIn, checkout);
        PageRequest pageRequest = PageRequest.ofSize(4).withPage(page);
        Page<Product> productoPage = productRepository.findAllFilters(cityId, categoryId, checkIn, checkout, pageRequest);
        return toProductPageDto(productoPage);
    }

    @Override
    public List<ProductResponse> getRandomProducts() {
        return productRepository.findRandom().stream().map(productMapper::productToProductResponse).toList();
    }

    @Override
    public ProductFullDto getProductById(Long id) {
        return productMapper.productToProductFullDto(existById(id));
    }

    @Override
    @Transactional
    public ProductResponse saveProduct(ProductRequest productRequest, Authentication authentication) {
        User user = userService.existById(authenticationUserService.getUserDetailsFromAuthentication(authentication).id());
        Product product = productMapper.productRequestToProduct(productRequest);
        product.setCategory(categoryService.existByIdValidation(product.getCategory().getId()));
        product.setCity(cityService.existByIdValidation(product.getCity().getId()));
        product.getImages().forEach(image -> image.setProduct(product));

        product.setAmenities(product.getAmenities().stream().map(amenity ->
                amenityService.existByIdValidation(amenity.getId())).collect(Collectors.toSet()));

        product.setPolicies(product.getPolicies().stream().peek(policy -> {
            policy.setPolicyType(policyTypeService.existById(policy.getPolicyType().getId()));
            policy.setProduct(product);
        }).collect(Collectors.toSet()));

        product.setBookings(new HashSet<>());
        product.setUser(user);

        user.addProduct(product);
        categoryService.incrementCount(productRequest.category().id());
        return productMapper.productToProductResponse(productRepository.save(product));
    }

    @Transactional
    @Override
    public void deleteProduct(Long id, Authentication authentication) {
        Product product = existById(id);
        canModifyProduct(product, authentication);

        boolean anyActiveBooking = product.getBookings().stream().anyMatch(booking -> (booking.getCheckOut().isAfter(LocalDate.now()) || booking.getCheckOut().isEqual(LocalDate.now())));
        if(anyActiveBooking) throw new BadRequestException("The product with ID " + id + " cannot be deleted as it is currently booked");

        Set<User> usuariosFav = product.getFavorites();
        usuariosFav.forEach(user -> user.removeFav(product));

        categoryService.decrementCount(product.getCategory().getId());
        productRepository.deleteById(id);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductUpdate productUpdateRequest, Authentication authentication) {
        Product product = existById(id);
        canModifyProduct(product, authentication);

        Product productUpdate = productMapper.productUpdateToProduct(productUpdateRequest);
        productUpdate.setId(id);
        productUpdate.setCategory(categoryService.existByIdValidation(productUpdate.getCategory().getId()));
        productUpdate.setCity(cityService.existByIdValidation(productUpdate.getCity().getId()));

        productUpdate.setAmenities(productUpdate.getAmenities().stream().map(amenity ->
                amenityService.existByIdValidation(amenity.getId())).collect(Collectors.toSet()));

        productUpdate.getImages().forEach(image -> {
            if(image.getId() != null) {
                Image saveImage = imageService.getImageById(image.getId());
                if(!Objects.equals(saveImage.getProduct().getId(), id))
                    throw new BadRequestException("The image with id " + image.getId() + " not found in product with id " + id);
            }
            image.setProduct(productUpdate);
        });

        productUpdate.getPolicies().forEach(policy -> {
            if(policy.getId() != null) {
                Policy savePolicy = policyService.getPolicyById(policy.getId());
                if(!Objects.equals(savePolicy.getProduct().getId(), id))
                    throw new BadRequestException("The policy with id " + policy.getId() + " not found in product with id " + id);
            }
            policy.setProduct(productUpdate);
        });

        productUpdate.setAverageRating(product.getAverageRating());
        productUpdate.setBookings(product.getBookings());
        productUpdate.setRatings(product.getRatings());
        productUpdate.setFavorites(product.getFavorites());
        productUpdate.setUser(product.getUser());

        if (!(productUpdate.getCategory().getId().equals(product.getCategory().getId()))) {
            categoryService.decrementCount(product.getCategory().getId());
            categoryService.incrementCount(productUpdate.getCategory().getId());
        }

        return productMapper.productToProductResponse(productRepository.save(productUpdate));
    }

    @Override
    public boolean existByCityId(Long id) {
        return productRepository.existsByCity_id(id);
    }

    public Product existById(Long id) {
        return productRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Product with id " + id + " not found"));
    }

    private void filtersValidations(Long cityId, Long categoryId, LocalDate checkIn, LocalDate checkOut) {
        if (cityId != null)
            cityService.existByIdValidation(cityId);
        if (categoryId != null)
            categoryService.existByIdValidation(categoryId);
        if(checkIn != null)
            notPastDate(checkIn);
        if(checkOut != null)
            notPastDate(checkOut);
        if (checkIn != null && checkOut != null && checkIn.isAfter(checkOut))
            throw new BadRequestException("The check-out date must be after the check-in date.");
    }

    private void notPastDate(LocalDate date) {
        if (date.isBefore(LocalDate.now()))
            throw new BadRequestException("The dates must not be earlier than the current date");
    }

    private void canModifyProduct(Product product, Authentication authentication) {
        UserDetailsSlim userDetails = authenticationUserService.getUserDetailsFromAuthentication(authentication);
        if (!userDetails.role().equals("ROLE_ADMIN")) {
            if (!product.getUser().getId().equals(userDetails.id()))
                throw new ForbiddenException("You cannot modify this product as it does not belong to you");
        }
    }

    private ProductPage toProductPageDto(Page<Product> page) {
        return new ProductPage(
                page.getContent().stream().map(productMapper::productToProductResponse).toList(),
                page.getTotalPages(),
                page.getNumber(),
                page.getTotalElements()
        );
    }
}
