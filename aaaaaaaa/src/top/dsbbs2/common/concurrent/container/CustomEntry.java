
package top.dsbbs2.common.concurrent.container;

import java.util.Map.Entry;

import java.util.Objects;

public class CustomEntry<K,V> implements Entry<K, V>
{
  private final K key;
  private volatile V value;
  private final Object lock=new Object();
  public CustomEntry(K key,V value)
  {
    this.key=Objects.requireNonNull(key);
    this.value=value;
  }
  @Override
  public K getKey()
  {
    return this.key;
  }

  @Override
  public V getValue()
  {
    synchronized (lock) {
      return this.value;
    }
  }

  @Override
  public V setValue(V value)
  {
    synchronized (lock) {
      this.value=value;
      return this.value;
    }
  }

}
