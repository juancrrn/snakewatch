Snakewatch

# Models sandbox

## User

Has:

- nickname (string)
- password (string)
- 
- ...

## Friendship

Many users to many users relationship.

Has:

- user1 (foreign reference)
- user2 (foreign reference)
- status (enumerate) - Petition status
- ...

## Level

Representation with the game board and obstacles.

Has:

- representation (string) - JSON (?)
- ...

## Room (sala de juego)

Has:

- visibility (enumerate) - Public or private
- max_users (integer)
- ...

## RoomUser

Many users to many rooms relationship, users that are in a room.

Has:

- room (foreign reference)
- user (foreign reference)
- admin (boolean) - User is room admin
- ...

## Match

Match or particular game.

Has:

- room (foreign reference)
- level (foreign reference)
- status (enumerate) - Waiting, ongoing, ended... (?)
- max_players (integer)
- winner (foreign reference) - Winner (?)
- ...

## MatchPlayer

Many users to a single match relationship, users that are playing a match. The same user cannot be playing in different matches at the same time.

Has:

- match (foreign reference to Match)
- user (foreign reference to User)
- result (int (-1 not defined)) - Individual result (?)
- ...

## Skin

Customizable snake skin. The skin data and representation is stored in the filesystem under an id-based name.

Has:

- creator (foreign reference to User)
- ...

## Setting

Global setting required by the app or the gaming logic.

E. g.: to set the default max. participants for new rooms.

Has:

- key (string)
- value (string)
- ...

## UserReport

User report.

Has:

- reporter (foreign reference to User) - User reporting
- reported (foreign reference to User) - User being reported
- reason (string)
- moderator (foreign reference to User)
- status (enumerate)