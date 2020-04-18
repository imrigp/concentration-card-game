# concentration-card-game
Basic online concentration game, implementing both server and client side

Nothing fancy, I built it for educational pureposes.
Most of the work was done on server-side, which implements a precise and safe protocol with the client.
The server holds all the information and doesn't expose it with the client. It has complete control over the match in order to to prevent cheating. Furthermore, I tried making it pretty robust using concurrency, so it could (potentially) support many clients.
