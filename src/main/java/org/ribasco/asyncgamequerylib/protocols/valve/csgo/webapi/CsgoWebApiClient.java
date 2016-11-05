package org.ribasco.asyncgamequerylib.protocols.valve.csgo.webapi;

import org.asynchttpclient.Response;
import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamWebApiClient;
import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamWebApiResponse;

public class CsgoWebApiClient extends SteamWebApiClient {

    public CsgoWebApiClient(String apiToken) {
        super(apiToken);
    }

    @Override
    protected SteamWebApiResponse createWebApiResponse(Response response) {
        return new CsgoWebApiResponse(response);
    }
}
