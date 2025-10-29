package com.tekup.multi_channel_notification_management_system.business.pattern;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Flyweight Factory Pattern
 * Manages and reuses MessageTemplate instances
 */
@Slf4j
@Component
public class MessageTemplateFactory {

    private final Map<String, MessageTemplate> templates = new ConcurrentHashMap<>();

    public MessageTemplate getTemplate(String templateType) {
        return templates.computeIfAbsent(templateType, this::createTemplate);
    }

    private MessageTemplate createTemplate(String type) {
        log.info("Creating new template: {}", type);

        return switch (type.toUpperCase()) {
            case "WELCOME" -> new MessageTemplate(
                    "WELCOME",
                    "Bienvenue {userName}",
                    "Merci de vous être inscrit sur {platformName}. " +
                            "Nous sommes ravis de vous compter parmi nous!",
                    "HTML"
            );

            case "ORDER_CONFIRM" -> new MessageTemplate(
                    "ORDER_CONFIRM",
                    "Commande #{orderId} confirmée",
                    "Votre commande d'un montant de {amount} a été confirmée. " +
                            "Date de livraison estimée: {deliveryDate}",
                    "HTML"
            );

            case "PASSWORD_RESET" -> new MessageTemplate(
                    "PASSWORD_RESET",
                    "Réinitialisation de mot de passe",
                    "Bonjour {userName}, utilisez le code suivant pour " +
                            "réinitialiser votre mot de passe: {resetCode}. " +
                            "Ce code expire dans {expirationTime} minutes.",
                    "TEXT"
            );

            case "PAYMENT_SUCCESS" -> new MessageTemplate(
                    "PAYMENT_SUCCESS",
                    "Paiement réussi",
                    "Votre paiement de {amount} pour {productName} " +
                            "a été effectué avec succès. Transaction ID: {transactionId}",
                    "HTML"
            );

            case "ACCOUNT_VERIFICATION" -> new MessageTemplate(
                    "ACCOUNT_VERIFICATION",
                    "Vérification de compte",
                    "Bonjour {userName}, votre code de vérification est: {verificationCode}",
                    "TEXT"
            );

            default -> new MessageTemplate(
                    "DEFAULT",
                    "Notification",
                    "Vous avez reçu une nouvelle notification.",
                    "TEXT"
            );
        };
    }

    public int getTemplateCount() {
        return templates.size();
    }

    public void clearCache() {
        log.info("Clearing template cache. Templates removed: {}", templates.size());
        templates.clear();
    }
}
