package server.communication;

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
			double timeFPS0 = System.currentTimeMillis();
			
			while(!quitThread)
			{
				double time = System.currentTimeMillis();
				double deltaTimeFPS = time - timeFPS0;
				
				if(deltaTimeFPS >= updateRate)
				{
					callback.generateSyncPack();
					timeFPS0 = time;
				}
			}
		}

		public synchronized void kill() 
		{
			quitThread = true;
		}
}
