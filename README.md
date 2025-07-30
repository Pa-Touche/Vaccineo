# Pokévax

## Glossary

- (x) => not done / TODO
- (/) => done
- (0) => ignored

## Features description 

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
- Add lower range into `lu.pokevax.business.vaccine.VaccineScheduleEntity` (current only upper *deadline**)
- Add notifcation active date within `lu.pokevax.business.notification.NotificationForVaccineEntity` so that the notifications element are only returned between both dates. 
notificationActivationDate >= today =< notificationExpirationDate.



## Enhancements

To keep things simple some shortcuts were made, some of those with explanation are listed here:

### Technical

- Do not use `@GeneratedValue(strategy = GenerationType.IDENTITY)` within JPA entities as Hibernate cannot batch 
inserts without round-trip to DBs (best to use one/mulitple common sequences so that Hibernate can batch those: i.e. 'load' 50+ ids at once)
- **Use** Spring-Security instead of doing it manually. Here was done for learning / exercise reasons  
  - bearer token / checking mechanism from Spring-Security
  - Password encryption: do not rely on 'self-made' solutions
- `@OneToOne(fetch = FetchType.EAGER)`: do not rely on eager strategy and favor more flexible solutions: entityGraph / projections.
- `@Enumerated(EnumType.STRING)` is more easily readable in DB but takes more space
- Exposing technical IDs: incremented numbers allow clients to gain knowledge about usage: Privacy and Securicy

### Features

- **Also** technical: i18n: reused wording from exercise.

## Steps

- Screens and related features to implement: 
  - (x) Login
  - (x) Create account
    - (x) Name
    - (x) Surname
    - (x) Email-address
    - (x) Date of birth
  - (x) List of vaccines of the current user
    - (x) Add vaccine
    - (x) Data-table: Vaccine type, 
    - (x) Filtering / sorting 
  - (x) My profile
    - (x) Name
    - (x) Surname
    - (x) Email-address
    - (x) Date of birth
    - (x) BUTTON: delete account with confirmation
- (/) Analyse data
  - What format
  - What column this could give
- (/) Draft screens on paper
- (x) Have a look at What Vaadin 8 provides
- (/) Draft DB structure 
- (/) Draft high level *business* modules
- (0) Draft Internationalization


## Ideas

- Range for notifications: buffer like approach: 10 days/months buffer after per example
- Cronjob for notifications ? 
  - On connection
  - Everynight 
- Encrypt data: using full encryption or simply for some columns: using 
- Stacktrace
- GDPR full download endpoint
- HTTPS
- Add ArchUnit tests for:
  - Entities:
    - Checking if index on foreign key
    - Inheriting BaseClass
    - Follows naming conventions

## Requirements

### Business

- Notifications 
  - Persisted in DB ? 
  - Cronjob ?
  - On connection ? 
- Login screen: (without auth so far)
  - Login already account
  - Create account
- Delete account


### Technical 

- Java 8
- Vaadin 8


### (Optional) Ideas

- I18N: internationalization
- GDPR-compliance: 
  - Being able to extract all data as JSON

## Useful links

- https://github.com/vaadin/bakery-app-starter-fw8-spring/tree/master
- [sqlitetutorial: SQL-Lite documentation](https://www.sqlitetutorial.net/)