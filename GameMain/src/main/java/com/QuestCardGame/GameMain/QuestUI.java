package com.QuestCardGame.GameMain;

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

	private Game game;
	private HotspotBehaviourFactory behaviourFactory;

	private HashMap<Integer, Group> cardAssets;
	private HashMap<Integer, EventHandler<MouseEvent>> dragListener;
	private Card draggingCard;

	private Hotspot[] stageHotspots;
	private Hotspot[] playHotspots;

	private PlayerGroup[] playerGroups;
	private StageGroup[] stageGroups;

	private Button acceptButton;
	private Button declineButton;
	private Text prompt;
	private Text playerBPDisplay, stageBPDisplay;
	private HashMap<String, EventHandler<ActionEvent>> dialogListeners;

	private Timer evalTimer = new Timer();
	private boolean isEvaluating = false;

	QuestUI(Game g) throws FileNotFoundException {
		super();

		game = g;
		int numPlayers = game.getNumPlayers();

		cardAssets = new HashMap<Integer, Group>();
		stageHotspots = new Hotspot[5];
		playHotspots = new Hotspot[numPlayers];
		dragListener = new HashMap<Integer, EventHandler<MouseEvent>>();
		dialogListeners = new HashMap<String, EventHandler<ActionEvent>>();
		behaviourFactory = new HotspotBehaviourFactory(game, this);

		playerGroups = new PlayerGroup[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			playerGroups[i] = new PlayerGroup();
			playerGroups[i].setTranslateX(0);
			playerGroups[i].setTranslateY(500);
			playerGroups[i].setVisible(i == 0);

			playHotspots[i] = new Hotspot();
			playHotspots[i].setHeight(100);
			playHotspots[i].setWidth(600);
			playHotspots[i].setStroke(Color.RED);
			playHotspots[i].setFill(Color.TRANSPARENT);
			playHotspots[i].setAction(behaviourFactory.playCard);

			playerGroups[i].getChildren().add(playHotspots[i]);
			getChildren().add(playerGroups[i]);
		}

		Image storyPic = new Image(new FileInputStream("./src/resources/Cards/Backs/Story.jpg"));
		ImageView storyDeck = new ImageView(storyPic);
		storyDeck.setFitHeight(100);
		storyDeck.setFitWidth(100);

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
		prompt.setTranslateX(0);
		prompt.setTranslateY(200);
		getChildren().add(prompt);
		playerBPDisplay = new Text();
		playerBPDisplay.setFont(new Font(20));
		getChildren().add(playerBPDisplay);
		stageBPDisplay = new Text();
		stageBPDisplay.setFont(new Font(20));
		getChildren().add(stageBPDisplay);
		acceptButton = new Button("Accept");
		acceptButton.setTranslateX(0);
		acceptButton.setTranslateY(225);
		getChildren().add(acceptButton);
		declineButton = new Button("Decline");
		declineButton.setTranslateX(0);
		declineButton.setTranslateY(250);
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
				game.acceptDeclineQuest(game.getPlayer(game.getCurrentActivePlayer()), false);
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
				game.finalizePlay();
				update();
			}
		});

		stageGroups = new StageGroup[5];
		for (int i = 0; i < 5; i++) {
			stageGroups[i] = new StageGroup();
			stageGroups[i].setTranslateX(100);
			stageGroups[i].setTranslateY(i * 100);
			stageGroups[i].setVisible(false);

			stageHotspots[i] = new Hotspot();
			stageHotspots[i].setHeight(100);
			stageHotspots[i].setWidth(600);
			stageHotspots[i].setStroke(Color.RED);
			stageHotspots[i].setFill(Color.TRANSPARENT);
			stageHotspots[i].setAction((HotspotBehaviour) behaviourFactory.getPlayToStage(i));
			stageHotspots[i].setActive(false);

			stageGroups[i].getChildren().add(stageHotspots[i]);
			getChildren().add(stageGroups[i]);
		}

		update();
	}

	private void dropCard(double x, double y) {
		for (Hotspot h : stageHotspots) {
			h.checkColision(draggingCard, x, y);
		}
		playHotspots[game.getCurrentActivePlayer()].checkColision(draggingCard, x, y);
	}

	private Group makeNewCardGroup(Card c) {
		Group g = new Group();
		Rectangle r = new Rectangle();
		r.setHeight(100);
		r.setWidth(100);
		r.setFill(Color.RED);
		Text t = new Text(40, 40, "" + c.getName());
		t.setFont(new Font(20));
		g.getChildren().addAll(r, t);

		final Card eventCard = c;
		final Group eventGroup = g;
		EventHandler<MouseEvent> dragged = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				draggingCard = eventCard;
				double x = e.getX();
				double y = e.getY();
				Point2D p = eventGroup.localToParent(x, y);
				eventGroup.setTranslateX(p.getX() - 50);
				eventGroup.setTranslateY(p.getY() - 50);
				update();
			}
		};
		EventHandler<MouseEvent> mouseUp = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				if (draggingCard != eventCard) {
					return;
				}
				dropCard(e.getSceneX(), e.getSceneY());
				System.out.println("Card Up");
				draggingCard = null;
				update();
			}
		};

		g.addEventHandler(MouseEvent.MOUSE_DRAGGED, dragged);
		dragListener.put(c.getId(), dragged);
		g.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseUp);

		return g;
	}

	private void repositionCards() {
		for (int i = 0; i < game.getNumPlayers(); i++) {
			Player p = game.getPlayer(i);
			ArrayList<AdventureCard> pHand = p.getHand();
			int xOffset = 0;
			for (Card c : pHand) {
				if (c == draggingCard)
					continue;
				Group g = findCardGroup(c);
				if (g == null) {
					g = makeNewCardGroup(c);
					playerGroups[i].addCardToHand(g);
					cardAssets.put(c.getId(), g);
				}
				g.setTranslateX(xOffset * 110.0);
				g.setTranslateY(0);
				xOffset++;
			}

			ArrayList<AdventureCard> pPlay = p.getPlay();
			xOffset = 0;
			for (Card c : pPlay) {
				if (c == draggingCard)
					continue;
				Group g = findCardGroup(c);
				if (g == null) {
					g = makeNewCardGroup(c);
					playerGroups[i].playCard(g);
					cardAssets.put(c.getId(), g);
				}
				g.setTranslateX(xOffset * 110.0);
				g.setTranslateY(0);
				xOffset++;
			}
		}

		Quest q = game.getActiveQuest();
		if (q != null) {
			int stg = 0;
			for (Stage s : q.getStages()) {
				int xOffset = 0;
				for (Card c : s.getCards()) {
					Group g = findCardGroup(c);
					if (g == null) {
						g = makeNewCardGroup(c);
						stageGroups[stg].addCardGroup(g);
						cardAssets.put(c.getId(), g);
					}
					g.setTranslateX(xOffset * 110.0);
					g.setTranslateY(0);
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
				prompt.setText("Play Cards for Stage");
			}
		} else {
			acceptButton.setVisible(false);
			declineButton.setVisible(false);
			prompt.setVisible(false);
		}

		i = 0;
		for (Hotspot h : stageHotspots) {
			stageGroups[i].setVisible(GS == GameStatus.BUILDING_QUEST && i < stages);
			h.setActive(GS == GameStatus.BUILDING_QUEST && i < stages);
			i++;
		}

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
						final ArrayList<AdventureCard> discard = game.evaluatePlayerEndOfStage(game.getCurrentActivePlayer());
						Platform.runLater(new Runnable() {
							public void run() {
								for (AdventureCard c : discard) {
									int id = c.getId();
									Group g = cardAssets.get(id);
									Group p = (Group) g.getParent();
									if (p != null) {
										p.getChildren().remove(g);
									}
								}
							}
						});
						isEvaluating = false;
						update();
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
		int i = 0;
		for (PlayerGroup p : playerGroups) {
			p.setVisible(i == activePlayer);
			i++;
		}
	}

	public Group findCardGroup(Card c) {
		return cardAssets.get(c.getId());
	}

	public EventHandler<MouseEvent> findCardListener(Card c) {
		return dragListener.get(c.getId());
	}

	public PlayerGroup getPlayerGroup(int i) {
		return playerGroups[i];
	}

	public StageGroup getStageGroup(int i) {
		return stageGroups[i];
	}
}
