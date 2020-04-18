# concentration-card-game
Basic online concentration game, implementing both server and client side

Nothing fancy, I built it for educational purposes.
Most of the work was done on server-side, which implements a precise and safe protocol with the client.
The server holds all the information and doesn't expose it with the client. It has complete control over the match in order to to prevent cheating. Furthermore, I tried making it pretty robust using concurrency, so it could (potentially) support many clients.

As for the client side, I implemented it using MVC. The user could choose the total cards per match, or go for the server-default configuration. There should be another player waiting for a match, who requested the same amount of cards.
The cards are stored locally, and the communication with the server is by cards' id, in order to reduce bandwidth.

