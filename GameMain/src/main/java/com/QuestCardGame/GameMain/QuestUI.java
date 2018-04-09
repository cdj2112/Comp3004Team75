package com.QuestCardGame.GameMain;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.scene.control.Button;
import javafx.geometry.Point2D;

import com.QuestCardGame.GameMain.Game.GameStatus;

public class QuestUI extends Group {

	private final double HEIGHT, WIDTH;

	private Game game;
	private HotspotBehaviourFactory behaviourFactory;
	private AssetStore assetStore;

	private Card draggingCard;
	private CardGroup activeStory = null;
	private Card hoverCard;
	private ImageView expandCard;

	private Hotspot[] stageHotspots;
	private Hotspot[] playHotspots;
	private Hotspot discardHotspot;

	private PlayerGroup[] playerGroups;
	private StageGroup[] stageGroups;

	private Button acceptButton;
	private Button declineButton;
	private Text prompt;
	private boolean canAccept = true;
	private Text stageBPDisplay;
	private HashMap<String, EventHandler<ActionEvent>> dialogListeners;
	private Text AIMessage;

	private Timer evalTimer = new Timer();
	private Timer evalTimer2 = new Timer();
	private Timer aiTimer = new Timer();
	private boolean isEvaluating = false;
	private boolean isAIPlaying = false;

