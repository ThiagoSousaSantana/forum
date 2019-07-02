package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.TopicoBigDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.dto.TopicoForm;
import br.com.alura.forum.controller.dto.TopicoUpdateForm;
import br.com.alura.forum.model.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/topicos")
public class TopicosController {

    private final TopicoRepository repository;

    private final CursoRepository cursoRepository;

    @Autowired
    public TopicosController(TopicoRepository repository, CursoRepository cursoRepository) {
        this.repository = repository;
        this.cursoRepository = cursoRepository;
    }

    @GetMapping
    public Page list(
            @RequestParam(required = false) String nomeCurso,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy
    ){
        Pageable paginacao = PageRequest.of(page,size, Sort.Direction.DESC, sortBy);
        Page<Topico> topicos;
        System.out.println(nomeCurso);
        if (nomeCurso != null) topicos = repository.findByCursoNome(nomeCurso, paginacao);
        else topicos = repository.findAll(paginacao);
        return TopicoDto.convert(topicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicoBigDto> details(@PathVariable Long id){
        var optional = repository.findById(id);
        if (optional.isPresent())
            return ResponseEntity.ok(new TopicoBigDto(optional.get()));
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<TopicoDto> insert(@RequestBody @Valid TopicoForm form,
                                            UriComponentsBuilder uriBuilder){
        var topico = form.convert(cursoRepository);
        repository.save(topico);

        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoDto> update(@PathVariable Long id, @RequestBody @Valid TopicoUpdateForm form){
        var topico = form.update(id, repository);
        return ResponseEntity.ok(new TopicoDto(topico));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
