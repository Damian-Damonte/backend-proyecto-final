package com.dh.digitalbooking;

import com.dh.digitalbooking.dto.UserDetailsDto;
import com.dh.digitalbooking.entity.*;
import com.dh.digitalbooking.repository.*;
import com.dh.digitalbooking.service.imp.CategoryServiceImp;
import com.dh.digitalbooking.service.imp.ProductoServiceImp;
import com.dh.digitalbooking.service.imp.RatingServiceImp;
import com.dh.digitalbooking.service.imp.BookingServiceImp;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class DigitalbookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalbookingApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(
            CategoryServiceImp categoriaServiceImp,
            CountryRepository countryRepository,
            CityRepository cityRepository,
            ProductoServiceImp productoServiceImp,
            AmenityRepository amenityRepository,
            PolicyTypeRepository policyTypeRepository,
            RoleRepository roleRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            RatingServiceImp puntuacionServiceImp,
            BookingServiceImp reservaServiceImp
    ) {
        return args -> {
            Rol rolUser = roleRepository.save(new Rol("ROLE_USER"));
            Rol rolAdmin = roleRepository.save(new Rol("ROLE_ADMIN"));

            Usuario usuarioAdmin = usuarioRepository.save(new Usuario(
                    "admin",
                    "admin",
                    "admin@gmail.com",
                    passwordEncoder.encode("admin"),
                    rolAdmin
            ));

            Category hotel = categoriaServiceImp.saveCategory(new Category("Hotel", "Descripcion de la categoria Hotel", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1770&q=80%27"));
            Category hostel = categoriaServiceImp.saveCategory(new Category("Hostel", "Descripcion de la categoria Hostel", "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1769&q=80%27"));
            Category departamento = categoriaServiceImp.saveCategory(new Category("Departamento", "Descripcion de la categoria Departamento", "https://images.unsplash.com/photo-1563298723-dcfebaa392e3?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1767&q=80%27"));
//            Category bedAndBreakfast = categoriaServiceImp.saveCategoria(new Category("Bed and breakfast", "Descripcion de la categoria Bed and breakfast", "https://images.unsplash.com/photo-1463620910506-d0458143143e?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1770&q=80%27"));

            Country argentina = countryRepository.save(Country.builder().name("Argentina").build());
            Country italia = countryRepository.save(Country.builder().name("Italia").build());
            Country brasil = countryRepository.save(Country.builder().name("Brasil").build());
            Country colombia = countryRepository.save(Country.builder().name("Colobia").build());
            Country estadosUnidos = countryRepository.save(Country.builder().name("Estados unidos").build());
            Country espania = countryRepository.save(Country.builder().name("España").build());
            Country cuba = countryRepository.save(Country.builder().name("Cuba").build());
            Country japon = countryRepository.save(Country.builder().name("Japon").build());

//            City puertoIguazu = cityRepository.save(City.builder().name("Puerto Iguazú").country(argentina).build());
//            City sanMartindelosAndes = cityRepository.save(new City("San Martín de los Andes", argentina));
//            City elCalafate = cityRepository.save(new City("El Calafate", argentina));
//            City neuquen = cityRepository.save(new City("Neuquen", argentina));
            City barcelona = cityRepository.save(City.builder().name("Barcelona").country(espania).build());
//            City grado = cityRepository.save(new City("Grado", italia));
//            City ushuaia = cityRepository.save(new City("Ushuaia", argentina));
//            City buzios = cityRepository.save(new City("Buzios", brasil));
            City miami = cityRepository.save(City.builder().name("Miami").country(estadosUnidos).build());
//            City santaMa = cityRepository.save(new City("Santa María", colombia));
//            City laHabana = cityRepository.save(new City("La Habana", cuba));
//            City newyork = cityRepository.save(new City("New York", estadosUnidos));
//            City tokio = cityRepository.save(new City("Tokio", japon));
//            City buenosaires = cityRepository.save(new City("Buenos Aires", argentina));
//            City roma = cityRepository.save(new City("Roma", italia));
//            City florencia = cityRepository.save(new City("Florencia", italia));

//            Amenity wifi = caracteristicaRepository.save(new Amenity("Wifi"));
            Amenity cocina = amenityRepository.save(Amenity.builder().name("Cocina").build());
            Amenity televisor = amenityRepository.save(Amenity.builder().name("Televisor").build());
            Amenity pileta = amenityRepository.save(Amenity.builder().name("Pileta").build());
            Amenity aptoMascotas = amenityRepository.save(Amenity.builder().name("Apto mascotas").build());
            Amenity parrilla = amenityRepository.save(Amenity.builder().name("Parrilla").build());
            Amenity lavadora = amenityRepository.save(Amenity.builder().name("Lavadora").build());
            Amenity banioPrivado = amenityRepository.save(Amenity.builder().name("Baño privado").build());
            Amenity noFumar = amenityRepository.save(Amenity.builder().name("No fumar").build());
            Amenity vistaCiudad = amenityRepository.save(Amenity.builder().name("Vistas a la city").build());

            Amenity desayuno = amenityRepository.save(Amenity.builder().name("Desayuno incluido").build());
            Amenity calefaccion = amenityRepository.save(Amenity.builder().name("Calefaccion").build());
            Amenity gimnasio = amenityRepository.save(Amenity.builder().name("Gimnasio").build());
            Amenity recepcion24 = amenityRepository.save(Amenity.builder().name("Recepcion 24hs").build());
            Amenity spa = amenityRepository.save(Amenity.builder().name("Spa").build());
            Amenity seguridad24 = amenityRepository.save(Amenity.builder().name("Seguridad las 24hs").build());
            Amenity bicicletas = amenityRepository.save(Amenity.builder().name("Alquiler de bicicletas").build());
            Amenity conferencias = amenityRepository.save(Amenity.builder().name("Sala de conferencias").build());
            Amenity restaurant = amenityRepository.save(Amenity.builder().name("Restaurant").build());
            Amenity playa = amenityRepository.save(Amenity.builder().name("Acceso a la playa").build());
            Amenity infantil = amenityRepository.save(Amenity.builder().name("Zona infantil").build());
            Amenity bar = amenityRepository.save(Amenity.builder().name("Bar").build());
            Amenity cajaFuerte = amenityRepository.save(Amenity.builder().name("Caja fuerte").build());
            Amenity minibar = amenityRepository.save(Amenity.builder().name("Minibar").build());
            Amenity chimenea = amenityRepository.save(Amenity.builder().name("Chimenea").build());

            PolicyType normasDeLaCasa = policyTypeRepository.save(PolicyType.builder().name("Normas de la casa").build());
            PolicyType saludYSeguridad = policyTypeRepository.save(PolicyType.builder().name("Salud y seguridad").build());
            PolicyType politicaDeCancelacion = policyTypeRepository.save(PolicyType.builder().name("Politicas de cancelacion").build());

            Policy policy1 = Policy.builder()
                    .description("""
                            Se aplican las pautas de distanciamiento social y otras normas relacionadas con el coronavirus
                            Detector de humo
                            Depósito de seguridad
                            Camaras de seguridad
                            Matafuegos
                            """)
                    .policyType(saludYSeguridad)
                    .build();

            Policy policy2 = Policy.builder()
                    .description("""
                            Check-out: 22:00
                            No se permiten fiestas
                            No fumar
                            Check-in: 10:00
                            """)
                    .policyType(normasDeLaCasa)
                    .build();

            Policy policy3 = Policy.builder()
                    .description("Agregá las fechas de tu viaje para obtener los detalles de cancelacion de esta estadía")
                    .policyType(politicaDeCancelacion)
                    .build();

            Set<Policy> setPolicies = new HashSet<>(Set.of(policy1, policy2, policy3));

            Set<Amenity> caracteristicas1 = new HashSet<>(Set.of(televisor, desayuno, bar, noFumar));
            Set<Amenity> caracteristicas2 = new HashSet<>(Set.of(cajaFuerte, parrilla, lavadora, pileta, aptoMascotas, seguridad24));
            Set<Amenity> caracteristicas3 = new HashSet<>(Set.of(televisor, pileta, aptoMascotas, banioPrivado, vistaCiudad, parrilla, lavadora));
            Set<Amenity> caracteristicas4 = new HashSet<>(Set.of(desayuno, calefaccion, gimnasio, recepcion24, spa, noFumar, bicicletas, conferencias, restaurant));
            Set<Amenity> caracteristicas5 = new HashSet<>(Set.of(playa, infantil, bar, cajaFuerte, seguridad24, pileta, aptoMascotas, televisor));
            Set<Amenity> caracteristicas6 = new HashSet<>(Set.of( desayuno, bicicletas, televisor, playa, noFumar, aptoMascotas, restaurant));


//          FOTOS 1
            Image foto1 = new Image("Image 1", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1770&q=80%27");
            Image foto2 = new Image("Image 2", "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1769&q=80%27");
            Image foto3 = new Image("Image 3", "https://images.unsplash.com/photo-1563298723-dcfebaa392e3?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1767&q=80%27");
            Image foto4 = new Image("Image 4", "https://images.unsplash.com/photo-1463620910506-d0458143143e?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1770&q=80%27");
            Image foto5 = new Image("Image 5", "https://images.unsplash.com/photo-1455587734955-081b22074882?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8M3x8aG90ZWx8ZW58MHx8MHx8&auto=format&fit=crop&w=500&q=60");
            Image foto6 = new Image("Image 6", "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NXx8aG90ZWx8ZW58MHx8MHx8&auto=format&fit=crop&w=500&q=60", 1);

            Image foto7 = new Image("Image 5", "https://images.unsplash.com/photo-1455587734955-081b22074882?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8M3x8aG90ZWx8ZW58MHx8MHx8&auto=format&fit=crop&w=500&q=60", 1);

            Set<Image> setFotos = new HashSet<>(Set.of(foto1, foto2, foto3, foto4, foto5, foto6));
            Set<Image> setFotos2 = new HashSet<>(Set.of(foto1, foto2, foto3, foto4, foto5, foto7));

            Producto producto1 = new Producto();
            producto1.setTitulo("Hotel numero 1");
            producto1.setTituloDescripcion("Titulo descripcion hotel 1");
            producto1.setDescripcion("Esta es la descripcion del hotel numero 1 y esto es un poco de texto de relleno");
            producto1.setDireccion("Avenida Congreso 2342");
            producto1.setPrecioPorNoche(new BigDecimal("25500.00"));
            producto1.setCategoria(hotel);
            producto1.setCiudad(barcelona);
            producto1.setCaracteristicas(caracteristicas1);
            producto1.setImagenes(setFotos);
            producto1.setPoliticas(setPolicies);
            producto1.setLatitude(new BigDecimal("-38.941697761424834"));
            producto1.setLongitude(new BigDecimal("-68.05962964717754"));
            productoServiceImp.saveProducto(producto1);

            UserDetailsDto userDetailsDtoAdmin = new UserDetailsDto();
            userDetailsDtoAdmin.setUserId(1L);
            userDetailsDtoAdmin.setUserRol("ADMIN");

            Rating ratingProducto1 = new Rating();
            ratingProducto1.setValue(4);
            ratingProducto1.setProduct(producto1);
            ratingProducto1.setUser(usuarioAdmin);
            puntuacionServiceImp.saveRating(ratingProducto1, userDetailsDtoAdmin);

            Booking booking1 = new Booking();
            booking1.setCheckIn(LocalDate.of(2023, 6, 22));
            booking1.setCheckOut(LocalDate.of(2023, 6, 25));
            booking1.setArrivalTime(LocalTime.now());
            booking1.setUserCity("Buenos aires");
            booking1.setUser(usuarioAdmin);
            booking1.setProduct(producto1);
            reservaServiceImp.saveBooking(booking1, userDetailsDtoAdmin);

            Producto producto2 = new Producto();
            producto2.setTitulo("Departamento numero 1");
            producto2.setTituloDescripcion("Titulo descripcion Departamento 1");
            producto2.setDescripcion("Esta es la descripcion del Departamento numero 1 y esto es un poco de texto de relleno");
            producto2.setDireccion("Avenida Tucuman 6435");
            producto2.setPrecioPorNoche(new BigDecimal("53200.00"));
            producto2.setCategoria(departamento);
            producto2.setCiudad(miami);
            producto2.setCaracteristicas(caracteristicas2);
            producto2.setImagenes(setFotos2);
            producto2.setPoliticas(setPolicies);
            producto2.setLatitude(new BigDecimal("11.24500468403564"));
            producto2.setLongitude(new BigDecimal("-74.21162384247455"));
            productoServiceImp.saveProducto(producto2);

            Rating ratingProducto2 = new Rating();
            ratingProducto2.setValue(5);
            ratingProducto2.setProduct(producto2);
            ratingProducto2.setUser(usuarioAdmin);
            puntuacionServiceImp.saveRating(ratingProducto2, userDetailsDtoAdmin);

            Booking booking2 = new Booking();
            booking2.setCheckIn(LocalDate.of(2023, 6, 22));
            booking2.setCheckOut(LocalDate.of(2023, 6, 25));
            booking2.setArrivalTime(LocalTime.now());
            booking2.setUserCity("La Plata");
            booking2.setUser(usuarioAdmin);
            booking2.setProduct(producto2);
            reservaServiceImp.saveBooking(booking2, userDetailsDtoAdmin);
        };
    }
}
