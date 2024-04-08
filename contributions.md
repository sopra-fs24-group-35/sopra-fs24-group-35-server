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


#### Nico (Eposs111)

    08.04.2024
        Links: 
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/39
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/44
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/37
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/36
            https://github.com/sopra-fs24-group-35/sopra-fs24-group-35-client/issues/24

    Description:
        I worked/help worked on the design aspect of the Website in the frontend. We focused on making a (kind of) good looking website
        and implemented the Join and Create Lobby site/buttons. (DISCLAIMER: we were a team of three in the FrontEnd therefor we all helped each           other if needed)


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

