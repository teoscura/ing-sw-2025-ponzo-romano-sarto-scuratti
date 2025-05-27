package it.polimi.ingsw.view.tui.formatters;

import it.polimi.ingsw.view.tui.TerminalWrapper;

import java.util.ArrayList;

public class HelpScreenFormatter {

	static public void format(TerminalWrapper terminal) {
		ArrayList<String> res = new ArrayList<>();
		res.add("╭─────────────────────────────────────────────────────── [Help] ───────────────────────────────────────────────────────╮");
		res.add("│  Argument types: <i>: number, <gt>: 1-2 gamemode type, <s>: game size 2-4, <r>: rotation in terms of 90* clockwise   │");
		res.add("│              <mvl>: value of merch [0: battery to 4: red], <cid>: component id, <b> 'true' or 'false'                │");
		res.add("│                      Commands:                           ││                         Symbols:                         │");
		res.add("│ '(red|blue|green|yellow)' select ship to display as main ││ '🔫': Cannon/Cannon PWR   │ '🮆🮆': Forbidden/Placeholder  │");
		res.add("│ 'entersetup'                                             ││ '🚀': Engine/Engine PWR   │ 'SC': Single Connector       │");
		res.add("│ 'leavesetup'                                             ││ '🔋': Battery/Batteries   │ 'DC': Double Connector       │");
		res.add("│ 'openlobby <gt> <s>'                                     ││ '🔒': Shield              │ 'UN': Universal connector    │");
		res.add("│ 'openunfinished <i>'                                     ││ '✅': Powered Component   │ '⏫': 🔫/🚀 direction        │");
		res.add("│ 'enterlobby <i>'                                         ││ '❎': Unpowered Component │ '⏩': 🔫/🚀 Direction        │");
		res.add("│ 'sendcontinue'                                           ││ '🟦': Blue cargo          │ '⏬': 🔫/🚀 Direction        │");
		res.add("│ 'takecomponent'                                          ││ '🟩': Green cargo         │ '⏪': 🔫/🚀 Direction        │");
		res.add("│ 'putcomponent <cid> <x> <y> <r>'                         ││ '🟨': Yellow cargo        │ '..': Empty space            │");
		res.add("│ 'discardcomponent'/'reservecomponent' (current comp.)    ││ '🟥': Red cargo           │ '🔗': Structural Component   │");
		res.add("│ 'takediscarded <cid>'                                    ││ '❌': Illegal Component   │ '🔋': Battery Component      │");
		res.add("│ 'togglehourglass'                                        ││ '🧍': Total crew          │ '🏦': Starting Cabin Comp.   │");
		res.add("│ 'selectblob <x> <y>'                                     ││ '😀': Human Crewmate      │ '🛖': Cabin Component        │");
		res.add("│ 'removecomponent <x> <y>'                                ││ '🐻': Brown Alien         │ '🤎': Brown Life Support     │");
		res.add("│ 'setcrew <x> <y> <0-2>' 0: human 1: brown 2: purple      ││ '😈': Purple Alien        │ '💜': Purple Life Support    │");
		res.add("│ 'takecargo <x> <y> <mvl>                                 ││ '💰': Credits             │ '🚙': Double Normal Storage  │");
		res.add("│ 'discardcargo <x> <y> <mvl>'                             ││ '✨': Score               │ '🛻': Triple Normal Storage  │");
		res.add("│ 'movecargo <x> <y> <mvl> <x> <y>' target, second source  ╰╰───────────────────────────┤ '🚚': Single Special Storage │");
		res.add("│ 'removecrew <x> <y>'                                        'selectlanding <i>'       │ '🚛': Double Special Storage │");
		res.add("│ 'turnon <x> <y> <x> <y>' first target, second source        'takereward <b>'          │                              │");
		res.add("│ 'giveup'                                                    'disconnect'              │                              │");
		res.add("╰─────────────────────────────────────────────────────── [Help] ────────────────────────┴──────────────────────────────╯");
		terminal.print(res, 2, 5);
	}

}
