package br.com.curso.listadetarefas.api.tarefa;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Comparator;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class TarefaWebController {

    private final TarefaService tarefaService;

    public TarefaWebController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @GetMapping
    public String index(Model model) {
        var tarefasOrdenadas = tarefaService.listarTodas()
                .stream()
                .sorted(Comparator.comparing(Tarefa::getId))
                .collect(Collectors.toList());
        model.addAttribute("tarefas", tarefasOrdenadas);
        return "index";
    }

    @PostMapping("/tarefas")
    public String criarTarefa(@RequestParam String titulo, Model model) {
        Tarefa nova = new Tarefa();
        nova.setTitulo(titulo);
        nova.setDescricao("");
        nova.setConcluida(false);
        var salva = tarefaService.criarTarefa(nova);
        model.addAttribute("tarefa", salva);
        return "fragments :: linha-tarefa";
    }

    @DeleteMapping("/tarefas/{id}")
    @ResponseBody
    public void deletar(@PathVariable Long id) {
        tarefaService.deletarTarefa(id);
    }

    @PostMapping("/tarefas/{id}/toggle")
    public String toggle(@PathVariable Long id, Model model) {
        tarefaService.findById(id).ifPresent(t -> {
            t.setConcluida(!t.isConcluida());
            tarefaService.atualizarTarefa(id, t);
            model.addAttribute("tarefa", t);
        });
        return "fragments :: linha-tarefa";
    }
}