	QuestUI(Game g, double h, double w) throws FileNotFoundException {
		super();

		HEIGHT = h;
		WIDTH = w;
		game = g;
		int numPlayers = game.getNumPlayers();

		assetStore = new AssetStore(this);
		stageHotspots = new Hotspot[5];
		playHotspots = new Hotspot[numPlayers];
		dialogListeners = new HashMap<String, EventHandler<ActionEvent>>();
		behaviourFactory = new HotspotBehaviourFactory(game, this);

		playerGroups = new PlayerGroup[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			playerGroups[i] = new PlayerGroup();
			playerGroups[i].setTranslateX(10);
			playerGroups[i].setTranslateY(HEIGHT - 310);
			playerGroups[i].setVisible(true);

			playHotspots[i] = new Hotspot();
			playHotspots[i].setHeight(150);
			playHotspots[i].setWidth(1000);
			playHotspots[i].setAction(behaviourFactory.playCard);

			playerGroups[i].getChildren().add(playHotspots[i]);
			getChildren().add(playerGroups[i]);
		}

		Image storyPic = new Image(new FileInputStream("./src/resources/Cards/Backs/Story.png"));
		ImageView storyDeck = new ImageView(storyPic);
		storyDeck.setFitHeight(150);
		storyDeck.setFitWidth(100);
		storyDeck.setTranslateX(400);

		expandCard = new ImageView();
		expandCard.setFitWidth(100);
		expandCard.setFitHeight(150);
		expandCard.setTranslateX(260);
		getChildren().add(expandCard);

		EventHandler<MouseEvent> drawStory = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				if (game.getGameStatus() != GameStatus.IDLE) {
					return;
				}
				game.playTurn();
				update();
			}
		};
		storyDeck.addEventHandler(MouseEvent.MOUSE_CLICKED, drawStory);
		getChildren().add(storyDeck);

		prompt = new Text();
		prompt.setFont(new Font(20));
		prompt.setTranslateX(400);
		prompt.setTranslateY(325);
		getChildren().add(prompt);
		stageBPDisplay = new Text();
		stageBPDisplay.setFont(new Font(20));
		getChildren().add(stageBPDisplay);
		acceptButton = new Button("Accept");
		acceptButton.setTranslateX(400);
		acceptButton.setTranslateY(350);
		getChildren().add(acceptButton);
		declineButton = new Button("Decline");
		declineButton.setTranslateX(400);
		declineButton.setTranslateY(375);
		getChildren().add(declineButton);

		// tour*****************************************************************************
		dialogListeners.put("acceptTour", new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				game.acceptDeclineTour(game.getPlayer(game.getCurrentActivePlayer()), true);
				update();
			}
		});
		dialogListeners.put("declineTour", new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				game.acceptDeclineTour(game.getPlayer(game.getCurrentActivePlayer()), false);
				update();
			}
		});
		dialogListeners.put("finalizePlayTour", new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				canAccept = game.finalizePlayTour();
				update();
			}
		});
		// ***********************************************************************************************

		dialogListeners.put("acceptSponsor", new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				game.acceptSponsor();
				update();
			}
		});
		dialogListeners.put("declineSponsor", new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				game.declineSponsor();
				update();
			}
		});
		dialogListeners.put("acceptQuest", new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				game.acceptDeclineQuest(game.getPlayer(game.getCurrentActivePlayer()), true);
				update();
			}
		});
		dialogListeners.put("declineQuest", new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				ArrayList<AdventureCard> discard = game
						.acceptDeclineQuest(game.getPlayer(game.getCurrentActivePlayer()), false);
				if (discard != null) {
					System.out.println("Start Discard " + discard.size());
					for (AdventureCard c : discard) {
						CardGroup cg = assetStore.getCardGroup(c);
						Group p = (Group) cg.getParent();
						if (p != null) {
							System.out.println("Discard " + c.getName());
							p.getChildren().remove(cg);
						}
					}
				}
				update();
			}
		});
		dialogListeners.put("finalizeQuest", new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				game.finalizeQuest();
				update();
			}
		});
		dialogListeners.put("finalizePlay", new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				canAccept = game.finalizePlay();
				update();
			}
		});

		stageGroups = new StageGroup[5];
		for (int i = 0; i < 5; i++) {
			stageGroups[i] = new StageGroup();
			stageGroups[i].setTranslateX(600);
			stageGroups[i].setTranslateY(i * 100);
			stageGroups[i].setVisible(false);

			stageHotspots[i] = new Hotspot();
			stageHotspots[i].setHeight(100);
			stageHotspots[i].setWidth(600);
			stageHotspots[i].setAction((HotspotBehaviour) behaviourFactory.getPlayToStage(i));
			stageHotspots[i].setActive(false);

			stageGroups[i].getChildren().add(stageHotspots[i]);
			getChildren().add(stageGroups[i]);
		}

		discardHotspot = new Hotspot();
		discardHotspot.setHeight(150);
		discardHotspot.setWidth(100);
		discardHotspot.setTranslateX(WIDTH - 150);
		discardHotspot.setTranslateY(0);
		discardHotspot.setAction(behaviourFactory.discardCard);
		getChildren().add(discardHotspot);

		AIMessage = new Text();
		AIMessage.setFont(new Font(80));
		AIMessage.setText("Waiting For AI Player...");
		AIMessage.setTranslateX(350);
		AIMessage.setTranslateY(HEIGHT / 2 - 40);
		AIMessage.setVisible(false);
		getChildren().add(AIMessage);

		update();
	}

	public void dropCard(double x, double y) {
		for (Hotspot h : stageHotspots) {
			h.checkColision(draggingCard, x, y);
		}
		playHotspots[game.getCurrentActivePlayer()].checkColision(draggingCard, x, y);
		discardHotspot.checkColision(draggingCard, x, y);
	}

	private void positionInactivePlayerGroup(int i) {
		Player p = game.getPlayer(i);
		GameStatus GS = game.getGameStatus();
		PlayerGroup inactivePlayerGroup = playerGroups[i];

		inactivePlayerGroup.setRankImage(p.getRankImagePath());
		ImageView rank = inactivePlayerGroup.getRankImage();
		rank.setFitWidth(50);
		rank.setFitHeight(75);
		rank.setTranslateX(0);
		rank.setTranslateY(0);

		inactivePlayerGroup.arrangeShieldLine();
		inactivePlayerGroup.setShields(p.getNumShields());

		inactivePlayerGroup.setBP(p.getBattlePoints());
		Text bp = inactivePlayerGroup.getBP();
		bp.setTranslateX(0);
		bp.setTranslateY(140);
		bp.setVisible(GS == GameStatus.EVAL_QUEST_STAGE || GS == GameStatus.EVAL_TOUR);

		ArrayList<AdventureCard> pHand = p.getHand();
		inactivePlayerGroup.setCardsInHand(pHand.size(), true);
		int xOffset = 0;
		for (Card c : pHand) {
			if (c == draggingCard)
				continue;
			CardGroup g = assetStore.getCardGroup(c);
			if (!inactivePlayerGroup.getHand().getChildren().contains(g)) {
				inactivePlayerGroup.addCardToHand(g);
			}
			g.setDragCard(false);
			g.setHoverCard(false);
			g.setVisible(false);
		}

		ArrayList<AdventureCard> pPlay = p.getPlay();
		xOffset = 0;
		for (Card c : pPlay) {
			if (c == draggingCard)
				continue;
			if (c == hoverCard) {
				expandCard.setTranslateY(inactivePlayerGroup.getTranslateY());
			}
			CardGroup g = assetStore.getCardGroup(c);
			if (!inactivePlayerGroup.getPlay().getChildren().contains(g)) {
				inactivePlayerGroup.playCard(g);
			}
			boolean faceUp = GS != GameStatus.PLAYING_TOUR || !game.getTournamentStash().contains(c);
			g.setFaceUpDown(faceUp);
			g.setDragCard(false);
			g.setHoverCard(true);
			g.setTranslateX(xOffset % 5 * 35 + 40);
			g.setTranslateY(Math.floor(xOffset / 5) * 52 - 50);
			g.setScaleX(1 / 3.0);
			g.setScaleY(1 / 3.0);
			xOffset++;
		}
	}

	private void positionActivePlayerGroup(int i) {
		Player p = game.getPlayer(i);
		GameStatus GS = game.getGameStatus();
		boolean ai = p.isAIPlayer();

		PlayerGroup activePlayerGroup = playerGroups[i];
		activePlayerGroup.setRankImage(p.getRankImagePath());
		ImageView rank = activePlayerGroup.getRankImage();
		rank.setFitWidth(75);
		rank.setFitHeight(112.5);
		rank.setTranslateX(1050);
		rank.setTranslateY(0);

		activePlayerGroup.arrangeShieldsGrid();
		activePlayerGroup.setShields(p.getNumShields());

		activePlayerGroup.setBP(p.getBattlePoints());
		Text bp = activePlayerGroup.getBP();
		bp.setVisible(!ai || GS == GameStatus.EVAL_QUEST_STAGE || GS == GameStatus.EVAL_TOUR);
		bp.setTranslateX(1050);
		bp.setTranslateY(140);

		ArrayList<AdventureCard> pHand = p.getHand();
		activePlayerGroup.setCardsInHand(pHand.size(), false);
		int xOffset = 0;
		for (Card c : pHand) {
			if (c == draggingCard)
				continue;
			CardGroup g = assetStore.getCardGroup(c);
			if (!activePlayerGroup.getHand().getChildren().contains(g)) {
				activePlayerGroup.addCardToHand(g);
			}
			g.setDragCard(!ai);
			g.setHoverCard(false);
			g.setFaceUpDown(!ai);
			g.setTranslateX(xOffset * 110.0);
			g.setTranslateY(0);
			g.setScaleX(1);
			g.setScaleY(1);
			g.setVisible(true);
			xOffset++;
		}

		ArrayList<AdventureCard> pPlay = p.getPlay();
		xOffset = 0;
		for (Card c : pPlay) {
			if (c == draggingCard)
				continue;
			CardGroup g = assetStore.getCardGroup(c);
			if (!activePlayerGroup.getPlay().getChildren().contains(g)) {
				activePlayerGroup.playCard(g);
			}
			boolean faceUp = !ai || GS != Game.GameStatus.PLAYING_TOUR || !game.getTournamentStash().contains(c);
			g.setFaceUpDown(faceUp);
			g.setDragCard(false);
			g.setHoverCard(false);
			g.setTranslateX(xOffset * 110.0);
			g.setTranslateY(0);
			g.setScaleX(1);
			g.setScaleY(1);
			xOffset++;
		}
	}

	private void repositionCards() {
		GameStatus GS = game.getGameStatus();
		int activePlayer = game.getCurrentActivePlayer();

		for (int i = 0; i < game.getNumPlayers(); i++) {
			if (i == activePlayer)
				positionActivePlayerGroup(i);
			else
				positionInactivePlayerGroup(i);
		}

		Card sCard = game.getActiveStoryCard();
		CardGroup sCG = sCard == null ? null : assetStore.getCardGroup(sCard);
		if (sCG != activeStory) {
			if (activeStory != null) {
				getChildren().remove(activeStory);
			}

			if (sCG != null) {
				getChildren().add(sCG);
			}

			activeStory = sCG;
		}
		if (activeStory != null) {
			activeStory.setTranslateX(400);
			activeStory.setTranslateY(155);
			activeStory.setDragCard(false);
			activeStory.setHoverCard(false);
		}

		Quest q = game.getActiveQuest();
		if (q != null) {
			int stage = q.getCurrentStageIndex();
			int stg = 0;
			for (Stage s : q.getStages()) {
				int xOffset = 0;
				for (Card c : s.getCards()) {
					CardGroup g = assetStore.getCardGroup(c);
					if (!stageGroups[stg].getChildren().contains(g)) {
						stageGroups[stg].addCardGroup(g);
					}
					g.setDragCard(false);
					g.setHoverCard(false);
					g.setTranslateX(xOffset * 75 - 15);
					g.setTranslateY(-25);
					g.setScaleX(0.666);
					g.setScaleY(0.666);
					g.setFaceUpDown(
							GS == GameStatus.BUILDING_QUEST || (GS == GameStatus.EVAL_QUEST_STAGE && stage == stg));
					xOffset++;
				}
				stg++;
			}
		}
	}

	private void readGameStatus() {
		GameStatus GS = game.getGameStatus();
		int stages = game.activeStages();

		int i = 0;
		int active = game.getCurrentActivePlayer();
		for (Hotspot h : playHotspots) {
			h.setActive(
					(i == active && GS == GameStatus.PLAYING_QUEST) || (i == active && GS == GameStatus.PLAYING_TOUR));
			h.setVisible(
					(i == active && GS == GameStatus.PLAYING_QUEST) || (i == active && GS == GameStatus.PLAYING_TOUR));
			i++;
		}

		if (GS == GameStatus.ENTERING_TOUR) {
			acceptButton.setVisible(true);
			declineButton.setVisible(true);
			prompt.setVisible(true);
			acceptButton.setOnAction(dialogListeners.get("acceptTour"));
			declineButton.setOnAction(dialogListeners.get("declineTour"));
			prompt.setText("Accept Tournament?");
		} else if (GS == GameStatus.PLAYING_TOUR) {
			acceptButton.setVisible(true);
			declineButton.setVisible(false);
			prompt.setVisible(true);
			acceptButton.setOnAction(dialogListeners.get("finalizePlayTour"));
			String promptText = !canAccept ? "You must discard or play a card" : "Play Cards for Tournaments";
			prompt.setText(promptText);
		} else if (GS == GameStatus.EVAL_TOUR) {
			acceptButton.setVisible(false);
			declineButton.setVisible(false);
			prompt.setVisible(false);

			if (!isEvaluating) {
				isEvaluating = true;
				evalTimer2.schedule(new TimerTask() {
					public void run() {
						final ArrayList<AdventureCard> discard = game.EvalTour();
						Platform.runLater(new Runnable() {
							public void run() {
								for (AdventureCard c : discard) {
									Group g = assetStore.getCardGroup(c);
									Group p = (Group) g.getParent();
									if (p != null) {
										p.getChildren().remove(g);
									}
								}
								update();
							}
						});
						isEvaluating = false;
					}
				}, (long) 2 * 1000);
			}
		}

		else if (GS == GameStatus.SPONSORING || GS == GameStatus.ACCEPTING_QUEST) {
			acceptButton.setVisible(true);
			declineButton.setVisible(true);
			prompt.setVisible(true);
			if (GS == GameStatus.SPONSORING) {
				acceptButton.setOnAction(dialogListeners.get("acceptSponsor"));
				declineButton.setOnAction(dialogListeners.get("declineSponsor"));
				prompt.setText("Sponsor Quest?");
			} else {
				acceptButton.setOnAction(dialogListeners.get("acceptQuest"));
				declineButton.setOnAction(dialogListeners.get("declineQuest"));
				prompt.setText("Accept Quest?");
			}
		} else if (GS == GameStatus.BUILDING_QUEST || GS == GameStatus.PLAYING_QUEST) {
			acceptButton.setVisible(true);
			declineButton.setVisible(false);
			prompt.setVisible(true);
			if (GS == GameStatus.BUILDING_QUEST) {
				acceptButton.setOnAction(dialogListeners.get("finalizeQuest"));
				prompt.setText("Build Quest");
			} else {
				acceptButton.setOnAction(dialogListeners.get("finalizePlay"));
				String promptText = !canAccept ? "You must discard or play a card" : "Play Cards for Stage";
				prompt.setText(promptText);
			}
		} else if (GS == GameStatus.PRE_QUEST_DISCARD || GS == GameStatus.END_TURN_DISCARD
				|| GS == GameStatus.PRE_TOUR_DISCARD) {
			String prefix = "You must discard ";
			int num = game.getPlayerDiscard(game.getCurrentActivePlayer());
			String suffix = num == 1 ? " card" : " cards";
			prompt.setText(prefix + num + suffix);
			prompt.setVisible(true);
			acceptButton.setVisible(false);
			declineButton.setVisible(false);
		} else {
			acceptButton.setVisible(false);
			declineButton.setVisible(false);
			prompt.setVisible(false);
		}

		i = 0;
		for (Hotspot h : stageHotspots) {
			boolean show = (GS == GameStatus.BUILDING_QUEST || GS == GameStatus.ACCEPTING_QUEST
					|| GS == GameStatus.PRE_QUEST_DISCARD || GS == GameStatus.PLAYING_QUEST
					|| GS == GameStatus.EVAL_QUEST_STAGE) && i < stages;
			stageGroups[i].setVisible(show);
			h.setActive(GS == GameStatus.BUILDING_QUEST && i < stages);
			i++;
		}

		boolean canDiscard = GS == GameStatus.PRE_QUEST_DISCARD || GS == GameStatus.PLAYING_QUEST
				|| GS == GameStatus.END_TURN_DISCARD || GS == GameStatus.PLAYING_TOUR
				|| GS == GameStatus.PRE_TOUR_DISCARD;
		discardHotspot.setActive(canDiscard);
		discardHotspot.setVisible(canDiscard);

		if (GS == GameStatus.EVAL_QUEST_STAGE) {
			int activeStage = game.getActiveQuest().getCurrentStageIndex();
			int stageBP = game.getActiveQuest().getCurrentStageBattlePoints();
			stageBPDisplay.setVisible(true);
			stageBPDisplay.setText("Battle Points: " + stageBP);
			int cards = game.getActiveQuest().getStages()[activeStage].getCards().size();
			stageBPDisplay.setTranslateX(655 + cards * 75);
			stageBPDisplay.setTranslateY(activeStage * 100 + 60);

			stageGroups[activeStage].setVisible(true);
			if (!isEvaluating) {
				isEvaluating = true;
				evalTimer.schedule(new TimerTask() {
					public void run() {
						final ArrayList<AdventureCard> discard = game
								.evaluatePlayerEndOfStage(game.getCurrentActivePlayer());
						Platform.runLater(new Runnable() {
							public void run() {
								for (AdventureCard c : discard) {
									Group g = assetStore.getCardGroup(c);
									Group p = (Group) g.getParent();
									if (p != null) {
										p.getChildren().remove(g);
									}
								}
								update();
							}
						});
						isEvaluating = false;
					}
				}, (long) 2 * 1000);
			}
		} else {
			stageBPDisplay.setVisible(false);
		}
	}

	public void update() {
		int activePlayer = game.getCurrentActivePlayer();
		repositionCards();
		readGameStatus();
		int numPlayers = game.getNumPlayers();
		for (int i = 0; i < numPlayers; i++) {
			if (i == activePlayer) {
				playerGroups[i].setTranslateX(10);
				playerGroups[i].setTranslateY(HEIGHT - 310);
			} else {
				int yOffset = 160 * ((activePlayer - i + numPlayers) % numPlayers - 1);
				playerGroups[i].setTranslateX(10);
				playerGroups[i].setTranslateY(yOffset);
			}
		}

		Player p = game.getPlayer(activePlayer);
		GameStatus GS = game.getGameStatus();
		if (p.isAIPlayer() && !isAIPlaying && GS != GameStatus.EVAL_QUEST_STAGE) {
			final Player ai = p;
			prompt.setVisible(false);
			acceptButton.setVisible(false);
			declineButton.setVisible(false);
			AIMessage.setVisible(true);
			isAIPlaying = true;
			aiTimer.schedule(new TimerTask() {
				public void run() {
					Platform.runLater(new Runnable() {
						public void run() {
							final ArrayList<AdventureCard> discard = ai.playTurn();
							if (discard != null) {
								for (AdventureCard c : discard) {
									Group g = assetStore.getCardGroup(c);
									Group p = (Group) g.getParent();
									if (p != null) {
										p.getChildren().remove(g);
									}
								}
							}
							update();
						}
					});
					isAIPlaying = false;
				}
			}, (long) 2 * 1000);
		} else if (!isAIPlaying) {
			AIMessage.setVisible(false);
		}
	}

	public Card getDraggingCard() {
		return draggingCard;
	}

	public void setDraggingCard(Card c) {
		draggingCard = c;
	}

	public Card getHoverCard() {
		return hoverCard;
	}

	public void setHoverCard(Card c) {
		hoverCard = c;
		if (c == null) {
			expandCard.setImage(null);
		} else {
			Image img = AssetStore.getImage(c.getFrontImagePath());
			expandCard.setImage(img);
		}
	}

	public EventHandler<MouseEvent> findCardListener(Card c) {
		return assetStore.getCardListener(c);
	}

	public PlayerGroup getPlayerGroup(int i) {
		return playerGroups[i];
	}

	public StageGroup getStageGroup(int i) {
		return stageGroups[i];
	}

	public CardGroup findCardGroup(Card c) {
		return assetStore.getCardGroup(c);
	}
}
