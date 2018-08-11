package com.ch.shiro.authc;

import com.google.common.collect.Lists;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.*;

/**
 * @author 01370603
 * @date 2018/8/11 16:23
 */
public class AuthPrincipals implements PrincipalCollection {

    private Map<String, Set> realmPrincipals;

    public AuthPrincipals(String username, String userId, String mobile, String email) {

        Collection principals = this.getPrincipalsLazy(username);
        principals.add(new Principal(username, userId, mobile, email));
    }

    public AuthPrincipals(String username) {
        Collection principals = this.getPrincipalsLazy(username);
        principals.add(new Principal(username, false));
    }

    public AuthPrincipals(String username, boolean superAdmin) {
        Collection principals = this.getPrincipalsLazy(username);
        principals.add(new Principal(username, superAdmin));
    }

    public class Principal {

        private String userId;
        private String username;
        private String phone;
        private String email;
        private boolean superAdmin = false;

        public Principal(String username, boolean superAdmin) {
            this.username = username;
            this.superAdmin = superAdmin;
        }

        public Principal(String username, String userId, String mobile, String email) {
            this.username = username;
            this.userId = userId;
            this.phone = mobile;
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public String getPhone() {
            return phone;
        }

        public String getEmail() {
            return email;
        }

        public String getUserId() {
            return userId;
        }

        public boolean isSuperAdmin() {
            return superAdmin;
        }
    }

    public Object getPrimaryPrincipal() {
        Principal principal = this.isEmpty() ? null : (Principal) this.iterator().next();
        return principal == null ? null : principal.getUsername();
    }

    @Override
    public <T> T oneByType(Class<T> type) {
        if (this.realmPrincipals != null && !this.realmPrincipals.isEmpty()) {
            Collection<Set> values = this.realmPrincipals.values();
            Iterator var3 = values.iterator();

            while (var3.hasNext()) {
                Set set = (Set) var3.next();
                Iterator var5 = set.iterator();

                while (var5.hasNext()) {
                    Object o = var5.next();
                    if (type.isAssignableFrom(o.getClass())) {
                        return (T) o;
                    }
                }
            }
            return null;
        } else {
            return null;
        }
    }

    @Override
    public <T> Collection<T> byType(Class<T> type) {
        if (this.realmPrincipals != null && !this.realmPrincipals.isEmpty()) {
            Set<T> typed = new LinkedHashSet();
            Collection<Set> values = this.realmPrincipals.values();
            Iterator var4 = values.iterator();

            while (var4.hasNext()) {
                Set set = (Set) var4.next();
                Iterator var6 = set.iterator();

                while (var6.hasNext()) {
                    Object o = var6.next();
                    if (type.isAssignableFrom(o.getClass())) {
                        typed.add((T) o);
                    }
                }
            }

            if (typed.isEmpty()) {
                return Collections.EMPTY_SET;
            } else {
                return Collections.unmodifiableSet(typed);
            }
        } else {
            return Collections.EMPTY_SET;
        }
    }

    @Override
    public List asList() {
        Set all = this.asSet();
        return all.isEmpty() ? Collections.EMPTY_LIST : Collections.unmodifiableList(Lists.newArrayList(all));
    }

    @Override
    public Set asSet() {
        if (this.realmPrincipals != null && !this.realmPrincipals.isEmpty()) {
            Set aggregated = new LinkedHashSet();
            Collection<Set> values = this.realmPrincipals.values();
            Iterator<Set> var3 = values.iterator();

            while (var3.hasNext()) {
                Set set = var3.next();
                aggregated.addAll(set);
            }

            return aggregated.isEmpty() ? Collections.EMPTY_SET : Collections.unmodifiableSet(aggregated);
        } else {
            return Collections.EMPTY_SET;
        }
    }

    @Override
    public Collection fromRealm(String realmName) {
        if (this.realmPrincipals != null && !this.realmPrincipals.isEmpty()) {
            Set principals = this.realmPrincipals.get(realmName);
            if (principals == null || principals.isEmpty()) {
                principals = Collections.EMPTY_SET;
            }

            return Collections.unmodifiableSet(principals);
        } else {
            return Collections.EMPTY_SET;
        }
    }

    public Set<String> getRealmNames() {
        return this.realmPrincipals == null ? null : this.realmPrincipals.keySet();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public Iterator iterator() {
        return this.asSet().iterator();
    }


    protected Collection getPrincipalsLazy(String realmName) {
        if (this.realmPrincipals == null) {
            this.realmPrincipals = new LinkedHashMap();
        }

        Set principals = (Set) this.realmPrincipals.get(realmName);
        if (principals == null) {
            principals = new LinkedHashSet();
            this.realmPrincipals.put(realmName, principals);
        }

        return (Collection) principals;
    }
}
