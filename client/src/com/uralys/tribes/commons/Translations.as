package com.uralys.tribes.commons
{

import mx.collections.ArrayCollection;

/**
0:fr
1:en
2:us
**/
public class Translations{

	//-----------------------------------------------------------------------------------//

	public static var idMessage:String = "5";
	
	[Bindable]
	public static var G_MESSAGE:ArrayCollection = new ArrayCollection(
									["Bienvenue sur la version 1.3 !\n\n" +
										"La carte a été agrandie, \n\n" +
										"Les rapports de combats sont en place !\n\n" +
										"Il est possible de tirer à distance avec les arcs\n\n" +
										"Faites un tour sur le forum pour proposer des évolutions du jeu\n" +
										"Ou pour remonter un bug.\n",
									"Welcome on the the 1.3 !\n\n" +
										"The map is now bigger, \n\n" +
										"You have access to the Fight Reports !\n\n" +
										"You may now on shoot with the bows.\n\n" +
										"Go see the forum to suggest a game evolution,\n" +
										"Or to tell us about un bug you found.",
									"Welcome on the the 1.3 !\n\n" +
										"The map is now bigger, \n\n" +
										"You have access to the Fight Reports !\n\n" +
										"You may now on shoot with the bows.\n\n" +
										"Go see the forum to suggest a game evolution,\n" +
										"Or to tell us about un bug you found.",]);
	[Bindable]
	public static var DO_NOT_SHOW_MESSAGE:ArrayCollection = new ArrayCollection(
									["Ne pas me montrer ce message la prochaine fois",
									"Do not show me that message next time",
									"Do not show me that message next time"]);
	
	//-----------------------------------------------------------------------------------//

	[Bindable]
	public static var WELCOME:ArrayCollection = new ArrayCollection(
									["Profil Uralys créé. Bienvenue !",
									"Uralys profile created. Welcome !",
									"Uralys profile created. Welcome !"]);

	[Bindable]
	public static var SETTINGS:ArrayCollection = new ArrayCollection(
									["Paramètres",
									"Settings",
									"Settings"]);
	
	[Bindable]
	public static var HOW_TO_PLAY:ArrayCollection = new ArrayCollection(
									["Comment jouer",
									"How to play",
									"How to play"]);

	[Bindable]
	public static var NAME_EXISTS:ArrayCollection = new ArrayCollection(
									["Nom déjà existant",
									"Name exists",
									"Name exists"]);


	[Bindable]
	public static var OPEN_TAB:ArrayCollection = new ArrayCollection(
									["S'ouvre dans une autre onglet",
									"Opens in a new tab",
									"Opens in a new tab"]);
	
	[Bindable]
	public static var CHECK_EMAIL:ArrayCollection = new ArrayCollection(
									["Vérifiez votre email",
									"Check your email",
									"Check your email"]);
	
	[Bindable]
	public static var CHECK_PWD:ArrayCollection = new ArrayCollection(
									["Minimum 3 caractères pour le mot de passe",
									"Password must be at least 3 characters",
									"Password must be at least 3 characters"]);

	[Bindable]
	public static var LESS_THAN_A_MINUTE:ArrayCollection = new ArrayCollection(
									["Moins d'un minute",
									"Less than a minute",
									"Less than a minute"]);
	
	//-----------------------------------------------------------------------------------//

	[Bindable]
	public static var LOG_IN:ArrayCollection = new ArrayCollection(
									["Se connecter",
									"Sign in",
									"Sign in"]);

	[Bindable]
	public static var LOG_AS:ArrayCollection = new ArrayCollection(
									["Connecté avec",
									"Logged as",
									"Logged as"]);

	[Bindable]
	public static var PASSWORD:ArrayCollection = new ArrayCollection(
									["Mot de passe",
									"Password",
									"Password"]);
	
	[Bindable]
	public static var CREATE_ACCOUNT:ArrayCollection = new ArrayCollection(
									["Créer mon compte",
									"Create my account",
									"Create my account"]);
	
	[Bindable]
	public static var LOG_ME_IN:ArrayCollection = new ArrayCollection(
									["Connexion",
									"Log me in",
									"Log me in"]);

	[Bindable]
	public static var GO_TO_FORUM:ArrayCollection = new ArrayCollection(
									["Aller sur le forum",
									"Go to the forum",
									"Go to the forum"]);
	[Bindable]
	public static var INFO:ArrayCollection = new ArrayCollection(
									["Info",
									"Info",
									"Info"]);

	[Bindable]
	public static var BOARDS:ArrayCollection = new ArrayCollection(
									["Classements",
									"Boards",
									"Boards"]);

	[Bindable]
	public static var LOADING_IMAGES:ArrayCollection = new ArrayCollection(
									["Téléchargement des images. Merci de patienter",
									"Downloading images. Please wait.",
									"Downloading images. Please wait."]);
	
	//-----------------------------------------------------------------------------------//

	[Bindable]
	public static var VALIDATE_MODIFICATIONS:ArrayCollection = new ArrayCollection(
									["Enregistrer les modifications",
									"Save Changes",
									"Save Changes"]);

	[Bindable]
	public static var CELL:ArrayCollection = new ArrayCollection(
									["Case",
									"Cell",
									"Cell"]);

	[Bindable]
	public static var WAY_SELECTION:ArrayCollection = new ArrayCollection(
									["Edition du chemin de l'unité",
									"Unit way edition",
									"Unit way edition"]);
	[Bindable]
	public static var TOO_LONG:ArrayCollection = new ArrayCollection(
									["Distance trop longue. Choisir une case à côté du dernier deplacement",
									"Too long way. Choose a cell next to your last move",
									"Too long way. Choose a cell next to your last move"]);

	[Bindable]
	public static var WRONG_SHOOT:ArrayCollection = new ArrayCollection(
									["Choisir une case cible !",
									"Choose a target cell !",
									"Choose a target cell !"]);

	[Bindable]
	public static var SHOOT_PROCEEDING:ArrayCollection = new ArrayCollection(
									["Tirs en cours. Rapport dans 3 secondes.",
									"Shooting on the target. Report within 3 secs.",
									"Shooting on the target. Report within 3 secs."]);
	[Bindable]
	public static var NO_TARGET:ArrayCollection = new ArrayCollection(
									["Pas de cible possible",
									"No available target",
									"No available target"]);
	[Bindable]
	public static var LIMIT_NB_MOVES:ArrayCollection = new ArrayCollection(
									["10 déplacements en mémoire maximum",
									"10 recorded moves max",
									"10 recorded moves max"]);
	[Bindable]
	public static var CITY_STOP:ArrayCollection = new ArrayCollection(
									["Arrêt dans une ville",
									"Stop by a city",
									"Stop by a city"]);

	[Bindable]
	public static var PROGRESS_BARS:ArrayCollection = new ArrayCollection(
									["Montrer les barres de temps",
									"Show the progress bars",
									"Show the progress bars"]);

	[Bindable]
	public static var LANDS:ArrayCollection = new ArrayCollection(
									["Contrées",
									"Lands",
									"Lands"]);
	[Bindable]
	public static var LAND:ArrayCollection = new ArrayCollection(
									["Contrée",
									"Land",
									"Land"]);

	[Bindable]
	public static var LAND_IS_TAKEN:ArrayCollection = new ArrayCollection(
									["Contrée colonisée : rafraichissement",
									"Land conquered : refresh",
									"Land conquered : refresh"]);

	[Bindable]
	public static var SHOOT:ArrayCollection = new ArrayCollection(
									["Tirer",
									"Shoot",
									"Shoot"]);
	[Bindable]
	public static var MOVING:ArrayCollection = new ArrayCollection(
									["Dépl.",
									"Moving",
									"Moving"]);
	[Bindable]
	public static var MOVE:ArrayCollection = new ArrayCollection(
									["Déplacer",
									"Move",
									"Move"]);
	[Bindable]
	public static var SHOW_LANDS:ArrayCollection = new ArrayCollection(
									["Montrer les contrées",
									"Show the lands",
									"Show the lands"]);

	[Bindable]
	public static var YOUR_GAMES:ArrayCollection = new ArrayCollection(
									["Parties en cours",
									"Your games",
									"Your games"]);
	
	[Bindable]
	public static var NEW_GAME:ArrayCollection = new ArrayCollection(
									["Créer une partie",
									"Create a new Game",
									"Create a new Game"]);

	[Bindable]
	public static var JOIN_GAME:ArrayCollection = new ArrayCollection(
									["Rejoindre une partie",
									"Join a Game",
									"Join a Game"]);

	[Bindable]
	public static var PROFILE:ArrayCollection = new ArrayCollection(
									["Profil",
									"Profile",
									"Profile"]);

	[Bindable]
	public static var MY_PROFILE:ArrayCollection = new ArrayCollection(
									["Mon Profil",
									"My Profile",
									"My Profile"]);
	
	[Bindable]
	public static var EDIT_PROFILE:ArrayCollection = new ArrayCollection(
									["Editez votre profil",
									"Edit your profile",
									"Edit your profile"]);

	[Bindable]
	public static var MESSAGES:ArrayCollection = new ArrayCollection(
									["Messages",
									"Messages",
									"Messages"]);

	[Bindable]
	public static var WRITE_MESSAGE:ArrayCollection = new ArrayCollection(
									["Ecrire un message à ",
									"Write a message to ",
									"Write a message to "]);
	
	[Bindable]
	public static var MESSAGE_SENT_TO:ArrayCollection = new ArrayCollection(
									["Message envoyé à",
									"Message sent to ",
									"Message sent to "]);

	[Bindable]
	public static var FREE_PEOPLE:ArrayCollection = new ArrayCollection(
									["Tout le monde travaille, libérez des travailleurs !",
									"Everybody is busy, free some workers !",
									"Everybody is busy, free some workers !"]);

	[Bindable]
	public static var REMAINING_TIME:ArrayCollection = new ArrayCollection(
									["Prochaine récolte dans",
									"Next step in",
									"Next step in"]);
	[Bindable]
	public static var REMAINS:ArrayCollection = new ArrayCollection(
									["Il reste",
									"Remains",
									"Remains"]);
	
	//-----------------------------------------------------------------------------------//

	[Bindable]
	public static var CONSUME:ArrayCollection = new ArrayCollection(
									["consomment",
									"consume",
									"consume"]);

	[Bindable]
	public static var PRODUCE:ArrayCollection = new ArrayCollection(
									["produisent",
									"produce",
									"produce"]);
			
	[Bindable]
	public static var PRODUCE_SING:ArrayCollection = new ArrayCollection(
									["produit",
									"produce",
									"produce"]);

	[Bindable]
	public static var EVERY_STEP:ArrayCollection = new ArrayCollection(
									["toutes les 10 minutes",
									"every 10 minutes",
									"every 10 minutes"]);

	//-----------------------------------------------------------------------------------//

	[Bindable]
	public static var CHOOSE_YOUR_NAME:ArrayCollection = new ArrayCollection(
									["Choisir votre nom dans cette partie",
									"Choose your name",
									"Choose your name"]);

	[Bindable]
	public static var CHOOSE_GAME_NAME:ArrayCollection = new ArrayCollection(
									["Entrer un nom de partie",
									"Enter the game's name",
									"Enter the game's name"]);
	
	//-----------------------------------------------------------------------------------//

	[Bindable]
	public static var GAME_LIST:ArrayCollection = new ArrayCollection(
									["Liste des parties en cours",
									"Game list",
									"Game list"]);

	[Bindable]
	public static var GAMES_TO_JOIN:ArrayCollection = new ArrayCollection(
									["Parties à joindre",
									"Games to join",
									"Games to join"]);

	[Bindable]
	public static var REFRESH:ArrayCollection = new ArrayCollection(
									["Rafraichir",
									"Refresh",
									"Refresh"]);
	
	//-----------------------------------------------------------------------------------//

	[Bindable]
	public static var PLAYERS:ArrayCollection = new ArrayCollection(
									["Joueurs",
									"Players",
									"Players"]);
	[Bindable]
	public static var ALLIES:ArrayCollection = new ArrayCollection(
									["Alliances",
									"Allies",
									"Allies"]);
	[Bindable]
	public static var ALLY:ArrayCollection = new ArrayCollection(
									["Alliance",
									"Ally",
									"Ally"]);
	[Bindable]
	public static var MY_ALLY:ArrayCollection = new ArrayCollection(
									["Mon Alliance",
									"My Ally",
									"My Ally"]);
	[Bindable]
	public static var CREATE_ALLY:ArrayCollection = new ArrayCollection(
									["Créer une Alliance",
									"Create an Ally",
									"Create an Ally"]);
	[Bindable]
	public static var LEAVE_ALLY:ArrayCollection = new ArrayCollection(
									["Quitter l'Alliance",
									"Leave the Ally",
									"Leave the Ally"]);
	[Bindable]
	public static var DELETE_ALLY:ArrayCollection = new ArrayCollection(
									["Supprimer l'Alliance",
									"Delete the Ally",
									"Delete the Ally"]);
	[Bindable]
	public static var JOIN_ALLY:ArrayCollection = new ArrayCollection(
									["Vous avez reçu une invitation\n pour rejoindre une Alliance",
									"You received an invitation\n to join an Ally",
									"You received an invitation\n to join an Ally"]);
	[Bindable]
	public static var EDIT_ALLY_PROFILE:ArrayCollection = new ArrayCollection(
									["Editer le profil de l'Alliance",
									"Edit the Ally's profile",
									"Edit the Ally's profile"]);
	[Bindable]
	public static var INVITATION_SENT:ArrayCollection = new ArrayCollection(
									["Invitation envoyée",
									"Invitation sent",
									"Invitation sent"]);
	[Bindable]
	public static var NO_ALLY:ArrayCollection = new ArrayCollection(
									["Pas d'Alliance",
									"No Ally",
									"No Ally"]);
	[Bindable]
	public static var INVITE_IN_ALLY:ArrayCollection = new ArrayCollection(
									["Inviter dans",
									"Invite in",
									"Invite in"]);
	
	[Bindable]
	public static var INVITATION_SENT_TO:ArrayCollection = new ArrayCollection(
									["Invitation envoyée à",
									"Invitation sent to ",
									"Invitation sent to "]);
	
	[Bindable]
	public static var CHOOSE_ALLY_NAME:ArrayCollection = new ArrayCollection(
									["Nom de l'Alliance",
									"Ally's name",
									"Ally's name"]);

	[Bindable]
	public static var PLAYER:ArrayCollection = new ArrayCollection(
									["joueur",
									"player",
									"player"]);

	[Bindable]
	public static var TURN:ArrayCollection = new ArrayCollection(
									["tour",
									"turn",
									"turn"]);

	[Bindable]
	public static var NOT_LAUNCHED:ArrayCollection = new ArrayCollection(
									["Elle n'est pas encore lancee",
									"Not launched yet",
									"Not launched yet"]);

	[Bindable]
	public static var MAX_TURN_TIME:ArrayCollection = new ArrayCollection(
									["Temps max par tour",
									"Max time for a turn",
									"Max time for a turn"]);

	[Bindable]
	public static var LAUNCH_GAME:ArrayCollection = new ArrayCollection(
									["Lancer la partie",
									"Launch Game",
									"Launch Game"]);

	[Bindable]
	public static var CREATE:ArrayCollection = new ArrayCollection(
									["Créer",
									"Create",
									"Create"]);

	[Bindable]
	public static var PLAY_TURN:ArrayCollection = new ArrayCollection(
									["Jouer le tour",
									"Play you turn",
									"Play you turn"]);
	
	[Bindable]
	public static var CHOOSE_NAME:ArrayCollection = new ArrayCollection(
									["Choisir votre nom dans cette partie",
									"Choose your name for this game",
									"Choose your name for this game"]);

	[Bindable]
	public static var JOIN:ArrayCollection = new ArrayCollection(
									["Rejoindre",
									"Join",
									"Join"]);
	
	[Bindable]
	public static var WAITING_FOR_PLAYERS:ArrayCollection = new ArrayCollection(
									["En attente des autres joueurs",
									"Waiting for the other players",
									"Waiting for the other players"]);
	
	[Bindable]
	public static var NOT_ENOUGH_PLAYERS:ArrayCollection = new ArrayCollection(
									["Pas assez de joueurs",
									"Not enough players",
									"Not enough players"]);

	//-----------------------------------------------------------------------------------//
	
	[Bindable]
	public static var TIME_REMAINING:ArrayCollection = new ArrayCollection(
									["Il reste",
									"Time remaining",
									"Time remaining"]);
	
	[Bindable]
	public static var SAVE_TURN:ArrayCollection = new ArrayCollection(
									["Enregistrer le tour",
									"Save turn",
									"Save turn"]);
	
	[Bindable]
	public static var LEAVE_THIS_GAME:ArrayCollection = new ArrayCollection(
									["Quitter la partie",
									"Leave this game",
									"Leave this game"]);
	
	//-----------------------------------------------------------------------------------//

	[Bindable]
	public static var MY_CITIES:ArrayCollection = new ArrayCollection(
									["Mes Villes",
									"My cities",
									"My cities"]);

	[Bindable]
	public static var MY_UNITS:ArrayCollection = new ArrayCollection(
									["Mes unités",
									"My units",
									"My units"]);

	[Bindable]
	public static var OTHER_CITIES:ArrayCollection = new ArrayCollection(
									["Autres Villes",
									"Other cities",
									"Other cities"]);
	
	[Bindable]
	public static var CITY_BUILDING_NAME:ArrayCollection = new ArrayCollection(
									["Ville en construction",
									"City being built",
									"City being built"]);

	[Bindable]
	public static var CITY_IS_TAKEN:ArrayCollection = new ArrayCollection(
									["La ville a été prise !",
									"City taken !",
									"City taken !"]);

	[Bindable]
	public static var CITY_IS_LOST:ArrayCollection = new ArrayCollection(
									["La ville a été perdue...",
									"City lost...",
									"City lost..."]);

	[Bindable]
	public static var CITY_BUILDING_DONE:ArrayCollection = new ArrayCollection(
									["Construction terminée dans",
									"Building terminated in ",
									"Building terminated in "]);

	[Bindable]
	public static var NEW_CITY:ArrayCollection = new ArrayCollection(
									["Nouvelle Ville",
									"New City",
									"New City"]);
	
	//-----------------------------------------------------------------------------------//

	[Bindable]
	public static var UNREAD:ArrayCollection = new ArrayCollection(
									["Non lus",
									"Unread",
									"Unread"]);

	[Bindable]
	public static var READ:ArrayCollection = new ArrayCollection(
									["Lus",
									"Read",
									"Read"]);

	[Bindable]
	public static var ARCHIVES:ArrayCollection = new ArrayCollection(
									["Archives",
									"Archives",
									"Archives"]);

	[Bindable]
	public static var MAX_ARCHIVE:ArrayCollection = new ArrayCollection(
									["20 messages max aux Archives",
									"Max 20 messages in the Archives",
									"Max 20 messages in the Archives"]);
	
	
	//-----------------------------------------------------------------------------------//
	
	[Bindable]
	public static var ARMY_POPULATION:ArrayCollection = new ArrayCollection(
									["Armées/Marchands créés ce tour",
									"Armies/Merchants created this turn",
									"Armies/Merchants created this turn"]);
	
	[Bindable]
	public static var POPULATION:ArrayCollection = new ArrayCollection(
									["Population",
									"Population",
									"Population"]);

	[Bindable]
	public static var UNEMPLOYED:ArrayCollection = new ArrayCollection(
									["Sans emploi",
									"Unemployed",
									"Unemployed"]);
	
	[Bindable]
	public static var WHEAT:ArrayCollection = new ArrayCollection(
									["Blé",
									"Wheat",
									"Wheat"]);

	[Bindable]
	public static var WOOD:ArrayCollection = new ArrayCollection(
									["Bois",
									"Wood",
									"Wood"]);

	[Bindable]
	public static var WOOD_SPENT:ArrayCollection = new ArrayCollection(
									["Bois dépensé",
									"Wood Spent",
									"Wood Spent"]);

	[Bindable]
	public static var IRON:ArrayCollection = new ArrayCollection(
									["Fer",
									"Iron",
									"Iron"]);

	[Bindable]
	public static var IRON_SPENT:ArrayCollection = new ArrayCollection(
									["Fer dépensé",
									"Iron Spent",
									"Iron Spent"]);

	[Bindable]
	public static var GOLD:ArrayCollection = new ArrayCollection(
									["Or",
									"Gold",
									"Gold"]);

	[Bindable]
	public static var WORKERS:ArrayCollection = new ArrayCollection(
									["Travailleurs",
									"Workers",
									"Workers"]);

	[Bindable]
	public static var SMITHS:ArrayCollection = new ArrayCollection(
									["Forgerons",
									"Smiths",
									"Smiths"]);

	[Bindable]
	public static var WARRIORS:ArrayCollection = new ArrayCollection(
									["Guerriers",
									"Warriors",
									"Warriors"]);
	
	[Bindable]
	public static var SMITHS_AVAILABLE:ArrayCollection = new ArrayCollection(
									["Forgerons disponibles",
									"Available smiths",
									"Available smiths"]);

	[Bindable]
	public static var AVAILABLE_SMITHS:ArrayCollection = new ArrayCollection(
									["Disponibles",
									"Available",
									"Available"]);

	[Bindable]
	public static var HARVEST:ArrayCollection = new ArrayCollection(
									["Récolte",
									"Harvest",
									"Harvest"]);

	[Bindable]
	public static var SPENDING:ArrayCollection = new ArrayCollection(
									["Dépense",
									"Spending",
									"Spending"]);

	[Bindable]
	public static var STARVATION:ArrayCollection = new ArrayCollection(
									["Famine prévue ! récoltez plus de blé !",
									"Starvation ! gather more wheat !",
									"Starvation ! gather more wheat !"]);

	[Bindable]
	public static var RESOURCES:ArrayCollection = new ArrayCollection(
									["Ressources",
									"Resources",
									"Resources"]);

	[Bindable]
	public static var LISTEN_NOW:ArrayCollection = new ArrayCollection(
									["Découvrir leur ",
									"Explore their ",
									"Explore their "]);

	[Bindable]
	public static var FORGE:ArrayCollection = new ArrayCollection(
									["Forge",
									"Forge",
									"Forge"]);

	[Bindable]
	public static var EQUIPMENT:ArrayCollection = new ArrayCollection(
									["Equipement",
									"Equipment",
									"Equipment"]);

	[Bindable]
	public static var MARKET:ArrayCollection = new ArrayCollection(
									["Marché",
									"Market",
									"Market"]);

	[Bindable]
	public static var MERCHANTS:ArrayCollection = new ArrayCollection(
									["Marchands",
									"Merchants",
									"Merchants"]);

	[Bindable]
	public static var NEW_MERCHANTS:ArrayCollection = new ArrayCollection(
									["Créer une caravane",
									"New caravan",
									"New caravan"]);

	[Bindable]
	public static var NB_CARAVAN:ArrayCollection = new ArrayCollection(
									["Nombre de marchands",
									"Merchants in this caravan",
									"Merchants in this caravan"]);

	[Bindable]
	public static var CARAVAN:ArrayCollection = new ArrayCollection(
									["Caravane",
									"Caravan",
									"Caravan"]);

	[Bindable]
	public static var NB_ARMY:ArrayCollection = new ArrayCollection(
									["Nombre de guerriers",
									"Warriors in this army",
									"Warriors in this army"]);

	[Bindable]
	public static var ARMY:ArrayCollection = new ArrayCollection(
									["Armée",
									"Army",
									"Army"]);
	[Bindable]
	public static var ARMIES:ArrayCollection = new ArrayCollection(
									["Armées",
									"Armies",
									"Armies"]);

	[Bindable]
	public static var NEW_ARMY:ArrayCollection = new ArrayCollection(
									["Créer une Armée",
									"New Army",
									"New Army"]);

	[Bindable]
	public static var VALIDATE:ArrayCollection = new ArrayCollection(
									["Valider",
									"Validate",
									"Validate"]);

	[Bindable]
	public static var CANCEL:ArrayCollection = new ArrayCollection(
									["Annuler",
									"Cancel",
									"Cancel"]);

	[Bindable]
	public static var NOT_VALIDATED:ArrayCollection = new ArrayCollection(
									["Non validée !",
									"Not validated !" ,
									"Not validated !"]);

	[Bindable]
	public static var DELETE_LAST_MOVE:ArrayCollection = new ArrayCollection(
									["Annuler le dernier",
									"Cancel the last one",
									"Cancel the last one"]);

	[Bindable]
	public static var MUST_VALIDATE_UNIT:ArrayCollection = new ArrayCollection(
									["Il faut d'abord valider cette unité !",
									"You must validate this unit first !",
									"You must validate this unit first !"]);

	[Bindable]
	public static var CITIES:ArrayCollection = new ArrayCollection(
									["Villes",
									"Cities",
									"Cities"]);

	[Bindable]
	public static var UNITS:ArrayCollection = new ArrayCollection(
									["Unités",
									"Units",
									"Units"]);

	[Bindable]
	public static var UNIT:ArrayCollection = new ArrayCollection(
									["Unité",
									"Unit",
									"Unit"]);

	[Bindable]
	public static var CITY:ArrayCollection = new ArrayCollection(
									["Ville",
									"City",
									"City"]);

	[Bindable]
	public static var READY:ArrayCollection = new ArrayCollection(
									["Pret à ",
									"ready at",
									"ready at"]);

	[Bindable]
	public static var BUYING:ArrayCollection = new ArrayCollection(
									["Achat",
									"Buying",
									"Buying"]);

	[Bindable]
	public static var UNIT_COST:ArrayCollection = new ArrayCollection(
									["Coût unitaire",
									"Unit cost",
									"Unit cost"]);

	[Bindable]
	public static var BUY:ArrayCollection = new ArrayCollection(
									["Effectuer l'achat",
									"Buy",
									"Buy"]);

	[Bindable]
	public static var SELL:ArrayCollection = new ArrayCollection(
									["Effectuer la vente",
									"Sell",
									"Sell"]);

	[Bindable]
	public static var PROFIT:ArrayCollection = new ArrayCollection(
									["Profit",
									"Profit",
									"Profit"]);

	[Bindable]
	public static var TOTAL_PROFIT:ArrayCollection = new ArrayCollection(
									["Profit Total",
									"Total Profit",
									"Total Profit"]);

	[Bindable]
	public static var QUANTITY:ArrayCollection = new ArrayCollection(
									["Quantité",
									"Quantity",
									"Quantity"]);

	[Bindable]
	public static var PRICE_TO_PAY:ArrayCollection = new ArrayCollection(
									["Coût",
									"Cost",
									"Cost"]);

	[Bindable]
	public static var TOTAL_SPENDING:ArrayCollection = new ArrayCollection(
									["Coût total",
									"Total cost",
									"Total cost"]);

	[Bindable]
	public static var SELLING:ArrayCollection = new ArrayCollection(
									["Vente",
									"Selling",
									"Selling"]);

	[Bindable]
	public static var MINIMUM:ArrayCollection = new ArrayCollection(
									["Min.",
									"Min.",
									"Min."]);

	[Bindable]
	public static var MAY_BUILD_CITY:ArrayCollection = new ArrayCollection(
									["Ok pour une ville !",
									"Ok for a new city !",
									"Ok for a new city !"]);

	[Bindable]
	public static var MAY_NOT_BUILD_CITY:ArrayCollection = new ArrayCollection(
									["Pas suffisant pour une ville",
									"Not enough for a new city",
									"Not enough for a new city"]);

	[Bindable]
	public static var CONFLICT:ArrayCollection = new ArrayCollection(
									["Conflit",
									"Conflict",
									"Conflict"]);
	
	[Bindable]
	public static var GATHERING:ArrayCollection = new ArrayCollection(
									["Rassemblement",
									"Gathering",
									"Gathering"]);
	[Bindable]
	public static var BOW_SHOT:ArrayCollection = new ArrayCollection(
									["Tir d'arcs",
									"Bow shots",
									"Bow shots"]);
	
	[Bindable]
	public static var RESULT:ArrayCollection = new ArrayCollection(
									["Résultat",
									"Result",
									"Result"]);
	
	[Bindable]
	public static var WINNER:ArrayCollection = new ArrayCollection(
									["Vainqueur",
									"Winner",
									"Winner"]);
	
	[Bindable]
	public static var OWNER:ArrayCollection = new ArrayCollection(
									["Chef",
									"Owner",
									"Owner"]);
	
	//-----------------------------------------------------------------------------------//

	[Bindable]
	public static var REQUIRES:ArrayCollection = new ArrayCollection(
									["requiert",
									"requires",
									"requires"]);
	[Bindable]
	public static var ADD:ArrayCollection = new ArrayCollection(
									["ajoute",
									"adds",
									"adds"]);
	[Bindable]
	public static var BOW:ArrayCollection = new ArrayCollection(
									["Arc",
									"Bow",
									"Bow"]);
	[Bindable]
	public static var BOWS:ArrayCollection = new ArrayCollection(
									["Arcs",
									"Bows",
									"Bows"]);

	[Bindable]
	public static var SWORD:ArrayCollection = new ArrayCollection(
									["Epée",
									"Sword",
									"Sword"]);
	[Bindable]
	public static var SWORDS:ArrayCollection = new ArrayCollection(
									["Epées",
									"Swords",
									"Swords"]);

	[Bindable]
	public static var ARMOR:ArrayCollection = new ArrayCollection(
									["Armure",
									"Armor",
									"Armor"]);
	[Bindable]
	public static var ARMORS:ArrayCollection = new ArrayCollection(
									["Armures",
									"Armors",
									"Armors"]);

	//-----------------------------------------------------------------------------------//
	
	[Bindable]
	public static var RESOURCES_UPDATED:ArrayCollection = new ArrayCollection(
									["Ressources arrivées !",
									"Resources received !",
									"Resources received !"]);

	//-----------------------------------------------------------------------------------//
	
	[Bindable]
	public static var STOCK:ArrayCollection = new ArrayCollection(
									["Stock",
									"Stock",
									"Stock"]);
	
	[Bindable]
	public static var PRODUCTION:ArrayCollection = new ArrayCollection(
									["Production",
									"Production",
									"Production"]);
	
	[Bindable]
	public static var PREVISION:ArrayCollection = new ArrayCollection(
									["Prévisions",
									"Expectations",
									"Expectations"]);
	
	[Bindable]
	public static var CARRIAGE:ArrayCollection = new ArrayCollection(
									["Transport",
									"Carriage",
									"Carriage"]);
	
	[Bindable]
	public static var AVAILABLE_STOCK:ArrayCollection = new ArrayCollection(
									["Stock disponible",
									"Available stock",
									"Available stock"]);
	
	//-----------------------------------------------------------------------------------//
	
	[Bindable]
	public static var BUILD_HERE:ArrayCollection = new ArrayCollection(
									["Construire une ville ici",
									"Build a city here",
									"Build a city here"]);
	
	[Bindable]
	public static var ENTER_CITY:ArrayCollection = new ArrayCollection(
									["Entrer dans la ville",
									"Enter city",
									"Enter city"]);
	
	//-----------------------------------------------------------------------------------//
	
	[Bindable]
	public static var SIZE:ArrayCollection = new ArrayCollection(
									["Taille",
									"Size",
									"Size"]);
	
	[Bindable]
	public static var NEW:ArrayCollection = new ArrayCollection(
									["Nouveau !",
									"New",
									"New"]);

	//-----------------------------------------------------------------------------------//
	
	[Bindable]
	public static var DETAILS:ArrayCollection = new ArrayCollection(
									["Détails",
									"Details",
									"Details"]);

	[Bindable]
	public static var IMAGES:ArrayCollection = new ArrayCollection(
									["Images",
									"Images",
									"Images"]);

	[Bindable]
	public static var TEXTS:ArrayCollection = new ArrayCollection(
									["Textes",
									"Texts",
									"Texts"]);

	//-----------------------------------------------------------------------------------//
	
	[Bindable]
	public static var CLICK_HERE:ArrayCollection = new ArrayCollection(
									["Cliquez ici",
									"Click here",
									"Click here"]);
	
	[Bindable]
	public static var OK:ArrayCollection = new ArrayCollection(
									["Ok",
									"Ok",
									"Ok"]);
	[Bindable]
	public static var ACCEPT:ArrayCollection = new ArrayCollection(
									["Accepter",
									"Accept",
									"Accept"]);
	[Bindable]
	public static var REFUSE:ArrayCollection = new ArrayCollection(
									["Refuser",
									"Refuse",
									"Refuse"]);
	[Bindable]
	public static var NEWNAME:ArrayCollection = new ArrayCollection(
									["Nouveau nom",
									"New name",
									"New name"]);
	
	[Bindable]
	public static var DELETE:ArrayCollection = new ArrayCollection(
									["Supprimer",
									"Delete",
									"Delete"]);

	[Bindable]
	public static var ARCHIVE:ArrayCollection = new ArrayCollection(
									["Archiver",
									"Archive",
									"Archive"]);
	[Bindable]
	public static var HELP:ArrayCollection = new ArrayCollection(
									["Aide",
									"Help",
									"Help"]);
	[Bindable]
	public static var BACK:ArrayCollection = new ArrayCollection(
									["Retour",
									"Back",
									"Back"]);
	[Bindable]
	public static var CLOSE:ArrayCollection = new ArrayCollection(
									["Fermer",
									"Close",
									"Close"]);
	[Bindable]
	public static var SKIP:ArrayCollection = new ArrayCollection(
									["Passer",
									"Skip",
									"Skip"]);
	[Bindable]
	public static var GAME:ArrayCollection = new ArrayCollection(
									["Partie",
									"Game",
									"Game"]);
									
	[Bindable]
	public static var EXIT:ArrayCollection = new ArrayCollection(
									["Quitter la partie",
									"Leave the game",
									"Leave the game"]);
	[Bindable]
	public static var TIME:ArrayCollection = new ArrayCollection(
									["Temps",
									"Time",
									"Time"]);
	[Bindable]
	public static var SPEED:ArrayCollection = new ArrayCollection(
									["Vitesse",
									"Speed",
									"Speed"]);
	[Bindable]
	public static var POINT:ArrayCollection = new ArrayCollection(
									["Point",
									"Point",
									"Point"]);
	[Bindable]
	public static var CREDITS:ArrayCollection = new ArrayCollection(
									["Crédits",
									"Credits",
									"Credits"]);
	[Bindable]
	public static var BONUS:ArrayCollection = new ArrayCollection(
									["Bonus",
									"Bonus",
									"Bonus"]);
	[Bindable]
	public static var PRICE:ArrayCollection = new ArrayCollection(
									["Prix",
									"Price",
									"Price"]);
	[Bindable]
	public static var MUSIC:ArrayCollection = new ArrayCollection(
									["Musique",
									"Music",
									"Music"]);
	[Bindable]
	public static var CREATOR:ArrayCollection = new ArrayCollection(
									["Conception et réalisation",
									"Design and development",
									"Design and development"]);

	[Bindable]
	public static var GRAPHIST:ArrayCollection = new ArrayCollection(
									["Graphistes",
									"Graphists",
									"Graphists"]);

	[Bindable]
	public static var TOOLS:ArrayCollection = new ArrayCollection(
									["Outils",
									"Tools",
									"Tools"]);
	[Bindable]
	public static var CONQUERING_LAND:ArrayCollection = new ArrayCollection(
									["Contrée",
									"Land",
									"Land"]);
	
	//-----------------------------------------------------------------------------------//

	[Bindable]
	public static var CHOOSE:ArrayCollection = new ArrayCollection(
									["Choisir",
									"Choose",
									"Choose"]);
	[Bindable]
	public static var CONTINUE:ArrayCollection = new ArrayCollection(
									["Continuer",
									"Continue",
									"Continue"]);
	[Bindable]
	public static var VICTORY:ArrayCollection = new ArrayCollection(
									["Victoire !",
									"Victory !",
									"Victory !"]);
	[Bindable]
	public static var DEFEAT:ArrayCollection = new ArrayCollection(
									["Défaite",
									"Defeat",
									"Defeat"]);
	[Bindable]
	public static var DRAW:ArrayCollection = new ArrayCollection(
									["Egalité",
									"Draw",
									"Draw"]);
	[Bindable]
	public static var ATTACKED:ArrayCollection = new ArrayCollection(
									["Vous avez été attaqué !",
									"You've been attacked !",
									"You've been attacked !"]);

	//-----------------------------------------------------------------------------------//

	[Bindable]
	public static var SELECTION:ArrayCollection = new ArrayCollection(
									["Sélection",
									"Selection",
									"Selection"]);
	[Bindable]
	public static var TO:ArrayCollection = new ArrayCollection(
									["pour",
									"to",
									"to"]);
	[Bindable]
	public static var USED:ArrayCollection = new ArrayCollection(
									["utilisés",
									"used",
									"used"]);
	[Bindable]
	public static var SELECT:ArrayCollection = new ArrayCollection(
									["Sélectionner",
									"Select",
									"Select"]);
	[Bindable]
	public static var LOADING:ArrayCollection = new ArrayCollection(
									["Enregistrement",
									"Loading",
									"Loading"]);
	[Bindable]
	public static var REPLAY:ArrayCollection = new ArrayCollection(
									["Voir le combat !",
									"Show the battle !",
									"Show the battle !"]);
	[Bindable]
	public static var THE_ENNEMY:ArrayCollection = new ArrayCollection(
									["Armée Ennemie",
									"The ennemy",
									"The ennemy"]);
	[Bindable]
	public static var YOUR_ARMY:ArrayCollection = new ArrayCollection(
									["Votre armée",
									"Your army",
									"Your army"]);

	[Bindable]
	public static var BUILDINGS:ArrayCollection = new ArrayCollection(
									["Constructions",
									"Buildings",
									"Buildings"]);

	[Bindable]
	public static var UPGRADE:ArrayCollection = new ArrayCollection(
									["Construire",
									"Upgrade",
									"Upgrade"]);

	//-----------------------------------------------------------------------------------//
	// TUTORIALS

}
}