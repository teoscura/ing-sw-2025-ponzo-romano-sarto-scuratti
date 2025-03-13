package it.polimi.ingsw.model.player;

public enum VerifyResult {
	GOOD (1),
	EMPTY (0),
	NOT_LINKED (-1),
	BROKEN (-2);

	private int result;

	VerifyResult(int result){
		this.result = result;
	}

	public int getResult(){
		return this.result;
	}

}
