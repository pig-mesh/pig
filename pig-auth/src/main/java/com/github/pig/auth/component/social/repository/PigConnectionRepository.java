package com.github.pig.auth.component.social.repository;

import org.springframework.social.connect.*;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * @author lengleng
 * @date 2018/1/18
 */
public class PigConnectionRepository implements ConnectionRepository {

    /**
     * Find all connections the current user has across all providers.
     * The returned map contains an entry for each provider the user is connected to.
     * The key for each entry is the providerId, and the value is the list of {@link Connection}s that exist between the user and that provider.
     * For example, if the user is connected once to Facebook and twice to Twitter, the returned map would contain two entries with the following structure:
     * <pre>
     * {
     *     "facebook" -&gt; Connection("Keith Donald") ,
     *     "twitter"  -&gt; Connection("kdonald"), Connection("springsource")
     * }
     * </pre>
     * The returned map is sorted by providerId and entry values are ordered by rank.
     * Returns an empty map if the user has no connections.
     *
     * @return all connections the current user has across all providers.
     */
    @Override
    public MultiValueMap<String, Connection<?>> findAllConnections() {
        return null;
    }

    /**
     * Find the connections the current user has to the provider registered by the given id e.g. 'facebook'.
     * The returned list is ordered by connection rank.
     * Returns an empty list if the user has no connections to the provider.
     *
     * @param providerId the provider id e.g. "facebook"
     * @return the connections the user has to the provider, or an empty list if none
     */
    @Override
    public List<Connection<?>> findConnections(String providerId) {
        return null;
    }

    /**
     * Find the connections the current user has to the provider of the given API e.g. Facebook.class.
     * Semantically equivalent to {@link #findConnections(String)}, but uses the apiType as the provider key instead of the providerId.
     * Useful for direct use by application code to obtain parameterized Connection instances e.g. <code>List&lt;Connection&lt;Facebook&gt;&gt;</code>.
     *
     * @param apiType the API type e.g. Facebook.class or Twitter.class
     * @return the connections the user has to the provider of the API, or an empty list if none
     */
    @Override
    public <A> List<Connection<A>> findConnections(Class<A> apiType) {
        return null;
    }

    /**
     * Find the connections the current user has to the given provider users.
     * The providerUsers parameter accepts a map containing an entry for each provider the caller is interested in.
     * The key for each entry is the providerId e.g. "facebook", and the value is a list of provider user ids to fetch connections to e.g. ("126500", "34521", "127243").
     * The returned map has the same structure and order, except the provider userId values have been replaced by Connection instances.
     * If no connection exists between the current user and a given provider user, a null value is returned for that position.
     *
     * @param providerUserIds the provider users map
     * @return the provider user connection map
     */
    @Override
    public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUserIds) {
        return null;
    }

    /**
     * Get a connection for the current user by its key, which consists of the providerId + providerUserId.
     *
     * @param connectionKey the service provider connection key
     * @return the connection
     * @throws NoSuchConnectionException if no such connection exists for the current user
     */
    @Override
    public Connection<?> getConnection(ConnectionKey connectionKey) {
        return null;
    }

    /**
     * Get a connection between the current user and the given provider user.
     * Semantically equivalent to {@link #getConnection(ConnectionKey)}, but uses the apiType as the provider key instead of the providerId.
     * Useful for direct use by application code to obtain a parameterized Connection instance.
     *
     * @param apiType        the API type e.g. Facebook.class or Twitter.class
     * @param providerUserId the provider user e.g. "126500".
     * @return the connection
     * @throws NoSuchConnectionException if no such connection exists for the current user
     */
    @Override
    public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId) {
        return null;
    }

    /**
     * Get the "primary" connection the current user has to the provider of the given API e.g. Facebook.class.
     * If the user has multiple connections to the provider associated with the given apiType, this method returns the one with the top rank (or priority).
     * Useful for direct use by application code to obtain a parameterized Connection instance.
     *
     * @param apiType the API type e.g. Facebook.class or Twitter.class
     * @return the primary connection
     * @throws NotConnectedException if the user is not connected to the provider of the API
     */
    @Override
    public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
        return null;
    }

    /**
     * Find the "primary" connection the current user has to the provider of the given API e.g. Facebook.class.
     * Semantically equivalent to {@link #getPrimaryConnection(Class)} but returns null if no connection is found instead of throwing an exception.
     *
     * @param apiType the API type e.g. Facebook.class or Twitter.class
     * @return the primary connection, or <code>null</code> if not found
     */
    @Override
    public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
        return null;
    }

    /**
     * Add a new connection to this repository for the current user.
     * After the connection is added, it can be retrieved later using one of the finders defined in this interface.
     *
     * @param connection the new connection to add to this repository
     * @throws DuplicateConnectionException if the user already has this connection
     */
    @Override
    public void addConnection(Connection<?> connection) {

    }

    /**
     * Update a Connection already added to this repository.
     * Merges the field values of the given connection object with the values stored in the repository.
     *
     * @param connection the existing connection to update in this repository
     */
    @Override
    public void updateConnection(Connection<?> connection) {

    }

    /**
     * Remove all Connections between the current user and the provider from this repository.
     * Does nothing if no provider connections exist.
     *
     * @param providerId the provider id e.g. 'facebook'
     */
    @Override
    public void removeConnections(String providerId) {

    }

    /**
     * Remove a single Connection for the current user from this repository.
     * Does nothing if no such connection exists.
     *
     * @param connectionKey the connection key
     */
    @Override
    public void removeConnection(ConnectionKey connectionKey) {

    }
}