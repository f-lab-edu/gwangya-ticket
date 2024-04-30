package com.gwangya.performance.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.hazelcast.aggregation.Aggregator;
import com.hazelcast.cardinality.CardinalityEstimator;
import com.hazelcast.client.ClientService;
import com.hazelcast.cluster.Cluster;
import com.hazelcast.cluster.Endpoint;
import com.hazelcast.collection.IList;
import com.hazelcast.collection.IQueue;
import com.hazelcast.collection.ISet;
import com.hazelcast.config.Config;
import com.hazelcast.config.IndexConfig;
import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.DistributedObjectListener;
import com.hazelcast.core.EntryView;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICacheManager;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.LifecycleService;
import com.hazelcast.cp.CPSubsystem;
import com.hazelcast.crdt.pncounter.PNCounter;
import com.hazelcast.durableexecutor.DurableExecutorService;
import com.hazelcast.flakeidgen.FlakeIdGenerator;
import com.hazelcast.jet.JetService;
import com.hazelcast.logging.LoggingService;
import com.hazelcast.map.EntryProcessor;
import com.hazelcast.map.IMap;
import com.hazelcast.map.LocalMapStats;
import com.hazelcast.map.MapInterceptor;
import com.hazelcast.map.QueryCache;
import com.hazelcast.map.listener.MapListener;
import com.hazelcast.map.listener.MapPartitionLostListener;
import com.hazelcast.multimap.MultiMap;
import com.hazelcast.partition.PartitionService;
import com.hazelcast.projection.Projection;
import com.hazelcast.query.Predicate;
import com.hazelcast.replicatedmap.ReplicatedMap;
import com.hazelcast.ringbuffer.Ringbuffer;
import com.hazelcast.scheduledexecutor.IScheduledExecutorService;
import com.hazelcast.splitbrainprotection.SplitBrainProtectionService;
import com.hazelcast.sql.SqlService;
import com.hazelcast.topic.ITopic;
import com.hazelcast.transaction.HazelcastXAResource;
import com.hazelcast.transaction.TransactionContext;
import com.hazelcast.transaction.TransactionException;
import com.hazelcast.transaction.TransactionOptions;
import com.hazelcast.transaction.TransactionalTask;

public class InMemoryHazelcastInstance implements HazelcastInstance {

	HashMap<String, IMap> maps = new HashMap<>();

	@Override
	public String getName() {
		return null;
	}

	@Override
	public <E> IQueue<E> getQueue(String name) {
		return null;
	}

	@Override
	public <E> ITopic<E> getTopic(String name) {
		return null;
	}

	@Override
	public <E> ISet<E> getSet(String name) {
		return null;
	}

	@Override
	public <E> IList<E> getList(String name) {
		return null;
	}

	@Override
	public <K, V> IMap<K, V> getMap(String name) {
		if (!Objects.isNull(maps.get(name))) {
			return maps.get(name);
		}
		IMap<K, V> occupiedSeats = new InMemoryIMap();
		maps.put("seatSession", occupiedSeats);
		return occupiedSeats;
	}

	@Override
	public <K, V> ReplicatedMap<K, V> getReplicatedMap(String name) {
		return null;
	}

	@Override
	public <K, V> MultiMap<K, V> getMultiMap(String name) {
		return null;
	}

	@Override
	public <E> Ringbuffer<E> getRingbuffer(String name) {
		return null;
	}

	@Override
	public <E> ITopic<E> getReliableTopic(String name) {
		return null;
	}

	@Override
	public Cluster getCluster() {
		return null;
	}

	@Override
	public Endpoint getLocalEndpoint() {
		return null;
	}

	@Override
	public IExecutorService getExecutorService(String name) {
		return null;
	}

	@Override
	public DurableExecutorService getDurableExecutorService(String name) {
		return null;
	}

	@Override
	public <T> T executeTransaction(TransactionalTask<T> task) throws TransactionException {
		return null;
	}

	@Override
	public <T> T executeTransaction(TransactionOptions options, TransactionalTask<T> task) throws TransactionException {
		return null;
	}

	@Override
	public TransactionContext newTransactionContext() {
		return null;
	}

	@Override
	public TransactionContext newTransactionContext(TransactionOptions options) {
		return null;
	}

	@Override
	public FlakeIdGenerator getFlakeIdGenerator(String name) {
		return null;
	}

	@Override
	public Collection<DistributedObject> getDistributedObjects() {
		return null;
	}

	@Override
	public UUID addDistributedObjectListener(DistributedObjectListener distributedObjectListener) {
		return null;
	}

	@Override
	public boolean removeDistributedObjectListener(UUID registrationId) {
		return false;
	}

	@Override
	public Config getConfig() {
		return null;
	}

	@Override
	public PartitionService getPartitionService() {
		return null;
	}

	@Override
	public SplitBrainProtectionService getSplitBrainProtectionService() {
		return null;
	}

	@Override
	public ClientService getClientService() {
		return null;
	}

	@Override
	public LoggingService getLoggingService() {
		return null;
	}

