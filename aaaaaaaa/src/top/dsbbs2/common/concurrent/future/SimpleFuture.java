
package top.dsbbs2.common.concurrent.future;

import top.dsbbs2.common.closure.Reference;

public class SimpleFuture
{
  private Reference<Boolean> done=new Reference<>(false);
  public void finish()
  {
    if(this.done.value)
      throw new RuntimeException("already done");
    this.done.value=true;
  }
  public void sync()
  {
    while(!this.done.value)
    {
      try {
        Thread.sleep(1);
      }catch(Throwable e) {throw new RuntimeException(e);}
    }
  }
}
