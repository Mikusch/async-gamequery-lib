package org.ribasco.agql.protocols.valve.dota2.webapi.interfaces.match;

import org.ribasco.agql.protocols.valve.dota2.webapi.Dota2ApiConstants;
import org.ribasco.agql.protocols.valve.dota2.webapi.requests.Dota2MatchRequest;

public class GetTeamInfoByTeamID extends Dota2MatchRequest {
    public GetTeamInfoByTeamID(int apiVersion, int startTeamId, int teamCount) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETTEAMINFOBYID, apiVersion, null);
        urlParam("start_at_team_id", startTeamId);
        urlParam("teams_requested", teamCount);
    }
}
