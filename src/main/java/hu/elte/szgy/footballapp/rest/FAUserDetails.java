package hu.elte.szgy.footballapp.rest;

import hu.elte.szgy.footballapp.data.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
/*import sun.reflect.generics.reflectiveObjects.NotImplementedException;*/

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FAUserDetails implements UserDetails {

    private User user;
    private List<GrantedAuthority> auths=new ArrayList<GrantedAuthority>(5);

    public FAUserDetails(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return auths;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
