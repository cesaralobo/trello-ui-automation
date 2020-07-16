package framework.utils.ws;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ClienteXSD {

    private Client client;
    private String urlAPI;

    public ClienteXSD(String BASE_URL, String BASE_USER){
        client = ClientBuilder.newBuilder().build().register(new Authenticator(BASE_USER,BASE_USER));
        setUrlAPI(BASE_URL);
    }

    public boolean checkServerXSD() {

        boolean result = false ;

        WebTarget target = client.target(urlAPI);
        Response response = target.request(MediaType.TEXT_XML).get();

        if (response.getStatus() == 200){
            if(response.readEntity(String.class).contains("validationManagerV2.xquery")){
                result = true;
            }
        }
        response.close();
        return result;
    }

    public void setUrlAPI(String urlAPI) {
        this.urlAPI = urlAPI;
    }

    public String getUrlAPI() {
        return urlAPI;
    }

    public boolean isValidWS(String workspace) {
        boolean result = false;

        WebTarget target = client.target(this.getUrlAPI()+"/validationManagerV2.xquery?path=/db/offer-editor/data/ws/"+workspace);
        Response response = target.request(MediaType.TEXT_XML).get();

        if (response.getStatus() == 200){

            if(response.readEntity(String.class).contains("valid")){
                result = true;
            }
        }
        response.close();

        return result;
    }


}
