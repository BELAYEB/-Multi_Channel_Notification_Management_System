# 🔔 Multi-Channel Notification Management System

## Architecture Logicielle - Projet Académique

**Étudiant:** Mahmoud BELAYEB  
**Email:** mahmoud.belayeb@tek-up.de  
**Module:** Architecture Logicielle  
**Date:** Octobre 2025  
**Établissement:** TEK-UP University

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue)](LICENSE)

---

## 📋 Table des Matières

1. [Présentation du Projet](#-présentation-du-projet)
2. [Style Architectural](#-style-architectural)
3. [Diagramme de Classes](#-diagramme-de-classes)
4. [Diagramme de Packages](#-diagramme-de-packages)
5. [Design Patterns Implémentés](#-design-patterns-implémentés)
6. [Principes SOLID](#-principes-solid)
7. [Technologies Utilisées](#-technologies-utilisées)


---

## 🎯 Présentation du Projet

### Description

Système complet de gestion de notifications multi-canal permettant l'envoi de messages via **Email**, **SMS** et **Push Notifications**. Le projet applique les meilleures pratiques architecturales, les design patterns du Gang of Four (GoF), et respecte rigoureusement les principes SOLID.

### Objectifs Pédagogiques

- ✅ Concevoir et implémenter une **architecture en couches** (Layered Architecture)
- ✅ Appliquer **deux design patterns GoF** :
  - **Chain of Responsibility** pour le routage des notifications
  - **Flyweight** pour l'optimisation mémoire
- ✅ Respecter les **5 principes SOLID**
- ✅ Produire des **diagrammes UML** clairs et cohérents
- ✅ Développer une application fonctionnelle avec tests réels

### Fonctionnalités Principales

#### 📧 Multi-Canal
- **Email** : Envoi via SMTP (Gmail) avec support HTML
- **SMS** : [Envoi via API Twilio]
- **Push** : Notifications via Firebase Cloud Messaging

#### 🎨 Gestion des Templates
- Templates réutilisables (Welcome, Order Confirmation, Password Reset, etc.)
- Cache intelligent pour optimisation mémoire (Pattern Flyweight)

#### 🔄 Routage Automatique
- Sélection automatique du canal via Chain of Responsibility
- Gestion intelligente des erreurs

#### 📊 API REST
- Endpoints RESTful pour toutes les opérations
- Statistiques en temps réel

---

## 🏗️ Style Architectural

### Architecture en Couches (Layered Architecture)

Le projet adopte une **architecture en couches** stricte, organisant le code en 4 couches distinctes avec des responsabilités clairement définies.


capture architecture


### Justification Architecturale

#### 1. Séparation des Responsabilités (Separation of Concerns)

Chaque couche a un rôle unique et bien défini :

- **Presentation** : Gère uniquement la communication HTTP/REST
- **Business** : Contient toute la logique métier
- **Persistence** : Responsable de l'accès aux données
- **Infrastructure** : Isole les dépendances externes

**Avantage** : Modifications localisées sans impact sur les autres couches.

#### 2. Principe de Dépendance Unidirectionnelle

Presentation → Business → Persistence → Infrastructure


Les dépendances vont uniquement vers le bas. Aucune couche inférieure ne connaît les couches supérieures.

**Avantage** : Couplage faible, testabilité maximale.

#### 3. Inversion de Dépendances (Dependency Inversion)

Les couches supérieures dépendent d'**abstractions** (interfaces), pas d'implémentations concrètes.

```java
public class NotificationService {
  private final NotificationRepository repository; // Interface
  private final EmailService emailService; // Interface
  private final MessageTemplateFactory templateFactory; // Abstraction
}
```


**Avantage** : Changement d'implémentation transparent (MySQL → PostgreSQL, Gmail → SendGrid).

#### 4. Maintenabilité et Évolutivité

- **Ajout de nouveaux canaux** : Créer un handler dans Business Layer
- **Changement de base de données** : Modifier uniquement Persistence Layer
- **Nouvelle API REST** : Ajouter un controller dans Presentation Layer
- **Service externe différent** : Remplacer dans Infrastructure Layer

#### 5. Règles Architecturales Strictes

✅ **Règle 1** : Presentation ne peut PAS accéder directement à Persistence  
✅ **Règle 2** : Business ne peut PAS accéder directement à Infrastructure  
✅ **Règle 3** : Toutes les communications passent par des interfaces  
✅ **Règle 4** : Aucun skip de couche autorisé  

---

## 📊 Diagramme de Classes

### Vue d'Ensemble Complète

Capture class diagram


### Relations Entre Classes

#### Pattern Chain of Responsibility
- `NotificationHandler` (interface) ← implémentée par `BaseNotificationHandler`
- `BaseNotificationHandler` ← étendue par `EmailHandler`, `SMSHandler`, `PushHandler`
- `NotificationChainBuilder` crée et configure la chaîne

#### Pattern Flyweight
- `MessageTemplateFactory` crée et met en cache les `MessageTemplate`
- `MessageTemplate` contient l'état intrinsèque (pattern, format)
- Les données extrinsèques (userName, etc.) sont passées en paramètre

---

## 📦 Diagramme de Packages

### Structure Complète des Packages

capture package diagram


### Justification de la Répartition

#### Package Presentation
**Responsabilité** : Interface avec le monde extérieur  
**Contenu** : Controllers REST, DTOs, Validation  
**Pourquoi** : Isoler la couche de présentation permet de changer l'interface (REST → GraphQL → gRPC) sans impacter le métier

#### Package Business
**Responsabilité** : Logique métier et patterns  
**Contenu** : Services, handlers (Chain of Responsibility), templates (Flyweight)  
**Pourquoi** : Cœur de l'application, totalement indépendant de l'infrastructure technique

#### Package Persistence
**Responsabilité** : Accès et stockage des données  
**Contenu** : Entities JPA, Repositories, Mappers  
**Pourquoi** : Facilite le changement de base de données ou de technologie de persistence

#### Package Infrastructure
**Responsabilité** : Services externes et techniques  
**Contenu** : Implémentations concrètes (Email, SMS, Push)  
**Pourquoi** : Découple les fournisseurs externes (Gmail → SendGrid, Twilio → AWS SNS, etc.)

#### Package Common
**Responsabilité** : Éléments réutilisables  
**Contenu** : Configuration, exceptions, enums, utilitaires  
**Pourquoi** : Éviter la duplication de code à travers les couches

### Flux de Données
Client HTTP Request

        ↓
        
NotificationController (Presentation)

        ↓ [DTO → Model]
        
NotificationService (Business)

        ↓ [Uses Chain]
        
EmailHandler/SMSHandler/PushHandler (Business)

        ↓ [Uses Infrastructure]
        
EmailService/SMSService/PushService (Infrastructure)

        ↓ [Saves to DB]
        
NotificationRepository (Persistence)

        ↓
        
Database (MySQL)


---

## 🎨 Design Patterns Implémentés

### 1. Chain of Responsibility (Chaîne de Responsabilité)

#### Problème Résolu

Sans ce pattern, le code ressemblerait à ceci :

```java
// ❌ APPROCHE PROBLÉMATIQUE - Couplage Fort
public class NotificationService {
public void sendNotification(Notification notification) {
if (notification.getChannel() == Channel.EMAIL) {
emailService.sendEmail(notification.getRecipient(),
notification.getSubject(),
notification.getMessage());
} else if (notification.getChannel() == Channel.SMS) {
smsService.sendSMS(notification.getRecipient(),
notification.getMessage());
} else if (notification.getChannel() == Channel.PUSH) {
pushService.sendPush(notification.getRecipient(),
notification.getSubject(),
notification.getMessage());
}
// Pour ajouter WhatsApp, il faut MODIFIER cette méthode
// Violation du principe Open/Closed !
}
}
```
**Problèmes identifiés :**
- ❌ Violation du principe **Open/Closed** (ajout = modification)
- ❌ **Couplage fort** entre le service et les implémentations
- ❌ Logique de routage **mélangée** avec la logique métier
- ❌ Difficile à **tester** unitairement
- ❌ Impossible d'ajouter de nouveaux canaux sans modifier le code existant

#### Solution avec Chain of Responsibility

##### Structure du Pattern

```java
// 1. INTERFACE - Contrat commun
public interface NotificationHandler {
void setNext(NotificationHandler handler);
void handle(Notification notification);
}

// 2. CLASSE ABSTRAITE - Logique commune de chaînage
@Slf4j
public abstract class BaseNotificationHandler implements NotificationHandler {
protected NotificationHandler next;
protected final String handlerName;

protected BaseNotificationHandler(String handlerName) {
    this.handlerName = handlerName;
}

@Override
public void setNext(NotificationHandler handler) {
    this.next = handler;
    log.info("🔗 {} → chaîné avec {}", 
            this.handlerName, 
            ((BaseNotificationHandler)handler).handlerName);
}

@Override
public void handle(Notification notification) {
    log.info("🔍 {} reçoit la notification", handlerName);
    
    if (canHandle(notification)) {
        log.info("✅ {} PEUT traiter cette notification!", handlerName);
        process(notification);
    } else {
        log.info("❌ {} ne peut PAS traiter le canal {}", 
                handlerName, notification.getChannel());
        passToNext(notification);
    }
}

protected void passToNext(Notification notification) {
    if (next != null) {
        log.info("⏭️  Passage à {} →", 
                ((BaseNotificationHandler)next).handlerName);
        next.handle(notification);
    } else {
        log.warn("⛔ Fin de chaîne atteinte. Aucun handler disponible.");
    }
}

// Méthodes abstraites à implémenter par les handlers concrets
protected abstract boolean canHandle(Notification notification);
protected abstract void process(Notification notification);
}

// 3. HANDLER CONCRET - Email
@Component
@RequiredArgsConstructor
public class EmailHandler extends BaseNotificationHandler {
private final EmailService emailService;

public EmailHandler(EmailService emailService) {
    super("📧 EmailHandler");
    this.emailService = emailService;
}

@Override
protected boolean canHandle(Notification notification) {
    return notification.getChannel() == Channel.EMAIL;
}

@Override
protected void process(Notification notification) {
    emailService.sendEmail(
        notification.getRecipient(),
        notification.getSubject(),
        notification.getMessage()
    );
    log.info("✅ Email envoyé avec succès!");
}
}

// 4. BUILDER - Construction de la chaîne
@Component
@RequiredArgsConstructor
public class NotificationChainBuilder {
private final EmailHandler emailHandler;
private final SMSHandler smsHandler;
private final PushHandler pushHandler;

public NotificationHandler buildChain() {
    log.info("╔════════════════════════════════════════╗");
    log.info("║   CONSTRUCTION DE LA CHAÎNE            ║");
    log.info("╚════════════════════════════════════════╝");
    
    // Construire la chaîne: Email → SMS → Push
    emailHandler.setNext(smsHandler);
    smsHandler.setNext(pushHandler);
    
    log.info("✅ Chaîne construite: 📧 Email → 📱 SMS → 🔔 Push");
    
    return emailHandler; // Retourner le premier maillon
}
}
```


#### Diagramme de Séquence - Envoi SMS

capture sequence diagram

#### Avantages Démontrés

##### 1. Extensibilité (Open/Closed Principle)

```java
// Pour ajouter WhatsApp, AUCUNE modification du code existant !
@Component
public class WhatsAppHandler extends BaseNotificationHandler {
  private final WhatsAppService whatsAppService;

  public WhatsAppHandler(WhatsAppService whatsAppService) {
    super("💬 WhatsAppHandler");
    this.whatsAppService = whatsAppService;
  }

  @Override
  protected boolean canHandle(Notification notification) {
    return notification.getChannel() == Channel.WHATSAPP;
  }

  @Override
  protected void process(Notification notification) {
    whatsAppService.sendMessage(notification.getRecipient(), 
                                notification.getMessage());
  }

}
// Dans le builder, on ajoute simplement dans la chaîne
emailHandler.setNext(smsHandler);
smsHandler.setNext(whatsAppHandler); // ← AJOUT
whatsAppHandler.setNext(pushHandler);
```


##### 2. Découplage Total

Le service ne connaît que l'interface `NotificationHandler`, pas les implémentations concrètes :

```java
@Service
public class NotificationServiceImpl {
  private final NotificationChainBuilder chainBuilder;

  public Notification sendNotification(Notification notification) {
    // Le service ne connaît PAS EmailHandler, SMSHandler, etc.
    // Il connaît uniquement l'interface
    NotificationHandler chain = chainBuilder.buildChain();
    chain.handle(notification);
    
    return notification;
  }
}
```


##### 3. Single Responsibility

Chaque handler a UNE responsabilité : gérer SON canal.

EmailHandler → Responsable UNIQUEMENT des emails
SMSHandler → Responsable UNIQUEMENT des SMS
PushHandler → Responsable UNIQUEMENT des notifications push


---

### 2. Flyweight (Poids Mouche)

#### Problème Résolu

Sans ce pattern, imaginez envoyer **10,000 notifications de bienvenue** :
```java
// ❌ APPROCHE PROBLÉMATIQUE - Duplication Massive
for (int i = 0; i < 10000; i++) {
Notification notif = new Notification();
notif.setTitle("Bienvenue {userName}"); // DUPLIQUÉ 10,000 fois !
notif.setBody("Merci de vous être inscrit sur {platformName}..."); // DUPLIQUÉ 10,000 fois !
notif.setRecipient(users[i].getEmail());
send(notif);
}
```

**Impact mémoire :**
- Chaque notification : ~500 bytes
- 10,000 notifications : **~5 MB** de données identiques en mémoire !
- 1,000,000 notifications : **~500 MB** gaspillés !

**Problèmes identifiés :**
- ❌ **Duplication** massive des templates en mémoire
- ❌ **Performance** dégradée (allocations répétées)
- ❌ **Scalabilité** compromise pour systèmes à grande échelle
- ❌ **Gaspillage** de ressources serveur

#### Solution avec Flyweight

Le pattern Flyweight sépare les données en deux types :

- **État INTRINSÈQUE** (intrinsic) : Partagé entre tous les objets
- **État EXTRINSÈQUE** (extrinsic) : Unique à chaque objet

##### Structure du Pattern

```java
// 1. FLYWEIGHT - Template Partagé
public class MessageTemplate {
  // ========== ÉTAT INTRINSÈQUE (partagé) ==========
private final String templateId;
private final String titlePattern;
private final String bodyPattern;
private final String format;

public MessageTemplate(String templateId, String titlePattern, 
                      String bodyPattern, String format) {
    this.templateId = templateId;
    this.titlePattern = titlePattern;
    this.bodyPattern = bodyPattern;
    this.format = format;
    
    System.out.println("Creating new template: " + templateId);
}

// ========== ÉTAT EXTRINSÈQUE (unique, passé en paramètre) ==========
public String renderTitle(Map<String, String> data) {
    return replacePlaceholders(titlePattern, data);
}

public String renderBody(Map<String, String> data) {
    return replacePlaceholders(bodyPattern, data);
}

private String replacePlaceholders(String pattern, Map<String, String> data) {
    String result = pattern;
    for (Map.Entry<String, String> entry : data.entrySet()) {
        result = result.replace("{" + entry.getKey() + "}", entry.getValue());
    }
    return result;
}
}
// 2. FLYWEIGHT FACTORY - Gestion du Cache
@Component
public class MessageTemplateFactory {
// Cache des templates (un seul objet par type)
private final ConcurrentHashMap<String, MessageTemplate> templates = 
        new ConcurrentHashMap<>();

public MessageTemplate getTemplate(String type) {
    // Si le template existe en cache, on le retourne
    // Sinon, on le crée et on le met en cache
    return templates.computeIfAbsent(type, this::createTemplate);
}

private MessageTemplate createTemplate(String type) {
    return switch (type) {
        case "WELCOME" -> new MessageTemplate(
            "WELCOME",
            "Bienvenue {userName}",
            "Merci de vous être inscrit sur {platformName}. " +
            "Nous sommes ravis de vous compter parmi nous !",
            "HTML"
        );
        
        case "ORDER_CONFIRM" -> new MessageTemplate(
            "ORDER_CONFIRM",
            "Commande #{orderId} confirmée",
            "Votre commande d'un montant de {amount} a été confirmée. " +
            "Livraison prévue le {deliveryDate}.",
            "HTML"
        );
        
        case "PASSWORD_RESET" -> new MessageTemplate(
            "PASSWORD_RESET",
            "Réinitialisation de mot de passe",
            "Bonjour {userName}, votre code de réinitialisation est: {resetCode}. " +
            "Ce code expire dans {expirationTime} minutes.",
            "TEXT"
        );
        
        default -> throw new IllegalArgumentException(
            "Unknown template type: " + type
        );
    };
}

public int getTemplateCount() {
    return templates.size();
}

public void clearCache() {
    templates.clear();
}
// 3. UTILISATION dans le Service
@Service
public class NotificationServiceImpl {
  private final MessageTemplateFactory templateFactory;

public Notification sendNotification(Notification notification) {
    if (notification.getTemplateType() != null) {
        // Récupération du template (partagé, une seule instance)
        MessageTemplate template = 
            templateFactory.getTemplate(notification.getTemplateType());
        
        // Rendu avec données spécifiques (état extrinsèque)
        String title = template.renderTitle(notification.getCustomData());
        String body = template.renderBody(notification.getCustomData());
        
        notification.setSubject(title);
        notification.setMessage(body);
    }
    
    // Envoi de la notification...
    return notification;
}
}
```

#### Comparaison Mémoire Détaillée

| Méthode | Notifications | Objets Template | Mémoire Utilisée | Économie |
|---------|--------------|-----------------|------------------|----------|
| **Sans Flyweight** | 10,000 | 10,000 (tous identiques) | ~5 MB | - |
| **Avec Flyweight** | 10,000 | 1 (partagé) | ~500 bytes + données extrinsèques | **99.99%** |
| **Sans Flyweight** | 1,000,000 | 1,000,000 | ~500 MB | - |
| **Avec Flyweight** | 1,000,000 | 1 (partagé) | ~500 bytes + données extrinsèques | **99.99%** |

#### Démonstration du Cache

```java
// Premier appel
NotificationService.sendNotification(notification1);
LOG: Creating new template: WELCOME ← Création

// Deuxième appel (même type de template)
NotificationService.sendNotification(notification2);
(aucun log "Creating") ← Réutilisation du cache !

// 9,998 appels suivants
(aucun log "Creating") ← Toujours le même objet !

// Statistiques
factory.getTemplateCount(); // Retourne: 1
// Une seule instance de template pour 10,000 notifications !
```

#### Avantages Démontrés

##### 1. Optimisation Mémoire Drastique
##### 2. Performance Améliorée
##### 3. Centralisation de la Gestion
##### 4. Scalabilité

---

## ✅ Principes SOLID

Les 5 principes SOLID sont **rigoureusement appliqués** dans tout le projet.

### 1. Single Responsibility Principle (SRP)

**Principe** : Une classe ne doit avoir qu'**une seule raison de changer**.

#### Application dans le Projet

##### Exemple 1: Services Spécialisés

```java
// ✅ Chaque service a UNE responsabilité

// Responsable UNIQUEMENT de l'envoi d'emails
@Service
public class EmailServiceImpl implements EmailService {
  private final JavaMailSender mailSender;

@Override
public void sendEmail(String to, String subject, String body) {
    // Logique SMTP uniquement
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject(subject);
    message.setText(body);
    mailSender.send(message);
}
}
// Responsable UNIQUEMENT de la validation
@Component
public class NotificationValidator {
public boolean validate(Notification notification) {
    return notification.getRecipient() != null 
        && notification.getChannel() != null
        && notification.getMessage() != null;
}
}

// Responsable UNIQUEMENT du mapping
@Component
public class NotificationMapper {
public NotificationEntity toEntity(Notification notification) {
    // Mapping Model → Entity
}

public Notification toModel(NotificationEntity entity) {
    // Mapping Entity → Model
}
}
```

##### Exemple 2: Handlers

```java
// EmailHandler : Responsable UNIQUEMENT des emails
public class EmailHandler extends BaseNotificationHandler {
// Logique email uniquement
}

// SMSHandler : Responsable UNIQUEMENT des SMS
public class SMSHandler extends BaseNotificationHandler {
// Logique SMS uniquement
}
```

**Avantage** : Modifications localisées. Changer la logique email n'affecte pas SMS.

---

### 2. Open/Closed Principle (OCP)

**Principe** : Ouvert à l'**extension**, fermé à la **modification**.

#### Application dans le Projet

##### Exemple 1: Ajout de Nouveau Canal sans Modification
```java
// ✅ Code EXISTANT - NON MODIFIÉ
@Component
public class EmailHandler extends BaseNotificationHandler {
// Code inchangé
}

@Component
public class SMSHandler extends BaseNotificationHandler {
// Code inchangé
}

// ✅ NOUVEAU CODE - EXTENSION
@Component
public class WhatsAppHandler extends BaseNotificationHandler {
  private final WhatsAppService whatsAppService;

@Override
protected boolean canHandle(Notification notification) {
    return notification.getChannel() == Channel.WHATSAPP;
}

@Override
protected void process(Notification notification) {
    whatsAppService.sendMessage(
        notification.getRecipient(),
        notification.getMessage()
    );
}
}
// Dans le builder, on AJOUTE simplement
public NotificationHandler buildChain() {
emailHandler.setNext(smsHandler);
smsHandler.setNext(whatsAppHandler); // ← AJOUT, pas modification
whatsAppHandler.setNext(pushHandler);

return emailHandler;

}

```

##### Exemple 2: Ajout de Nouveau Template
```java
// Dans MessageTemplateFactory, on AJOUTE un cas
private MessageTemplate createTemplate(String type) {
return switch (type) {
case "WELCOME" -> /* ... /;
case "ORDER_CONFIRM" -> / ... */;
case "PAYMENT_SUCCESS" -> new MessageTemplate( // ← NOUVEAU
"PAYMENT_SUCCESS",
"Paiement réussi",
"Votre paiement de {amount} a été confirmé",
"HTML"
);
default -> throw new IllegalArgumentException("Unknown: " + type);
};
}
```

**Avantage** : Zéro risque de régression. Le code existant reste intact.

---

### 3. Liskov Substitution Principle (LSP)

**Principe** : Les sous-classes doivent être **substituables** à leurs classes parentes.

#### Application dans le Projet
```java
// ✅ Tous les handlers sont substituables

NotificationHandler handler1 = new EmailHandler(emailService);
NotificationHandler handler2 = new SMSHandler(smsService);
NotificationHandler handler3 = new PushHandler(pushService);

// Tous peuvent être utilisés via l'interface
public void processNotification(NotificationHandler handler, Notification notif) {
handler.handle(notif); // Fonctionne pour tous les handlers
}

// Polymorphisme parfait
List<NotificationHandler> handlers = Arrays.asList(
handler1, handler2, handler3
);
handlers.forEach(h -> h.handle(notification));
```

**Preuve** : Le comportement est cohérent quel que soit le handler utilisé.

**Avantage** : Polymorphisme fonctionnel. Le code client ne connaît que l'interface.

---

### 4. Interface Segregation Principle (ISP)

**Principe** : Ne pas forcer à dépendre de méthodes **non utilisées**.

#### Application dans le Projet

##### ❌ Mauvaise Approche (ISP Violé)
```java
// Interface trop large - Force à implémenter tout
public interface NotificationService {
void sendEmail(String to, String subject, String body);
void sendSMS(String phone, String message);
void sendPush(String token, String title, String body);
void sendWhatsApp(String number, String message);
void sendSlack(String channel, String message);
void sendTelegram(String chatId, String message);
}

// EmailHandler est forcé d'implémenter SMS, Push, etc.
// Même s'il ne les utilise pas !
```

##### ✅ Bonne Approche (ISP Respecté)
```java
// Interfaces spécifiques et cohésives

public interface EmailService {
void sendEmail(String to, String subject, String body);
void sendHtmlEmail(String to, String subject, String htmlBody);
}

public interface SMSService {
void sendSMS(String phoneNumber, String message);
}

public interface PushService {
void sendPushNotification(String deviceToken, String title, String body);
}

// Chaque handler dépend uniquement de ce dont il a besoin
public class EmailHandler extends BaseNotificationHandler {
private final EmailService emailService; // ✅ Seulement EmailService
}

public class SMSHandler extends BaseNotificationHandler {
private final SMSService smsService; // ✅ Seulement SMSService
}
```

**Avantage** : Classes légères. Dépendances minimales. Tests simplifiés.

---

### 5. Dependency Inversion Principle (DIP)

**Principe** : Dépendre d'**abstractions**, pas d'**implémentations** concrètes.

#### Application dans le Projet

##### ✅ Dépendances via Interfaces
```java
@Service
public class NotificationServiceImpl implements NotificationService {
// Toutes les dépendances sont des ABSTRACTIONS (interfaces)
private final NotificationRepository repository;      // Interface
private final NotificationMapper mapper;              // Abstraction
private final MessageTemplateFactory templateFactory; // Abstraction
private final NotificationChainBuilder chainBuilder;  // Abstraction

// Injection via constructeur (Dependency Injection)
public NotificationServiceImpl(
        NotificationRepository repository,
        NotificationMapper mapper,
        MessageTemplateFactory templateFactory,
        NotificationChainBuilder chainBuilder) {
    this.repository = repository;
    this.mapper = mapper;
    this.templateFactory = templateFactory;
    this.chainBuilder = chainBuilder;
}
}
```

**Avantage** : Découplage maximal. Testabilité (mocks). Flexibilité totale.

---

### Tableau Récapitulatif SOLID

| Principe | Où dans le Projet | Exemple Concret | Bénéfice |
|----------|-------------------|-----------------|----------|
| **SRP** | EmailService, SMSService, PushService | Chaque service gère UN canal | Modifications localisées |
| **OCP** | Ajout de WhatsAppHandler sans modification | Nouveau handler = nouvelle classe | Zéro régression |
| **LSP** | Tous les handlers substituables | `NotificationHandler handler = new EmailHandler()` | Polymorphisme fonctionnel |
| **ISP** | Interfaces EmailService, SMSService séparées | EmailHandler ne dépend pas de SMSService | Dépendances minimales |
| **DIP** | Service dépend d'interfaces, pas d'implémentations | `private final EmailService emailService` | Découplage, testabilité |

---

## 🛠️ Technologies Utilisées

### Backend

| Technologie | Version | Rôle dans le Projet |
|-------------|---------|---------------------|
| **Java** | 17 | Langage principal |
| **Spring Boot** | 3.2.0 | Framework principal |
| **Spring Data JPA** | 3.2.0 | ORM et persistence |
| **Spring Web** | 3.2.0 | API REST |
| **Spring Mail** | 3.2.0 | Envoi d'emails |
| **Hibernate** | 6.2.x | Implémentation JPA |
| **MySQL** | 8.0 | Base de données |
| **Lombok** | 1.18.30 | Réduction boilerplate |
| **Maven** | 3.8+ | Build et dépendances |

### Services Externes

| Service | Version | Usage |
|---------|---------|-------|
| **Gmail SMTP** | - | Envoi d'emails réels |
| **Twilio API** | SDK 9.14.1 | Envoi de SMS |
| **Firebase FCM** | Admin SDK 9.3.0 | Push notifications |

### Documentation & Testing

| Outil | Version | Usage |
|-------|---------|-------|
| **Postman** | Latest | Tests API manuels |

---

## ⚙️ Installation et Configuration

### Prérequis

- ☕ **Java 17** ou supérieur
- 🗄️ **MySQL 8.0** ou supérieur
- 📦 **Maven 3.8** ou supérieur
- 🔧 **Git** pour cloner le projet


















