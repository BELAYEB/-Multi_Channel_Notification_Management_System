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




