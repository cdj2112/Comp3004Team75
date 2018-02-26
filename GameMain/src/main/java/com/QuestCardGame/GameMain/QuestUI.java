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

	private Hotspot[] stageHotspots;
	private Hotspot[] playHotspots;
	private Hotspot discardHotspot;

	private PlayerGroup[] playerGroups;
	private StageGroup[] stageGroups;

	private Button acceptButton;
	private Button declineButton;
	private Text prompt;
	private boolean canAccept = true;
	private Text playerBPDisplay, stageBPDisplay;
	private HashMap<String, EventHandler<ActionEvent>> dialogListeners;

	private Timer evalTimer = new Timer();
	private boolean isEvaluating = false;

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
		playerBPDisplay = new Text();
		playerBPDisplay.setFont(new Font(20));
		getChildren().add(playerBPDisplay);
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
					System.out.println("Start Discard "+discard.size());
					for (AdventureCard c : discard) {
						CardGroup cg = assetStore.getCardGroup(c);
						Group p = (Group) cg.getParent();
						if (p != null) {
							System.out.println("Discard "+c.getName());
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

		playerGroups[i].setRankImage(p.getRankImagePath());
		ImageView rank = playerGroups[i].getRankImage();
		rank.setTranslateX(0);
		rank.setTranslateY(0);

		ArrayList<AdventureCard> pHand = p.getHand();
		int xOffset = 0;
		for (Card c : pHand) {
			if (c == draggingCard)
				continue;
			CardGroup g = assetStore.getCardGroup(c);
			if (!playerGroups[i].getHand().getChildren().contains(g)) {
				playerGroups[i].addCardToHand(g);
				EventHandler<MouseEvent> drag = assetStore.getCardListener(c);
				g.addEventHandler(MouseEvent.MOUSE_DRAGGED, drag);
			}
			g.setVisible(false);
		}

		ArrayList<AdventureCard> pPlay = p.getPlay();
		xOffset = 0;
		for (Card c : pPlay) {
			if (c == draggingCard)
				continue;
			CardGroup g = assetStore.getCardGroup(c);
			if (!playerGroups[i].getPlay().getChildren().contains(g)) {
				playerGroups[i].playCard(g);
				EventHandler<MouseEvent> drag = assetStore.getCardListener(c);
				g.addEventHandler(MouseEvent.MOUSE_DRAGGED, drag);
			}
			g.setTranslateX(xOffset % 4 * 80 + 87.5);
			g.setTranslateY(Math.floor(xOffset / 4) * 115 - 18.75);
			g.setScaleX(0.75);
			g.setScaleY(0.75);
			xOffset++;
		}
	}

	private void positionActivePlayerGroup(int i) {
		Player p = game.getPlayer(i);

		playerGroups[i].setRankImage(p.getRankImagePath());
		ImageView rank = playerGroups[i].getRankImage();
		rank.setTranslateX(1050);
		rank.setTranslateY(0);

		ArrayList<AdventureCard> pHand = p.getHand();
		int xOffset = 0;
		for (Card c : pHand) {
			if (c == draggingCard)
				continue;
			CardGroup g = assetStore.getCardGroup(c);
			if (!playerGroups[i].getHand().getChildren().contains(g)) {
				playerGroups[i].addCardToHand(g);
				EventHandler<MouseEvent> drag = assetStore.getCardListener(c);
				g.addEventHandler(MouseEvent.MOUSE_DRAGGED, drag);
			}
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
			if (!playerGroups[i].getPlay().getChildren().contains(g)) {
				playerGroups[i].playCard(g);
				EventHandler<MouseEvent> drag = assetStore.getCardListener(c);
				g.addEventHandler(MouseEvent.MOUSE_DRAGGED, drag);
			}
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
						EventHandler<MouseEvent> drag = assetStore.getCardListener(c);
						g.addEventHandler(MouseEvent.MOUSE_DRAGGED, drag);
					}
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
			h.setActive(i == active && GS == GameStatus.PLAYING_QUEST);
			i++;
		}

		if (GS == GameStatus.SPONSORING || GS == GameStatus.ACCEPTING_QUEST) {
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
		} else if (GS == GameStatus.PRE_QUEST_DISCARD || GS == GameStatus.END_TURN_DISCARD) {
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
				|| GS == GameStatus.END_TURN_DISCARD;
		discardHotspot.setActive(canDiscard);

		if (GS == GameStatus.EVAL_QUEST_STAGE) {
			int activeStage = game.getActiveQuest().getCurrentStageIndex();
			int stageBP = game.getActiveQuest().getCurrentStageBattlePoints();
			stageBPDisplay.setVisible(true);
			stageBPDisplay.setText("" + stageBP);
			stageBPDisplay.setTranslateX(100);
			stageBPDisplay.setTranslateY(300);

			int playerBP = game.getPlayer(active).getBattlePoints();
			playerBPDisplay.setVisible(true);
			playerBPDisplay.setText("" + playerBP);
			playerBPDisplay.setTranslateX(0);
			playerBPDisplay.setTranslateY(300);

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
			playerBPDisplay.setVisible(false);
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
				int yOffset = 200 * ((activePlayer - i + numPlayers) % numPlayers - 1);
				playerGroups[i].setTranslateX(10);
				playerGroups[i].setTranslateY(yOffset);
			}
		}
	}

	public Card getDraggingCard() {
		return draggingCard;
	}

	public void setDraggingCard(Card c) {
		draggingCard = c;
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
