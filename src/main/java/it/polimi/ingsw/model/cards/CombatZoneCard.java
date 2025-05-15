package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.cards.state.CombatZoneAnnounceState;
import it.polimi.ingsw.model.cards.utils.CombatZoneSection;
import it.polimi.ingsw.model.cards.utils.Projectile;
import it.polimi.ingsw.model.cards.utils.ProjectileArray;
import it.polimi.ingsw.model.state.VoyageState;

import java.util.ArrayList;

public class CombatZoneCard extends Card {

	private final ArrayList<CombatZoneSection> sections;
	private final ProjectileArray shots;

	public CombatZoneCard(int id, ArrayList<CombatZoneSection> sections, ProjectileArray shots) {
		super(id, 0);
		if (shots == null || sections == null) throw new NullPointerException();
		this.sections = sections;
		this.shots = shots;
	}

	@Override
	public CardState getState(VoyageState state) {
		return new CombatZoneAnnounceState(state, this.getId(), sections, shots);
	}

	//Used only to fix offsets to ensure same results during tests.
	public ArrayList<Projectile> getShots() {
		return this.shots.getProjectiles();
	}

}
