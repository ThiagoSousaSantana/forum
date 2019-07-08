package br.com.alura.forum.config.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AutenticacaoFilter extends OncePerRequestFilter {


    private TokenService tokenService;

    AutenticacaoFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {

        String token = recuperarToken(httpServletRequest);
        boolean valido = tokenService.isTokenValido(token);

        if (valido) autenticarRequest(token);

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void autenticarRequest(String token) {
        var usuario = tokenService.getUsuarioFrom(token);
        var autentication = new UsernamePasswordAuthenticationToken(usuario, usuario.getSenha(), usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(autentication);
    }

    private String recuperarToken(HttpServletRequest request) {
        var token = request.getHeader("Authorization");

        if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) return null;
        return token.substring(7);
    }
}
