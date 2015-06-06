package iosr.tokenservice.access;

import iosr.tokenservice.Token;

public interface TokenProvider {
    void redeemCode(String authorizationCode) throws AuthorizationException;
    Token getToken();
}
