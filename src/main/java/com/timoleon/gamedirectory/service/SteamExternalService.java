package com.timoleon.gamedirectory.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timoleon.gamedirectory.service.dto.steam.SteamGameDTO;
import com.timoleon.gamedirectory.web.rest.errors.BadRequestAlertException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SteamGameDTO}.
 */
@Service
@Transactional
public class SteamExternalService {

    private final Logger log = LoggerFactory.getLogger(SteamExternalService.class);

    private final String steamExternalUri = "http://store.steampowered.com/api/appdetails";

    public SteamExternalService() {}

    public SteamGameDTO getSteamGame(Long apiid) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = steamExternalUri + "?appids=" + apiid + "&l=english";

        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("User-Agent", "Mozilla/5.0");
            httpGet.addHeader("Accept", "application/json, text/plain, */*");
            httpGet.addHeader("Accept-Language", "el-GR,el;q=0.8,en-US;q=0.5,en;q=0.3");

            HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                String responseBody = EntityUtils.toString(response.getEntity());

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                JsonNode jsonNode = mapper.readTree(responseBody);
                String content = jsonNode.get(apiid.toString()).toString();

                return mapper.readValue(content, SteamGameDTO.class);
            } else {
                throw new BadRequestAlertException("There was an error connecting to the steam api", "steamApi", "error");
            }
        } catch (Exception e) {
            throw new BadRequestAlertException("There was an error connecting to the steam api", "steamApi", "error");
        }
    }
}
