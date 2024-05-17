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

    22.04.2024
        Links:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/20
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/18
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/15
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/11

        Description:
            Added a modal which opens after selecting two territories to handle the attacks, shows the defending user and attacking user,
            shows the defending territory name and attacking territory name and their respective troop amounts. Also added selectors to
            select the amount of troops the attacker wants to use to attack and another selector to select how many times the attacker
            attacks.
    
    29.04.2024
        Links:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/14
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/13
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/11
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/15
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/20
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/18

        Description:
            Fixed many issues/bugs and connected the main game screen to the back-end. 
            Added the attack modal to the main game screen. 
            Fixed issues with troop movement. 
            Fixed issues with movement after attack. Implemented game model on 
            the client-side. Helped updating the screen for all users. 
            Improved avatar selection on registration screen.
            Connected client-side buttons to server territories so they update their numbers when the server gets updated.
            Improve game flow and UI "prettiness".
            Various other fixes and implementations.
    
    06.05.2024
        Links:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/39
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/38
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/41
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/43

        Description:
            Added the RiskCardModal.tsx for the user to view his cards and trade if possible.
            At the moment not connected to the backend.
            Otherwise everything went smooth.
    
    13.05.2024
        Links:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/39
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/126

        Description:
            Merged the risk card functionality into the development branch and connected it to the backend.
            Added general quality of life changes to the game loop:
                - Automatically changes the phase after placing all troops
                - Automatically ends turn after moving troops in the movement phase
                - Automatically opens the card modal if the player has 5 or more cards at the beginning of their turn
                - etc.
            Tried to make the player information in the game modular to improve scalability, but eventually stopped after facing flickering issues and Ralph found a solution. 

