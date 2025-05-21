package it.polimi.ingsw.view.tui.formatters;

import java.util.ArrayList;

import it.polimi.ingsw.view.tui.TerminalWrapper;

public class HelpScreenFormatter {

    static public void format(TerminalWrapper terminal){
        ArrayList<String> res = new ArrayList<>();
        res.add("╭─────────────────────────────────────────────────────── [Help] ───────────────────────────────────────────────────────╮");
        res.add("│  Argument types: <i>: number,<gt>: 1-2 gamemode type, <s>: game size 2-4, <r>: rotation in terms of 90* clockwise    │");
        res.add("│   <cv>: coordinate value, <mvl>: value of merch [0: battery, 4: red], <cid>: component id, <b> \"true\" or \"false\"     │");
        res.add("│                      Commands:                           ││                         Symbols:                         │");
        res.add("│ '(red|blue|green|yellow)' select ship to display as main ││ '🔫': Cannon/Cannon PWR   │ '🮆🮆': Forbidden/Placeholder  │");
        res.add("│ 'entersetup'                                             ││ '🚀': Engine/Engine PWR   │ 'SC': Single Connector       │");
        res.add("│ 'leavesetup'                                             ││ '🔋': Battery/Batteries   │ 'DC': Double Connector       │");
        res.add("│ 'openlobby <gt> <s>'                                     ││ '🔒': Shield              │ 'UN': Universal connector    │");
        res.add("│ 'openunfinished <i>'                                     ││ '✅': Powered Component   │ '⏫': 🔫/🚀 direction        │");
        res.add("│ 'enterlobby <i>'                                         ││ '❎': Unpowered Component │ '⏩': 🔫/🚀 Direction        │");
        res.add("│ 'sendcontinue'                                           ││ '🟦': Blue cargo          │ '⏬': 🔫/🚀 Direction        │");
        res.add("│ 'takecomponent'                                          ││ '🟩': Green cargo         │ '⏪': 🔫/🚀 Direction        │");
        res.add("│ 'putcomponent <cid> <cv> <cv> <r>'                       ││ '🟨': Yellow cargo        │ '..': Empty space            │");
        res.add("│ 'discardcomponent <cid>'                                 ││ '🟥': Red cargo           │ '🔗': Structural Component   │");
        res.add("│ 'togglehourglass'                                        ││ '❌': Illegal Component   │ '🔋': Battery Component      │");
        res.add("│ 'removecomponent <cv> <cv>'                              ││ '🧍': Total crew          │ '🏦': Starting Cabin Comp.   │");
        res.add("│ 'setcrew <cv> <cv> <0-2>' 0: human 1: brown 2: purple    ││ '😀': Human Crewmate      │ '🛖': Cabin Component        │");
        res.add("│ 'takecargo <cv> <cv> <mvl>'                              ││ '🐻': Brown Alien         │ '🤎': Brown Life Support     │");
        res.add("│ 'discardcargo <cv> <cv> <mvl>'                           ││ '😈': Purple Alien        │ '💜': Purple Life Support    │");
        res.add("│ 'movecargo <cv> <cv> <mvl> <cv> <cv>'                    ││ '💰': Credits             │ '🚙': Double Normal Storage  │");
        res.add("│ 'removecrew <cv> <cv>'                                   ││ '✨': Score               │ '🛻': Triple Normal Storage  │");
        res.add("│ 'selectlanding <i>'                                      ││                           │ '🚚': Single Special Storage │");
        res.add("│ 'takereward <b>'                                         ││                           │ '🚛': Double Special Storage │");
        res.add("│ 'turnon <cv> <cv> <cv> <cv>' first target, second source ││                           │                              │");
        res.add("│ 'giveup'                                                 ││                           │                              │");
        res.add("╰─────────────────────────────────────────────────────── [Help] ────────────────────────┴──────────────────────────────╯");
        terminal.print(res, 2, 5);
    }

}
