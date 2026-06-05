package com.estoque.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Este componente implementa o "Controlador" da arquitetura Blackboard.
 * Ele não decide nada por si só — apenas fica escutando o banco de dados
 * e, quando o trigger dispara, acorda o EspecialistaReposicaoService.
 */
@Component
public class PgNotificationListener implements ApplicationRunner {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private EspecialistaReposicaoService especialistaReposicao;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void run(ApplicationArguments args) {
        // Roda em uma thread separada para não bloquear a aplicação
        Thread listenerThread = new Thread(this::escutarNotificacoes);
        listenerThread.setDaemon(true);
        listenerThread.setName("pg-notification-listener");
        listenerThread.start();
    }

    private void escutarNotificacoes() {
        try (Connection conn = dataSource.getConnection()) {

            // Registra no canal de notificações do PostgreSQL
            PGConnection pgConn = conn.unwrap(PGConnection.class);
            conn.createStatement().execute("LISTEN reposicao_necessaria");

            System.out.println("[BLACKBOARD] Listener ativo — aguardando notificações de reposição...");

            // Loop infinito: verifica notificações a cada 500ms
            while (!Thread.currentThread().isInterrupted()) {

                PGNotification[] notifications = pgConn.getNotifications(500);

                if (notifications != null) {
                    for (PGNotification notification : notifications) {
                        processarNotificacao(notification.getParameter());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[BLACKBOARD] Erro no listener: " + e.getMessage());
        }
    }

    private void processarNotificacao(String payload) {
        try {
            // O payload é um JSON com produto_id e loja_id
            JsonNode json = objectMapper.readTree(payload);
            Long produtoId = json.get("produto_id").asLong();
            Long lojaId = json.get("loja_id").asLong();

            System.out.println("[BLACKBOARD] Notificação recebida! Produto ID: "
                + produtoId + " | Loja ID: " + lojaId);
            System.out.println("[BLACKBOARD] Acordando Especialista de Reposição...");

            // Acorda o Especialista de Reposição (o Knowledge Source)
            especialistaReposicao.analisarReposicao(produtoId, lojaId);

        } catch (Exception e) {
            System.err.println("[BLACKBOARD] Erro ao processar notificação: " + e.getMessage());
        }
    }
}