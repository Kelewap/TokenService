package iosr.tokenservice.access;

import iosr.tokenservice.Token;

public interface TokenProvider {
    //TODO: maybe more interface segragation - maybe can extract refreshing token to another interface
    void redeemCode(String authorizationCode) throws AuthorizationException;
    void refreshToken() throws AuthorizationException;
    Token getToken();
}
