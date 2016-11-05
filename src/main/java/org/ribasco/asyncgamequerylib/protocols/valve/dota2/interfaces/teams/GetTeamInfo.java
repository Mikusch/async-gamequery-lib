package org.ribasco.asyncgamequerylib.protocols.valve.dota2.interfaces.teams;

import org.ribasco.asyncgamequerylib.protocols.valve.dota2.Dota2ApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.interfaces.Dota2TeamsRequest;

public class GetTeamInfo extends Dota2TeamsRequest {
    public GetTeamInfo(int apiVersion, int teamId, int leagueId) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETTEAMINFO, apiVersion, null);
        urlParam("team_id", teamId);
        urlParam("league_id", leagueId);
    }
}
