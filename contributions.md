## Contributions  

#### Michael (Cross55):  

    08.04.2024
        Links:  
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/38#issue-2200276059
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/41#issue-2200281419
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/37
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/31

        Description:  
            Struggled with the branches in git and getting the users in the lobby to show because of a missing REST request.  
            Now everything is working properly. Also helped implementing a way to join lobby using the lobby code. 
    
    15.04.2024
        Links:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/35#issue-2200270336
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/33#issue-2200246624
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/30#issue-2200193611

        Description:
            Fixed several REST requests, added a start game functionality in the lobby screen and made an adjacency dictionary for all the territories.

    15.04.2024
        Links:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/20
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/18
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/15
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/11

        Description:
            Added a modal which opens after selecting two territories to handle the attacks, shows the defending user and attacking user, shows the defending territory name and attacking territory name and their respective troop amounts. Also added selectors to select the amount of troops the attacker wants to use to attack and another selector to select how many times the attacker attacks.


#### Nico (Eposs111)

    08.04.2024
        Links: 
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/39
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/37
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/36
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/24

    Description:
        I worked/help worked on the design aspect of the Website in the frontend. We focused on making a (kind of) good looking website
        and implemented the Join and Create Lobby site/buttons. (DISCLAIMER: we were a team of three in the FrontEnd therefor we all helped each           other if needed)


    15.04.2024
        Links:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/45
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/46
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/47

        Description:
            I worked on the external API. I integrated it and made a mechanism to change and safe the avatar on registration. 
            My problem was, that I started wrong and had to restart halfway through.


#### Ralph Kosch (RPKosch)

    08.04.2024
        Links: 
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/44
        
        This was quite chaotic. We split up the team into frontend and backend and split our task in the team
        I was responsible for the Overall Design but we did not though about this when creating the Issues that this would be more efficient and therefore should be done.
        I then created this Task late Sunday to show my progress but it should have been splitted up more like this:
        Therefore I have currently just one task done on github.

        More Structured would have been:
            Issue 44 - Creating new button to click that gets you to the Game Site
            Issue x - Coming up with a fitting design for the Game Risk that other member can copy paste from. (General Design Idea)
            Issue y - Changing in Login Screen the Inputs to *
            Issue z - Create FormField and FormFieldPassword to clean up Duplicated code in the corresponding Login and Registration tsx Files. (Clean up)


    Description:
    Created a button that gets you to the new /Game site.
        Come up with a first Design at 28.3 -> Real Struggle with sccs
        After Team Inputs I redesigned everything again and came up with the current Implementation choice.
        Create SCCS Files TitleScreen
        Create RiskMainScreen.tsx
        Added FormField and FormFieldPassword
        Adjusted Login and Registration Code

