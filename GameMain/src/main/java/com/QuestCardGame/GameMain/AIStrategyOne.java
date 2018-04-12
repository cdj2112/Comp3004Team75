package com.QuestCardGame.GameMain;

import java.util.ArrayList;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class AIStrategyOne extends AIPlayer {

	private static final Logger logger = LogManager.getLogger("AILogger");

	public AIStrategyOne(Game g, Player p) {
		super();
		player = p;
		game = g;
	}

	public boolean doIJoinTournament() {
		boolean joinTournament = false;
		if (aPlayerCanWinGame(true) || aPlayerCanEvolve(true)) {
			joinTournament = true;
			game.acceptDeclineTour(player, true);
			logger.info("A player can win or evolve. AI [ONE] will enter the tournament");
			logger.info("AI Player " + player.getPlayerNumber() + " with strategy [ONE] ENTERED the tournament");
		} else {
			joinTournament = false;
			game.acceptDeclineTour(player, false);
			logger.info("No player can win or evolve. AI [ONE] will NOT enter the tournament");
			logger.info("AI Player " + player.getPlayerNumber() + " with strategy [ONE] did NOT ENTER the tournament");
		}
		return joinTournament;
	}

	// TODO need to add condition for another player in tournament to win/evolve to
	// play best hand
	public ArrayList<AdventureCard> playCardsForTournament() {
		ArrayList<AdventureCard> cardsForTournament = new ArrayList<AdventureCard>();
		int totalBattlePoints = 0;

		if (canIWinGame()) {
			cardsForTournament = player.getHand().getBestPossibleHand(player.getPlay());
			logger.info("AI Player " + player.getPlayerNumber()
					+ " with strategy [ONE] can win the game. Will play best hand.");
		} else {
			cardsForTournament = player.getHand().getDuplicateWeapons();
			logger.info("AI Player " + player.getPlayerNumber()
					+ " with strategy [ONE] can not win the game. Will play duplicate weapons.");
		}
		for (AdventureCard c : cardsForTournament) {
			logger.info("AI Player " + player.getPlayerNumber() + " with strategy [ONE] played [" + c.getName()
					+ "] for tournament");
			totalBattlePoints += c.getBattlePoint(false);
		}
		logger.info("AI Player " + player.getPlayerNumber() + " with strategy [ONE] played [" + totalBattlePoints
				+ "] battle points for tournament");
		game.playerPlayCards(player, cardsForTournament);
		game.finalizePlayTour();
		return cardsForTournament;
	}

	public ArrayList<AdventureCard> evalTour() {
		return game.EvalTour();
	}

	public boolean doISponsorAQuest() {
		QuestCard quest = ((QuestCard) game.getActiveStoryCard());
		int numStages = quest.getStages();
		boolean hasCardsToSponsor = player.getHand().hasCardsToSponsorQuest(numStages);

		// has to have cards, and nobody else can win/evolve
		if (hasCardsToSponsor && !(aPlayerCanWinGame(false) || aPlayerCanEvolve(false))) {
			game.acceptSponsor();
			logger.info("AI Player " + player.getPlayerNumber() + " with strategy [ONE] SPONSORED the quest ["
					+ quest.getName() + "]");
			return true;
		} else {
			game.declineSponsor();
			logger.info("AI Player " + player.getPlayerNumber() + " with strategy [ONE] did NOT SPONSOR the quest ["
					+ quest.getName() + "]");
			return false;
		}
	}

	// last stage 50
	// second last test (if possible)
	// strongest foe with any weapon i have duplicates of
	public ArrayList<AdventureCard> createQuest() {
		ArrayList<AdventureCard> cardsForQuest = new ArrayList<AdventureCard>();
		ArrayList<AdventureCard> cardsForStage = new ArrayList<AdventureCard>();
		int numStages = game.getActiveQuest().getNumStages();

		logger.info("AI Player [" + player.getPlayerNumber()
				+ "] with strategy [ONE] needs stage n battle points >= 50. n-1 a test. Else strongest foe and any duplicate weapons.");

		// intentionally going backwards due to requirements
		int stageBattlePoints = 0;
		for (int i = numStages - 1; i >= 0; i--) {
			cardsForStage = getCardsForQuestStage(i, numStages, stageBattlePoints); // stageBattlePoints is zero for the
																					// highest stage. It's okay, it's
																					// not used to get cards for that
																					// stage.
			stageBattlePoints = 0;
			for (AdventureCard c : cardsForStage) {
				stageBattlePoints += c.getBattlePoint(false);
				game.sponsorAddCardToStage(c, i);
				logger.info("AI Player [" + player.getPlayerNumber() + "] with strategy [ONE] added [" + c.getName()
						+ "] to stage [" + i + "]");
			}
			logger.info("AI Player [" + player.getPlayerNumber() + "] with strategy [ONE] played total battle points ["
					+ stageBattlePoints + "] for stage [" + i + "]");
			cardsForQuest.addAll(cardsForStage);
		}

		game.finalizeQuest();
		return cardsForQuest;
	}

	/**
	 * Requirement states allies/weapons per stage >= 2 I will assume "/" means OR,
	 * not AND.
	 */
	public ArrayList<AdventureCard> doIJoinQuest() {
		QuestCard activeQuest = ((QuestCard) game.getActiveStoryCard());
		int numStages = activeQuest.getStages();
		int numAllies = player.getHand().getNumUniqueCards(AdventureCard.AdventureType.ALLY);
		int numWeapons = player.getHand().getNumUniqueCards(AdventureCard.AdventureType.WEAPON);
		int numFoesToDiscardForTest = player.getHand().getNumFoesToDiscard(20);
		int alliesPerStage = numAllies / numStages;
		int weaponsPerStage = numWeapons / numStages;

		if ((alliesPerStage >= 2 || weaponsPerStage >= 2) && numFoesToDiscardForTest >= 2) {
			logger.info("AI Player [" + player.getPlayerNumber() + "] with strategy [ONE] ACCEPTED quest ["
					+ activeQuest.getName() + "]");
			return game.acceptDeclineQuest(player, true);
		} else {
			logger.info("AI Player [" + player.getPlayerNumber() + "] with strategy [ONE] DECLINED quest ["
					+ activeQuest.getName() + "]");
			return game.acceptDeclineQuest(player, false);
		}
	}

	public ArrayList<AdventureCard> playCardsForQuestStage() {
		ArrayList<AdventureCard> cardsToPlay = new ArrayList<AdventureCard>();
		ArrayList<AdventureCard> cardsInPlay = player.getPlay();
		int cardsNeededForStage = 2;
		int currentStage = game.getActiveQuest().getCurrentStageIndex() + 1; // currentStage starts at 0
		int totalStages = game.getActiveQuest().getNumStages();

		if (currentStage == totalStages) {
			cardsToPlay = player.getHand().getBestPossibleHand(cardsInPlay);
			logger.info("Final quest stage. AI Player [" + player.getPlayerNumber()
					+ "] with strategy [ONE] will play best hand.");
		} else {
			logger.info("Not final quest stage. AI Player [" + player.getPlayerNumber()
					+ "] with strategy [ONE] 2 ALLY/AMOURS or weakest WEAPONS to get to 2 cards.");
			player.getHand().sortDescendingByBattlePoints(); // need strongest allies/amour
			ArrayList<AdventureCard> cards = player.getHand().getUniqueCards(AdventureCard.AdventureType.ALLY, 2);
			cardsToPlay.addAll(cards);
			if (cardsToPlay.size() < cardsNeededForStage && !iHaveAmourInPlay()) {
				cards = player.getHand().getUniqueCards(AdventureCard.AdventureType.AMOURS, 1); // only can play 1
																								// anyway
				cardsToPlay.addAll(cards);
			}
			if (cardsToPlay.size() < cardsNeededForStage) {
				player.getHand().sortAscendingByBattlePoints(); // need weakest weapons
				cards = player.getHand().getUniqueCards(AdventureCard.AdventureType.WEAPON,
						cardsNeededForStage - cardsToPlay.size());
				cardsToPlay.addAll(cards);
			}
		}
		for (AdventureCard c : cardsToPlay) {
			logger.info(
					"AI Player [" + player.getPlayerNumber() + "] with strategy [ONE] played [" + c.getName() + "]");
		}
		game.playerPlayCards(player, cardsToPlay);
		game.finalizePlay();
		return cardsToPlay;
	}

	public int getBidForTest() {
		int numToBid = 0;
		int currentRound = game.getActiveQuest().getBiddingRound();
		String storyCard = game.getActiveStoryCard().getName();

		// don't bid unless first round
		if (currentRound == 0) {
			numToBid = player.getHand().getNumFoesToDiscard(20) + player.getBids(storyCard);
			int currentBid = game.getActiveQuest().getBids();
			logger.info("First round of test. AI Player [" + player.getPlayerNumber()
					+ "] with strategy [ONE] will bid number of foes < 20 battle points");
			if (numToBid > currentBid) {
				logger.info("AI Player [" + player.getPlayerNumber() + "] with strategy [ONE] bid [" + numToBid + "]");
				game.placeBid(numToBid);
			} else {
				logger.info("AI Player [" + player.getPlayerNumber()
						+ "] with strategy [ONE] bid is too low and will drop out");
				game.playerDropOut();
			}
		} else {
			numToBid = 0;
			// logger.info("Second round of test. AI Player [" + player.getPlayerNumber() +
			// "] with strategy [ONE] will bid 0");
			logger.info(
					"AI Player [" + player.getPlayerNumber() + "] with strategy [ONE] will drop out in second round");
		}

		return numToBid;
	}

	public ArrayList<AdventureCard> discardAfterWinningTest() {
		Hand hand = player.getHand();
		ArrayList<AdventureCard> cardsToDiscard = new ArrayList<AdventureCard>();
		for (AdventureCard c : hand) {
			if (c.getCardType() == AdventureCard.AdventureType.FOE && c.getBattlePoint(false) <= 20) {
				cardsToDiscard.add(c);
				game.playerDiscardAdventrueCard(player, c);
			}
		}

		logger.info("AI Player [" + player.getPlayerNumber() + "] with strategy [ONE] won the test.");
		for (AdventureCard c : cardsToDiscard) {
			game.playerDiscardAdventrueCard(player, c);
			logger.info(
					"AI Player [" + player.getPlayerNumber() + "] with strategy [ONE] discarded [" + c.getName() + "]");
		}

		return cardsToDiscard;
	}

	public ArrayList<AdventureCard> evalQuestStage() {
		int i = game.getPlayerIndex(player);
		return game.evaluatePlayerEndOfStage(i);
	}

	public void endTurn() {

	}

	private boolean canIWinGame() {
		StoryCard storyCard = (StoryCard) game.getActiveStoryCard();
		int potentialReward = storyCard.getShieldReward();

		// rank 2 is Champion Knight
		return (player.rank == 2 && player.getShieldsNeeded() <= potentialReward);
	}

	private boolean aPlayerCanWinGame(boolean includingMyself) {
		StoryCard storyCard = (StoryCard) game.getActiveStoryCard();
		int potentialReward = storyCard.getShieldReward();
		boolean aPlayerCanWin = false;

		for (int i = 0; i < game.getNumPlayers(); i++) {
			Player p = game.getPlayer(i);
			if (p == this.player && !includingMyself)
				continue;
			if (p.getRankName().equals("Champion Knight") && p.getShieldsNeeded() <= potentialReward) {
				aPlayerCanWin = true;
				logger.info("Player " + p.getPlayerNumber() + " can win game");
			}
		}
		return aPlayerCanWin;
	}

	private boolean aPlayerCanEvolve(boolean includingMyself) {
		StoryCard storyCard = (StoryCard) game.getActiveStoryCard();
		int potentialReward = storyCard.getShieldReward();
		boolean aPlayerCanEvolve = false;

		for (int i = 0; i < game.getNumPlayers(); i++) {
			Player p = game.getPlayer(i);
			if (p == this.player && !includingMyself)
				continue;
			if (p.getShieldsNeeded() <= potentialReward) {
				aPlayerCanEvolve = true;
				logger.info("Player " + p.getPlayerNumber() + " can evolve");
			}
		}
		return aPlayerCanEvolve;
	}

	private boolean iHaveAmourInPlay() {
		for (AdventureCard c : player.getPlay()) {
			if (c.getCardType() == AdventureCard.AdventureType.AMOURS)
				return true;
		}
		return false;
	}

	private ArrayList<AdventureCard> getCardsForQuestStage(int stage, int totalStages, int nextStageBattlePoints) {
		player.getHand().sortDescendingByBattlePoints();
		ArrayList<AdventureCard> foes = player.getHand().getUniqueCards(AdventureCard.AdventureType.FOE, 12); // 12
																												// means
																												// get
																												// as
																												// many
																												// as we
																												// can
		ArrayList<AdventureCard> cardsForStage = new ArrayList<AdventureCard>();
		ArrayList<AdventureCard> weaponsInHand = player.getHand().getUniqueCards(AdventureCard.AdventureType.WEAPON,
				12);
		ArrayList<AdventureCard> duplicateWeapons = player.getHand().getDuplicateWeapons();
		duplicateWeapons.sort(new BattlePointComparatorDescending());
		foes.sort(new BattlePointComparatorDescending());
		int stageBattlePoints = 0;
		AdventureCard strongestFoe = foes.get(0); // by this stage we've already checked we have foes for quest
		AdventureCard foeForNotLastStage = foes.get(0);

		// Building quest in descending order of stages. Need to prevent the case where
		// we play
		// the same foe twice in a row because they're both the next strongest foe. So
		// the current
		// foe must be strictly lower BP than the next stage
		for (AdventureCard c : foes) {
			if (c.getBattlePoint(false) < nextStageBattlePoints) {
				foeForNotLastStage = c;
				break;
			}
		}

		// last stage - need 50 pts
		if (stage + 1 == totalStages) {
			stageBattlePoints += strongestFoe.getBattlePoint(false);
			logger.info("AI Player [" + player.getPlayerNumber() + "] with strategy [ONE] plays strongest foe: "
					+ strongestFoe.getName() + " in last stage");
			cardsForStage.add(strongestFoe);
			for (AdventureCard c : weaponsInHand) {
				stageBattlePoints += c.getBattlePoint(false);
				logger.info("AI Player [" + player.getPlayerNumber() + "] with strategy [ONE] adds weapon: "
						+ c.getName() + " to get battle points over 50");
				cardsForStage.add(c);
				if (stageBattlePoints >= 50)
					break;
			}
		} // second last stage, test if possible
		else if (stage + 2 == totalStages) {
			AdventureCard test = player.getHand().getTestCard();
			if (test != null) {
				logger.info("AI Player [" + player.getPlayerNumber() + "] with strategy [ONE] plays test: "
						+ test.getName() + " in second to last stage");
				cardsForStage.add(test);
			} else {
				logger.info(
						"AI Player [" + player.getPlayerNumber() + "] with strategy [ONE] plays next strongest foe: "
								+ foeForNotLastStage.getName() + " to stage " + (stage + 1));
				cardsForStage.add(foeForNotLastStage);
				stageBattlePoints += foeForNotLastStage.getBattlePoint(false);
				if (duplicateWeapons.size() > 0 && (stageBattlePoints
						+ duplicateWeapons.get(0).getBattlePoint(false) < nextStageBattlePoints)) {
					AdventureCard c = duplicateWeapons.get(0);
					logger.info("AI Player [" + player.getPlayerNumber()
							+ "] with strategy [ONE] plays strongest duplicate weapon: " + c.getName() + " to stage "
							+ (stage + 1));
					cardsForStage.add(c);
				}
			}
		} else {
			logger.info("AI Player [" + player.getPlayerNumber() + "] with strategy [ONE] plays next strongest foe: "
					+ foeForNotLastStage.getName() + " to stage " + (stage + 1));
			cardsForStage.add(foeForNotLastStage);
			stageBattlePoints += foeForNotLastStage.getBattlePoint(false);
			if (duplicateWeapons.size() > 0
					&& (stageBattlePoints + duplicateWeapons.get(0).getBattlePoint(false) < nextStageBattlePoints)) {
				AdventureCard c = duplicateWeapons.get(0);
				logger.info("AI Player [" + player.getPlayerNumber()
						+ "] with strategy [ONE] plays strongest duplicate weapon: " + c.getName() + " to stage "
						+ (stage + 1));
				cardsForStage.add(c);
			}
		}
		return cardsForStage;
	}

	public boolean isAIPlayer() {
		return true;
	}

}
