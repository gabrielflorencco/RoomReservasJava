package com.roomreservas.infrastructure.persistence.adapter;

import com.roomreservas.domain.entity.Usuario;
import com.roomreservas.domain.repository.UsuarioRepository;
import com.roomreservas.infrastructure.persistence.repository.UsuarioJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UsuarioRepositoryAdapter implements UsuarioRepository {

    private final UsuarioJpaRepository jpaRepository;

    @Override public Usuario salvar(Usuario usuario) { return jpaRepository.save(usuario); }
    @Override public Optional<Usuario> buscarPorId(UUID id) { return jpaRepository.findById(id); }
    @Override public Optional<Usuario> buscarPorEmail(String email) { return jpaRepository.findByEmail(email); }
    @Override public List<Usuario> listarTodos() { return jpaRepository.findAll(); }
    @Override public void deletar(UUID id) { jpaRepository.deleteById(id); }
    @Override public boolean existePorEmail(String email) { return jpaRepository.existsByEmail(email); }
}
