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

L'applicazione è stata progettata in modo da poter essere facilmente estesa, ad esempio integrando un'architettura event-driven (es. Kafka).


## Descrizione del progetto
* Java
* Spring Boot
* Spring Security
* JWT (JSON Web Token)
* JPA / Hibernate
* Maven


### Build ed esecuzione
Per avviare il progetto:

_mvn clean install_

_mvn spring-boot:run_

L'applicazione sarà disponibile su:

http://localhost:8080

Swagger UI:

http://localhost:8080/swagger-ui.html

### Autenticazione e autorizzazione
Il sistema utilizza autenticazione stateless basata su JWT.

Dopo il login, viene restituito un token che deve essere incluso nelle richieste successive.

È stato implementato un sistema RBAC con i seguenti ruoli:

* OWNER
  * DEVELOPER
    * REPORTER

### Scelte tecniche

Durante lo sviluppo ho cercato di applicare alcune buone pratiche tipiche di progetti reali:
* **Separazione tra Entity e DTO**
  per evitare esposizione diretta del modello dati
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

### Possibili evoluzioni

Il progetto è stato pensato per essere esteso facilmente. Alcune possibili evoluzioni:

* integrazione con Kafka per gestione eventi
*  persistenza dei log di audit su database
*  gestione più avanzata dei permessi
*  monitoring e metrics

### Note finali

Il focus principale è stato creare una base solida e leggibile, più che aggiungere funzionalità complesse.

Ho cercato di mantenere il codice il più possibile pulito e organizzato, seguendo principi di separazione delle responsabilità.
