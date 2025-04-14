package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.controller.client.RMIServerStub;
import it.polimi.ingsw.controller.client.iRMIClient;

public interface iRMIStubProvider {
    RMIServerStub accept(iRMIClient client);
}
