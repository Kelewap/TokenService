package iosr.tokenservice.access;

import iosr.tokenservice.Token;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractTokenProvider implements TokenProvider {

    private final Lock lock = new ReentrantLock();
    private Token token;

    @Override
    public void redeemCode(String authorizationCode) throws AuthorizationException {
        lock.lock();
        try {
            String accessToken = obtainAccessToken(authorizationCode);
            this.token = new Token(accessToken);
        } finally {
            lock.unlock();
        }
    }

    protected abstract String obtainAccessToken(String code) throws AuthorizationException;

    @Override
    public void refreshToken() throws AuthorizationException {
        lock.lock();
        try {
            this.token = new Token(getRefreshedToken());
        } finally {
            lock.unlock();
        }
    }

    protected abstract String getRefreshedToken() throws AuthorizationException;

    @Override
    public Token getToken() {
        //TODO: well is locking here even needed?
        lock.lock();
        try {
            return token;
        } finally {
            lock.unlock();
        }
    }
}
