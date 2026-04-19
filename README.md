# User Management System

## Descrizione del progetto

Questo progetto è un'applicazione Spring Boot che espone API REST per la gestione degli utenti.

L'obiettivo è stato costruire un sistema semplice ma strutturato, con particolare attenzione a:
* autenticazione e autorizzazione
* organizzazione del codice
* ottimizzazione delle performance
* tracciamento delle operazioni

Le principali funzionalità includono:
* registrazione e gestione utenti
* autenticazione tramite JWT
* controllo accessi basato su ruoli (RBAC)
* validazione dei dati in ingresso
* paginazione nelle liste utenti
* caching per migliorare le performance
* audit logging delle operazioni principali

## Descrizione del progetto
* Java 17
* Spring Boot
* Spring Security
* JWT (JSON Web Token)
* JPA / Hibernate
* PostgreSQL
* Maven
* Apache Kafka

---

### Scelte tecniche

Durante lo sviluppo ho cercato di applicare alcune buone pratiche tipiche di progetti reali:
* **Separazione tra Entity e DTO**
  per evitare esposizione diretta del modello dati
* **Mapper dedicati**
  per ridurre accoppiamento tra livelli
* **Pagination**
  per gestire in modo efficiente liste di utenti
* **Caching**
  per ridurre accessi ripetuti al database
* **Ottimizzazione query (EntityGraph)**
  per evitare problemi di N+1
* **Audit logging con AOP**
  per tracciare le operazioni senza sporcare il codice business
* **Validazione input**
  per garantire coerenza dei dati
* **PostgreSQL**
  è stato scelto per robustezza, supporto a JSONB e capacità di scalare rispetto a H2/MySQL in scenari reali
* **Versioning db**
  gestito tramite Flyway

---

### Scelta del database

È stato utilizzato PostgreSQL per:
- robustezza e affidabilità
- maggiore aderenza a scenari reali rispetto a database embedded
- possibilità di estendere facilmente il modello dati in futuro

---

### Build ed esecuzione
Per avviare il progetto:

_mvn clean install_

_mvn spring-boot:run_

L'applicazione sarà disponibile su:

http://localhost:8080

Swagger UI:

http://localhost:8080/swagger-ui.html

---

### Autenticazione e autorizzazione
Il sistema utilizza autenticazione stateless basata su JWT.

1. Login

Effettuare una richiesta: 
_POST /auth/login_

Esempio body:

```json
{
"username": "admin",
"password": "admin"
}
```

2. Utilizzo del token

Dopo il login, viene restituito un token che deve essere incluso nelle richieste successive.

- copiare il token restituito
- cliccare su Authorize in Swagger ed inserire: _Bearer token_

#### Autorizzazione (RBAC)

Ho implementato RBAC utilizzando Spring Security e l’annotazione _@PreAuthorize_, definendo regole di accesso basate sui ruoli dell’utente per ogni endpoint.

Esempio:
```java
@PreAuthorize("hasAnyRole('OWNER','DEVELOPER','REPORTER')")
```

---


### Branching strategy

Il progetto è stato sviluppato utilizzando una strategia a feature branches:

- _main_ contiene una versione stabile del codice
- ogni funzionalità è sviluppata su branch separati
- merge effettuati al completamento delle feature

---
### Eventi e integrazione Kafka

L’applicazione include un primo esempio di integrazione event-driven utilizzando Apache Kafka.

#### Evento: UserCreatedEvent

Quando viene creato un nuovo utente, viene pubblicato un evento:

```java
public class UserCreatedEvent {
    private Long userId;
    private String username;
    private LocalDateTime createdAt;
}
```
Scopo dell’evento

L’obiettivo dell’evento è disaccoppiare la logica di creazione utente da eventuali azioni successive, permettendo di reagire alla creazione di un utente in modo asincrono.

In questo caso, l’evento viene utilizzato per simulare l’invio di una email di benvenuto.

Producer

Il producer invia l’evento su un topic Kafka:
```java
kafkaTemplate.send(topic, event.getUserId().toString(), event);
```

Consumer

Un consumer intercetta l’evento e gestisce la logica successiva:

```java
@KafkaListener(topics = "${kafka.topic.user-created}", groupId = "user-group")
```

Nel caso attuale:

- viene loggato l’evento ricevuto
- viene simulato l’invio di una email di benvenuto

Esempio di utilizzo

Alla creazione di un utente:

- viene salvato nel database
- viene pubblicato un evento UserCreatedEvent
- il consumer riceve l’evento
- viene inviata una email di benvenuto (simulata via log)

Vantaggi dell’approccio
- disaccoppiamento tra servizi
- maggiore scalabilità
- possibilità di aggiungere nuovi consumer senza modificare il codice esistente
- migliore gestione di operazioni asincrone

---

### Mascheramento dei dati in base al ruolo

Nel sistema ho implementato un meccanismo di field-level masking basato su ruolo, applicato in fase di serializzazione JSON tramite Jackson.
Alcuni campi dei DTO sono annotati con @MaskForRoles, che indica per quali ruoli il valore deve essere mascherato.

A runtime, un _BeanSerializerModifier_ e un _BeanPropertyWriter_ custom:

- leggono l’annotazione _@MaskForRoles_ sui campi
- recuperano i ruoli dell’utente autenticato dal _SecurityContext_
- sostituiscono il valore reale con una stringa mascherata (es. "******") quando il ruolo corrente è tra quelli configurati




---

### CI/CD – Configurazione base

Il progetto è predisposto per essere integrato con una pipeline CI/CD utilizzando strumenti come GitHub Actions, GitLab CI o Jenkins.

#### Continuous Integration (CI)

Ad ogni push o pull request sul branch principale (`main`), la pipeline esegue:

1. **Checkout del codice**
2. **Build dell’applicazione**

   ```bash
   mvn clean install
   ```
3. **Esecuzione dei test**

    * Test unitari (JUnit)
    * Test di integrazione
4. **Analisi statica del codice (opzionale)**

    * Checkstyle / SpotBugs

---

#### Continuous Delivery (CD)

Dopo una build completata con successo:

1. **Build dell’immagine Docker**

   ```bash
   docker build -t user-management-app .
   ```

2. **Avvio dei servizi tramite Docker Compose**

   ```bash
   docker-compose up -d
   ```

3. **Configurazione ambienti**
   L’applicazione utilizza i seguenti profili Spring:

    * `dev` → funzionalità di sviluppo (es. bootstrap dati)
    * `postgres` → configurazione database PostgreSQL


### Possibili evoluzioni

* Integrazione completa con GitHub Actions
* Push automatico dell’immagine Docker su registry (Docker Hub)
* Deploy su cloud (AWS, Azure, GCP)
* Monitoraggio con strumenti come Prometheus e Grafana

---

### Possibili evoluzioni progettuali

Il progetto è stato pensato per essere esteso facilmente. Alcune possibili evoluzioni:

* gestione più avanzata dei permessi
* monitoring e metrics
* containerizzazione dell'applicativo

---
### Note finali

Il focus principale è stato creare una base solida e leggibile, più che aggiungere funzionalità complesse.

Ho cercato di mantenere il codice il più possibile pulito e organizzato, seguendo principi di separazione delle responsabilità.
