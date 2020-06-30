
package top.dsbbs2.common.lambda;

@FunctionalInterface
public interface INoThrowsRunnable extends Runnable
{
  void runInternal() throws Throwable;

  static void ct(final Throwable e)
  {
    if (e instanceof Error) {
      throw (Error) e;
    } else if (e instanceof RuntimeException) {
      throw (RuntimeException) e;
    } else {
      throw new RuntimeException(e);
    }
  }

  @Override
  default void run()
  {
    this.invoke();
  }

  default void invoke()
  {
    try {
      this.runInternal();
    } catch (final Throwable e) {
      INoThrowsRunnable.ct(e);
    }
  }

  static void invoke(final INoThrowsRunnable r)
  {
    r.invoke();
  }

  static INoThrowsRunnable fromRunnable(final Runnable r)
  {
    return r::run;
  }
}
