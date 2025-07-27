# Pokévax

## Glossary

- (x) => not done / TODO
- (/) => done

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
- (x) Analyse data
  - What format
  - What column this could give
- (x) Draft screens on paper
- (x) Have a look at What Vaadin 8 provides
- (x) Draft DB structure 
- (x) Draft high level *business* modules
- (x) Draft Internationalization
- (x) Scaffold project with inspiration from SORMAS


## Ideas

- Range for notifications: buffer like approach: 10 days/months buffer after per example
- Cronjob for notifications ? 
  - On connection
  - Everynight 
- Encrypt data: using full encryption or simply for some columns: using 
- Stacktrace
- GDPR full download endpoint
- HTTPS

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