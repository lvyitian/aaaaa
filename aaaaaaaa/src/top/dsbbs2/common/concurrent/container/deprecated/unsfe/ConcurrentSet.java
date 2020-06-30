
package top.dsbbs2.common.concurrent.container.deprecated.unsfe;

import java.util.Collection;
import java.util.Vector;
import java.util.stream.Collectors;

@Deprecated
public class ConcurrentSet<T> extends Vector<T>
{

  /**
   *
   */
  @Deprecated
  private static final long serialVersionUID = -2162251099316171449L;

  @Deprecated
  @Override
  public synchronized boolean add(final T e)
  {
    if (this.contains(e)) {
      return false;
    }
    return super.add(e);
  }

  @Deprecated
  @Override
  public synchronized void setElementAt(final T obj, final int index)
  {
    if (this.contains(obj)) {
      return;
    }
    super.setElementAt(obj, index);
  }

  @Deprecated
  @Override
  public synchronized void insertElementAt(final T obj, final int index)
  {
    if (this.contains(obj)) {
      return;
    }
    super.insertElementAt(obj, index);
  }

  @Deprecated
  @Override
  public synchronized void addElement(final T obj)
  {
    if (this.contains(obj)) {
      return;
    }
    super.addElement(obj);
  }

  @Deprecated
  @Override
  public synchronized T set(final int index, final T element)
  {
    if (this.contains(element)) {
      return element;
    }
    return super.set(index, element);
  }

  @Deprecated
  @Override
  public void add(final int index, final T element)
  {
    if (this.contains(element)) {
      return;
    }
    super.add(index, element);
  }

  @Deprecated
  @Override
  public synchronized boolean addAll(final Collection<? extends T> c)
  {
    return super.addAll(c.stream().filter(i -> !this.contains(i)).collect(Collectors.toList()));
  }

  @Deprecated
  @Override
  public synchronized boolean addAll(final int index, final Collection<? extends T> c)
  {
    return super.addAll(index, c.stream().filter(i -> !this.contains(i)).collect(Collectors.toList()));
  }

}
