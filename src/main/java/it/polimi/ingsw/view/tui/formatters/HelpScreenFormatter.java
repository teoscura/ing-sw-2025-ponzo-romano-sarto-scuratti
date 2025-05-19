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
        res.add("│ 'ship [color]' specify ship to view as main.             ││ '🔫': Cannon/Cannon PWR   │ '🮆🮆': Forbidden/Placeholder  │");
        res.add("│ 'entersetup'                                             ││ '🚀': Engine/Engine PWR   │ 'SC': Single Connector       │");
        res.add("│ 'leavesetup'                                             ││ '🔋': Battery/Batteries   │ 'DC': Double Connector       │");
        res.add("│ 'openlobby <gt> <s>'                                     ││ '🔒': Shield               │ 'UN': Universal connector   │");
        res.add("│ 'openunfinished <i>'                                     ││ '✅': Powered Component   │ '⏫': 🔫/🚀 direction        │");
        res.add("│ 'enterlobby <i>'                                         ││ '❎': Unpowered Component │ '⏩': 🔫/🚀 Direction        │");
        res.add("│ 'sendcontinue'                                           ││ '📦': Storage             │ '⏬': 🔫/🚀 Direction        │");
        res.add("│ 'takecomponent'                                          ││ '🟦': Blue cargo          │ '⏪': 🔫/🚀 Direction        │");
        res.add("│ 'putcomponent <cid> <cv> <cv> <r>'                       ││ '🟩': Green cargo         │ '..': Empty space            │");
        res.add("│ 'discardcomponent <cid>'                                 ││ '🟨': Yellow cargo        │ '🔗': Structural Component   │");
        res.add("│ 'togglehourglass'                                        ││ '🟥': Red cargo           │ '🔋': Battery Component      │");
        res.add("│ 'removecomponent <cv> <cv>'                              ││ '❌': Illegal Component   │ '🏦': Starting Cabin Comp.   │");
        res.add("│ 'setcrew <cv> <cv> <0-2>' 0: human 1: brown 2: purple    ││ '🧍': Total crew          │ '🛖': Cabin Component        │");
        res.add("│ 'takecargo <cv> <cv> <mvl>'                              ││ '😀': Human Crewmate      │ '🤎': Brown Life Support     │");
        res.add("│ 'discardcargo <cv> <cv> <mvl>'                           ││ '🐻': Brown Alien         │ '💜': Purple Life Support    │");
        res.add("│ 'movecargo <cv> <cv> <mvl> <cv> <cv>'                    ││ '😈': Purple Alien        │                              │");
        res.add("│ 'removecrew <cv> <cv>'                                   ││ '💰': Credits             │                              │");
        res.add("│ 'selectlanding <i>'                                      ││ '✨': Score               │                              │");
        res.add("│ 'takereward <b>'                                         ││                           │                              │");
        res.add("│ 'turnon <cv> <cv> <cv> <cv>' first target, second source ││                           │                              │");
        res.add("│ 'giveup'                                                 ││                           │                              │");
        res.add("╰─────────────────────────────────────────────────────── [Help] ────────────────────────┴──────────────────────────────╯");
        terminal.print(res, 2, 5);
    }

}
