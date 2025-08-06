# Vaccinéo

Vaccinéo is a Java (8) Spring Boot 2.7.18 web application that handle users and their vaccine schedule.

This project is not meant to be used as-is in PROD, mostly due to security issues.

## Get started

If you are an IntelliJ user you can use the "local" run configuration: in `./run`.

Following JDK was used for the tests: `temurin-1.8.0_452`

Otherwise you can use:

```
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

Once the application started you can verify it's behaving correctly by executing unit and integration Tests: 

`mvn verify` 

## Known errors

- Reloading the dashboard once connected fails but it's okay if you switch back and forth to the profile page.
- Enter keyboard press fails in signup (but works on other places)

## Type of tests

- **Unit tests**
- **Whitebox tests**: to check that all "attached" entities where properly deleted
- **Spring Boot Tests**: allows to execute tests that load the Spring Boot contexts, not an integration test but for
  this application provides very similar safety.

## Requirements

### Business

- Notifications
- Login & signup screens: (without auth so far)
  - Login already account
  - Create account
- Delete account with confirmation modal
- Search vaccine screen
- Add administered vaccine

### Technical

- Java 8
- Vaadin 8
- Hibernate

## Enhancements

### Functional

- Range for notifications: example 10 days before / after deadline
- GDPR full download endpoint: download every data present in DB and create a single JSON
- Handle security token expiration:
  - User must connect again ?
  - Auto-refresh if still 'using' the system ?

### Technical

- Do not use `@GeneratedValue(strategy = GenerationType.IDENTITY)` within JPA entities as Hibernate cannot batch
  inserts without round-trip to DBs (best to use one/mulitple common sequences so that Hibernate can batch those: i.e. '
  load' 50+ ids at once)
- **Use** Spring-Security instead of doing it manually. Here was done for learning / exercise reasons
  - bearer token / checking mechanism from Spring-Security
  - Password encryption: do not rely on 'self-made' solutions
- `@OneToOne(fetch = FetchType.LAZY)`: do not rely on eager strategy and favor more flexible solutions: entityGraph /
  projections.
- `@Enumerated(EnumType.STRING)` is more easily readable in DB but takes more space
- Exposing technical IDs: incremented numbers allow clients to gain knowledge about usage: Privacy and Securicy
- HTTPS
- Add ArchUnit tests for:
  - Entities:
    - Checking if index on foreign key
    - Inheriting BaseClass
    - Follows naming conventions
- Logging: Create AOP annotation to enable default logging.
- I18N: internationalization
- JPA model gen to provide type satefy for entities fields.

## TO-DOs

- Error messages:
  - ConstraintViolation handling: show above form in frontend ?
  - notifications
  - Exception: show message ?
- Check for invalid vaccine name / dosis number

## Features description 

### Vaccine search

The request is not paginated as it's not expected that avery huge amount of vaccines will be created.
In PROD additional checks should be added to avoid retrieving too much data (volume).

### Batch

To schedule the batch: Spring's @Scheduled was used, this works in environments with
a single instance of the service.
In environments where multiple instances (of same service) coexist something
like:

- [db-scheduler](https://github.com/kagkarlsson/db-scheduler)
- [Spring Batch](https://spring.io/projects/spring-batch)
  should be preferred.

A single job was created that deletes the expired notifications, a second batch could be created that sends an email per
example a few days before expiration.

### Security

Security was purposefully designed in a simplified manner.
**DO NOT** use this project in PROD with those security measures.
Spring-security should be used instead

#### Exception handling in case of invalid email / password

Many systems (in PROD) do show a generic response in case of: email is invalid or email/password tuple is invalid.
This is a safety choice **BUT** only if the sign-up screen doesn't show an error in case email already exists.
Otherwise, a malicious user can still identify if an email is used on the system, which breaks the above measure.

Here for UX reasons the distinction was done and on sign-up email will be rejected if already used.

Ideally this information should not be provided in both cases: invalid credentials + user creation.

### Notification

Notification are implemented in a hybrid mode between daily batch + event-driven: 

- When user creates account: all notifications elements are pre-computed and stored
- When user adds vaccine: deletes the corresponding notification
- When users displays notifications: all notifications belonging to the user are returned
- Daily Batch: deletes notification that are marked for deletion

Missing pieces to make the feature complete: 
- Add notifications when a new vaccine is added (or new dose)
- Remove notifications when a dose is removed.
- Update (or not) existing reminders. 

#### Motivation 

- Keep the cron (very) simple: **deletion only**.
This can avoid the scenario where the batch simply takes too long to execute and notifications cannot be computed anymore on a daily basis for all users.
- Event-design: optimize the scenarios that happens frequently:
  - User creates account (will create N rows where N is the number of vaccine which should be lower than number of users)
  - User adds vaccine
- Event-design: Accept worse performance on the scenarios that happens less-frequently.
  - Added Vaccine / Dose
  - Deleted Vaccine / Dose
  - Changed Vaccine deadline.
  - User account deletion (this might not be that infrequent!)

#### Known limits

Notifications were implemented in a simplified manner and **DO NOT** take lower ranges into account but only as a Deadline mechanism.
A possible implementation that handles real intervals: [3-6] months per example:  

- Add lower range into `lu.vaccineo.business.vaccine.VaccineScheduleEntity` (current only upper *deadline**)
- Add notifcation active date within `lu.vaccineo.business.notification.VaccineNotificationEntity` so that the
  notifications element are only returned between both dates.
  notificationActivationDate >= today =< notificationExpirationDate.


## Useful links

- https://github.com/vaadin/bakery-app-starter-fw8-spring/tree/master
- [sqlitetutorial: SQL-Lite documentation](https://www.sqlitetutorial.net/)