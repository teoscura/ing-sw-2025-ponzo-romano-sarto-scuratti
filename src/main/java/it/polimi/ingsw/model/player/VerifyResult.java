package it.polimi.ingsw.model.player;

public enum VerifyResult {
	GOOD (1),
	UNCHECKED (false),
	NOT_LINKED (-1),
	BROKEN (-2);

	private int result = 0;
	private boolean checked = true;

	VerifyResult(int result){
		this.result = result;
	}

	VerifyResult(boolean checked){
		this.checked = checked;
	}

	public int getResult(){
		return this.result;
	}

	public boolean getChecked(){
		return this.checked;
	}

}
