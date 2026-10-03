# 🗣 cite [![Build](https://github.com/dontlookawayco/cite/actions/workflows/android.yml/badge.svg)](https://github.com/dontlookawayco/cite/actions/workflows/android.yml)
An Android app for quotes

## About
This application will be a quotes app meant as a school project but will probably end up on the app stores in the future as well :) The documentation (the mark down files) might be partially Dutch but will be translated to English in the future.

## Run mongo
```docker run -p 27017:27017 -d --rm --name QuoteMongo mongo```

## Endpoints
![Endpoints](https://file.coffee/u/QVsKPzFeWU.png)

## User Stories, etc.
Quote: het bericht wat gequote is
Quotee: de persoon die gequote is
Quoter: de persoon die gequote heeft
User Stories
Als [ROLE] wil ik [ACTIE]

### Auth
| User Story ID | Description |
|----------|:-------------:|
| UC-A |1 Als gast wil ik een account kunnen aanmaken.
| UC-A2 | Als gebruiker wil ik kunnen inloggen.
| UC-A |3 Als gebruiker wil ik kunnen uitloggen.
| UC-A4 | Als gebruiker wil ik mijn gegevens kunnen aanpassen.
| UC-A5 | Als gebruiker wil ik mijn account kunnen verwijderen.

### Groups
| User Story ID | Description |
|----------|:-------------:|
| UC-G1 | Als gebruiker wil ik een groep kunnen aanmaken. |
| UC-G2 | Als gebruiker wil ik me bij een groep kunnen aansluiten. |
| UC-G3 | Als groep admin wil ik de groepsnaam kunnen wijzigen. |
| UC-G4 | Als groep admin wil ik mensen kunnen uitnodigen tot mijn groep. |
| UC-G5 | Als groep admin wil ik aansluit verzoeken goed of afkeuren. |
| UC-G6 | Als groep admin wil ik leden uit de groep kunnen verwijderen. |
| UC-G7 | Als groep eigenaar wil ik de groep kunnen verwijderen. |
| UC-G8 | Als groep eigenaar wil ik leden kunnen promoveren tot groep admin. |
| UC-G9 | Als groep eigenaar wil ik groep admins kunnen degraderen tot lid. |

### Quotes
| User Story ID | Description |
|----------|:-------------:|
| UC-Q1 | Als groepslid wil ik iemand kunnen quoten. |
| UC-Q2 | Als groepslid wil ik quotes kunnen liken. |
| UC-Q3 | Als groepslid wil ik afbeeldingen kunnen quoten. |
| UC-Q4 | Als groepslid wil ik afbeeldingen bij mijn quote kunnen bijvoegen. |
| UC-Q5 | Als quoter wil ik mijn quotes kunnen bewerken. |
| UC-Q6 | Als quoter wil ik mijn quotes kunnen verwijderen. |

## MoSCoW
| Use Case | Beschrijving | MoSCoW |
|----------|:-------------:|------:|
| UC-A1 | Als gast wil ik een account kunnen aanmaken. | M |
| UC-A2 | Als gebruiker wil ik kunnen inloggen. | M |
| UC-A3 | Als gebruiker wil ik kunnen uitloggen. | S | 
| UC-A4 | Als gebruiker wil ik mijn gegevens kunnen aanpassen. | S | 
| UC-A5 | Als gebruiker wil ik mijn account kunnen verwijderen. | S |
| UC-G1 | Als gebruiker wil ik een groep kunnen aanmaken. | M | 
| UC-G2 | Als gebruiker wil ik me bij een groep kunnen aansluiten. | S | 
| UC-G3 | Als groep admin wil ik de groepsnaam kunnen wijzigen. | C | 
| UC-G4 | Als groep admin wil ik mensen kunnen uitnodigen tot mijn groep. | M | 
| UC-G5 | Als groep admin wil ik aansluit verzoeken goed of afkeuren. | S | 
| UC-G6 | Als groep admin wil ik leden uit de groep kunnen verwijderen. | S | 
| UC-G7 | Als groep eigenaar wil ik de groep kunnen verwijderen. | S | 
| UC-G8 | Als groep eigenaar wil ik leden kunnen promoveren tot groep admin. | C |
| UC-G9 | Als groep eigenaar wil ik groep admins kunnen degraderen tot lid. | C | 
| UC-G10 | Als gebruiker wil ik een uitnodiging tot een groep kunnen goed of afkeuren. | M | 
| UC-G11 | Als gebruiker wil ik een groep kunnen verlaten | W | 
| UC-Q1 | Als groepslid wil ik iemand kunnen quoten. | M | 
| UC-Q2 | Als groepslid wil ik quotes kunnen liken. | S | 
| UC-Q3 | Als groepslid wil ik afbeeldingen kunnen quoten. | C | 
| UC-Q4 | Als groepslid wil ik afbeeldingen bij mijn quote kunnen bijvoegen. | C | 
| UC-Q5 | Als quoter wil ik mijn quotes kunnen bewerken. | S | 
| UC-Q6 | Als quoter wil ik mijn quotes kunnen verwijderen. | S | 

