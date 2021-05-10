package client.engine;

/*
 * This thread is responsible to send the input from the client in a constant rate to the server.
 */

public class UpdateThread extends Thread 
{
		private int updateRate;
		private InputHandler callback;
		private boolean quitThread;
		
		public UpdateThread(InputHandler callback, int updateRate)
		{
			Thread.currentThread().setName("SendingInputThread");
			this.callback = callback;
			this.updateRate = updateRate;
		}
		
		@Override
		public void run()
		{
			double time0 = System.currentTimeMillis();
			
			while(!quitThread)
			{
				double time = System.currentTimeMillis();
				double deltaTime = time - time0;
				
				if(deltaTime >= updateRate)
				{
					callback.sendActionPack();
					time0 = time;
				}
			}
		}

		public void kill() 
		{
			quitThread = true;
		}
}
