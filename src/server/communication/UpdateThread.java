package server.communication;

/*
 * This class is responsible for updating the communication and so, send a synchronization to the server.
 */

public class UpdateThread extends Thread 
{
		private int updateRate;
		private CommsHandler callback;
		private boolean quitThread;
		
		public UpdateThread(CommsHandler callback, int updateRate)
		{
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
					callback.generateSyncPack();
					time0 = time;
				}
			}
		}

		public synchronized void kill() 
		{
			quitThread = true;
		}
}
