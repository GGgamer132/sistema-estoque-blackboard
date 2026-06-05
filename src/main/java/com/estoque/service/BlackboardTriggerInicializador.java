package com.estoque.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Responsável por criar o trigger PostgreSQL que representa o "sensor" do Blackboard.
 * @EventListener(ApplicationReadyEvent.class) garante que esse código só
 * roda DEPOIS que todas as tabelas já foram criadas pelo Hibernate.
 * Se rodasse antes, o trigger tentaria referenciar a tabela "estoque"
 * que ainda não existiria.
 */
@Component
public class BlackboardTriggerInicializador {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void criarTrigger() {
        try {
            System.out.println("[BLACKBOARD] Inicializando trigger de monitoramento...");

            jdbcTemplate.execute(
                "DROP TRIGGER IF EXISTS trigger_verifica_estoque_critico ON estoque"
            );
            jdbcTemplate.execute(
                "DROP FUNCTION IF EXISTS verificar_reposicao()"
            );
            jdbcTemplate.execute(
                "CREATE OR REPLACE FUNCTION verificar_reposicao() " +
                "RETURNS TRIGGER AS $$ " +
                "DECLARE " +
                "    v_limiar INTEGER; " +
                "BEGIN " +
                "    SELECT limiar_critico INTO v_limiar " +
                "    FROM produto " +
                "    WHERE id = NEW.produto_id; " +
                "    IF NEW.quantidade_atual < v_limiar AND OLD.quantidade_atual >= v_limiar THEN " +
                "        PERFORM pg_notify( " +
                "            'reposicao_necessaria', " +
                "            json_build_object( " +
                "                'produto_id', NEW.produto_id, " +
                "                'loja_id', NEW.loja_id, " +
                "                'quantidade_atual', NEW.quantidade_atual, " +
                "                'limiar_critico', v_limiar " +
                "            )::text " +
                "        ); " +
                "    END IF; " +
                "    RETURN NEW; " +
                "END; " +
                "$$ LANGUAGE plpgsql"
            );

            jdbcTemplate.execute(
                "CREATE TRIGGER trigger_verifica_estoque_critico " +
                "AFTER UPDATE ON estoque " +
                "FOR EACH ROW " +
                "EXECUTE FUNCTION verificar_reposicao()"
            );

            System.out.println("[BLACKBOARD] Trigger criado com sucesso! " +
                "Sistema pronto para monitorar o estoque.");

        } catch (Exception e) {
            System.err.println("[BLACKBOARD] Erro ao criar trigger: " + e.getMessage());
        }
    }
}