package io.overpoet.lutron.leap.client.protocol;

import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;

import io.overpoet.lutron.leap.client.model.SwitchedLevel;
import io.overpoet.lutron.leap.client.model.Zone;

public class GoToLevel extends AbstractRequestMessage {

    public GoToLevel(Zone zone, int level) {
        communiqueType("CreateRequest");
        url(zone.href() + "/commandprocessor");
        JsonObjectBuilder command = JsonProvider.provider().createObjectBuilder();
        command.add("CommandType", "GoToLevel");
        JsonObjectBuilder parameterBody = JsonProvider.provider().createObjectBuilder();
        parameterBody.add("Type", "Level");
        parameterBody.add("Value", level);

        JsonArrayBuilder parameter = JsonProvider.provider().createArrayBuilder();
        parameter.add(parameterBody);
        command.add( "Parameter", parameter);
        body("Command", command.build());
    }

    public GoToLevel(Zone zone, SwitchedLevel level) {
        communiqueType("CreateRequest");
        url(zone.href() + "/commandprocessor");
        JsonObjectBuilder command = JsonProvider.provider().createObjectBuilder();
        command.add("CommandType", "SetSwitchedLevel");
        JsonObjectBuilder parameterBody = JsonProvider.provider().createObjectBuilder();
        parameterBody.add("Type", "SwitchedLevel");
        parameterBody.add("Value", level.toString());

        JsonArrayBuilder parameter = JsonProvider.provider().createArrayBuilder();
        parameter.add(parameterBody);
        command.add( "Parameter", parameter);
        body("Command", command.build());
    }
}
