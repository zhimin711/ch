package com.ch.shiro.authc;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.MergableAuthenticationInfo;
import org.apache.shiro.subject.MutablePrincipalCollection;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 01370603
 * @date 2018/8/11 17:26
 */
public class AuthInfo implements MergableAuthenticationInfo {

    protected PrincipalCollection principals;
    protected Object credentials;

    public AuthInfo(PrincipalCollection principals, Object credentials) {
        this.principals = principals;
        this.credentials = credentials;
    }

    @Override
    public PrincipalCollection getPrincipals() {
        return principals;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public void merge(AuthenticationInfo info) {

        if (info != null && info.getPrincipals() != null && !info.getPrincipals().isEmpty()) {
            if (this.principals == null) {
                this.principals = info.getPrincipals();
            } else {
                if (!(this.principals instanceof MutablePrincipalCollection)) {
                    this.principals = new SimplePrincipalCollection(this.principals);
                }

                ((MutablePrincipalCollection) this.principals).addAll(info.getPrincipals());
            }

            Object thisCredentials = this.getCredentials();
            Object otherCredentials = info.getCredentials();
            if (otherCredentials != null) {
                if (thisCredentials == null) {
                    this.credentials = otherCredentials;
                } else {
                    if (!(thisCredentials instanceof Collection)) {
                        Set newSet = new HashSet();
                        newSet.add(thisCredentials);
                        this.setCredentials(newSet);
                    }

                    Collection credentialCollection = (Collection) this.getCredentials();
                    if (otherCredentials instanceof Collection) {
                        credentialCollection.addAll((Collection) otherCredentials);
                    } else {
                        credentialCollection.add(otherCredentials);
                    }

                }
            }
        }
    }

    public void setCredentials(Object credentials) {
        this.credentials = credentials;
    }
}
