package br.com.alura.forum.config.security;

import br.com.alura.forum.model.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService implements UserDetailsService {

    private UsuarioRepository repository;

    @Autowired
    public AutenticacaoService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Usuario loadUserByUsername(String email) throws UsernameNotFoundException {
        var usuario = repository.findByEmail(email);
        if(usuario.isPresent()) return usuario.get();
        throw new UsernameNotFoundException("Usuario ou senha invados!");
    }
}
