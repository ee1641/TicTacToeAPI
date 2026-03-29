TICTACTOE API

To run it you must have java 21, maven installed.

I used H2 db because its native to spring boot and it requires no set up. It resets everytime you run the app.

As bonus i included the postman collection that i used to test the core functionality of the app. Import it into postman. Requests are in order, so just run it top to bottom. 
Note: There are 2 log in requests, one is for player1 (me) and the other is for the loosing player. Copy the token from the reponse and paste it under authorization tab, select bearer token.
Any request that ends with P2 must have the player 2 login token, otherwise player 1 (first log in request token).

Author: Ejup Elezi
