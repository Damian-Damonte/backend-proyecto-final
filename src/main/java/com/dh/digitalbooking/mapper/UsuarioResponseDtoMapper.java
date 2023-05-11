package com.dh.digitalbooking.mapper;

import com.dh.digitalbooking.dto.UsuarioResponseDto;
import com.dh.digitalbooking.entity.Usuario;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioResponseDtoMapper {

    public UsuarioResponseDto toUsuarioResponseDto(Usuario usuario) {
        UsuarioResponseDto usuarioResponseDto = new UsuarioResponseDto();
        usuarioResponseDto.setId(usuario.getId());
        usuarioResponseDto.setNombre(usuario.getNombre());
        usuarioResponseDto.setApellido(usuario.getApellido());
        usuarioResponseDto.setEmail(usuario.getEmail());
        usuarioResponseDto.setCiudad(usuario.getCiudad());
        usuarioResponseDto.setRol(usuario.getRol());
        usuarioResponseDto.setReservas(usuario.getBookings());
        usuarioResponseDto.setFavoritos(usuario.getFavoritos());

        return usuarioResponseDto;
    }

    public Usuario toUsuario(UsuarioResponseDto dto) {
        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setCiudad(dto.getCiudad());
        usuario.setRol(dto.getRol());
        usuario.setFavoritos(dto.getFavoritos());
        return usuario;
    }

    public List<UsuarioResponseDto> toListUsuarioResponseDto(List<Usuario> usuarios) {
        return usuarios.stream().map(this::toUsuarioResponseDto).collect(Collectors.toList());
    }
}