#### Robin Burkard (roburkUZH):

	08.04.2024
		Links:

            Issue #58:
            https://github.com/orgs/sopra-fs24-group-35/projects/3/views/1?pane=issue&itemId=57223770
            Issue #57:
            https://github.com/orgs/sopra-fs24-group-35/projects/3/views/1?pane=issue&itemId=57223773
            Issue #59:
            https://github.com/orgs/sopra-fs24-group-35/projects/3/views/1?pane=issue&itemId=57223767

        Description:

            I did the following:
            For Issue #58:
            - Added REST controller and coded GET, POST, PUT and DELETE methods
            - Did some successful testing of all four methods using Postman (after also doing Issues #57 and #59)
            - Authentication currently outcommented since branch with LobbyService not merged with branch containing GameController

            For Issue #57:
            - Created Game entity
            - Created all the compositions of the Game entity, e.g. Board, Continent, Territory, Phase, Player, RiskCard, TurnCycle
            - Defined JPA associations (compositions) between those entities (such as @OneToMany, @OneToOne)

            For Issue #59:
            - Created JPA repository for the Game entity
            - Created GameService with methods to create, fetch, update and delete game data
            - For the createGame method, I also implemented an initialization method which creates all the territories needed. It's not complete yet, since it needs to be extended in other engineering tasks.

            I have spent a lot of time researching on how to implement the associations between the entities such that they are stored in the database in a meaningful way. I first tried to use @Embeddable and @Embedded, but it turned out that this isn't the right way to go. Finally, I learned about @OneToOne and @OneToMany annotations which turned out to work properly. However, every entity had to be equipped with a primary key (ID).
            Currently, the initializeGame() function in the GameService file is huge regarding number of lines. Maybe there is a way to strip it down. But first, we aim to achieve a "minimum viable product".
            When scrolling through the server-sided engineering tasks on GitHub that aren't assigned yet, I noticed that there exist some which I have done by a lare part by accident, because of some overlapping with the three engineering tasks above. However I think this isn't necessarily a bad thing, because the way is going to be well paved when they're being implemented in the future. 

    15.04.2024
		Links:

            Issue #60:
            https://github.com/orgs/sopra-fs24-group-35/projects/3/views/7?pane=issue&itemId=57223763
            Issue #55:
            https://github.com/orgs/sopra-fs24-group-35/projects/3/views/7?pane=issue&itemId=57215139
            Issue #27:
            https://github.com/orgs/sopra-fs24-group-35/projects/3/views/7?pane=issue&itemId=57112487
            Issue #54:
            https://github.com/orgs/sopra-fs24-group-35/projects/3/views/7?pane=issue&itemId=57215140
            Issue #34:
            https://github.com/orgs/sopra-fs24-group-35/projects/3/views/7?pane=issue&itemId=57215141
            Issue #31:
            https://github.com/orgs/sopra-fs24-group-35/projects/3/views/7?pane=issue&itemId=57215143
            Issue #32:
            https://github.com/orgs/sopra-fs24-group-35/projects/3/views/7?pane=issue&itemId=57215142

        Description:

            I did the following:
            For Issue #60:
            - Implemented by Marlon, I did some refinements

            For Issue #55:
            - Created executeAttack function which performs an attack with dice rolling and performing all the consequences of the dice results
            - Created a new attack entity and an attack DTO which can be sent by the clients to perform an attack
            - Tested it with Postman

            For Issue #27:
            - This one was a bit redundant, since the card object has already been created by a large part.

            For Issue #54:
            - This one was also a bit redundant, because the territory entities were already implemented

            For Issue #34:
            - Closely related to #55
            - I have designed the executeAttack function such that the attacker can choose how many troops he wants to attack with. The information is sent via the attack DTO.

            For Issues #31 and #32:
            - Created a new GameService function executeMultipleAttacks which can execute multiple attacks in a row as long as enough troops are left or a specified number defined by the client and sent by the attack DTO ('repeats') is reached.
            - Tested it with Postman

            The biggest part was the implementation of the executeAttack function. A lot of thinking went into this, not only the best way to implement the dice rolling and evaluation, but also how the client should tell the server to execute an attack. Originally this was planned to be done via the Game DTO, but it appeared to be much more easy to create a new attack DTO for this, despite violation of our REST specification, but this is an advantage of an agile process.

    22.04.2024
		Links:

			Issue #69:
			https://github.com/orgs/sopra-fs24-group-35/projects/3/views/7?pane=issue&itemId=60045233
			Issue #35:
			https://github.com/orgs/sopra-fs24-group-35/projects/3/views/7?pane=issue&itemId=57114279

        Description:

        I did the following:
        For Issue #69:
        - Written Rest controller tests for GameController
        - Written GameRepository integration tests
        - Written GameService unit tests
        - Written GameService integration tests
        - Jacoco-Report

        For Issue #35:
        - Created code such that the server calculates the amount of troops a player should get each round
        - The result is saved to the player object, and can be read by the client

        A lot of time went into testing and it took longer than those 8h suggested by the dev task. The main issue was several errors appearing, especially with the integration tests. The GameService integration tests for example worked individually just fine, but failed when run together. It took a long time to figure that out.




#### Marlon Anderes [(SylverSezari)]:
    08.04.2024
        Links:
            Issue #49:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/49

            Issue #24:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/24

            Issue #61:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/61

            I created the Lobby Class, LobbyController and LobbyService and everything else to do with Lobby in the server and modified the user so it fits our purposes. I also created the UserList class so it is easier to return a list of users.


    15.04.2024
        Links:
            Issue #60:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/60

            Issue #67
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/67

            Issue #68
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/68

            I have linked the lobby and game together with Robin. I have also created a new function in GameController so all players are created and added to the game.
            I have also created a way to randomly distribute territories but it is not yet comiited.

