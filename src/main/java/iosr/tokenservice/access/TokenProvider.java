package iosr.tokenservice.access;

import iosr.tokenservice.Token;

public interface TokenProvider {
    Token getToken();
}
