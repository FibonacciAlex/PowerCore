package core.game.timer;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;


/**
 * <pre>
 * 用一个map 包装成set
 * A {@link Map} - backed {@link set}
 * </pre>
 * @author Alex
 * 2015年7月31日 上午11:19:37
 */
public final class MapBackedSet<E> extends AbstractSet<E> implements Serializable{

	private static final long serialVersionUID = 1L;
	private final Map<E, Boolean> map;
	
	

	public MapBackedSet(Map<E, Boolean> map) {
		this.map = map;
	}

	@Override
	public Iterator<E> iterator() {
		return map.keySet().iterator();
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean contains(Object o) {
		return map.containsKey(o);
	}

	@Override
	public boolean add(E e) {
		return map.put(e, Boolean.TRUE) == null;
	}

	@Override
	public boolean remove(Object o) {
		return map.remove(o) != null;
	}

	@Override
	public void clear() {
		map.clear();
	}

}
