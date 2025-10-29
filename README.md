# Multi_Channel_Notification_Management_System - Architecture Logicielle

Système de gestion de notifications multi-canal utilisant les patterns **Chain of Responsibility** et **Flyweight**, respectant les principes **SOLID**.

## 🏗️ Architecture

**Style architectural:** Architecture en Couches (Layered Architecture)

### Couches
1. **Presentation Layer** - Controllers, DTOs, Validation
2. **Business Layer** - Services, Handlers, Design Patterns
3. **Persistence Layer** - Entities, Repositories, Mappers
4. **Infrastructure Layer** - Services externes (Email, SMS, Push)

## 🎯 Design Patterns Implémentés

### 1. Chain of Responsibility
- **Objectif:** Router les notifications vers le bon canal
- **Implémentation:** `NotificationHandler`, `EmailHandler`, `SMSHandler`, `PushHandler`
- **Avantage:** Extensibilité - ajout de nouveaux canaux sans modifier le code existant

### 2. Flyweight
- **Objectif:** Optimiser la mémoire en partageant les templates de messages
- **Implémentation:** `MessageTemplate`, `MessageTemplateFactory`
- **Avantage:** Économie de mémoire pour des milliers de notifications

## ✅ Principes SOLID

- **S** - Single Responsibility: Chaque classe a une responsabilité unique
- **O** - Open/Closed: Ouvert à l'extension, fermé à la modification
- **L** - Liskov Substitution: Les handlers peuvent être substitués
- **I** - Interface Segregation: Interfaces spécifiques et cohésives
- **D** - Dependency Inversion: Dépendance sur des abstractions


1. Cloner le projet