	@Override
	public LifecycleService getLifecycleService() {
		return null;
	}

	@Override
	public <T extends DistributedObject> T getDistributedObject(String serviceName, String name) {
		return null;
	}

	@Override
	public ConcurrentMap<String, Object> getUserContext() {
		return null;
	}

	@Override
	public HazelcastXAResource getXAResource() {
		return null;
	}

	@Override
	public ICacheManager getCacheManager() {
		return null;
	}

	@Override
	public CardinalityEstimator getCardinalityEstimator(String name) {
		return null;
	}

	@Override
	public PNCounter getPNCounter(String name) {
		return null;
	}

	@Override
	public IScheduledExecutorService getScheduledExecutorService(String name) {
		return null;
	}

	@Override
	public CPSubsystem getCPSubsystem() {
		return null;
	}

	@Override
	public SqlService getSql() {
		return null;
	}

	@Override
	public JetService getJet() {
		return null;
	}

	@Override
	public void shutdown() {

	}
}

class InMemoryIMap<K, V> implements IMap<K, V> {
	HashMap data = new HashMap();

	@Override
	public void putAll(Map m) {
		data.putAll(m);
	}

	@Override
	public int size() {
		return data.size();
	}

	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return data.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return data.containsValue(value);
	}

	@Override
	public V get(Object key) {
		return (V)data.get(key);
	}

	@Override
	public Object put(Object key, Object value) {
		return data.put(key, value);
	}

	@Override
	public V remove(Object key) {
		return (V)data.remove(key);
	}

	@Override
	public boolean remove(Object key, Object value) {
		return false;
	}

	@Override
	public void removeAll(Predicate predicate) {

	}

	@Override
	public void delete(Object key) {

	}

	@Override
	public void flush() {

	}

	@Override
	public Map getAll(Set keys) {
		return null;
	}

	@Override
	public void loadAll(boolean replaceExistingValues) {

	}

	@Override
	public void loadAll(Set keys, boolean replaceExistingValues) {

	}

	@Override
	public void clear() {

	}

	@Override
	public CompletionStage getAsync(Object key) {
		return null;
	}

	@Override
	public CompletionStage putAsync(Object key, Object value) {
		return null;
	}

	@Override
	public CompletionStage putAsync(Object key, Object value, long ttl, TimeUnit ttlUnit) {
		return null;
	}

	@Override
	public CompletionStage putAsync(Object key, Object value, long ttl, TimeUnit ttlUnit, long maxIdle,
		TimeUnit maxIdleUnit) {
		return null;
	}

	@Override
	public CompletionStage<Void> putAllAsync(Map map) {
		return null;
	}

	@Override
	public CompletionStage<Void> setAsync(Object key, Object value) {
		return null;
	}

	@Override
	public CompletionStage<Void> setAsync(Object key, Object value, long ttl, TimeUnit ttlUnit) {
		return null;
	}

	@Override
	public CompletionStage<Void> setAsync(Object key, Object value, long ttl, TimeUnit ttlUnit, long maxIdle,
		TimeUnit maxIdleUnit) {
		return null;
	}

	@Override
	public CompletionStage removeAsync(Object key) {
		return null;
	}

	@Override
	public boolean tryRemove(Object key, long timeout, TimeUnit timeunit) {
		return false;
	}

	@Override
	public boolean tryPut(Object key, Object value, long timeout, TimeUnit timeunit) {
		return false;
	}

	@Override
	public Object put(Object key, Object value, long ttl, TimeUnit ttlUnit) {
		return null;
	}

	@Override
	public Object put(Object key, Object value, long ttl, TimeUnit ttlUnit, long maxIdle, TimeUnit maxIdleUnit) {
		return null;
	}

	@Override
	public void putTransient(Object key, Object value, long ttl, TimeUnit ttlUnit) {

	}

	@Override
	public void putTransient(Object key, Object value, long ttl, TimeUnit ttlUnit, long maxIdle, TimeUnit maxIdleUnit) {

	}

	@Override
	public Object putIfAbsent(Object key, Object value) {
		return null;
	}

	@Override
	public Object putIfAbsent(Object key, Object value, long ttl, TimeUnit ttlUnit) {
		return null;
	}

	@Override
	public Object putIfAbsent(Object key, Object value, long ttl, TimeUnit ttlUnit, long maxIdle,
		TimeUnit maxIdleUnit) {
		return null;
	}

	@Override
	public boolean replace(Object key, Object oldValue, Object newValue) {
		return false;
	}

	@Override
	public Object replace(Object key, Object value) {
		return null;
	}

	@Override
	public void set(Object key, Object value) {

	}

	@Override
	public void set(Object key, Object value, long ttl, TimeUnit ttlUnit) {

	}

	@Override
	public void set(Object key, Object value, long ttl, TimeUnit ttlUnit, long maxIdle, TimeUnit maxIdleUnit) {

	}

	@Override
	public void setAll(Map map) {

	}

	@Override
	public CompletionStage<Void> setAllAsync(Map map) {
		return null;
	}

	@Override
	public void lock(Object key) {

	}

	@Override
	public void lock(Object key, long leaseTime, TimeUnit timeUnit) {

	}

	@Override
	public boolean isLocked(Object key) {
		return false;
	}

	@Override
	public boolean tryLock(Object key) {
		return false;
	}

	@Override
	public boolean tryLock(Object key, long time, TimeUnit timeunit) throws InterruptedException {
		return false;
	}

	@Override
	public boolean tryLock(Object key, long time, TimeUnit timeunit, long leaseTime, TimeUnit leaseTimeunit) throws
		InterruptedException {
		return false;
	}

	@Override
	public void unlock(Object key) {

	}

	@Override
	public void forceUnlock(Object key) {

	}

	@Override
	public UUID addLocalEntryListener(MapListener listener) {
		return null;
	}

	@Override
	public UUID addLocalEntryListener(MapListener listener, Predicate predicate, boolean includeValue) {
		return null;
	}

	@Override
	public UUID addLocalEntryListener(MapListener listener, Predicate predicate, Object key, boolean includeValue) {
		return null;
	}

	@Override
	public String addInterceptor(MapInterceptor interceptor) {
		return null;
	}

	@Override
	public boolean removeInterceptor(String id) {
		return false;
	}

	@Override
	public UUID addEntryListener(MapListener listener, boolean includeValue) {
		return null;
	}

	@Override
	public boolean removeEntryListener(UUID id) {
		return false;
	}

	@Override
	public UUID addPartitionLostListener(MapPartitionLostListener listener) {
		return null;
	}

	@Override
	public boolean removePartitionLostListener(UUID id) {
		return false;
	}

	@Override
	public UUID addEntryListener(MapListener listener, Object key, boolean includeValue) {
		return null;
	}

	@Override
	public UUID addEntryListener(MapListener listener, Predicate predicate, boolean includeValue) {
		return null;
	}

	@Override
	public UUID addEntryListener(MapListener listener, Predicate predicate, Object key, boolean includeValue) {
		return null;
	}

	@Override
	public EntryView getEntryView(Object key) {
		return null;
	}

	@Override
	public boolean evict(Object key) {
		return false;
	}

	@Override
	public void evictAll() {

	}

	@Override
	public Set keySet() {
		return null;
	}

	@Override
	public Collection values() {
		return null;
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return data.entrySet();
	}

	@Override
	public Set keySet(Predicate predicate) {
		return null;
	}

	@Override
	public Set<Entry> entrySet(Predicate predicate) {
		return null;
	}

	@Override
	public Collection values(Predicate predicate) {
		return null;
	}

	@Override
	public Set localKeySet() {
		return null;
	}

	@Override
	public Set localKeySet(Predicate predicate) {
		return null;
	}

	@Override
	public void addIndex(IndexConfig indexConfig) {

	}

	@Override
	public LocalMapStats getLocalMapStats() {
		return null;
	}

	@Override
	public QueryCache getQueryCache(String name) {
		return null;
	}

	@Override
	public QueryCache getQueryCache(String name, Predicate predicate, boolean includeValue) {
		return null;
	}

	@Override
	public QueryCache getQueryCache(String name, MapListener listener, Predicate predicate, boolean includeValue) {
		return null;
	}

	@Override
	public boolean setTtl(Object key, long ttl, TimeUnit timeunit) {
		return false;
	}

	@Override
	public Object computeIfPresent(Object key, BiFunction remappingFunction) {
		return null;
	}

	@Override
	public Object computeIfAbsent(Object key, Function mappingFunction) {
		return null;
	}

	@Override
	public Object compute(Object key, BiFunction remappingFunction) {
		return null;
	}

	@Override
	public Object merge(Object key, Object value, BiFunction remappingFunction) {
		return null;
	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		return null;
	}

	@Override
	public Iterator<Entry<K, V>> iterator(int fetchSize) {
		return null;
	}

	@Override
	public Collection project(Projection projection, Predicate predicate) {
		return null;
	}

	@Override
	public Collection project(Projection projection) {
		return null;
	}

	@Override
	public Object aggregate(Aggregator aggregator, Predicate predicate) {
		return null;
	}

	@Override
	public Object aggregate(Aggregator aggregator) {
		return null;
	}

	@Override
	public Map executeOnEntries(EntryProcessor entryProcessor, Predicate predicate) {
		return null;
	}

	@Override
	public Map executeOnEntries(EntryProcessor entryProcessor) {
		return null;
	}

	@Override
	public CompletionStage submitToKey(Object key, EntryProcessor entryProcessor) {
		return null;
	}

	@Override
	public CompletionStage<Map> submitToKeys(Set keys, EntryProcessor entryProcessor) {
		return null;
	}

	@Override
	public Map executeOnKeys(Set keys, EntryProcessor entryProcessor) {
		return null;
	}

	@Override
	public Object executeOnKey(Object key, EntryProcessor entryProcessor) {
		return null;
	}

	@Override
	public String getPartitionKey() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getServiceName() {
		return null;
	}

	@Override
	public void destroy() {

	}
}