
package top.dsbbs2.common.concurrent.container;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class CustomMap<K, V> implements ConcurrentMap<K, V>
{
  private final Object lock = new Object();
  private final Vector<CustomEntry<K, V>> entries = new Vector<>();

  @Override
  public int size()
  {
    synchronized (this.lock) {
      return this.entries.size();
    }
  }

  @Override
  public boolean isEmpty()
  {
    synchronized (this.lock) {
      return this.entries.isEmpty();
    }
  }

  @Override
  public boolean containsKey(final Object key)
  {
    synchronized (this.lock) {
      for (final Entry<K, V> i : this.entries) {
        if ((i != null) && Objects.equals(i.getKey(), key)) {
          return true;
        }
      }
      return false;
    }
  }

  public int indexOfKey(final Object key)
  {
    synchronized (this.lock) {
      for (int i2 = 0; i2 < this.entries.size(); i2++) {
        final Entry<K, V> i = this.entries.get(i2);
        if ((i != null) && Objects.equals(i.getKey(), key)) {
          return i2;
        }
      }
      return -1;
    }
  }

  public int indexOfValue(final Object value)
  {
    synchronized (this.lock) {
      for (int i2 = 0; i2 < this.entries.size(); i2++) {
        final Entry<K, V> i = this.entries.get(i2);
        if ((i != null) && Objects.equals(i.getValue(), value)) {
          return i2;
        }
      }
      return -1;
    }

  }

  @Override
  public boolean containsValue(final Object value)
  {
    synchronized (this.lock) {
      for (final Entry<K, V> i : this.entries) {
        if ((i != null) && Objects.equals(i.getValue(), value)) {
          return true;
        }
      }
      return false;
    }

  }

  @Override
  public V get(final Object key)
  {
    synchronized (this.lock) {
      for (final Entry<K, V> i : this.entries) {
        if ((i != null) && Objects.equals(i.getKey(), key)) {
          return i.getValue();
        }
      }
      return null;
    }

  }

  @Override
  public V put(final K key, final V value)
  {
    synchronized (this.lock) {
      if (this.containsKey(key)) {
        this.entries.get(this.indexOfKey(key)).setValue(value);
      } else {
        this.entries.add(new CustomEntry<>(key, value));
      }
      return value;
    }

  }

  @Override
  public V remove(final Object key)
  {
    synchronized (this.lock) {
      final V ret = this.get(key);
      final int i = this.indexOfKey(key);
      this.entries.remove(i);
      return ret;
    }

  }

  @Override
  public void putAll(final Map<? extends K, ? extends V> m)
  {
    synchronized (this.lock) {
      m.forEach(this::put);
    }

  }

  @Override
  public void clear()
  {
    synchronized (this.lock) {
      this.entries.clear();
    }

  }

  @Override
  public Set<K> keySet()
  {
    synchronized (this.lock) {
      final Vector<K> keys = new Vector<>();
      this.entries.forEach(i -> keys.add(i.getKey()));
      return keys.stream().collect(Collectors.toSet());
    }

  }

  @Override
  public Collection<V> values()
  {
    synchronized (this.lock) {
      final Vector<V> values = new Vector<>();
      this.entries.forEach(i -> values.add(i.getValue()));
      return values;
    }

  }

  @Override
  public Set<Entry<K, V>> entrySet()
  {
    synchronized (this.lock) {
      return this.entries.stream().collect(Collectors.toSet());
    }

  }

  @Override
  public V putIfAbsent(final K key, final V value)
  {
    synchronized (this.lock) {
      if (!this.containsKey(key)) {
        this.put(key, value);
      }
      return value;
    }

  }

  @Override
  public boolean remove(final Object key, final Object value)
  {
    synchronized (this.lock) {
      for (final Entry<K, V> i : this.entries) {
        if ((i != null) && Objects.equals(i.getKey(), key) && Objects.equals(i.getValue(), value)) {
          this.entries.remove(i);
          return true;
        }
      }
      return false;
    }

  }

  @Override
  public boolean replace(final K key, final V oldValue, final V newValue)
  {
    synchronized (this.lock) {
      for (final Entry<K, V> i : this.entries) {
        if ((i != null) && Objects.equals(i.getKey(), key) && Objects.equals(i.getValue(), oldValue)) {
          i.setValue(newValue);
          return true;
        }
      }
      return false;
    }

  }

  @Override
  public V replace(final K key, final V value)
  {
    synchronized (this.lock) {
      for (final Entry<K, V> i : this.entries) {
        if ((i != null) && Objects.equals(i.getKey(), key)) {
          i.setValue(value);
          break;
        }
      }
      return value;
    }

  }

}
