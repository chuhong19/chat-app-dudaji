package vn.giabaochatapp.giabaochatappserver.services.authentication;

import vn.giabaochatapp.giabaochatappserver.data.domains.User;

import java.util.Map;

public interface ITokenClaimComponent {
    Map<String, Object> getClaims(Map<String, Object> extraClaims, User userDetails, long expiration);
}
