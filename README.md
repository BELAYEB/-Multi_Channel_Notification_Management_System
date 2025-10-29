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
8. [Installation et Configuration](#-installation-et-configuration)
9. [API Documentation](#-api-documentation)
10. [Tests et Démonstration](#-tests-et-démonstration)

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
- **SMS** : Envoi via API Twilio
- **Push** : Notifications via Firebase Cloud Messaging

#### 🎨 Gestion des Templates
- Templates réutilisables (Welcome, Order Confirmation, Password Reset, etc.)
- Personnalisation dynamique avec variables
- Cache intelligent pour optimisation mémoire (Pattern Flyweight)

#### 🔄 Routage Automatique
- Sélection automatique du canal via Chain of Responsibility
- Gestion intelligente des erreurs
- Mécanisme de retry automatique

#### 📊 API REST
- Endpoints RESTful pour toutes les opérations
- Documentation interactive (Swagger/OpenAPI)
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