#### Nico (Eposs111)

    08.04.2024
        Links: 
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/39
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/37
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/36
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/24

        Description:
            I worked/help worked on the design aspect of the Website in the frontend. We focused on making a (kind of) good looking website
            and implemented the Join and Create Lobby site/buttons. (DISCLAIMER: we were a team of three in the FrontEnd therefor we all helped 
            each other if needed). 


    15.04.2024
        Links:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/45
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/46
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/47

        Description:
            I worked on the external API. I integrated it and made a mechanism to change and safe the avatar on registration. 
            My problem was, that I started wrong and had to restart halfway through.

    22.04.2024
        Links:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/49
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/48

        Description:
            I was busy displaying the Avatars on the bottom right part of the screen. I took about 10 hours to accomplish this because a set function 
            did not work. I managed to fix it in the end but have no idea how. Further I fixed some other bugs and refined in issue 48. I also
            made sure that the Lobby redirects to "/risk/gameId" instead of "/risk". (All of this worked once but somehow now there are errors
            despite me not doing anything before it broke)

    29.04.2024:
        I canno't provide a detailed Links to issues I was bussy to bugfix whatever didn't work. Some of them:

        https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/49
        https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/50
        https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/18
        https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/34

        Description:
        Bugfixes after Bugfixes to get the minimal working program to work for the submission. The bugfixes were limited to 
        the frontend. It included the display of the Avatars, adding and fixing a Win/Loose screen and fixing some error 
        would not let you start the lobby.

    06.05.2024:
        Unfortunately I had to go to the hospital the last Tuesday and they only let me go this morning. So I wasn't able 
        to do any Tasks this week. I apologise to my teammtates. I will do more next week. (Doctors Note and Confirmation
        of Stationary stay has been sent to my TA Nils Grob)
        
    13.05.2024:
        After a quick break at home I was readmitted to the hospitl due to shortness of breath. So I only managed to do:
        
        https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/124

        Here I coded a loading screen that shows up if the game is not fully loaded to not disturb the game feel.

    20.05.2024:

        So far I did:

        https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/86
        https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/87
        https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/145

        These include mostly the correct display of a Win-/Loosescreen and soe "cosmetic" and convenience updates like not
        showing the announcements to a player that has already lost or won.
        
            
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

    15.04.2024  -> WIll be Updated
        Links:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/45
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/46
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/47

        Description:
            I worked on the external API. I integrated it and made a mechanism to change and safe the avatar on registration. 
            My problem was, that I started wrong and had to restart halfway through.

    22.04.2024
        Links:
            Issue #26:
            https://github.com/orgs/sopra-fs24-group-35/projects/3/views/7?pane=issue&itemId=57221163
            Issue #27:
            https://github.com/orgs/sopra-fs24-group-35/projects/3/views/7?pane=issue&itemId=57221161
            Issue #12:
            https://github.com/orgs/sopra-fs24-group-35/projects/3/views/7?pane=issue&itemId=57215178
            Issue #11 (Mechanic Implemented):
            https://github.com/orgs/sopra-fs24-group-35/projects/3/views/7?pane=issue&itemId=57215179
            Issue #75:
            https://github.com/orgs/sopra-fs24-group-35/projects/3/views/7?pane=issue&itemId=60475125

        Description:
    
        I did the following:
        For Issue #26, #27, #12 and #11:
        - Updated Design for the Game itself
        - Implemented the tracking of the buttons only in the picture. Also if the picture Ratio is too wide or to high.
        - Redifined buttons for easier use of multiple of them. 
        - Written Code that buttons Redefine themselves for the ratio of the picture
        - Implemented all the Button on the correct location of the map with the corresponding id
    
        For Issue #75:
        - Implemented Tracking of the last two clicked buttons
        - Implemnted a Line and a arrow from Territorie A to territorie B for better visability
        - Implemented Math Formula that for all distances the arrow is visable
        - Implemented reset when somebody Clicks another Territorie
        - Redesigned this Task because screen was always flickering while reloading

        Overall:
        I struggled really hard on this task: Implemented the tracking of the buttons only in the
        picture. Also if the picture Ratio is too wide or to high. If the picture ratio is always the 
        same it's not a problem but it I pull the window a new column left and right and on top and on
        button can appear that also scales with the picture itself. Was really hard to find this fix. 


    26.04.2024
        Links:
            Issue #17
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/17
            Issue 12: 
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/12
            Issue 3:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/3
            Issue #10:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/10
            Issue #10:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/10
            Issue #14:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/14
            Issue #13:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/13
            Issue #20 (Mechanic Implemented):
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/20
            Issue #75:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/75
            Issue #81:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/81
            Issue #30:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/30
            Issue #32:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/32 
            Issue #49:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/49
            Issue #57:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/57
            Issue #58:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/58
            Issue #59:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/59      
            Issue #106:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/106 -
            Issue #107:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/107 -
            (Probably a few More)

        Description:
        Yeah I coded a few hours xd
        This sums up our week: 
        https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/commit/2fdf1fed368810ba84ca497cd906b38a07a41098


    6.05.2024
        Links:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/111
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/112
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/110
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/9
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/86
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/87

        Description:
            Redesign the Avatars that only those are visible that are also in the Game 
            Implement functionality for visability of troop amounts and amount of territories for all players
            When only one player is left in the lobby the game should display a win screen (Had a bug before)
            When a player has no more troops it should display a defeat screen (Had also a bug)
            Changing Layout of Game for better Visibility -> Changed Avatar that only Players getting displayed
            Changed Avatars to the right side 
            Added Leave Button and Additional Modal that you cannot accidentally leave the game



    13.06.2024
        Links:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/125
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/9
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/118
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/112
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/120

        Description:
            125 -> Add Symbols and Pictures to Avatars when Players loose or quit / Also added a arrow that points to the current Player 
            9 -> Full leaving implementation for the frontend
            118 -> When a Player wins and then leaves the lobby gets terminated and all other players are getting kicked after a 10 second cooldown.
            112 -> Adding Troop Amount display and amount of territoroes for all Player and updating those.
            120 -> For all the Players I also added the Username underneath the Avatars to know exactly who you are



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

    26.04.2024
		Links:

            Issue #82:
            https://github.com/orgs/sopra-fs24-group-35/projects/3/views/7?pane=issue&itemId=60872647
            Issue #51:
            https://github.com/orgs/sopra-fs24-group-35/projects/3/views/7?pane=issue&itemId=60919114

        Description:

        I did the following:
        For Issue #82:
        - Created the title screen for the game
        - Designed a logo using Procreate for the title screen
        - Adjustments in the main menu such that it starts with the title screen with all important menu options
        - Added the logo also in the login screen

        For Issue #51:
        - Completely redrawn the background image of the map using Procreate, such that it looks way nicer and our application doesn't contain 'stolen' images anymore.
        - Also changed the background image for the main menu/title screen

        I have also spent a lot of time with bugfixing and helping to make the client work properly.

    06.05.2024
		Links:

            Issue #42:
            https://github.com/orgs/sopra-fs24-group-35/projects/3/views/9?pane=issue&itemId=57251785
            Issue #28:
            https://github.com/orgs/sopra-fs24-group-35/projects/3/views/9?pane=issue&itemId=57112873

        Description:

        I did the following:
        For Issue #42:
        - Designed Risk Cards
        - Created images for the Risk Cards
        - Created a scrollable React component for the cards

        For Issue #28:
        - Created pullCard method in the server
        - Added automatic card pull after a player wins a territory, but only once in a turn
        - Also added a REST method including a test to manually pull a card by the client

    13.05.2024
        Links:

            Issue #113:
            https://github.com/orgs/sopra-fs24-group-35/projects/3/views/9?pane=issue&itemId=61996187
            Issue #115:
            https://github.com/orgs/sopra-fs24-group-35/projects/3/views/9?pane=issue&itemId=61999091
            Issue #123:
            https://github.com/orgs/sopra-fs24-group-35/projects/3/views/9?pane=issue&itemId=62438121

        Description:

        I did the following:
        For Issue #113:
        - Implemented the cards in the server such that there are 44 distinct Risk cards, which are managed by the server. Cards are no longer generated randomly, but are predefined according to the actual Risk game.

        For Issue #115:
        - Created a new REST endpoint such that the client can trade Risk cards.
        - Created a method to trade cards for a fixed troop bonus.
        
        For Issue #123:
        - Modified the method to calculate bonusses when trading Risk cards depending on which cards are traded in and which territories the player owns.
        - The method can also handle joker cards.

        I also created an image for the joker RISK cards.






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


    22.04.2024
        Links:
            Issue #46:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/46

            Issue #47:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/47

            Issue #68:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/68

            Issue #73:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/73

            Issue #70:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/70

            In Progress
            Issue #71:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/71

        I have implemented the necessary methods to initiate a random beginning at the start of a new game. I have also made a function to return just one territory for the client.
        I have made various smaller changes to the code to make it easier for the client. I have started and am nearly finished with writing test for the LobbyController and LobbyService.

    26.04.2024
        Links:
            Issue #33:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/33

            Issue #83:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/83

            Issue #14:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/14

        I have implemented the continent bonus and fixed various bug that appeared when testing our servers. I have also done all the merging into the two main branches.
        I also aided the client at various tasks, like fixing the flickering and getting the screens of non current players to update.

    06.05.2024
        Links:
            Issue #90:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/90

            Issue #89:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/89

            Issue #92:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/92

            Issue #91:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/91

            Issue #109:
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-server/issues/109

        I tried to implement to leaveGame function but at the moment not everything is working. I also needed to change the primary Key of the entity palyer.
        I also fixed some bugs that we discovered after the presentation like a person being in the lobby twice.

