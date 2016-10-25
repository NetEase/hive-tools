
package netease.bigdata.hivetools.meta;


public class DelegationTokens {

    private String token_ident;
    private String token;

    public void DelegationTokens() {
    }

    public void setTokenIdent(String tokenIdent_) {
        token_ident = tokenIdent_;
    }

    public String getTokenIdent() {
        return token_ident;
    }

    public void setToken(String token_) {
        token = token_;
    }

    public String getToken() {
        return token;
    }

}
