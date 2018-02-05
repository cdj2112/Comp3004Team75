package com.QuestCardGame.GameMain;
import java.util.*;

public class Game {

	private Player player;
	private Deck storyDeck;
	private Deck adventureDeck;
	private CardList cards;
	private List<String> adv;
	private List<String> att;

	Game() {
		player = new Player();
		cards = new CardList();
		adv = cards.getAdventureList();
		att = cards.getAttributeList();
		initStoryDeck();
		initAdventureDeck();
		for (int i = 0; i < 12; i++) {
			playerDrawAdventureCard(player);
		}
	}

	public Card playerDrawAdventureCard(Player p) {

		Card c = adventureDeck.drawCard();

		if (c != null)
			p.drawCard(c);

		return c;
	}

	public Card getStoryCard() {
		Card c = storyDeck.drawCard();

		if (c != null)
			return c;
		else
			return null;

	}

	public Player getPlayer() {
		return player;
	}

	private void initStoryDeck() {
		storyDeck = new Deck();

		storyDeck.addCard(
				new QuestCard("Journey Through the Enchanted Forest", 3));
		storyDeck.addCard(new QuestCard("Vanquish King Arthur's Enemies", 3));
		storyDeck.addCard(new QuestCard("Repel the Saxon Raiders", 2));
	}

	private void initAdventureDeck() {
		adventureDeck = new Deck();
		int z = 0;
		int[] values = new int[125];
		Random random = new Random();

		for (int i = 0; i < values.length; i++) {
			int number = random.nextInt(125);

			for (int j = 0; j <= i; j++) {
				if (number != values[j]) {
					values[i] = number;
				}
			}
		}

		for (int i = 0; i < values.length; i++) {
			z = values[i];
			if (z < 49) {
				int m = Integer.parseInt(att.get(z));
				// System.out.println(m);
				adventureDeck.addCard(new WeaponCard(adv.get(z), m));
			} else if (z >= 49 && z < 99) {
				String s[] = att.get(z).split("&");
				int s1 = Integer.parseInt(s[0]);
				int s2 = Integer.parseInt(s[1]);
				// System.out.println(s1+" "+s2);
				adventureDeck.addCard(new Foe(adv.get(z), s1, s2));
			} else if (z >= 99 && z < 107) {
				adventureDeck.addCard(new Test(adv.get(z)));
			} else if (z >= 107 && z < 117) {
				adventureDeck.addCard(new Ally(adv.get(z), 5, " "));
			} else {
				adventureDeck.addCard(new Amours());
			}
		}
	}
}
